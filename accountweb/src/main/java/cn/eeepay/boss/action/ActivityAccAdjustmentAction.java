package cn.eeepay.boss.action;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.boss.util.Constants;
import cn.eeepay.boss.util.DownloadUtil;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.ActivityAccAdjustmentService;
import cn.eeepay.framework.service.bill.AgentAccPreAdjustService;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.service.bill.HlfAgentDebtRecordService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/5/14
 * Time: 11:27
 * Description: 类注释
 */
@Controller
@RequestMapping("/activityAccAdjustment")
public class ActivityAccAdjustmentAction {

    Logger log = LoggerFactory.getLogger(ActivityAccAdjustmentAction.class);

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private ActivityAccAdjustmentService activityAccAdjustmentService;
    @Resource
    private AgentAccPreAdjustService agentAccPreAdjustService;
    @Resource
    private AgentPreRecordTotalService agentPreRecordTotalService;
    @Resource
    private HlfAgentDebtRecordService hlfAgentDebtRecordService;


    //预调账申请查询
    @PreAuthorize("hasAuthority('beforeAdjustment:query')")
    @RequestMapping("/toBeforeAdjustment.do")
    public String toBeforeAdjustment(ModelMap model,@RequestParam Map<String, String> params){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currenDate = sdf.format(new Date());

        String date1 = DateUtil.subDayFormatLong(new Date(),7);
        model.put("date1",date1);
        model.put("date2",currenDate);

        return "activityAccAdjustment/activityAdjustmentQuery";
    }

    //预调账申请查询
    @PreAuthorize("hasAuthority('beforeAdjustment:query')")
    @RequestMapping("/beforeAdjustment.do")
    public @ResponseBody Object beforeAdjustment(@ModelAttribute BeforeAdjustApply beforeAdjustApply, @RequestParam Map<String, String> params,
                                                 @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<BeforeAdjustApply> page){
        log.info("活动补贴账户预调账申请查询......");
        //beforeAdjustApply.setIsApply(1);
        activityAccAdjustmentService.findBeforeAdjustApplyList(beforeAdjustApply, params, sort, page);
        return page;
    }


    //单个预调账
    @PreAuthorize("hasAuthority('activityAccAdjustmentQuery:preAdjustment')")
    @RequestMapping(value = "/activityAccPreAdjustment.do")
    public String activityAccPreAdjustment() throws Exception {
        log.info("活动补贴账户预调账");
        return "activityAccAdjustment/activityAccPreAdjustment";
    }





