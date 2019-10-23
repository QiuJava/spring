package cn.eeepay.boss.action;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.boss.util.ConfigUtil;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.YfbProfitCollection;
import cn.eeepay.framework.model.nposp.YfbProfitDetail;
import cn.eeepay.framework.model.nposp.YfbRepayPlan;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.nposp.YfbProfitCollectionService;
import cn.eeepay.framework.service.nposp.YfbProfitDetailService;
import cn.eeepay.framework.service.nposp.YfbRepayPlanService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.NewListDataExcelExport;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/serviceDuiAccount")
public class ServiceDuiAccountAction {

    private static final Logger log = LoggerFactory.getLogger(ServiceDuiAccountAction.class);

    @Resource
    public SysDictService sysDictService;
    @Resource
    public YfbRepayPlanService yfbRepayPlanService;
    @Resource
    public YfbProfitDetailService yfbProfitDetailService;
    @Resource
    public YfbProfitCollectionService yfbProfitCollectionService;


    /**
     * 信用卡还款订单页面跳转
     *
     * @param model
     * @param params
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('cardOrderList:query')")
    @RequestMapping(value = "/toCardOrderList.do")
    public String toCardOrderList(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
        log.info("进入信用卡还款订单查询页面---->");

        List<SysDict> cardRepaymentStatusList = null;

        try {
            cardRepaymentStatusList = sysDictService.findSysDictGroup("card_repayment_status");
        } catch (Exception e) {
            log.info("进入信用卡还款订单查询页面异常----> " + e.toString());
        }

        model.put("cardRepaymentStatusList", cardRepaymentStatusList);
        model.put("params", params);
        return "creditCardRepayment/cardOrderQuery";
    }

    /**
     * 查询信用卡还款订单明细
     *
     * @param sort
     * @param page
     * @return
     */
    @PreAuthorize("hasAuthority('cardOrderList:query')")
    @RequestMapping(value = "/findCardOrderList.do")
    @ResponseBody
    public Page<YfbRepayPlan> findSuperPushShareList(
            @ModelAttribute("yfbRepayPlan") YfbRepayPlan yfbRepayPlan,
            @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<YfbRepayPlan> page) {
        log.info("进入查询信用卡还款订单明细grid--->");
        try {
            yfbRepayPlanService.findSuperPushShareList(yfbRepayPlan,sort,page);
        } catch (Exception e) {
            log.error("查询信用卡还款订单明细grid异常:", e);
        }
        return page;
    }


    /**
     * 导出数据(信用卡还款订单)
     *
     * @param params
     * @param response
     * @param request
     * @throws IOException
     */
    @PreAuthorize("hasAuthority('cardOrderList:export')")
    @RequestMapping(value = "exportCardOrderList.do", method = RequestMethod.POST)
    public void exportSuperPushShareList(@RequestParam Map<String, String> params,
                                         @ModelAttribute YfbRepayPlan yfbRepayPlan, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        //用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "信用卡还款订单数据导出_" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        try {
            List<YfbRepayPlan> list = yfbRepayPlanService.exportSuperPushShareList(yfbRepayPlan);
            if(list!=null && list.size()>0){
                for(YfbRepayPlan yrp:list){
                    Map<String,String> map = new HashMap<>();
                    map.put("batchNo",yrp.getBatchNo());
                    map.put("merchantNo",yrp.getMerchantNo());
                    map.put("userName",yrp.getUserName());
                    map.put("mobileNo",yrp.getMobileNo());
                    map.put("repayAmount",String.valueOf(yrp.getRepayAmount().doubleValue()));
                    map.put("ensureAmount",String.valueOf(yrp.getEnsureAmount().doubleValue()));
                    map.put("repayFee",String.valueOf(yrp.getRepayFee().doubleValue()));
                    map.put("countTime", yrp.getCountTime()==null?"":DateUtil.getLongFormatDate(yrp.getCountTime()));
                    map.put("serviceOrderNo",yrp.getServiceOrderNo());
                    String status = yrp.getRuStatus();
                    if(status.equals("0")){
                        status = "未记账";
                    }else if(status.equals("1")){
                        status = "发起记账失败";
                    }else if(status.equals("2")){
                        status = "记账成功";
                    }else if(status.equals("3")){
                        status = "记账失败";
                    }else {
                        status = "";
                    }
                    map.put("ruStatus",status);
                    map.put("tallyTime",yrp.getTallyTime()==null?"":DateUtil.getLongFormatDate(yrp.getTallyTime()));
                    data.add(map);
                }
            }
        } catch (Exception e) {
            log.error("导出数据(信用卡还款订单查询)异常:", e);
        }

