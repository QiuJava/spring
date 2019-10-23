package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.action.SurveyOrderAction;
import cn.eeepay.framework.dao.SurveyOrderDao;
import cn.eeepay.framework.dao.YfbPayOrderDao;
import cn.eeepay.framework.daoHistory.HistoryTransOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.model.surveyOrder.SurveyUrgeRecord;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MXG
 * create 2018/09/07
 */
@Service
public class SurveyOrderServiceImpl implements SurveyOrderService {

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SurveyOrderDao surveyOrderDao;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private PosCardBinService posCardBinService;
    @Resource
    private YfbPayOrderDao yfbPayOrderDao;
    @Resource
    private SurveyOrderReplyService surveyOrderReplyService;

    private static final Logger log = LoggerFactory.getLogger(SurveyOrderServiceImpl.class);

    @Override
    public Map<String, Object> selectOrderByParam(SurveyOrderInfo order, int pageNo, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent != null) {
            if(order != null ){
                if(order.getAgentNode() == null){
                    order.setAgentNode(loginAgent.getAgentNode());
                }else if(!order.getAgentNode().startsWith(loginAgent.getAgentNode())){
                        result.put("status", false);
                        result.put("msg", "非法操作");
                        return result;
                }
            }else {
                order = new SurveyOrderInfo();
                order.setAgentNode(loginAgent.getAgentNode());
            }
        }else {
            result.put("status", false);
            result.put("msg", "请先登录");
            return result;
        }
        Page<SurveyOrderInfo> page = new Page<>(pageNo, pageSize);
        surveyOrderDao.selectByParamWithPage(page,order);
        List<SurveyOrderInfo> list = page.getResult();
        for (SurveyOrderInfo orderInfo : list) {
            // 0-未回复 1-未回复（下级已提交） 2-已回复 3-逾期未回复 4-逾期未回复（下级已提交） 5-逾期已回复
            String replyStatus = orderInfo.getReplyStatus();
            String dealStatus = orderInfo.getDealStatus();

            if(loginAgent.getAgentLevel() == 1){ // 一级代理商
                // 查看权限
                if("0".equals(replyStatus) || "3".equals(replyStatus)){
                    orderInfo.setCanReply("1");
                }else if(("2".equals(replyStatus) || "5".equals(replyStatus))){
                    if("2".equals(dealStatus) || "3".equals(dealStatus) || "6".equals(dealStatus) || "0".equals(dealStatus) ||
                            "8".equals(dealStatus)){
                        orderInfo.setCanReply("0");
                    }else{
                        orderInfo.setCanReply("1");
                    }
                }else{
                    orderInfo.setCanReply("0");
                }

                // 修改权限
                if("0".equals(dealStatus) || "8".equals(dealStatus)){ //未处理（包括0未处理，8已回退）的时候可以修改
                    if("1".equals(replyStatus) || "4".equals(replyStatus)){//未回复（下级已提交）、逾期未回复（下级已提交）
                        orderInfo.setCanEdit("1");
                    }else if(("2".equals(replyStatus)||"5".equals(replyStatus)) &&
                            ("0".equals(dealStatus) || "8".equals(dealStatus))){//已确认（逾期确认）+未处理（包括0未处理，8已回退） 一级代理商可以修改
                        orderInfo.setCanEdit("1");
                    } else {
                        orderInfo.setCanEdit("0");
                    }
                }else {
                    orderInfo.setCanEdit("0");
                }
            }else if(loginAgent.getAgentNode().equals(orderInfo.getAgentNode())){//直属代理商
                // 查看权限
                if("0".equals(replyStatus) || "3".equals(replyStatus)){
                    orderInfo.setCanReply("1");
                }else{
                    orderInfo.setCanReply("0");
                }

                // 修改权限
                if("0".equals(dealStatus) || "8".equals(dealStatus)){ //未处理（包括0未处理，8已回退）的时候可以修改
                    if("1".equals(replyStatus) || "4".equals(replyStatus)){ //未回复（下级已提交）、逾期未回复（下级已提交）
                        // 如果是商户回复，直属代理商不能回复
                        SurveyReplyRecord replyRecord =
                                surveyOrderReplyService.selectRecordByOrderNo(orderInfo.getOrderNo(), loginAgent.getAgentNode());
                        orderInfo.setCanEdit("M".equals(replyRecord.getReplyRoleType()) ? "0" : "1");// 排除商户
                    } else {
                        orderInfo.setCanEdit("0");
                    }
                }else {
                    orderInfo.setCanEdit("0");
                }

            }else {//既不是一级也不是直属
                orderInfo.setCanReply("0");
                order.setCanEdit("0");
            }

            //审核
            if(loginAgent.getAgentLevel() == 1){
                orderInfo.setCanCheck("1".equals(orderInfo.getReplyStatus()) || "4".equals(orderInfo.getReplyStatus()) ? "1" : "0");
            }else {
                orderInfo.setCanCheck("0");
            }
        }
        result.put("status", true);
        result.put("page", page);
        return result;
    }

    @Override
    public void export(SurveyOrderInfo order, HttpServletResponse response, HttpServletRequest request) throws Exception {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent != null) {
            if(order != null ){
                if(order.getAgentNode() == null){
                    order.setAgentNode(loginAgent.getAgentNode());
                }
            }else {
                order = new SurveyOrderInfo();
                order.setAgentNode(loginAgent.getAgentNode());
            }
        }else {
            log.error("导出调单失败：用户未登录");
            return;
        }
        List<SurveyOrderInfo> list = surveyOrderDao.selectByParam(order);
        createTable(list, response, request);
    }

    @Override
    public SurveyOrderInfo selectByIdAndAgentNode(String id, String agentNode) {
        SurveyOrderInfo order = surveyOrderDao.selectByIdAndAgentNode(id, agentNode);
        if(order == null){
            return null;
        }
        String accountNo = order.getTransAccountNo();
        //发卡行、卡类别
        if(StringUtils.isNotBlank(accountNo)){
            PosCardBin cardBin = posCardBinService.queryInfo(accountNo);
            order.setBankName(cardBin.getBankName());
            order.setCardType(cardBin.getCardType());
        }
        Map<String,String> payMethodMap = sysDictService.selectMapByKey("PAY_METHOD_TYPE");//交易方式
        order.setPayMethod(payMethodMap.get(order.getPayMethod()));
        Map<String,String> orderTypeMap = sysDictService.selectMapByKey("ORDER_TYPE_CODE");//调单类型
        order.setOrderTypeCode(orderTypeMap.get(order.getOrderTypeCode()));
        if(StringUtils.isNotBlank(order.getTemplateFilesName())){
            String[] str=order.getTemplateFilesName().split(",");
            if(str!=null&&str.length>0){
                order.setTemplateList(Arrays.asList(str));
            }
        }
        return order;
    }

    @Override
    public List<SurveyUrgeRecord> selectUrgeRecordList(String orderNo) {
        List<SurveyUrgeRecord> records = surveyOrderDao.selectUrgeRecordList(orderNo);
        return records;
    }

    @Override
    public Map<String, Object> getUnDealOrder() throws Exception{
        Map<String, Object> result = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        //未处理调单数量
        List<SurveyOrderInfo> unDealOrders = new ArrayList<>();
        if(loginAgent.getAgentLevel() == 1){
            unDealOrders = surveyOrderDao.countUnConfirmOrder(loginAgent.getAgentNode());//统计直属未提交+一级未确认调单数量+已逾期
        }else {
            unDealOrders = surveyOrderDao.countUnReplyOrder(loginAgent.getAgentNode());//统计未提交调单数（包括逾期）
        }
        result.put("num", unDealOrders.size());//未处理调单数量

        //已逾期未处理调单列表
        List<String> overdueOrders = new ArrayList<>();
        for (SurveyOrderInfo order : unDealOrders) {
            if("1".equals(order.getOverdue()) && !overdueOrders.contains(order.getTransOrderNo())){
                overdueOrders.add(order.getTransOrderNo());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < overdueOrders.size(); i++) {
            sb.append(i==0?overdueOrders.get(i):"、"+overdueOrders.get(i));
        }
        result.put("overdueOrders", sb.toString());//逾期调单订单号

        //催单弹窗信息:一级代理商才显示
        if(loginAgent.getAgentLevel() == 1){
            List<SurveyUrgeRecord> surveyUrgeRecords = getSurveyUrgeRecords(loginAgent);
            result.put("surveyUrgeRecords", surveyUrgeRecords);
        }else {
            result.put("surveyUrgeRecords", null);
        }
        return result;
    }

    private List<SurveyUrgeRecord> getSurveyUrgeRecords(AgentInfo loginAgent) throws Exception{
        SysDict sysDict = sysDictService.getByKey("SURVEY_ORDER_URGE_MSG");
        List<SurveyUrgeRecord> surveyUrgeRecords = new ArrayList<>();
        if(loginAgent.getAgentLevel() == 1){
            surveyUrgeRecords = surveyOrderDao.selectOneAgentUrgeRecord(loginAgent.getAgentNode(), loginAgent.getAgentNo());
        }else {
            surveyUrgeRecords = surveyOrderDao.selectUrgeRecordByAgentNode(loginAgent.getAgentNode(), loginAgent.getAgentNo());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (SurveyUrgeRecord record : surveyUrgeRecords) {
            record.setMsg(sysDict.getSysValue());
            long seconds = (DateUtils.getMillis(dateFormat.parse(record.getCurrentTime())) - DateUtils.getMillis(dateFormat.parse(record.getCreateTime())));
            long hour = seconds/(60*60*1000);
            long minute = seconds/(60*1000);
            if(minute < 60){
                if(minute < 1){
                    minute = 1;
                }
                record.setTime(minute+"分钟前");
            }else if(hour >= 24){
                long day = hour/24;
                long remainHour = hour - day*24;
                if(day >= 30){
                    record.setTime("一个月前");
                }else if(remainHour == 0){
                    record.setTime(day+"天前");
                }else {
                    record.setTime(day+"天" + remainHour+"小时前");
                }
            }else{
                record.setTime(hour+"小时前");
            }
        }
        return surveyUrgeRecords;
    }

    @Override
    public void haveLook(String id) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        SurveyUrgeRecord urgeRecord = surveyOrderDao.selectUrgeRecordById(id);
        String haveLookNo = urgeRecord.getHaveLookNo();
        if(StringUtils.isNotBlank(haveLookNo)){
            urgeRecord.setHaveLookNo(haveLookNo + "," + loginAgent.getAgentNo());
        }else {
            urgeRecord.setHaveLookNo(loginAgent.getAgentNo());
        }
        surveyOrderDao.haveLook(urgeRecord);
    }

    /**
     * 生成表格
     * @param list
     * @param response
     * @param request
     * @throws Exception
     */
    private void createTable(List<SurveyOrderInfo> list, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String,String> orderTypeMap = sysDictService.selectMapByKey("ORDER_TYPE_CODE");//调单类型
        Map<String,String> serviceTypeMap = sysDictService.selectMapByKey("ORDER_SERVICE_CODE");//业务类型
        Map<String,String> dealStatusMap = sysDictService.selectMapByKey("ORDER_DEAL_STATUS");//处理状态
        Map<String,String> transStatusMap = sysDictService.selectMapByKey("TRANS_STATUS");//交易状态
        Map<String,String> payMethodMap = sysDictService.selectMapByKey("PAY_METHOD_TYPE");//支付方式
        Map<String,String> replyStatusMap = sysDictService.selectMapByKey("REPLY_STATUS_CODE");//回复状态
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "调单查询"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderTypeCode",null);
            maps.put("transOrderNo",null);
            maps.put("orderServiceCode",null);
            maps.put("acqReferenceNo",null);
            maps.put("createTime",null);
            maps.put("replyEndTime", null);
            maps.put("replyStatus", null);
            maps.put("dealStatus",null);
            maps.put("urgeNum",null);
            maps.put("merchantNo",null);
            maps.put("merGroup",null);
            maps.put("transAccountNo",null);
            maps.put("transAmount",null);
            maps.put("transTime",null);
            maps.put("agentName",null);
            maps.put("payMethod",null);
            maps.put("transStatus",null);
            data.add(maps);
        }else{
            for (SurveyOrderInfo order : list) {
                Map<String, String> maps = new HashMap<String, String>();
                String orderTypeCode = order.getOrderTypeCode();
                maps.put("orderTypeCode", orderTypeMap.get(orderTypeCode)==null?"":orderTypeMap.get(orderTypeCode));
                maps.put("transOrderNo", order.getTransOrderNo()==null?"":order.getTransOrderNo());
                String orderServiceCode = order.getOrderServiceCode();
                maps.put("orderServiceCode", serviceTypeMap.get(orderServiceCode)==null?"":serviceTypeMap.get(orderServiceCode));
                maps.put("acqReferenceNo", order.getAcqReferenceNo()==null?"":order.getAcqReferenceNo());
                maps.put("createTime", order.getCreateTime());
                maps.put("replyEndTime", order.getReplyEndTime());
                String replyStatus = replyStatusMap.get(order.getReplyStatus());
                maps.put("replyStatus", replyStatus==null?"":replyStatus);
                String dealStatus = order.getDealStatus();
                if("1".equals(dealStatus) || "2".equals(dealStatus) || "3".equals(dealStatus) ||
                        "4".equals(dealStatus) || "5".equals(dealStatus) || "6".equals(dealStatus) || "7".equals(dealStatus)){
                    maps.put("dealStatus", "已处理");
                }else{
                    maps.put("dealStatus", dealStatusMap.get(dealStatus)==null?"":dealStatusMap.get(dealStatus));
                }
                maps.put("urgeNum", order.getUrgeNum()==null?"":order.getUrgeNum());
                maps.put("merchantNo", order.getMerchantNo()==null?"":order.getMerchantNo());
                maps.put("merGroup", order.getMerGroup()==null?"":order.getMerGroup());
                maps.put("transAccountNo", order.getTransAccountNo()==null?"":order.getTransAccountNo());
                maps.put("transAmount", order.getTransAmount()==null?"":order.getTransAmount());
                maps.put("transTime", order.getTransTime());
                maps.put("agentName", order.getAgentName()==null?"":order.getAgentName());
                maps.put("payMethod", payMethodMap.get(order.getPayMethod())==null?"":payMethodMap.get(order.getPayMethod()));
                maps.put("transStatus", transStatusMap.get(order.getTransStatus())==null?"":transStatusMap.get(order.getTransStatus()));
                data.add(maps);
            }
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderTypeCode","transOrderNo","orderServiceCode","acqReferenceNo","createTime",
                "replyEndTime","replyStatus","dealStatus","urgeNum","merchantNo","merGroup","transAccountNo","transAmount",
                "transTime","agentName","payMethod","transStatus"};
        String[] colsName = new String[]{"类型","订单编号","业务类型","系统参考号","发起时间","截止回复时间","回复状态","处理状态",
                "催单次数","商户编号","商户组织","交易卡号","交易金额","交易时间","所属代理商","交易方式","交易状态"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("调单导出失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public YfbPayOrder selectTradeOrderDetail(String orderNo) {
        return yfbPayOrderDao.selectTradeOrderDetail(orderNo);
    }

    @Override
    public SurveyOrderInfo selectByOrderNo(String orderNo, String agentNode) {
        return surveyOrderDao.selectByOrderNo(orderNo, agentNode);
    }
}