     //活动补贴账户预调账保存
    @RequestMapping(value = "/saveAdjustInfo.do")
    @Logs(description = "活动补贴账户预调账保存")
    @ResponseBody
    public Map<String, Object> saveAdjustInfo(@ModelAttribute AgentPreAdjust agentPreAdjust) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", true);
        msg.put("msg", "保存成功");
        try {
            log.info("活动补贴账户预调账保存");

            if (agentPreAdjust.getAdjustAmount() == null) {
                msg.put("msg","预调账金额不能为空");
                msg.put("status",false);
                return msg;
            }

            // 获取到登录者信息
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            agentPreAdjust.setApplicant(userInfo.getUsername());// 创建者
            AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentPreAdjust.getAgentNo());
            agentPreAdjust.setAgentName(agentInfo.getAgentName());
            activityAccAdjustmentService.insertActivityAdjustment(msg,agentPreAdjust,agentInfo);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "保存失败:"+e.getMessage());
            log.error("提交失败！", e);
            log.error(msg.toString());
        }

        return msg;
    }






    //批量预调账
    @PreAuthorize("hasAuthority('activityAccAdjustmentQuery:batchPreAdjustment')")
    @RequestMapping(value = "/activityAccBatchPreAdjustment.do")
    public String activityAccBatchPreAdjustment(ModelMap model, @RequestParam Map<String, String> params)
            throws Exception {
        log.info("活动补贴账户批量预调账");
        return "activityAccAdjustment/activityAccBatchPreAdjustment";
    }


    // 上传模板,批量预调账
    @PreAuthorize("hasAuthority('activityAccAdjustmentQuery:batchPreAdjustment')")
    @ResponseBody
    @RequestMapping(value = "/batchPreAdjustFileUpload.do", method = RequestMethod.POST)
    public Map<String, Object> batchPreAdjustFileUpload(HttpServletRequest request, @RequestParam final Map<String, String> params) {
        log.info("活动补贴账户批量预调账上传");
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> result=new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("fileupload");
        MultipartFile file=files.get(0);
        String fileName  = file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            result.put("statu",false);
            result.put("msg","请导入正确格式批量预调账excel文件!");
            return result;
        }
        //保存单据
        try {
            File temp = File.createTempFile(file.getName(), ".xls");
            file.transferTo(temp);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String uname=userInfo.getUsername();
            map = activityAccAdjustmentService.resolvebatchPreAdjustFile(temp,uname);
            boolean mapStatus = (boolean) map.get("status");
            if(mapStatus){
                List<AgentPreAdjust> agentPreAdjusts = (List<AgentPreAdjust>) map.get("agentPreAdjusts");
                if(agentPreAdjusts != null && agentPreAdjusts.size()>0){
                    for(AgentPreAdjust apa : agentPreAdjusts){
                        apa.setApplicant(uname);
                        AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(apa.getAgentNo());
                        activityAccAdjustmentService.insertActivityAdjustment(map,apa,agentInfo);
                    }
                }else{
                    result.put("status", false);
                    result.put("msg", "导入失败,excel中没有数据");
                    return result;
                }
            }else{
                result.put("status", false);
                result.put("msg", "导入失败,请检查excel格式");
                return result;
            }
            result = activityAccAdjustmentService.leadingInBatchPreAdjustDetails(map);

            result.put("statu",true);
            result.put("msg","操作成功！请查看结果列表！");
        } catch (Exception e) {
            log.error("批量预调账导入异常 :" + e);
            result.put("status", false);
            result.put("msg", "导入失败,请检查excel格式");
//			e.printStackTrace();
        }

        return result;
    }

    //预调账 下载模板
    @PreAuthorize("hasAuthority('activityAccAdjustmentQuery:downLoad')")
    @RequestMapping(value="/DownloadBacthAdjustTemplate.do")
    public String downloadBacthAdjustTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getSession().getServletContext().getRealPath("/")+ Constants.BACTH_ACTIVITY_ADJUST_TEMPLATE;
        DownloadUtil.download(response, filePath,"活动补贴账户批量预调账明细模板.xls");
        return null;
    }


    //导出数据 活动补贴账户申请调账
    @PreAuthorize("hasAuthority('activityAccAdjustmentQuery:export')")
    @RequestMapping(value = "/exportPreAdjustResult.do", method = RequestMethod.POST)
    public void exportPreAdjustResult(@RequestParam Map<String, String> params,
                                      @ModelAttribute BeforeAdjustApply beforeAdjustApply, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        // 用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "活动补贴账户申请调账记录导出_" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            Map<String, String> tempMap = null;
            // 查询代理商分润预调账明细
            //beforeAdjustApply.setIsApply(1);
            List<BeforeAdjustApply> list = activityAccAdjustmentService.exportBeforeAdjustApplyList(beforeAdjustApply);
            if (list != null && list.size() > 0) {
                for (BeforeAdjustApply beforeAdjustApply1 : list) {
                    tempMap = new HashMap<String, String>();
                    tempMap.put("applyDate", beforeAdjustApply1.getApplyDate() == null ? "" : df.format(beforeAdjustApply1.getApplyDate()));
                    tempMap.put("applicant", beforeAdjustApply1.getApplicant());
                    tempMap.put("agentNo", beforeAdjustApply1.getAgentNo());
                    tempMap.put("agentName", beforeAdjustApply1.getAgentName());
                    tempMap.put("freezeAmount",beforeAdjustApply1.getFreezeAmount() == null ? "" : beforeAdjustApply1.getFreezeAmount().toString());
                    tempMap.put("activityAvailableAmount", beforeAdjustApply1.getActivityAvailableAmount() == null ? "" : beforeAdjustApply1.getActivityAvailableAmount().toString());
                    tempMap.put("activityFreezeAmount", beforeAdjustApply1.getActivityFreezeAmount() == null ? "" : beforeAdjustApply1.getActivityFreezeAmount().toString());
                    tempMap.put("generateAmount",beforeAdjustApply1.getGenerateAmount() == null ? "" : beforeAdjustApply1.getGenerateAmount().toString());
                    tempMap.put("remark", beforeAdjustApply1.getRemark());
                    data.add(tempMap);
                }
            }
        } catch (Exception e) {
            log.error("异常:", e);
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[] { "applyDate", "applicant", "agentNo", "agentName", "freezeAmount", "activityAvailableAmount",
                "activityFreezeAmount", "generateAmount", "remark"};

        String[] colsName = new String[] { "申请调账时间","申请人","代理商编号","代理商名称","申请调账金额","活动补贴账户可用余额调账金额","活动补贴账户冻结余额调账金额","生成预调账金额","备注"};
        export.export(cols, colsName, data, response.getOutputStream());

    }



    /**
     * 活动补贴账户汇总
     */
    @PreAuthorize("hasAuthority('beforeAdjustmentList:collectionData')")
    @RequestMapping(value = "/findBeforeAdjustmentListCollection.do")
    @ResponseBody
    public Map<String, Object> findBeforeAdjustmentListCollection(@ModelAttribute BeforeAdjustApply beforeAdjustApply, @RequestParam Map<String, String> params) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            //beforeAdjustApply.setIsApply(1);
            Map<String,BigDecimal> map = activityAccAdjustmentService.findBeforeAdjustApplyListCollection(beforeAdjustApply);
            BigDecimal allFreezeAmount = BigDecimal.ZERO;
            BigDecimal allActivityAvailableAmount =  BigDecimal.ZERO;
            BigDecimal allActivityFreezeAmount =  BigDecimal.ZERO;
            BigDecimal allGenerateAmount =  BigDecimal.ZERO;
            if (map != null) {
                allFreezeAmount = map.get("allFreezeAmount");
                allActivityAvailableAmount = map.get("allActivityAvailableAmount");
                allActivityFreezeAmount = map.get("allActivityFreezeAmount");
                allGenerateAmount = map.get("allGenerateAmount");
            }
            msg.put("allFreezeAmount", allFreezeAmount.toString());//申请调账总金额
            msg.put("allActivityAvailableAmount", allActivityAvailableAmount.toString());//活动补贴账户可用余额调账总金额
            msg.put("allActivityFreezeAmount", allActivityFreezeAmount.toString());//活动补贴账户冻结余额调账总金额
            msg.put("allGenerateAmount", allGenerateAmount.toString());//生成预调账总金额

        } catch (Exception e) {
            log.error("异常:",e);
        }
        return msg;
    }





    //调账余额查询
    @PreAuthorize("hasAuthority('adjustmentAmount:query')")
    @RequestMapping("/toAdjustmentAmount.do")
    public String toAdjustmentAmount(ModelMap model){
        log.info("调账余额查询");
        List<AgentInfo> agentInfoList = null;
        try {
            agentInfoList = agentInfoService.findAllOneAgentInfoList();
        } catch (Exception e) {
            log.error("异常:", e);
        }
        model.put("agentInfoList", agentInfoList) ;
        return "activityAccAdjustment/activityAmountQuery";
    }


    //调账余额查询
    @PreAuthorize("hasAuthority('adjustmentAmount:query')")
    @RequestMapping("/adjustmentAmount.do")
    public @ResponseBody Object adjustmentAmount(@ModelAttribute AgentAccPreAdjust agentAccPreAdjust, @RequestParam Map<String, String> params,
                                                 @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<AgentAccPreAdjust> page) throws Exception {

        agentAccPreAdjustService.findAgentAccPreAdjustList(agentAccPreAdjust,params,sort,page);
        return page;
    }

    //导出数据 活动补贴账户金额查询
    @PreAuthorize("hasAuthority('adjustmentAmount:export')")
    @RequestMapping(value = "/exportAdjustmentAmount.do", method = RequestMethod.POST)
    public void exportAdjustmentAmount(@RequestParam Map<String, String> params,
                                      @ModelAttribute AgentAccPreAdjust agentAccPreAdjust, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        // 用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "活动补贴账户调账余额导出_" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            Map<String, String> tempMap = null;
            // 查询代理商分润预调账明细
            List<AgentAccPreAdjust> list = agentAccPreAdjustService.exportAgentAccPreAdjustList(agentAccPreAdjust);
            if (list != null && list.size() > 0) {
                for (AgentAccPreAdjust aap : list) {
                    tempMap = new HashMap<String, String>();
                    tempMap.put("agentNo", aap.getAgentNo());
                    tempMap.put("agentName", aap.getAgentName());
                    tempMap.put("agentLevel", aap.getAgentLevel());
                    tempMap.put("adjustAmount",aap.getAdjustAmount() == null ? "0.0" : aap.getAdjustAmount().toString());

                    tempMap.put("activitySubsidyFreeze",aap.getActivitySubsidyFreeze() == null ? "0.0" : aap.getActivitySubsidyFreeze().toString());
                    tempMap.put("activitySubsidyBalance",aap.getActivitySubsidyBalance() == null ? "0.0" : aap.getActivitySubsidyBalance().toString());
                    tempMap.put("activitySubsidyAvailableBalance",aap.getActivitySubsidyAvailableBalance() == null ? "0.0" : aap.getActivitySubsidyAvailableBalance().toString());

                    data.add(tempMap);
                }
            }
        } catch (Exception e) {
            log.error("异常:", e);
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[] { "agentNo", "agentName", "agentLevel", "adjustAmount", "activitySubsidyBalance", "activitySubsidyFreeze","activitySubsidyAvailableBalance"};

        String[] colsName = new String[] { "代理商编号","代理商名称","代理商等级","剩余预调账金额","活动补贴账户余额","活动补贴账户已冻结金额","活动补贴账户可用余额"};
        export.export(cols, colsName, data, response.getOutputStream());

    }



    /**
     * 活动补贴账户金额查询汇总
     */
    @RequestMapping(value = "/findAdjustmentAmountCollection.do")
    @PreAuthorize("hasAuthority('adjustmentAmount:collectionData')")
    @ResponseBody
    public Map<String, Object> findAdjustmentAmountCollection(
            ModelMap model, @ModelAttribute("agentAccPreAdjust") AgentAccPreAdjust agentAccPreAdjust, @RequestParam Map<String, String> params) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Object allAdjustAmount = "0.0";
            Object activitySubsidyFreeze =  "0.0";
            Object activitySubsidyBalance =  "0.0";
            Object activitySubsidyAvailableBalance =  "0.0";

            Map<String, Object> adjustmentAmountCollection = agentAccPreAdjustService.findAdjustmentAmountCollection(agentAccPreAdjust);

            if(adjustmentAmountCollection!=null){
                allAdjustAmount = adjustmentAmountCollection.get("allAdjustAmount") == null ? "0.0" : adjustmentAmountCollection.get("allAdjustAmount");
                activitySubsidyFreeze = adjustmentAmountCollection.get("allActivitySubsidyFreeze") == null ? "0.0" : adjustmentAmountCollection.get("allActivitySubsidyFreeze");
                activitySubsidyBalance = adjustmentAmountCollection.get("allActivitySubsidyBalance") == null ? "0.0" : adjustmentAmountCollection.get("allActivitySubsidyBalance");
                activitySubsidyAvailableBalance = adjustmentAmountCollection.get("allActivitySubsidyAvailableBalance") == null ? "0.0" : adjustmentAmountCollection.get("allActivitySubsidyAvailableBalance");
            }
            msg.put("allAdjustAmount", allAdjustAmount);    //剩余调账金额
            msg.put("activitySubsidyFreeze", activitySubsidyFreeze);    //活动补贴账户已冻结金额
            msg.put("activitySubsidyBalance", activitySubsidyBalance);  //活动补贴账户余额
            msg.put("activitySubsidyAvailableBalance", activitySubsidyAvailableBalance);    //活动补贴账户可用余额

        } catch (Exception e) {
            log.error("异常:",e);
        }
        return msg;
    }





    //调账明细查询
    @PreAuthorize("hasAuthority('adjustmentDetail:query')")
    @RequestMapping("/toAdjustmentDetail.do")
    public String toAdjustmentDetail(ModelMap model, @RequestParam Map<String,String> params){
        String agentNo = params.get("agentNo");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currenDate = sdf.format(new Date());

        String date1 = DateUtil.subDayFormatLong(new Date(),7);
        model.put("date1",date1);
        model.put("date2",currenDate);

        if(StringUtils.isNotBlank(agentNo)){
            AgentInfo agent = agentInfoService.findEntityByAgentNo(agentNo);
            model.put("agentName",agent.getAgentName());
            model.put("agentNo",agentNo);
        }
        return "activityAccAdjustment/activityDetailQuery";
    }

    //调账明细查询
    @PreAuthorize("hasAuthority('adjustmentDetail:query')")
    @RequestMapping("/adjustmentDetail.do")
    public @ResponseBody Object adjustmentDetail(@ModelAttribute BeforeAdjustApply beforeAdjustApply, @RequestParam Map<String, String> params,
                                                 @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<BeforeAdjustApply> page){
        log.info("调账明细查询......");
        beforeAdjustApply.setIsDetail(1);
        activityAccAdjustmentService.findBeforeAdjustApplyList(beforeAdjustApply, params, sort, page);
        return page;
    }



    //导出数据 活动补贴账户调账明细
    @PreAuthorize("hasAuthority('activityAdjustmentDetail:export')")
    @RequestMapping(value = "/exportAdjustmentDetail.do", method = RequestMethod.POST)
    public void exportAdjustmentDetail(@RequestParam Map<String, String> params,
                                      @ModelAttribute BeforeAdjustApply beforeAdjustApply, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        // 用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "活动补贴账户调账明细导出_" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            Map<String, String> tempMap = null;
            // 查询代理商分润预调账明细
            beforeAdjustApply.setIsDetail(1);
            List<BeforeAdjustApply> list = activityAccAdjustmentService.exportBeforeAdjustApplyList(beforeAdjustApply);
            if (list != null && list.size() > 0) {
                for (BeforeAdjustApply beforeAdjustApply1 : list) {
                    BigDecimal bigDecimal1 = BigDecimal.ZERO;
                    BigDecimal bigDecimal2 = BigDecimal.ZERO;
                    tempMap = new HashMap<String, String>();
                    tempMap.put("applyDate", beforeAdjustApply1.getApplyDate() == null ? "" : df.format(beforeAdjustApply1.getApplyDate()));
                    tempMap.put("applicant", beforeAdjustApply1.getApplicant());
                    tempMap.put("agentNo", beforeAdjustApply1.getAgentNo());
                    tempMap.put("agentName", beforeAdjustApply1.getAgentName());

                    bigDecimal1 = beforeAdjustApply1.getActivityAvailableAmount() == null ? bigDecimal1 :  beforeAdjustApply1.getActivityAvailableAmount();
                    bigDecimal2 = beforeAdjustApply1.getActivityFreezeAmount() == null ? bigDecimal2 :  beforeAdjustApply1.getActivityFreezeAmount();

                    tempMap.put("freezeAmount",bigDecimal1.add(bigDecimal2).toString());

                    tempMap.put("activityAvailableAmount", bigDecimal1.toString());
                    tempMap.put("activityFreezeAmount", bigDecimal2.toString());
                    tempMap.put("remark", beforeAdjustApply1.getRemark());
                    data.add(tempMap);
                }
            }
        } catch (Exception e) {
            log.error("异常:", e);
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[] { "applyDate", "applicant", "agentNo", "agentName", "freezeAmount","activityAvailableAmount", "activityFreezeAmount","remark"};

        String[] colsName = new String[] { "调账时间","发起人","代理商编号","代理商名称","实际调账金额","活动补贴账户可用余额调账金额","活动补贴账户冻结余额调账金额","备注"};
        export.export(cols, colsName, data, response.getOutputStream());

    }



    /**
     * 活动补贴账户调账明细汇总
     */
    @RequestMapping(value = "/findAdjustmentDetailCollection.do")
    @PreAuthorize("hasAuthority('findAdjustmentDetail:collectionData')")
    @ResponseBody
    public Map<String, Object> findAdjustmentDetailCollection(
            ModelMap model, @ModelAttribute("beforeAdjustApply") BeforeAdjustApply beforeAdjustApply, @RequestParam Map<String, String> params) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            beforeAdjustApply.setIsDetail(1);
            Map<String,BigDecimal> map = activityAccAdjustmentService.findBeforeAdjustApplyListCollection(beforeAdjustApply);
            //BigDecimal allFreezeAmount = BigDecimal.ZERO;
            BigDecimal allActivityAvailableAmount =  BigDecimal.ZERO;
            BigDecimal allActivityFreezeAmount =  BigDecimal.ZERO;
            if (map != null) {
                //allFreezeAmount = map.get("allFreezeAmount");
                allActivityAvailableAmount = map.get("allActivityAvailableAmount");
                allActivityFreezeAmount = map.get("allActivityFreezeAmount");
            }
            msg.put("allFreezeAmount", allActivityAvailableAmount.add(allActivityFreezeAmount));//申请调账总金额
            msg.put("allActivityAvailableAmount", allActivityAvailableAmount.toString());//活动补贴账户可用余额调账总金额
            msg.put("allActivityFreezeAmount", allActivityFreezeAmount.toString());//活动补贴账户冻结余额调账总金额

        } catch (Exception e) {
            log.error("异常:",e);
        }
        return msg;
    }

    
    //不满扣明细查询
    @PreAuthorize("hasAuthority('hlfAgentDebtDetail:query')")
    @RequestMapping("/toHlfAgentDebtDetail.do")
    public String toHlfAgentDebtDetail(ModelMap model){
        log.info("不满扣明细查询");
        List<AgentInfo> agentInfoList = null;
        try {
            agentInfoList = agentInfoService.findAllOneAgentInfoList();
        } catch (Exception e) {
            log.error("异常:", e);
        }
        model.put("agentInfoList", agentInfoList) ;
        return "activityAccAdjustment/hlfAgentDebtDetailQuery";
    }
    
    //不满扣明细列表查询
    @PreAuthorize("hasAuthority('hlfAgentDebtDetail:query')")
    @RequestMapping("/findHlfAgentDebtRecordList.do")
    public @ResponseBody Object findHlfAgentDebtRecordList(@ModelAttribute HlfAgentDebtRecord hlfAgentDebtRecord, @RequestParam Map<String, String> params,
                                                 @ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<HlfAgentDebtRecord> page) throws Exception {
    	log.info("不满扣明细列表查询......");
    	hlfAgentDebtRecordService.findHlfAgentDebtRecordList(hlfAgentDebtRecord,params,sort,page);
        return page;
    }
    
    
    /**
     * 不满扣明细汇总
     */
    @RequestMapping(value = "/findHlfAgentDebtRecordListCollection.do")
    @PreAuthorize("hasAuthority('hlfAgentDebtDetail:collectionData')")
    @ResponseBody
    public Map<String, Object> findHlfAgentDebtRecordListCollection(
            ModelMap model, @ModelAttribute("hlfAgentDebtRecord") HlfAgentDebtRecord hlfAgentDebtRecord, @RequestParam Map<String, String> params) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map<String,BigDecimal> debtAmountMap = hlfAgentDebtRecordService.findHlfAgentDebtRecordListCollection(hlfAgentDebtRecord);
            BigDecimal debtAmount =  BigDecimal.ZERO;//实际扣款
            if (debtAmountMap != null) {
            	debtAmount = debtAmountMap.get("debtAmount");
            }
            
