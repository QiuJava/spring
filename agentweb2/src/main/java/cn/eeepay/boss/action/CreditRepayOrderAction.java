package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.CreditRepayOrderDetailService;
import cn.eeepay.framework.service.CreditRepayOrderService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 信用卡还款订单
 * @author liuks
 */
@Controller
@RequestMapping(value = "/creditRepayOrder")
public class CreditRepayOrderAction {
    private static final Logger log = LoggerFactory.getLogger(CreditRepayOrderAction.class);

    @Resource
    private CreditRepayOrderService creditRepayOrderService;
    @Resource
    private CreditRepayOrderDetailService creditRepayOrderDetailService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SysDictService sysDictService;


    /**
     * 查询信用卡还款订单列表
     */
    @RequestMapping(value = "/listCreditRepayOrder.do")
    @ResponseBody
    public ResponseBean listCreditRepayOrder(@RequestParam("baseInfo") String param,
                                      Page<CreditRepayOrder> page) {
        try{
            CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<CreditRepayOrder> creditRepayOrders = creditRepayOrderService.listCreditRepayOrder(order,loginAgent, page);
            CreditRepayOrder orderAll=creditRepayOrderService.countCreditRepayOrder(order, loginAgent);
            Map<String, Object> resultMap=new HashMap<>();
            if(creditRepayOrders != null && !creditRepayOrders.isEmpty()){
                for (CreditRepayOrder temp : creditRepayOrders){
                    if (loginAgent.getAgentLevel() != 1){
                        temp.setMobileNo(StringUtils.trimToEmpty(temp.getMobileNo()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
                    }
                    temp.setUserName(StringUtils.trimToEmpty(temp.getUserName()).replaceAll("^(.).*?$", "$1**"));
                    temp.setAccountNo(StringUtils.trimToEmpty(temp.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$", "$1****$2"));
                }
            }

            resultMap.put("list",creditRepayOrders);
            resultMap.put("orderAll",orderAll);
            return new ResponseBean(resultMap, page.getTotalCount());
        } catch (Exception e){
            log.error("查询信用卡还款订单列表失败!",e);
            return new ResponseBean(e);
        }
    }


    /**
     * 查询订单详情
     * @param batchNo 批次号
     */
    @RequestMapping(value = "/selectByBatchNo/{batchNo}")
    @ResponseBody
    public Map<String,Object> selectByBatchNo(@PathVariable("batchNo") String batchNo) throws Exception {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            CreditRepayOrder order = creditRepayOrderService.selectById(batchNo, loginAgent);
            List<CreditRepayOrderDetail> detailList = null;
            if (order != null){
                detailList=creditRepayOrderDetailService.selectDetailList(batchNo);
            }
            if (order != null){
                if (loginAgent.getAgentLevel() != 1){
                    order.setMobileNo(StringUtils.trimToEmpty(order.getMobileNo()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
                }
                order.setIdCardNo(StringUtils.trimToEmpty(order.getIdCardNo()).replaceAll("^(.{2}).*?(.{2})$", "$1****$2"));
                order.setUserName(StringUtils.trimToEmpty(order.getUserName()).replaceAll("^(.).*$", "$1**"));
                order.setAccountNo(StringUtils.trimToEmpty(order.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$","$1****$2"));
            }
            if (detailList != null && !detailList.isEmpty()){
                for (CreditRepayOrderDetail detail : detailList){
                    detail.setAccountNo(StringUtils.trimToEmpty(detail.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$","$1****$2"));
                }
            }

            msg.put("order",order);
            msg.put("detailList",detailList);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询订单详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询订单详情失败!");
        }
        return msg;
    }


    /**
     * 导出数据表
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/exportCreditRepayOrder")
    @ResponseBody
    public void exportCreditRepayOrder(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception {
        CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        List<CreditRepayOrder> list=creditRepayOrderService.exportSelectAllList(order, loginAgent);
        export(list,response,request,loginAgent);
    }


    /**
     * 查询信用卡还款订单列表
     * @param param
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectDetail.do")
    @ResponseBody
    public Map<String, Object> selectDetailByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<CreditRepayOrderDetail> page) throws Exception {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            CreditRepayOrderDetail orderDetail = JSONObject.parseObject(param, CreditRepayOrderDetail.class);
            creditRepayOrderDetailService.selectDetailAllList(orderDetail,loginAgent, page);
            if (page.getResult() != null && !page.getResult().isEmpty()){
                for (CreditRepayOrderDetail detail : page.getResult()){
                    detail.setAccountNo(StringUtils.trimToEmpty(detail.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$", "$1****$2"));
                }
            }
            msg.put("page",page);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询信用卡还款订单列表失败!",e);
            msg.put("msg","查询信用卡还款订单列表失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 导出还款订单处理流水
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/exportDetailAllInfo")
    @ResponseBody
    public void exportDetailAllInfo(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception {
        CreditRepayOrderDetail orderDetail = JSONObject.parseObject(param, CreditRepayOrderDetail.class);

        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        List<CreditRepayOrderDetail> list=creditRepayOrderDetailService.exportDetailAllList(orderDetail, loginAgent);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "还款订单处理流水"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("planNo",null);
            maps.put("planAmount",null);
            maps.put("accountNo",null);
            maps.put("batchNo",null);
            maps.put("merchantNo",null);
            maps.put("planType",null);
            maps.put("planStatus",null);
            maps.put("resMsg",null);
            maps.put("createTime",null);
            maps.put("planTime",null);
            maps.put("bak1",null);
            data.add(maps);
        }else{
            for (CreditRepayOrderDetail or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("planNo",or.getPlanNo()==null?"":or.getPlanNo());
                maps.put("planAmount",or.getPlanAmount()==null?null:or.getPlanAmount().toString());
                String accountNo = StringUtils.trimToEmpty(or.getAccountNo());
                accountNo = accountNo.replaceAll("^(.{6}).*?(.{4})$", "$1****$2");
                maps.put("accountNo",accountNo);
                maps.put("batchNo",or.getBatchNo()==null?"":or.getBatchNo());
                maps.put("merchantNo",or.getMerchantNo()==null?"":or.getMerchantNo());
                if(or.getPlanType()!=null){
                    if("IN".equals(or.getPlanType())){
                        maps.put("planType","给用户还款");
                    }else if("OUT".equals(or.getPlanType())){
                        maps.put("planType","用户消费");
                    }else{
                        maps.put("planType","");
                    }
                }else{
                    maps.put("planType","");
                }
                if(or.getPlanStatus()!=null){
                    if("0".equals(or.getPlanStatus())){
                        maps.put("planStatus","未执行");
                    }else if("1".equals(or.getPlanStatus())){
                        maps.put("planStatus","执行中");
                    }else if("2".equals(or.getPlanStatus())){
                        maps.put("planStatus","执行成功");
                    }else if("3".equals(or.getPlanStatus())){
                        maps.put("planStatus","执行失败");
                    }else{
                        maps.put("planStatus","");
                    }
                }else{
                    maps.put("planStatus","");
                }
                maps.put("resMsg",or.getResMsg()==null?"":or.getResMsg());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("planTime", or.getPlanTime()==null?"":sdf1.format(or.getPlanTime()));
                maps.put("bak1",or.getBak1()==null?"":or.getBak1());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"planNo","planAmount","accountNo","batchNo","merchantNo","planType",
                "planStatus","resMsg","createTime","planTime", "bak1"
        };

        String[] colsName = new String[]{"任务流水号","金额(元)","还款卡号","来源订单号","用户编号","类型",
                "状态","错误信息","创建时间","计划时间", "备注"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出还款订单处理流水失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }


    /**
     * 查询异常信用卡还款订单列表
     * @param param
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectAbnormalByParam.do")
    @ResponseBody
    public Map<String, Object> selectAbnormalByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<CreditRepayOrder> page) throws Exception {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
            order.setStatus("5");
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            creditRepayOrderService.listCreditRepayOrder(order, loginAgent, page);
            msg.put("page",page);
            CreditRepayOrder orderAll=creditRepayOrderService.countCreditRepayOrder(order, loginAgent);
            msg.put("orderAll",orderAll);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询异常信用卡还款订单查询失败!",e);
            msg.put("msg","查询异常信用卡还款订单查询失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 导出数据表
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/exportAbnormalAllInfo")
    @ResponseBody
    public void exportAbnormalAllInfo(@RequestParam("baseInfo") String param, HttpServletResponse response, HttpServletRequest request) throws Exception {
        CreditRepayOrder order = JSONObject.parseObject(param, CreditRepayOrder.class);
        order.setStatus("5");
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        List<CreditRepayOrder> list=creditRepayOrderService.exportSelectAllList(order, loginAgent);
        export(list,response,request, loginAgent);
    }

    /**
     * 模板导出
     * @param list
     * @param response
     * @param request
     * @param loginAgent
     * @throws Exception
     */
    private void export(List<CreditRepayOrder> list, HttpServletResponse response, HttpServletRequest request, AgentInfo loginAgent) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "信用卡还款订单查询"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("batchNo",null);
            maps.put("merchantNo", null);
            maps.put("nickname", null);
            maps.put("userName", null);
            maps.put("mobileNo", null);
            maps.put("status", null);
            maps.put("repayAmount", null);
            maps.put("ensureAmount", null);
            maps.put("repayFee", null);
            maps.put("successPayAmount", null);
            maps.put("successRepayAmount", null);
            maps.put("actualPayFee", null);
            maps.put("actualWithdrawFee", null);
            maps.put("repayNum", null);
            maps.put("accountNo", null);
            maps.put("bankName", null);
            maps.put("createTime", null);
            maps.put("repayBeginTime", null);
            maps.put("repayEndTime", null);
            maps.put("mission", null);
            maps.put("billingStatus", null);
            maps.put("agentName", null);
            maps.put("repayType", null);
            maps.put("completeTime", null);
            data.add(maps);
        }else{
            for (CreditRepayOrder or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("batchNo",or.getBatchNo()==null?"":or.getBatchNo());
                maps.put("merchantNo", or.getMerchantNo()==null?"":or.getMerchantNo());
                maps.put("nickname", or.getNickname()==null?"":or.getNickname());
                String userName =StringUtils.trimToEmpty(or.getUserName());
                userName = userName.replaceAll("^(.).*$", "$1**");
                maps.put("userName", userName);
                String mobileNo =StringUtils.trimToEmpty(or.getMobileNo());
                if (loginAgent.getAgentLevel() != 1){
                    mobileNo = mobileNo.replaceAll("^(.{3}).*?(.{4})$", "$1****$2");
                }
                maps.put("mobileNo", mobileNo);
                if(or.getStatus()!=null){
                    if("0".equals(or.getStatus())){
                        maps.put("status","初始化");
                    }else if("1".equals(or.getStatus())){
                        maps.put("status","未执行");
                    }else if("2".equals(or.getStatus())){
                        maps.put("status","还款中");
                    }else if("3".equals(or.getStatus())){
                        maps.put("status","还款成功");
                    }else if("4".equals(or.getStatus())){
                        maps.put("status","还款失败");
                    }else if("5".equals(or.getStatus())){
                        maps.put("status","挂起");
                    }else if("6".equals(or.getStatus())){
                        maps.put("status","终止");
                    }else if("7".equals(or.getStatus())){
                        maps.put("status","逾期待还");
                    }else{
                        maps.put("status","");
                    }
                }else{
                    maps.put("status","");
                }
                maps.put("repayAmount", or.getRepayAmount()==null?"":or.getRepayAmount().toString());
                maps.put("ensureAmount", or.getEnsureAmount()==null?"":or.getEnsureAmount().toString());
                maps.put("repayFee", or.getRepayFee()==null?"":or.getRepayFee().toString());

                maps.put("successPayAmount", or.getSuccessPayAmount()==null?"":or.getSuccessPayAmount().toString());
                maps.put("successRepayAmount", or.getSuccessRepayAmount()==null?"":or.getSuccessRepayAmount().toString());
                maps.put("actualPayFee", or.getActualPayFee()==null?"":or.getActualPayFee().toString());
                maps.put("actualWithdrawFee", or.getActualWithdrawFee()==null?"":or.getActualWithdrawFee().toString());


                maps.put("repayNum", or.getRepayNum() + "");
                String accountNo = StringUtils.trimToEmpty(or.getAccountNo());
                accountNo = accountNo.replaceAll("^(.{6}).*?(.{4})$", "$1****$2");
                maps.put("accountNo", accountNo);
                maps.put("bankName", or.getBankName()==null?"":or.getBankName());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                maps.put("repayBeginTime", or.getRepayBeginTime()==null?"":sdf1.format(or.getRepayBeginTime()));
                maps.put("repayEndTime", or.getRepayEndTime()==null?"":sdf1.format(or.getRepayEndTime()));
                maps.put("mission", or.getMission()==null?"":or.getMission());
                if(or.getBillingStatus()!=null){
                    if("0".equals(or.getBillingStatus())){
                        maps.put("billingStatus","未记账");
                    }else if("1".equals(or.getBillingStatus())){
                        maps.put("billingStatus","发起记账失败");
                    }else if("2".equals(or.getBillingStatus())){
                        maps.put("billingStatus","记账成功");
                    }else if("3".equals(or.getBillingStatus())){
                        maps.put("billingStatus","记账失败");
                    }else{
                        maps.put("billingStatus","");
                    }
                }else{
                    maps.put("billingStatus","");
                }
                maps.put("agentName", or.getAgentName());
                maps.put("repayType", StringUtils.equals("1",or.getRepayType())?"分期还款":StringUtils.equals("2",or.getRepayType())?"全额还款":"完美还款");
                maps.put("completeTime", or.getCompleteTime()==null?"":sdf1.format(or.getCompleteTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"batchNo","merchantNo","nickname","userName","mobileNo","agentName", "status",
                "repayAmount","ensureAmount","repayFee","successPayAmount","successRepayAmount","actualPayFee","actualWithdrawFee",
                "repayNum","accountNo","bankName","createTime","repayBeginTime",
                "repayEndTime","completeTime","mission","repayType", "billingStatus"
        };
        String[] colsName = new String[]{"订单ID","用户编号","昵称","姓名","手机号","代理商名称","订单状态","任务金额(元)",
                "保证金(元)","服务费(元)","已消费总额(元)","已还款总额(元)","实际消费手续费(元)","实际还款手续费(元)",
                "还款期数","还款卡号","还款银行","创建时间","开始时间","结束时间","终态时间","任务","订单类型", "记账状态"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出信用卡还款订单数据失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    /**
     * 分润预调帐查询
     * @return
     * @throws Exception
     * @author rpc
     * @date 2019-09-12 14:37:57
     */
    @RequestMapping(value = "/profitAdvanceQuery")
    @ResponseBody
    public Object profitAdvanceQuery(@RequestParam("info") String param){
        log.info("=================>>进入分润预调账明细查询!");
        log.info("=================>>param="+param);
        Map<String, Object> data = new HashMap<String, Object>();
        try{
            JSONObject json = JSON.parseObject(param);
            SysDict sysDict = sysDictService.getByKey("ACCOUNT_SERVICE_URL");
            String accessUrl = sysDict.getSysValue() + "/agentProfitController/findAgentAdjustApplyNoteList.do";

            data = agentInfoService.profitAdvanceQuery(accessUrl,json);
        }catch(Exception e){
            log.error("分润预调账明细查询异常!",e);
            data.put("bols", false);
            data.put("msg", "分润预调账明细查询异常");
        }
        return data;
    }

    /**
     * 分润预调账明细导
     * @param info
     * @param response
     * @param request
     * @throws Exception
     * @author rpc
     * @date 2019-09-16 16:12:48
     */
    @SystemLog(description = "分润预调账明细导出")
    @RequestMapping(value="/profitAdvanceExport")
    @ResponseBody
    public void profitAdvanceExport(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
        info = new String(info.getBytes("ISO-8859-1"),"UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        String fileName = "分润预调账明细"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>();
        SysDict sysDict = sysDictService.getByKey("ACCOUNT_SERVICE_URL");
        String accessUrl = sysDict.getSysValue() + "/agentProfitController/exportAgentAdjustApplyNoteList.do";
        String dataStr = agentInfoService.profitAdvanceExport(accessUrl,jsonObject);
        str2List(data, dataStr);
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"agentNo","agentName","applyDate","applyFreezeAmount","availableAmount",
                "generateAmount","remark"};
        String[] colsName = new String[]{"服务商编号","服务商名称","申请预调账时间","调账金额","账户可用余额调账金额",
                "预调账金额","备注"};
        OutputStream ouputStream = response.getOutputStream();
        export.export(cols, colsName, data, response.getOutputStream());
        ouputStream.close();
    }
    private void str2List(List<Map<String, String>> data, String dataStr) {
        if(StringUtils.isNotBlank(dataStr)){
            JSONArray jsonArray = JSONArray.parseArray(dataStr);
            Map<String, String> map = null;
            JSONObject jsObject = null;
            Set<String> keySet = null;
            for (Object obj:jsonArray) {
                map = new HashMap<String,String>();
                jsObject = JSONObject.parseObject(obj.toString());
                keySet = jsObject.keySet();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (String key:keySet) {
                    if(key.toLowerCase().contains("time")||key.toLowerCase().contains("date")){
                        map.put(key,simpleDateFormat.format(new Date(Long.valueOf(jsObject.getString(key)))));
                    }else{
                        map.put(key,String.valueOf(jsObject.get(key)));
                    }
                }
                data.add(map);
            }
        }
    }

}