        NewListDataExcelExport export = new NewListDataExcelExport();
        String[] cols = new String[] {
                "batchNo", "merchantNo",
                "userName", "mobileNo",
                "repayAmount", "ensureAmount",
                "repayFee", "countTime",
                "serviceOrderNo",
                "ruStatus", "tallyTime"};
        String[] colsName = new String[] {
                "订单ID","还款人ID",
                "姓名","手机号",
                "任务金额","保证金",
                "服务费","汇总时间",
                "汇总批次",
                "入账状态","入账时间"};
        OutputStream os = response.getOutputStream();
        export.export(cols, colsName, data, os);
        os.close();
    }

    /**
     * 入账成功总金额和未入账金额
     * @return
     */
    @PreAuthorize("hasAuthority('cardOrderCollectionDataCount:count')")
    @RequestMapping(value = "/cardOrderCollectionDataCount.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> cardOrderCollectionDataCount(@ModelAttribute YfbRepayPlan yfbRepayPlan){
        Map<String,Object> map = new HashMap<>();

        try{
            Map<String,Object> mapWait = yfbRepayPlanService.cardOrderCollectionDataCount(yfbRepayPlan,0);      //获取未入账
            if(mapWait==null){
                map.put("mapSuccess","0.00");
                map.put("mapWait","0.00");
                return map;
            }
            BigDecimal count = (BigDecimal) mapWait.get("allEnterAmount");
            map.put("mapWait",count==null?"0.00":String.valueOf(count.doubleValue()));
            Map<String,Object> mapSuccess = yfbRepayPlanService.cardOrderCollectionDataCount(yfbRepayPlan,2);      //获取记账成功
            count = (BigDecimal) mapSuccess.get("allEnterAmount");
            map.put("mapSuccess",count==null?"0.00":String.valueOf(count.doubleValue()));

        }catch (Exception e){
            log.error("信用卡还款订单入账汇总数据查询异常-----"+e.getMessage());
        }

        return map;

    }



    /**
     * 服务商分润入账页面跳转
     *
     * @param model
     * @param params
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('serviceInAccountList:query')")
    @RequestMapping(value = "/toServiceInAccountList.do")
    public String toServiceInAccountList(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
        log.info("进入服务商分润入账页面---->");

        List<SysDict> cardRepaymentStatusList = null;

        try {
            cardRepaymentStatusList = sysDictService.findSysDictGroup("card_repayment_status");
        } catch (Exception e) {
            log.info("进入服务商分润入账页面异常----> " + e.toString());
        }

        model.put("cardRepaymentStatusList", cardRepaymentStatusList);
        model.put("params", params);
        return "creditCardRepayment/serviceInAccount";
    }


    /**
     * 服务商分润入账明细
     *
     * @param sort
     * @param page
     * @return
     */
    @PreAuthorize("hasAuthority('serviceInAccountList:query')")
    @RequestMapping(value = "/serviceInAccountList.do")
    @ResponseBody
    public Page<YfbProfitCollection> serviceInAccountList(
            @ModelAttribute("yfbProfitCollection") YfbProfitCollection yfbProfitCollection,
            @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<YfbProfitCollection> page) {
        log.info("进入查询信用卡还款订单明细grid--->");
        try {
            yfbProfitCollectionService.findServiceShareList(yfbProfitCollection,sort,page);
        } catch (Exception e) {
            log.error("查询信用卡还款订单明细grid异常:", e);
        }
        return page;
    }

    /**
     * 服务商分润入账总额
     *
     * @param sort
     * @param page
     * @return
     */
    @PreAuthorize("hasAuthority('serviceInAccountCollectionDataCount:collectionData')")
    @RequestMapping(value = "/serviceInAccountCollectionDataCount.do")
    @ResponseBody
    public Map<String,String> serviceInAccountCollectionDataCount(
            @ModelAttribute("yfbProfitCollection") YfbProfitCollection yfbProfitCollection,
            @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<YfbProfitCollection> page) {
        log.info("进入查询信用卡还款订单明细grid--->");
        Map<String,String> map = new HashMap<>();
        try {
            Map<String,Object> countMap = yfbProfitCollectionService.serviceInAccountCollectionDataCount(yfbProfitCollection);
            if(countMap==null){
                map.put("mapSuccess","0.00");
                return map;
            }
            BigDecimal count = (BigDecimal) countMap.get("countAmount");
            map.put("mapSuccess",count==null?"0.00":String.valueOf(count.doubleValue()));
        } catch (Exception e) {
            log.error("查询信用卡还款订单明细grid异常:", e);
        }

        return map;
    }


    /**
     * 导出数据(信用卡服务商分润入账)
     *
     * @param params
     * @param response
     * @param request
     * @throws IOException
     */
    @PreAuthorize("hasAuthority('serviceInAccountList:export')")
    @RequestMapping(value = "exportServiceInAccountList.do", method = RequestMethod.POST)
    public void exportServiceInAccountList(@RequestParam Map<String, String> params,
                                         @ModelAttribute YfbProfitCollection yfbProfitCollection, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        //用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "信用卡还款服务商分润入账数据导出_" + sdf.format(new Date()) + ".xlsx";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if(!StringUtils.isBlank(yfbProfitCollection.getAgentName())){
            yfbProfitCollection.setAgentName(URLDecoder.decode(yfbProfitCollection.getAgentName(),"UTF-8"));
        }
        try {
            List<YfbProfitCollection> list = yfbProfitCollectionService.exportServiceInAccountList(yfbProfitCollection);
            if(list!=null && list.size()>0){
                for(YfbProfitCollection yrp:list){
                    Map<String,String> map = new HashMap<>();
                    map.put("collectionBatchNo",yrp.getCollectionBatchNo());
                    map.put("collectionTime",yrp.getCollectionTime()==null?"":DateUtil.getLongFormatDate(yrp.getCollectionTime()));
                    map.put("merNo",yrp.getMerNo());
                    map.put("agentName",yrp.getAgentName());
                    map.put("agentLevel",yrp.getAgentLevel());

                    String profitType = "";
                    if(StringUtils.isNotBlank(yrp.getProfitType())){
                        if(yrp.getProfitType().equals("1")){
                            profitType = "分期还款分润";
                        }else if(yrp.getProfitType().equals("2")){
                            profitType = "全额还款分润";
                        }else if(yrp.getProfitType().equals("3")){
                            profitType = "保证金分润";
                        }else if(yrp.getProfitType().equals("4")){
                            profitType = "完美还款分润";
                        }
                    }
                    map.put("profitType",profitType);

                    map.put("profitAmount",String.valueOf(yrp.getProfitAmount().doubleValue()));
                    map.put("serviceCostRate",String.valueOf(yrp.getServiceCostRate().doubleValue()));
                    map.put("createTime",yrp.getCreateTime()==null?"":DateUtil.getLongFormatDate(yrp.getCreateTime()));
                    map.put("incomeTime", yrp.getIncomeTime()==null?"":DateUtil.getLongFormatDate(yrp.getIncomeTime()));
                    Integer i = yrp.getIncomeStatus();
                    String status = "";
                    if(i==0){
                        status = "未入账";
                    }else if(i==1){
                        status = "已入账";
                    }
                    map.put("incomeStatus",status);


                    String ai = yrp.getAllowIncome()==null?"":yrp.getAllowIncome();
                    String allowIncome = "";
                    if(ai.equals("1")){
                        allowIncome = "需要";
                    }else if(ai.equals("0")){
                        allowIncome = "不需要";
                    }
                    map.put("allowIncome",allowIncome);


                    data.add(map);
                }
            }
        } catch (Exception e) {
            log.error("导出数据(信用卡还款入账信息查询)异常:", e);
        }

        NewListDataExcelExport export = new NewListDataExcelExport();
        String[] cols = new String[] {
                "collectionBatchNo", "merNo",
                "agentName", "agentLevel","profitType","profitAmount",
                "serviceCostRate", "collectionTime","createTime",
                "incomeStatus","allowIncome", "incomeTime"};
        String[] colsName = new String[] {
                "批次","服务商编号",
                "服务商名称","代理商级别","分润类型","分润",
                "实际费率","交易时间","创建时间",
                "入账状态","是否需要入账","入账时间"};
        OutputStream os = response.getOutputStream();
        export.export(cols, colsName, data, os);
        os.close();

    }


    /**
     * 服务商分润入账
     * @param createDate
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('serviceShareInAccount:batchEnterAccount')")
    @RequestMapping(value = "serviceShareInAccount.do")
    @Logs(description="代理商分润入账")
    @ResponseBody
    public Map<String,Object> serviceShareInAccount(String createDate,String batchNo,Integer type) throws Exception{
        log.info("进入代理商分润入账------> ");
        Map<String,Object> msg=new HashMap<>();
        Map<String,String> map = new HashMap<>();
        boolean isReturn = false;
        if(type==1){
            map.put("tallyDate",createDate);
            if (StringUtils.isBlank(createDate)) {
                msg.put("msg","交易日期不能为空");
                msg.put("status",false);
                isReturn = true;
            }
        }else{
            map.put("collectionNo",batchNo);
            if (StringUtils.isBlank(batchNo)) {
                msg.put("msg","请选择需要的入账编号");
                msg.put("status",false);
                isReturn = true;
            }
        }

        if (!isReturn) {
            // 获取到登录者信息
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            try {
                String username = userInfo.getUsername();
                //Map<String,Object> result = yfbProfitDetailService.serviceShareCollection(createDate,username);
                //请求汇总接口
                String boosUrl = ConfigUtil.getConfig("v2.boos2.url");
                boosUrl = boosUrl+"/tally/agentProfit";
                String result = HttpConnectUtil.postHttp(boosUrl,map);
                JSONObject jsonObject = JSONObject.parseObject(result);
                Integer status = (Integer) jsonObject.get("status");
                String msgStr = (String) jsonObject.get("msg");
                List data = (List) jsonObject.get("data");
                if(status==200){
                    msg.put("msg","入账成功");
                    msg.put("status",true);
                    if(data!=null && data.size()==0){
                        data = null;
                    }
                    msg.put("data",data);
                    log.info(msg.toString());
                }else{
                    msg.put("msg",msgStr);
                    msg.put("status",false);
                    log.info(msg.toString());
                }
            } catch (Exception e) {
                //e.printStackTrace();
                msg.put("status",false);
                msg.put("msg",e.toString());
                log.error("代理商分润入账异常:",e);
            }
        }

        return msg;
    }






    /**
     * 服务商查询页面跳转
     *
     * @param model
     * @param params
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('serviceFenDuiAccountList:query')")
    @RequestMapping(value = "/toServiceFenDuiAccountList.do")
    public String toServiceFenDuiAccountList(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
        log.info("进入服务商查询页面---->");

        List<SysDict> cardRepaymentStatusList = null;

        try {
            cardRepaymentStatusList = sysDictService.findSysDictGroup("card_repayment_status");
        } catch (Exception e) {
            log.info("进入服务商查询页面异常----> " + e.toString());
        }

        model.put("cardRepaymentStatusList", cardRepaymentStatusList);
        model.put("params", params);
        return "creditCardRepayment/serviceFenDuiAccount";
    }


    /**
     * 服务商分润查询列表
     *
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('serviceFenDuiAccountList:query')")
    @RequestMapping(value = "/serviceFenDuiAccountList.do")
    @ResponseBody
    public Page<YfbProfitDetail> findSuperPushShareList(
            @ModelAttribute("yfbProfitDetail") YfbProfitDetail yfbProfitDetail,
            @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<YfbProfitDetail> page) {
        log.info("进入服务商分润查询列表grid--->");
        try {
            yfbProfitDetailService.findServicePushShareList(yfbProfitDetail,sort,page);
        } catch (Exception e) {
            log.error("服务商分润查询列表grid异常:", e);
        }
        return page;
    }


    /**
     * 服务商分润汇总
     * @param createDate
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('serviceShareDaySettle:collection')")
    @RequestMapping(value = "serviceShareCollection.do")
    @Logs(description="代理商分润汇总")
    @ResponseBody
    public Map<String,Object> serviceShareCollection(String createDate) throws Exception{
        log.info("进入代理商分润汇总------> ");
        Map<String,Object> msg=new HashMap<>();
        boolean isReturn = false;
        if (StringUtils.isBlank(createDate)) {
            msg.put("msg","交易日期不能为空");
            msg.put("status",false);
            isReturn = true;
        }
        if (!isReturn) {
            // 获取到登录者信息
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            try {
                String username = userInfo.getUsername();
                //Map<String,Object> result = yfbProfitDetailService.serviceShareCollection(createDate,username);
                //请求汇总接口
                String boosUrl = ConfigUtil.getConfig("v2.boos2.url");
                Map<String,String> map = new HashMap<>();
                map.put("profitDate",createDate);
                map.put("operator",username);
                boosUrl = boosUrl+"/summary/agentProfit";
                String result = HttpConnectUtil.postHttp(boosUrl,map);
                JSONObject jsonObject = JSONObject.parseObject(result);
                Integer status = (Integer) jsonObject.get("status");
                String msgStr = (String) jsonObject.get("msg");
                if(status==200){
                    msg.put("msg","汇总成功");
                    msg.put("status",true);
                    log.info(msg.toString());
                }else{
                    msg.put("msg",msgStr);
                    msg.put("status",false);
                    log.info(msg.toString());
                }
            } catch (Exception e) {
                //e.printStackTrace();
                msg.put("status",false);
                msg.put("msg",e.toString());
                log.error("代理商分润汇总异常:",e);
            }
        }

        return msg;
    }

    /**
     * 导出数据(服务商分润明细)
     *
     * @param params
     * @param response
     * @param request
     * @throws IOException
     */
    @PreAuthorize("hasAuthority('serviceShareList:export')")
    @RequestMapping(value = "/exportServiceShareList.do", method = RequestMethod.POST)
    public void exportServiceShareList(@RequestParam Map<String, String> params,
                                         @ModelAttribute YfbProfitDetail yfbProfitDetail, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        //用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "服务商分润记录数据导出_" + sdf.format(new Date()) + ".xlsx";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        if(!StringUtils.isBlank(yfbProfitDetail.getAgentName())){
            yfbProfitDetail.setAgentName(URLDecoder.decode(yfbProfitDetail.getAgentName(),"UTF-8"));
        }
        if(!StringUtils.isBlank(yfbProfitDetail.getUserName())){
            yfbProfitDetail.setUserName(URLDecoder.decode(yfbProfitDetail.getUserName(),"UTF-8"));
        }

        try {
            List<YfbProfitDetail> list = yfbProfitDetailService.exportServiceShareList(yfbProfitDetail);
            if(list!=null && list.size()>0){
                for(YfbProfitDetail yrp:list){
                    Map<String,String> map = new HashMap<>();
                    map.put("orderNo",yrp.getOrderNo());
                    map.put("collectionBatchNo",yrp.getCollectionBatchNo());
                    map.put("transTime",yrp.getTransTime()==null?"":DateUtil.getLongFormatDate(yrp.getTransTime()));
                    map.put("profitMerNo",yrp.getProfitMerNo());
                    map.put("agentName",yrp.getAgentName());

                    map.put("agentLevel",yrp.getAgentLevel());
                    map.put("oneAgentName",yrp.getOneAgentName());
                    map.put("oneLevelId",yrp.getOneLevelId());

                    String profitType = "";
                    if(StringUtils.isNotBlank(yrp.getProfitType())){
                        if(yrp.getProfitType().equals("1")){
                            profitType = "分期还款分润";
                        }else if(yrp.getProfitType().equals("2")){
                            profitType = "全额还款分润";
                        }else if(yrp.getProfitType().equals("3")){
                            profitType = "保证金分润";
                        }else if(yrp.getProfitType().equals("4")){
                            profitType = "完美还款分润";
                        }
                    }
                    map.put("profitType",profitType);

                    map.put("successPayAmount",yrp.getSuccessPayAmount()==null||yrp.getProfitType().equals("3")?"":String.valueOf(yrp.getSuccessPayAmount().doubleValue()));
                    map.put("successRepayAmount",yrp.getSuccessRepayAmount()==null||yrp.getProfitType().equals("3")?"":String.valueOf(yrp.getSuccessRepayAmount().doubleValue()));
                    map.put("acqCode",yrp.getAcqCode());
                    map.put("fee",yrp.getFee()==null?"":String.valueOf(yrp.getFee().doubleValue()));
                    map.put("payFl",yrp.getPayFl()==null?"":String.valueOf(yrp.getPayFl().doubleValue()));



                    map.put("shareAmount",yrp.getShareAmount()==null?"":String.valueOf(yrp.getShareAmount().doubleValue()));
                    map.put("toProfitAmount",yrp.getToProfitAmount()==null?"":String.valueOf(yrp.getToProfitAmount().doubleValue()));
                    map.put("repayAmount",yrp.getRepayAmount()==null||yrp.getProfitType().equals("3")?"":String.valueOf(yrp.getRepayAmount().doubleValue()));
                    map.put("ensureAmount",yrp.getEnsureAmount()==null||yrp.getProfitType().equals("3")||yrp.getProfitType().equals("4")?"":String.valueOf(yrp.getEnsureAmount().doubleValue()));
                    map.put("repayFee",yrp.getRepayFee()==null||yrp.getProfitType().equals("3")?"":String.valueOf(yrp.getRepayFee().doubleValue()));
                    map.put("actualPayFee",yrp.getActualPayFee()==null||yrp.getProfitType().equals("3")?"":String.valueOf(yrp.getActualPayFee().doubleValue()));
                    map.put("actualWithdrawFee",yrp.getActualWithdrawFee()==null||yrp.getProfitType().equals("3")?"":String.valueOf(yrp.getActualWithdrawFee().doubleValue()));
                    map.put("repayFeeRate",yrp.getRepayFeeRate());
                    map.put("merchantNo",yrp.getMerchantNo());
                    map.put("userName",yrp.getUserName());

                    data.add(map);
                }
            }
        } catch (Exception e) {
            log.error("导出数据(信用卡还款订单查询)异常:", e.getMessage());
        }

        NewListDataExcelExport export = new NewListDataExcelExport();
        String[] cols = new String[] {
                "orderNo","collectionBatchNo", "transTime",
                "profitMerNo", "agentName",
                "agentLevel","oneAgentName",
                "oneLevelId","profitType","successPayAmount",
                "successRepayAmount","acqCode",
                "fee","payFl",
                "shareAmount","toProfitAmount", "repayAmount",
                "ensureAmount", "repayFee",
                "actualPayFee",
                "actualWithdrawFee", "repayFeeRate",
                "merchantNo","userName"};
        String[] colsName = new String[] {
                "关联还款订单","汇总批次号","订单时间",
                "服务商编号","服务商名称",
                "代理商级别","一级代理商名称",
                "一级代理商编号","订单类型","已消费总金额",
                "已还款金额","收单机构",
                "收单扣率","收单手续费",
                "分润","产生分润金额","任务金额",
                "保证金","服务费",
                "实际交易手续费",
                "实际代付手续费","实际费率",
                "用户编号","用户名称"};
        OutputStream os = response.getOutputStream();
        export.export(cols, colsName, data, os);
        os.close();
    }


    /**
     * 分润汇总总金额
     * @return
     */
    @PreAuthorize("hasAuthority('serviceCollectionDataCount:count')")
    @RequestMapping(value = "/serviceCollectionDataCount.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> serviceCollectionDataCount(@ModelAttribute YfbProfitDetail yfbProfitDetail){
        Map<String,Object> map = new HashMap<>();

        try{
            Map<String,Object> countMap = yfbProfitDetailService.serviceCollectionDataCount(yfbProfitDetail);      //获取未入账
            if(countMap==null){
                map.put("mapSuccess","0.00");
                return map;
            }
            BigDecimal count = (BigDecimal) countMap.get("countAmount");
            map.put("mapSuccess",count==null?"0.00":String.valueOf(count.doubleValue()));
        }catch (Exception e){
            log.error("服务商分润总金额数据查询异常-----"+e.getMessage());
        }

        return map;

    }



}