//            Map<String,BigDecimal> shouldDebtAmountMap = hlfAgentDebtRecordService.findHlfAgentDebtRecordShouldDebtAmountCollection(hlfAgentDebtRecord);
//            BigDecimal shouldDebtAmount =  BigDecimal.ZERO;//应该扣款
//            if (debtAmountMap != null) {
//            	shouldDebtAmount = shouldDebtAmountMap.get("shouldDebtAmount");
//            }
            
            BigDecimal totalDebtAmount =  BigDecimal.ZERO;//累计待扣款
            List<HlfAgentDebtRecord> hlfAgentDebtRecordQueryList = hlfAgentDebtRecordService.findHlfAgentDebtRecordAgentNo(hlfAgentDebtRecord);
            List<String> agentInfoList = new ArrayList<String>() ;
            for (HlfAgentDebtRecord hlfAgentDebtRecord2 : hlfAgentDebtRecordQueryList) {
            	agentInfoList.add(hlfAgentDebtRecord2.getAgentNo());
			}
            if(agentInfoList == null || agentInfoList.size() ==0){
            	totalDebtAmount = BigDecimal.ZERO;//累计待扣款
            }else{
            	Map<String,BigDecimal> totalDebtAmountMap = hlfAgentDebtRecordService.findHlfAgentDebtRecordListCollectionByList(StringUtils.join(agentInfoList, ","));
                if (totalDebtAmountMap != null) {
                	totalDebtAmount = totalDebtAmountMap.get("totalDebtAmount");
                }
            }
            msg.put("debtAmount", debtAmount.multiply(new BigDecimal(-1)));//实际扣款
            msg.put("shouldDebtAmount", debtAmount.multiply(new BigDecimal(-1)).add(totalDebtAmount.multiply(new BigDecimal(-1))));//应该扣款
            msg.put("totalDebtAmount", totalDebtAmount.multiply(new BigDecimal(-1)));//累计扣款
        } catch (Exception e) {
            log.error("异常:",e);
        }
        return msg;
    }
    
    
    //导出数据 不满扣明细
    @PreAuthorize("hasAuthority('hlfAgentDebtDetail:export')")
    @RequestMapping(value = "/exportHlfAgentDebtDetail.do", method = RequestMethod.POST)
    public void exportHlfAgentDebtDetail(@RequestParam Map<String, String> params,
                                      @ModelAttribute HlfAgentDebtRecord hlfAgentDebtRecord, HttpServletResponse response, HttpServletRequest request)
            throws IOException {

        // 用于对数据字典的数据进行格式化显示
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "欢乐返不满扣明细导出_" + sdf.format(new Date()) + ".xls";
        String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            Map<String, String> tempMap = null;
            // 查询 不满扣明细
            List<HlfAgentDebtRecord> list = hlfAgentDebtRecordService.exportHlfAgentDebtRecordList(hlfAgentDebtRecord);
            for (HlfAgentDebtRecord hlfAgentDebtRecordExport : list) {
            	hlfAgentDebtRecordExport.setAdjustAmount(hlfAgentDebtRecordExport.getAdjustAmount().multiply(new BigDecimal(-1)));
            	hlfAgentDebtRecordExport.setDebtAmount(hlfAgentDebtRecordExport.getDebtAmount().multiply(new BigDecimal(-1)));
            	hlfAgentDebtRecordExport.setShouldDebtAmount(hlfAgentDebtRecordExport.getShouldDebtAmount().multiply(new BigDecimal(-1)));
			}
            if (list != null && list.size() > 0) {
                for (HlfAgentDebtRecord hadr : list) {
                    tempMap = new HashMap<String, String>();
                    tempMap.put("shouldDebtAmount", hadr.getShouldDebtAmount() == null?"0.00":hadr.getShouldDebtAmount().abs().toString());
                    tempMap.put("debtAmount", hadr.getDebtAmount() == null?"0.00":hadr.getDebtAmount().abs().toString());
                    tempMap.put("adjustAmount", hadr.getAdjustAmount() == null?"0.00":hadr.getAdjustAmount().abs().toString());
                    tempMap.put("agentNo", hadr.getAgentNo());
                    AgentInfo agentInfo = agentInfoService.findAgentByUserId(hadr.getAgentNo());
                    tempMap.put("agentName", agentInfo == null ? "": agentInfo.getAgentName());
                    tempMap.put("parentAgentNo", hadr.getParentAgentNo());
                    AgentInfo parentAgentInfo = agentInfoService.findAgentByUserId(hadr.getParentAgentNo());
                    tempMap.put("parentAgentName", parentAgentInfo == null ? "": parentAgentInfo.getAgentName());
                    tempMap.put("oneAgentNo", hadr.getOneAgentNo());
                    AgentInfo oneAgentInfo = agentInfoService.findAgentByUserId(hadr.getOneAgentNo());
                    tempMap.put("oneAgentName", oneAgentInfo == null ? "": oneAgentInfo.getAgentName());
                    tempMap.put("debtTime", hadr.getDebtTime() == null ? "" : df.format(hadr.getDebtTime()));
                    tempMap.put("orderNo", hadr.getOrderNo());
                    tempMap.put("merchantNo", hadr.getMerchantNo());
                    data.add(tempMap);
                }
            }
        } catch (Exception e) {
            log.error("异常:", e);
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[] { "shouldDebtAmount", "debtAmount", "adjustAmount", "agentNo", "agentName","parentAgentNo", "parentAgentName","oneAgentNo","oneAgentName","merchantNo","orderNo","debtTime"};

        String[] colsName = new String[] {"应扣款金额（元）","实际扣款金额（元）","累计代扣款金额（元）","扣款代理商编号","扣款代理商名称","所属上级代理商编号","所属上级代理商名称","所属一级代理商编号","所属一级代理商名称","商户编号","欢乐返订单","日期"};
        export.export(cols, colsName, data, response.getOutputStream());

    }
}
