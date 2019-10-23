package cn.eeepay.boss.action;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.AuditorManager;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.ExaminationsLog;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.MerchantRequireItem;
import cn.eeepay.framework.model.MerchantService;
import cn.eeepay.framework.model.MerchantServiceQuota;
import cn.eeepay.framework.model.MerchantServiceRate;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.UserLoginInfo;

@Controller
@RequestMapping(value = "/merchantBusinessProduct")
public class MerchantBusinessProductAction {

    private static final Logger log = LoggerFactory.getLogger(MerchantBusinessProductAction.class);
    @Resource
    private AgentFunctionService agentFunctionService;
    //商户业务产品
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;

    //代理商
    @Resource
    private AgentInfoService agentInfoService;

    //业务产品（服务套餐）定义
    @Resource
    private BusinessProductDefineService businessProductDefineService;

    //商户信息
    @Resource
    private MerchantInfoService merchantInfoService;

    //商户银行卡
    @Resource
    private MerchantCardInfoService merchantCardInfoService;
    
    @Resource
    private PerAgentService perAgentService;

    //商户服务限额信息
    @Resource
    private MerchantServiceQuotaService merchantServiceQuotaService;

    //商户服务签约费率
    @Resource
    private MerchantServiceRateService merchantServiceRateService;

    //商户进件条件表(明细)
    @Resource
    private MerchantRequireItemService merchantRequireItemService;

    //审核记录
    @Resource
    private ExaminationsLogService examinationsLogService;

    @Resource
    private SysDictService sysDictService;

    //进件提交信息
    @Resource
    private AddRequireItemService addRequireItemService;

    //服务费率拼接
    @Resource
    private ServiceProService serviceProService;

    //商户服务表
    @Resource
    private MerchantServiceProService merchantServiceProService;

    //业务进件资料表
    @Resource
    private BusinessRequireItemService businessRequireItemService;

    @Resource
    private TerminalInfoService terminalInfoService;

    @Resource
    private AuditorManagerService auditorManagerService;


    //商户初始化
    @RequestMapping(value = "/selectAllInfo.do")
    @ResponseBody
    public Object selectAllInfo(@ModelAttribute("page") Page<MerchantBusinessProduct> page) throws Exception {
        List<MerchantBusinessProduct> listMerbp = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            listMerbp = merchantBusinessProductService.selectAllInfo(page, principal.getUserEntityInfo().getEntityId());
            //20170112
            String agentNo = principal.getUserEntityInfo().getEntityId();
            for (MerchantBusinessProduct merchantBusinessProduct : listMerbp) {
                if (agentInfoService.selectLevelOne(principal.getUserId()) == null) {
                    merchantBusinessProduct.setMobilePhone(CommonUtil.replaceMask(merchantBusinessProduct.getMobilePhone(), "^(.{4}).*?$", "$1*******"));
                }
                if (merchantBusinessProduct.getAgentNo().equals(agentNo)) {
                    merchantBusinessProduct.setShowReplace("1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化失败----", e);
        }
        return page;
    }

    //商户审核初始化
    @RequestMapping(value = "/selectAllByStatusInfo")
    @ResponseBody
    public Object selectAllByStatusInfo(@ModelAttribute("page") Page<MerchantBusinessProduct> page) throws Exception {
        List<MerchantBusinessProduct> listMerbp = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            listMerbp = merchantBusinessProductService.selectAllByStatusInfo(page, principal.getUserEntityInfo().getEntityId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化失败----", e);
        }
        return page;
    }


    /**
     * 更换业务产品
     *
     * @param param
     * @return
     * @throws Exception
     */
    @SystemLog(description = "更改商户业务产品")
    @RequestMapping(value = "/replaceBusinessProduct")
    @ResponseBody
    public Map<String, Object> replaceBusinessProduct(@RequestBody String param) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String agentNo = principal.getUserEntityInfo().getEntityId();
            if (!merchantBusinessProductService.isOpenAgentUpdateBpSwitch(agentNo)) {
                map.put("status", false);
                map.put("msg", "暂不支持此功能修改");
                return map;
            }
            JSONObject json = JSON.parseObject(param);
            String oldBpId = json.getString("sBpId");
            String newBpId = json.getString("bpId");
            String merchantNo = json.getString("merchantNo");
            if (!StringUtils.isNotBlank(oldBpId) || !StringUtils.isNotBlank(newBpId) || !StringUtils.isNotBlank(merchantNo)) {
                map.put("status", false);
                map.put("msg", "必传参数为空");
                return map;
            }
            log.info("======>正在进入更换业务产品流程" + oldBpId + "==" + "bpId" + "===" + "merchantNo");
            Map<String, Object> merSounceBpInfo = merchantBusinessProductService.selectMerBpInfo(merchantNo, oldBpId);
            if (merSounceBpInfo == null) {
                map.put("status", false);
                map.put("msg", "原业务产品不存在,不能更换");
                return map;
            }
            String merStatus = (String) merSounceBpInfo.get("status");
            if (!"4".equals(merStatus)) {
                map.put("status", false);
                map.put("msg", "您的业务产品没有审核成功暂时,不能更换");
                return map;
            }
            List<Map<String, Object>> merTerBpList = merchantBusinessProductService.selectTerBpInfo(merchantNo, oldBpId);
            if (merTerBpList == null || merTerBpList.size() < 1) {
                map.put("status", false);
                map.put("msg", "该业务产品的机具已经解绑,不能更换");
                return map;
            }
            Map<String, Object> merBpInfo = merchantBusinessProductService.selectMerBpInfo(merchantNo, newBpId);
            if (merBpInfo != null) {
                map.put("status", false);
                map.put("msg", "您已存在该业务产品,不能更换");
                return map;
            }
            boolean status = merchantBusinessProductService.replaceBusinessProduct(merchantNo, oldBpId, newBpId, agentNo);
            if (status) {
                map.put("status", true);
                map.put("msg", "更换业务产品成功");
            } else {
                map.put("status", false);
                map.put("msg", "更换业务产品失败");
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("msg", "更换业务产品失败");
            log.error("============================>更换业务产品失败", e);
        }
        return map;

    }

    /**
     * 显示可更换的业务产品
     *
     * @param bpId
     * @param merchantNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showGroupBpInfo")
    public @ResponseBody
    Map<String, Object> showGroupBpInfo(String bpId, String merchantNo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            System.out.println("=========>更换的业务产品ID:" + bpId + "fd" + merchantNo);
            Map<String, Object> merSounceBpInfo = merchantBusinessProductService.selectMerBpInfo(merchantNo, bpId);
            if (merSounceBpInfo == null) {
                map.put("status", false);
                map.put("msg", "原业务产品不存在");
                return map;
            }
            String merStatus = (String) merSounceBpInfo.get("status");
            if (!"4".equals(merStatus)) {
                map.put("status", false);
                map.put("msg", "您的业务产品没有审核成功暂时不能行更换");
                return map;
            }
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String agentNo = principal.getUserEntityInfo().getEntityId();
            List<BusinessProductDefine> list = merchantBusinessProductService.selectGroupBpInfo(agentNo, bpId, bpId);
            if (list == null || list.size() < 1) {
                map.put("status", false);
                map.put("msg", "暂时没有更换的业务产品");
                return map;
            }
            map.put("list", list);
            map.put("status", true);
        } catch (Exception e) {
            map.put("status", false);
            map.put("msg", "显示更换业务产品失败");
            log.error("============================>显示更换业务产品失败", e);
        }
        return map;
    }

    /**
     * 校验商户查询里是否显示更改业务产品按钮
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showReplace.do")
    @ResponseBody
    public Map<String, Object> showReplace() throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String agentNo = principal.getUserEntityInfo().getEntityId();
            boolean openAgentUpdateBpSwitch = merchantBusinessProductService.isOpenAgentUpdateBpSwitch(agentNo);
            map.put("status", openAgentUpdateBpSwitch);
            return map;
        } catch (Exception e) {
            map.put("status", false);
            log.error("校验一级代理商是否有更改的按钮失败----", e);
        }
        return map;

    }

    //商户模糊查询
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public ResponseBean selectByParam(@RequestBody SelectParams selectParams,
                                      @RequestParam(defaultValue = "1") int pageNo,
                                      @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginAgentNo = principal.getUserEntityInfo().getEntityId();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        // 判断当前登录用户是否有设置业务范围，防止非法操作
        String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
        if(StringUtils.isNotBlank(accessTeamId) && !accessTeamId.equals(selectParams.getMerTeamId())){
            return new ResponseBean(null, 0);
        }
        if(StringUtils.isNotBlank(accessTeamId)){
            selectParams.setMerTeamId(accessTeamId);
        }
        try {
            if ("0".equals(selectParams.getAgentNode())) {//包含下级
                if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                    AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                    if (ais != null) {
                        selectParams.setAgentName(null);
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                    } else {
                        return new ResponseBean(null, 0);
                    }
                } else {
                    selectParams.setAgentNode(null);
                }
            } else {
                selectParams.setAgentNode(null);
            }
            Page<MerchantBusinessProduct> page = new Page<>(pageNo, pageSize);
            List<MerchantBusinessProduct> mbplist = merchantBusinessProductService.selectByParam(page, selectParams, loginAgent.getAgentNo());
            for (MerchantBusinessProduct merchantBusinessProduct : mbplist) {
                /**
            	 * 机构可以查看所有商户的手机号码，大盟主只能查看直营商户的手机号码，非直营商户的手机号码打上掩码
            	 */
            	if ("11".equals(loginAgent.getAgentType())) {
            		PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
            		String merchantNo = merchantBusinessProduct.getMerchantNo();
            		Map<String,String> merchantInfoMap = perAgentService.selectByMerchantNo(merchantNo);
            		log.info("用商户号 === >" + merchantNo + "查询到pa_mer_info表 ==>" + merchantInfoMap);
//            		if (loginAgent.getAgentLevel() != 1 && !loginAgentNo.equals(merchantBusinessProduct.getAgentNo())) {
            		if (loginAgent.getAgentLevel() != 1 && !(paUserInfo.getUserCode()).equals(merchantInfoMap == null ? "" : merchantInfoMap.get("user_code"))) {
            			merchantBusinessProduct.setMobilePhone(StringUtil.isBlank(merchantBusinessProduct.getMobilePhone()) ? "" : merchantBusinessProduct.getMobilePhone().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
            		}
				}else{
            		if (!agentFunctionService.queryFunctionValue(loginAgent, "001")) {
            			merchantBusinessProduct.setMobilePhone(CommonUtil.replaceMask(merchantBusinessProduct.getMobilePhone(), "^(.{4}).*?$", "$1*******"));
            		}
            	}
                if (merchantBusinessProduct.getAgentNo().equals(loginAgentNo)) {
                    merchantBusinessProduct.setShowReplace("1");
                }
                
               // Map<String, Object> map = merchantInfoService.getMerGroupByTeamId(merchantBusinessProduct.getMerTeamId());
               // merchantBusinessProduct.setMerGroup((String)map.get("team_name"));
            }
            return new ResponseBean(mbplist, page.getTotalCount());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模糊查询失败失败----", e);
            return new ResponseBean(e);
        }
    }


    @SystemLog(description = "导出商户信息")
    @RequestMapping(value = "/exportMerchantInfo")
    @ResponseBody
    public void exportMerchantInfo(@RequestParam("info") String info, HttpServletResponse response) throws Exception {
        SelectParams selectParams = JSON.parseObject(info, SelectParams.class);
        if (StringUtils.equals("0", selectParams.getAgentNode())) {
            if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                if (ais != null) {
                    selectParams.setAgentName(null);
                    selectParams.setAgentNode(ais.getAgentNode() + "%");
                }
            } else {
                selectParams.setAgentNode(null);
            }
        } else {
            selectParams.setAgentNode(null);
        }
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String agentNo = principal.getUserEntityInfo().getEntityId();
        // 判断当前登录用户是否有设置业务范围，防止非法操作
        String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
        if(StringUtils.isNotBlank(accessTeamId) && !accessTeamId.equals(selectParams.getMerTeamId())){
            log.error("只能到处当前登录代理商业务范围内的商户信息");
            return;
        }
        List<MerchantBusinessProduct> mbplist = merchantBusinessProductService.exportMerchantInfo(selectParams, agentNo);
        String fileName = "商户查询" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date());
        String[] columnNames = {"商户编号", "商户简称", "商户手机号", "业务产品", "状态", "商户冻结状态", "创建时间", "推广来源", "绑定快捷支付卡","机具编号","商户组织"};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Workbook workbook = ExcelUtils.createWorkBook("商户查询", mbplist, columnNames, createMerchantInfoCreateRow());
        if ("998".equals(principal.getTeamId())) {
            String[] perAgentColumnNames = {"商户编号", "商户简称", "商户手机号", "业务产品", "状态", "商户冻结状态", "创建时间", "绑定快捷支付卡","机具编号","商户组织"};
            workbook = ExcelUtils.createWorkBook("商户查询", mbplist, perAgentColumnNames, createMerchantInfoCreateRow());
        }
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
        workbook.write(response.getOutputStream());
        response.flushBuffer();

    }

    CreateRow<MerchantBusinessProduct> merchantInfoCreateRow = null;

    private CreateRow<MerchantBusinessProduct> createMerchantInfoCreateRow() {
        if (merchantInfoCreateRow != null) {
            return merchantInfoCreateRow;
        }
        merchantInfoCreateRow = new CreateRow<MerchantBusinessProduct>() {
            private Map<String, String> statusMap = new HashMap<>();
            private Map<String, String> riskStatusMap = new HashMap<>();
            private Map<String, String> recommendedSourceMap = new HashMap<>();
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            {
                statusMap.put("1", "待一审");
                statusMap.put("2", "待平台审核");
                statusMap.put("3", "审核失败");
                statusMap.put("4", "正常");
                statusMap.put("0", "关闭");

                riskStatusMap.put("1", "正常");
                riskStatusMap.put("2", "只进不出");
                riskStatusMap.put("3", "不进不出");

                recommendedSourceMap.put("0", "正常注册");
                recommendedSourceMap.put("1", "超级推");
                recommendedSourceMap.put("2", "代理商分享");
            }

            @Override
            public void writeRow(Row row, MerchantBusinessProduct info) {
                int index = 0;
                row.createCell(index++).setCellValue(info.getMerchantNo());    //商户编号
                row.createCell(index++).setCellValue(info.getMerchantName());    //商户简称
                row.createCell(index++).setCellValue(CommonUtil.replaceMask(info.getMobilePhone(), "^(.{4}).*?$", "$1*******"));    //商户手机号
                row.createCell(index++).setCellValue(info.getBpName());    //业务产品
                row.createCell(index++).setCellValue(statusMap.get(info.getStatus()));    //状态
                row.createCell(index++).setCellValue(riskStatusMap.get(info.getRiskStatus()));    //商户冻结状态
                String createTime;
                try {
                    createTime = sdf.format(info.getCreateTime());
                } catch (Exception e) {
                    createTime = "";
                }
                row.createCell(index++).setCellValue(createTime);    //创建时间
                final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!"998".equals(principal.getTeamId())) {
                    row.createCell(index++).setCellValue(recommendedSourceMap.get(info.getRecommendedSource()));    //推广来源
                }
                row.createCell(index++).setCellValue(info.getQuickPayment() > 0 ? "是" : "否");    //绑定快捷支付卡
                row.createCell(index++).setCellValue(info.getSn());    //机具编号
                /*Map<String, Object> map = merchantInfoService.getMerGroupByTeamId(info.getMerTeamId());
                if (map!=null) {
                	row.createCell(index++).setCellValue((String)map.get("team_name")); 
				}*/
                row.createCell(index++).setCellValue(info.getMerGroup()); 
            }
        };
        return merchantInfoCreateRow;
    }

    //商户审核模糊查询
    @RequestMapping(value = "/selectByStatusParam")
    @ResponseBody
    public Object selectByStatusParam(@RequestParam("info") String param, @ModelAttribute("page") Page<MerchantBusinessProduct> page) throws Exception {
        List<MerchantBusinessProduct> mbplist = null;
        Map<String, Object> jsonMap = new HashMap<>();
        SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                if (ais != null) {
                    if (selectParams.getAgentNode().equals("0")) {
                        selectParams.setAgentNode(ais.getAgentNode());
                    } else {
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                    }
                } else {
                    return page;
                }
            } else {
                selectParams.setAgentNode(null);
            }
            mbplist = merchantBusinessProductService.selectByStatusParam(page, selectParams, principal.getUserEntityInfo().getEntityId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模糊查询失败失败----", e);
        }

        return page;
    }

    //商户详情和审核查询
    @RequestMapping(value = "/selectDetailInfo.do")
    @ResponseBody
    public ResponseBean selectDetailInfo(String ids) {
        log.info("selectDetailInfo : " + ids);
        Map<String, Object> maps = new HashMap<>();

        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String entityId = principal.getUserEntityInfo().getEntityId();    // 当前登陆代理商

        //商户业务产品
        MerchantBusinessProduct mbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(ids));

        List<MerchantServiceRate> listmr = new ArrayList<MerchantServiceRate>();
        List<MerchantServiceQuota> listmsq = new ArrayList<MerchantServiceQuota>();
        List<MerchantRequireItem> listmri = new ArrayList<MerchantRequireItem>();
        List<MerchantInfo> mi = null;
        List<TerminalInfo> tis = null;
        List<MerchantService> listms = null;
        MerchantServiceRate msr = new MerchantServiceRate();
        MerchantServiceQuota msq = new MerchantServiceQuota();
        List<ExaminationsLog> listel = null;
        try {
            if (!agentInfoService.merchantIsBelongToAgent(ids, entityId)) {
                throw new AgentWebException("该商户不属于当前登陆代理商的商户,无权查看该商户的信息");
            }
            List<String> listStr = merchantBusinessProductService.querySerivceId(mbp.getBpId());
            if (mbp.getMerchantNo() != null) {
                tis = terminalInfoService.selectAllInfoBymerNoAndBpId(mbp.getMerchantNo(), mbp.getBpId());
                mi = merchantInfoService.selectByMertId(mbp.getMerchantNo());
                if (mi.size() == 0) {
                    maps.put("msg", "没有该商户~");
                    maps.put("bols", false);
                    return new ResponseBean(maps);
                }
                mi.get(0).setIdCardNo("**************" + (mi.get(0).getIdCardNo()).substring(14));
                mi.get(0).setMobilephone("*******" + mi.get(0).getMobilephone().substring(7, 11));
                if (StringUtils.isNotBlank(mi.get(0).getBusinessType())) {
                    mi.get(0).setSysName(merchantInfoService.selectSysDictByKey(mi.get(0).getBusinessType()).getSysName());
                    if (mi.get(0).getIndustryType() != null) {
                        mi.get(0).setTwoSysName(merchantInfoService.selectSysDictByKey(mi.get(0).getIndustryType()).getSysName());
                    }
                }
            }
            if (mbp.getMerchantNo() != null) {
                msr.setMerchantNo(mbp.getMerchantNo());
                msr.setUseable("1");
                for (String str : listStr) {
                    ServiceInfo sis = serviceProService.queryServiceInfo(Long.valueOf(str));
                    if (sis.getFixedRate() == 1) {
                        List<ServiceRate> srlist = serviceProService.getServiceAllRate(sis.getServiceId(), mi.get(0).getOneAgentNo());
                        for (ServiceRate serviceRate : srlist) {
                            String oneRate = serviceProService.profitExpression(serviceRate);
                            MerchantServiceRate msrs = new MerchantServiceRate();
                            msrs.setServiceId(sis.getServiceId().toString());
                            msrs.setServiceName(sis.getServiceName());
                            msrs.setCardType(serviceRate.getCardType());
                            msrs.setHolidaysMark(serviceRate.getHolidaysMark());
                            msrs.setRateType(serviceRate.getRateType());
                            msrs.setCapping(serviceRate.getCapping());
                            msrs.setRate(serviceRate.getRate());
                            msrs.setSafeLine(serviceRate.getSafeLine());
                            msrs.setSingleNumAmount(serviceRate.getSingleNumAmount());
                            msrs.setLadder1Max(serviceRate.getLadder1Max());
                            msrs.setLadder1Rate(serviceRate.getLadder1Rate());
                            msrs.setLadder2Max(serviceRate.getLadder2Max());
                            msrs.setLadder2Rate(serviceRate.getLadder2Rate());
                            msrs.setLadder3Max(serviceRate.getLadder3Max());
                            msrs.setLadder3Rate(serviceRate.getLadder3Rate());
                            msrs.setLadder3Max(serviceRate.getLadder4Max());
                            msrs.setLadder4Rate(serviceRate.getLadder4Rate());
                            msrs.setOneRate(oneRate);
                            msrs.setMerRate(merchantServiceRateService.profitExpression(msrs));
                            String ss = String.valueOf(sis.getFixedRate());
                            msrs.setFixedMark(ss);
                            listmr.add(msrs);
                        }
                    } else {
                        List<MerchantServiceRate> listmrs = merchantServiceRateService.selectByMertIdAndSerivceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceRate merchantServiceRate : listmrs) {
                            //查询服务管控表的费率
                            ServiceRate sr = new ServiceRate();
                            sr.setAgentNo(mi.get(0).getOneAgentNo());
                            sr.setServiceId(Long.valueOf(merchantServiceRate.getServiceId()));
                            sr.setCardType(merchantServiceRate.getCardType());
                            sr.setHolidaysMark(merchantServiceRate.getHolidaysMark());
                            sr.setRateType(merchantServiceRate.getRateType());

                            //查询出一级代理商的费率
                            String oneRate = serviceProService.profitExpression(serviceProService.queryServiceRate(sr));
                            //判断是否固定
                            if (sis.getFixedRate() == 0) {
                                merchantServiceRate.setMerRate(merchantServiceRateService.profitExpression(merchantServiceRate));
                                merchantServiceRate.setFixedMark("0");
                                merchantServiceRate.setOneRate(oneRate);
                            } else {
                                merchantServiceRate.setFixedMark("1");
                                merchantServiceRate.setOneRate(oneRate);
                                merchantServiceRate.setMerRate(oneRate);
                            }
                            listmr.add(merchantServiceRate);
                        }
                    }
                }

            }
            List<ServiceQuota> sqlist1 = new ArrayList<ServiceQuota>();
            if (mbp.getMerchantNo() != null) {
                msq.setMerchantNo(mbp.getMerchantNo());
                for (String str : listStr) {
                    ServiceInfo sis = serviceProService.queryServiceInfo(Long.valueOf(str));
                    if (sis.getFixedQuota() == 1) {
                        List<ServiceQuota> sqlist = serviceProService.getServiceAllQuota(sis.getServiceId(), mi.get(0).getOneAgentNo());
                        for (ServiceQuota serviceQuota : sqlist) {
                            MerchantServiceQuota msqs = new MerchantServiceQuota();
                            msqs.setServiceId(sis.getServiceId().toString());
                            msqs.setServiceName(sis.getServiceName());
                            msqs.setCardType(serviceQuota.getCardType());
                            msqs.setHolidaysMark(serviceQuota.getHolidaysMark());
                            msqs.setSingleCountAmount(serviceQuota.getSingleCountAmount());
                            msqs.setSingleMinAmount(serviceQuota.getSingleMinAmount());
                            msqs.setSingleDayAmount(serviceQuota.getSingleDayAmount());
                            msqs.setSingleDaycardAmount(serviceQuota.getSingleDaycardAmount());
                            msqs.setSingleDaycardCount(serviceQuota.getSingleDaycardCount());
                            String ss = String.valueOf(sis.getFixedQuota());
                            msqs.setFixedMark(ss);
                            listmsq.add(msqs);
                        }
                    } else {
                        List<MerchantServiceQuota> listmsqs = merchantServiceQuotaService.selectByMertIdAndServiceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceQuota merchantServiceQuota : listmsqs) {
                            //查询服务限额表的费率
                            ServiceQuota sq = new ServiceQuota();
                            sq.setAgentNo(mi.get(0).getOneAgentNo());
                            sq.setServiceId(Long.valueOf(merchantServiceQuota.getServiceId()));
                            sq.setCardType(merchantServiceQuota.getCardType());
                            sq.setHolidaysMark(merchantServiceQuota.getHolidaysMark());

                            //查询出一级代理商的限额
                            sq = serviceProService.queryServiceQuota(sq);
                            sqlist1.add(sq);
                            //判断是否固定
                            if (sis.getFixedQuota() == 0) {
                                merchantServiceQuota.setFixedMark("0");
                            } else {
                                merchantServiceQuota.setFixedMark("1");
                            }
                            listmsq.add(merchantServiceQuota);
                        }
                    }
                }

            }
            if (mbp.getMerchantNo() != null) {
                listms = merchantServiceProService.selectByMerId(mbp.getMerchantNo());
            }
            List<MerchantRequireItem> listmris = new ArrayList<MerchantRequireItem>();
            if (mbp.getMerchantNo() != null) {
                List<String> listStrs = businessRequireItemService.findByProduct(mbp.getBpId());
                //判断1图片、2文件
                Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                for (String string : listStrs) {
                    MerchantRequireItem mri = new MerchantRequireItem();
                    mri = merchantRequireItemService.selectByMriId(string, mbp.getMerchantNo());
                    if (mri == null) {
                        continue;
                    }
                    mri.setMerBpId(mbp.getId().toString());
                    if (mri.getExampleType() != null) {
                        if (mri.getExampleType().equals("1")) {//照片
                            if (mri.getStatus().equals("2") && mri.getContent() != null) {//等于2,审核失败,正常显示,否则显示示例照片
                                String content = mri.getContent();
                                String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
                                mri.setContent(newContent);
                                listmris.add(mri);
                                continue;
                            } else {
                                //显示示例照片,从add_require_item表中查询出来把默认示例照片设置进去
                                List<AddRequireItem> items = merchantRequireItemService.selectPhotoAddress();
                                for (int i = 7; i < items.size(); i++) {
                                    AddRequireItem item = items.get(i);
                                    switch (i) {
                                        case 7:
                                            setAddress(item, "手持身份证", mri);
                                            break;
                                        case 8:
                                            setAddress(item, "身份证正面", mri);
                                            break;
                                        case 9:
                                            setAddress(item, "身份证反面", mri);
                                            break;
                                        case 10:
                                            setAddress(item, "银行卡正面", mri);
                                            break;
                                        case 11:
                                            setAddress(item, "营业执照", mri);
                                            break;
                                        case 12:
                                            setAddress(item, "门头照", mri);
                                            break;
                                        case 13:
                                            setAddress(item, "店内照", mri);
                                            break;
                                        case 15:
                                            setAddress(item, "活体照片", mri);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                listmris.add(mri);
                                continue;
                            }
                        }
                        if (mri.getExampleType().equals("2")) {
                            String content = mri.getContent();
                            String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
                            mri.setContent(newContent);
                        }
                    }
                    listmri.add(mri);
                }
            }
            String str1 = "";
            String str2 = "";
            List<MerchantBusinessProduct> merProList = merchantBusinessProductService.selectMerPro(mbp.getMerchantNo());

            Pattern p = Pattern.compile("\\d+(\\d{4})");
            for (MerchantRequireItem list : listmri) {
                System.out.println(list.getMriId() + "========");
                if ("1".equals(list.getStatus())) {//校验是否显示那个复选框gw
                    if ("5".equals(list.getMriId())) {//为联行行号
                        list.setIsShowCheck("1");//不显示按钮
                        continue;
                    }
                    if (merProList != null) {
                        for (MerchantBusinessProduct mbpMap : merProList) {
                            String bpId = mbpMap.getBpId();
                            List<String> merMridList = businessRequireItemService.findMerItem(bpId, list.getMriId());
                            if (merMridList != null && merMridList.size() > 0) {//表示有成功进件
                                list.setIsShowCheck("1");//不显示按钮
                                break;
                            }
                        }
                    }
                }

                if (list.getMriId().equals("3")) {
                    Matcher matcher = p.matcher(list.getContent());
                    String content = matcher.replaceAll("***************$1");
                    list.setContent(content);
                }
                if (list.getMriId().equals("6")) {
                    list.setContent("**************" + list.getContent().substring(14));
                }
                if (list.getMriId().equals("2") || list.getMriId().equals("6")) {
                    str1 += list.getItemName() + ":" + list.getContent() + ",";
                }
                if (list.getMriId().equals("3") || list.getMriId().equals("4")) {
                    str2 += list.getItemName() + ":" + list.getContent() + ",";
                }
            }
            if (listmris.size() > 0) {
                for (MerchantRequireItem lists : listmris) {
                    if ("1".equals(lists.getStatus())) {//校验是否显示那个复选框gw
                        if (merProList != null) {
                            for (MerchantBusinessProduct mbpMap : merProList) {
                                String bpId = mbpMap.getBpId();
                                List<String> merMridList = businessRequireItemService.findMerItem(bpId, lists.getMriId());
                                if (merMridList != null && merMridList.size() > 0) {//表示有成功进件
                                    lists.setIsShowCheck("1");//不显示按钮
                                    break;
                                }
                            }
                        }
                    }
                    if (lists.getMriId().equals("9")) {
                        lists.setRemark(str1);
                        continue;
                    }
                    if (lists.getMriId().equals("11")) {
                        lists.setRemark(str2);
                        continue;
                    }
                    lists.setRemark("");
                }
            }
            //审核记录
            listel = examinationsLogService.selectByMerchantId(mbp.getId().toString());
            maps.put("mbp", mbp);
            maps.put("mi", mi.get(0));
            maps.put("listmr", listmr);
            maps.put("tis", tis);
            maps.put("listmsq", listmsq);
            maps.put("sqlist", sqlist1);
            maps.put("listel", listel);
            maps.put("listmri", listmri);//商户文字进件项集合
            maps.put("listmris", listmris);//商户图片进件项集合
            maps.put("serviceMgr", listms);
            maps.put("bols", true);
            return new ResponseBean(maps);
        } catch (Exception e) {
            return new ResponseBean(e);
        }
    }

    //审核意见查询
    @RequestMapping(value = "/selectMriremark.do")
    @ResponseBody
    public Object selectMriremark(String ids) throws Exception {
        AddRequireItem ari = new AddRequireItem();
        try {
            ari = addRequireItemService.selectByMeriId(ids);
        } catch (Exception e) {
            log.error("审核意见查询失败------", e);
        }
        return ari;
    }

    /**
     * 校验成功的进件项是否可以成功不成功
     *
     * @param mri
     * @param merchantNo
     * @return
     * @author: gaowei
     * @date: 2017年4月21日 下午3:08:22
     */
    public boolean checkItem(List<MerchantRequireItem> mri, String merchantNo) {
        log.info("===>校验成功的进件项是否可以成功不成功");
        List<MerchantBusinessProduct> merProList = merchantBusinessProductService.selectMerPro(merchantNo);
        for (MerchantRequireItem info : mri) {
            if (info.getaStatus() == null) {
                continue;
            }
            if ("不通过".equals(info.getaStatus())) {
                MerchantRequireItem merItem = merchantRequireItemService.selectByNoAndId(merchantNo, info.getMriId());
                if (merItem != null && "1".equals(merItem.getStatus())) {//对于成功的进件项改失败校验
                    if (merProList != null) {
                        for (MerchantBusinessProduct mbpMap : merProList) {
                            String bpId = mbpMap.getBpId();
                            List<String> merMridList = businessRequireItemService.findMerItem(bpId, info.getMriId());
                            if (merMridList != null && merMridList.size() > 0) {//表示有成功进件
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    //审核
    @RequestMapping(value = "/examineRcored.do")
    @ResponseBody
    public Object examineRcored(@RequestBody String param) throws Exception {
        JSONObject jsonMap = new JSONObject();
        JSONObject json = JSON.parseObject(param);
        //判断是那种状态提交
        int val = json.getIntValue("val");
        List<MerchantRequireItem> mri = JSON.parseArray(json.getJSONArray("listMri").toJSONString(), MerchantRequireItem.class);
        List<MerchantRequireItem> mris = JSON.parseArray(json.getJSONArray("listMris").toJSONString(), MerchantRequireItem.class);//图片
        for (MerchantRequireItem merchantRequireItem : mris) {
            mri.add(merchantRequireItem);
        }
        //商户信息
        MerchantInfo merchantInfo = json.getObject("info", MerchantInfo.class);
        if (!checkItem(mri, merchantInfo.getMerchantNo())) {
            jsonMap.put("result", false);
            jsonMap.put("msg", "成功的进件项已经在正常使用不能改不通过");
            return jsonMap;
        }
        if (!checkItem(mris, merchantInfo.getMerchantNo())) {
            jsonMap.put("result", false);
            jsonMap.put("msg", "成功的进件项已经在正常使用不能改不通过");
            return jsonMap;
        }
        //意见
        String opinion = json.getString("opinion");
        if (opinion == null || opinion.equals("")) {
            opinion = "审核成功";
        }
        //商户进件ID
        String merbpId = json.getString("merbpId");
        String bpId = json.getString("bpId");
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //审核记录
        ExaminationsLog el = new ExaminationsLog();
        el.setCreateTime(new Date());
        el.setExaminationOpinions(opinion);
        el.setItemNo(merbpId);
        //获取当前登录的人
        el.setOperator(principal.getUsername());
        int num = 0;

        //MerchantCardInfo mci=new MerchantCardInfo();
        //mci.setMerchantId(merchantInfo.getMerchantNo());
        //mci.setCardType("1");
        //mci.setStatus("1");
        //mci.setDefQuickPay("2");
        //mci.setQuickPay("2");
        try {
            int ns = 0;
            for (int i = 0; i < mri.size(); i++) {
                if (ns == 1) {
                    if (mri.get(i).getMriId().equals("4") || mri.get(i).getMriId().equals("15")) {
                        merchantRequireItemService.updateBymriId(mri.get(i).getId(), "2");
                        continue;
                    }
                }
                if (ns == 2) {
                    if (mri.get(i).getMriId().equals("4")) {
                        merchantRequireItemService.updateBymriId(mri.get(i).getId(), "2");
                        continue;
                    }
                    if (mri.get(i).getMriId().equals("15")) {
                        merchantRequireItemService.updateBymriId(mri.get(i).getId(), "2");
                        continue;
                    }
                }
                if (val == 1 || val == 3) {
                    //修改进件明细的状态
                    merchantRequireItemService.updateBymriId(mri.get(i).getId(), "0");
                }
                if (val == 4 || val == 2) {
                    if ("不通过".equals(mri.get(i).getaStatus())) {
                        //修改进件明细的状态
                        if (mri.get(i).getMriId().equals("3")) {
                            ns = 1;
                        }
                        if (mri.get(i).getMriId().equals("4")) {
                            ns = 2;
                        }
                        if (mri.get(i).getMriId().equals("15")) {
                            for (int j = 0; j < mri.size(); j++) {
                                if (mri.get(j).getMriId().equals("4")) {
                                    merchantRequireItemService.updateBymriId(mri.get(j).getId(), "2");
                                }
                            }
                            ns = 2;
                        }
                        merchantRequireItemService.updateBymriId(mri.get(i).getId(), "2");
                        num = 1;
                        continue;
                    } else {
                        //修改进件明细的状态
                        merchantRequireItemService.updateBymriId(mri.get(i).getId(), "0");
                    }
                }
//				mci.setCreateTime(new Date());
                //			//开户行全称
			/*	if(mri.get(i).getMriId().equals("4"))
					mci.setBankName(mri.get(i).getContent());
	//			//卡号
				if(mri.get(i).getMriId().equals("3"))	
					mci.setAccountNo(mri.get(i).getContent());
	//			//开户名
				if(mri.get(i).getMriId().equals("2"))
					mci.setAccountName(mri.get(i).getContent());
				//账户类型
				if(mri.get(i).getMriId().equals("1")){
					if(mri.get(i).getContent().equals("对公")){
						mci.setAccountType("1");
					}else{
						mci.setAccountType("2");
					}
				}*/
                //联行行号
                //if(mri.get(i).getMriId().equals("5"))
                //	mci.setCnapsNo(mri.get(i).getContent());
                //经营地址拆分
                //	if(mri.get(i).getMriId().equals("7")){
                //	String[] str=mri.get(i).getContent().split("-");
//					if(str.length<=3){
//						jsonMap.put("result", false);
//						jsonMap.put("msg", "经营地址格式不对");
//						return jsonMap;
//					}
//					merchantInfo.setAddress(str[0]+str[1]+str[2]+str[3]);
//					merchantInfo.setProvince(str[0]);
//					merchantInfo.setCity(str[1]);
//					merchantInfo.setDistrict(str[2]);
//				}
                //开户地址拆分
//				if(mri.get(i).getMriId().equals("15")){
//					String content = mri.get(i).getContent();
//					String[] str=content.split("-");
//					if(str.length<2){
//						continue;
//					}
//					mci.setAccountProvince(str[0]);
//					mci.setAccountCity(str[1]);
//					mci.setAccountDistrict(str[2]);
//				}
            }
            //判断是否是成功
            List<AuditorManager> auditorList = auditorManagerService.findAllInfo(bpId);//审核人员查询
            String auditorId = null;
            if (auditorList.size() != 0) {
                Random r = new Random();
                int randint = r.nextInt(auditorList.size());
                auditorId = auditorList.get(randint).getAuditorId();
            }
            //商户审核记录和银行卡添加商户地址修改
            int i = (int) merchantBusinessProductService.updateMerBusiOProInfo(num, merbpId, auditorId);
            if (i > 0) {
                //跳下一条
                if (val == 3 || val == 4) {
                    Page<MerchantBusinessProduct> page = new Page<MerchantBusinessProduct>();
                    List<MerchantBusinessProduct> list = merchantBusinessProductService.selectAllByStatusInfo(page, principal.getUserEntityInfo().getEntityId());
                    if (list.size() < 1) {
                        jsonMap.put("result", true);
                        jsonMap.put("msg", "没有要审核的商户");
                    } else {
                        jsonMap.put("msg", "操作成功");
                        jsonMap.put("result", true);
                        jsonMap.put("infos", list.get(0).getId().toString());
//						jsonMap.put("infos", selectDetailInfo(list.get(0).getId().toString()));
                    }
                } else {
                    jsonMap.put("msg", "操作成功");
                    jsonMap.put("result", true);
                }
            } else {
                jsonMap.put("msg", "操作失败");
                jsonMap.put("result", false);
            }

        } catch (Exception ex) {
            log.error("审核出错-----", ex);
            jsonMap.put("result", false);
            jsonMap.put("msg", "审核报错");
        }

        return jsonMap;
    }

    //图片修改
    @RequestMapping(value = "/updateItemImg.do")
    @ResponseBody
    public Object updateItemImg(@RequestBody String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject json = JSON.parseObject(param);
        try {
            Long id = json.getLong("id");
            String img = json.getString("img");
            MerchantRequireItem mri = new MerchantRequireItem();
            mri.setId(id);
            mri.setContent(img);
            int num = 0;
            num = merchantRequireItemService.updateByMbpId(mri);
            if (num > 0) {
                Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, img, expiresDate);
                jsonMap.put("result", true);
                jsonMap.put("datas", newContent);
            } else {
                jsonMap.put("result", false);
            }
        } catch (Exception ex) {
            log.error("商户修改出错------", ex);
            jsonMap.put("result", false);
        }

        return jsonMap;
    }

    //商户信息修改
    @RequestMapping(value = "/updateMerchantInfo.do")
    @ResponseBody
    public Object updateMerchantInfo(@RequestBody String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject json = JSON.parseObject(param);
        try {
            //商户业务产品
            MerchantBusinessProduct mbp = json.getObject("mbp", MerchantBusinessProduct.class);
            int i = 0;
            //商户信息
            MerchantInfo mis = json.getObject("info", MerchantInfo.class);
            //审核明细表
            List<MerchantRequireItem> mris = JSON.parseArray(json.getJSONArray("listMri").toJSONString(), MerchantRequireItem.class);

            //商户费率
            List<MerchantServiceRate> msr = JSON.parseArray(json.getJSONArray("listMsr").toJSONString(), MerchantServiceRate.class);

            //商户限额
            List<MerchantServiceQuota> msq = JSON.parseArray(json.getJSONArray("listMsq").toJSONString(), MerchantServiceQuota.class);
            //状态
            int status = json.getInteger("status");
            i = merchantBusinessProductService.updateByItemAndMbpId(mbp, mis, mris, msr, msq, status);

            if (i < 1) {
                jsonMap.put("msg", "商户修改失败~~~~~");
                jsonMap.put("result", false);
            } else {
                Sms.sendMsg(mis.getMobilephone(), "您的手机号修改成功,密码为初始密码:" + sysDictService.selectRestPwd().getSysValue() + ",移动支付，快乐生活！");
                jsonMap.put("msg", "商户修改成功~~~~~");
                jsonMap.put("result", true);
            }
        } catch (Exception ex) {
            log.error("商户修改出错------", ex);
            jsonMap.put("result", false);
            String str = ex.getMessage();
            if (ex.getMessage() == null) {
                jsonMap.put("msg", "修改失败");
                return jsonMap;
            }
            if (str.contains("\r\n") || str.contains("\n"))
                jsonMap.put("msg", "修改信息异常");
            else
                jsonMap.put("msg", str);
        }

        return jsonMap;
    }

    //商户服务表修改
    @RequestMapping(value = "updateMerchantService.do")
    @ResponseBody
    public Object updateMerchantService(@RequestBody String param) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject json = JSON.parseObject(param);
        MerchantBusinessProduct mbp = json.getObject("mbp", MerchantBusinessProduct.class);
        try {
            int i = 0;
            Long id = json.getLong("id");
            String status = json.getString("status");
            MerchantService msi = new MerchantService();
            msi.setId(id);
            msi.setStatus(status);
            i = merchantServiceProService.updateByPrimaryKey(msi);
            if (i > 0) {
                jsonMap.put("result", true);
                if (mbp.getMerchantNo() != null) {
                    jsonMap.put("serviceMgr", merchantServiceProService.selectByMerId(mbp.getMerchantNo()));
                }
            } else {
                jsonMap.put("result", false);

            }
        } catch (Exception e) {
            log.error("商户修改出错------", e);
            jsonMap.put("result", false);
        }
        //商户服务

        return jsonMap;
    }

    //对于需要转换为Date类型的属性，使用DateEditor进行处理
    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    public static class SelectParams {
        private String quickPayment;
        private String eTime;
        private String sTime;
        private String termianlType;
        private String productType;
        private String agentName;
        private String agentNode;
        private String merchantExamineState;
        private String merchantNo;
        private String mbpId;
        private String mobilephone;
        private String riskStatus;
        private String activityType;
        private String recommendedSource;//是否是超级推用户
        private String merTeamId;
        private String terminalNo;
        
        private String teamEntryId;
        
        public String getTerminalNo() {
			return terminalNo;
		}

		public void setTerminalNo(String terminalNo) {
			this.terminalNo = terminalNo;
		}

		public String getMerTeamId() {
			return merTeamId;
		}

		public void setMerTeamId(String merTeamId) {
			this.merTeamId = merTeamId;
		}

		public String getRecommendedSource() {
            return recommendedSource;
        }

        public void setRecommendedSource(String recommendedSource) {
            this.recommendedSource = recommendedSource;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String geteTime() {
            return eTime;
        }

        public void seteTime(String eTime) {
            this.eTime = eTime;
        }

        public String getsTime() {
            return sTime;
        }

        public void setsTime(String sTime) {
            this.sTime = sTime;
        }

        public String getTermianlType() {
            return termianlType;
        }

        public void setTermianlType(String termianlType) {
            this.termianlType = termianlType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getMerchantExamineState() {
            return merchantExamineState;
        }

        public void setMerchantExamineState(String merchantExamineState) {
            this.merchantExamineState = merchantExamineState;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getMbpId() {
            return mbpId;
        }

        public void setMbpId(String mbpId) {
            this.mbpId = mbpId;
        }

        public String getAgentNode() {
            return agentNode;
        }

        public void setAgentNode(String agentNode) {
            this.agentNode = agentNode;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getRiskStatus() {
            return riskStatus;
        }

        public void setRiskStatus(String riskStatus) {
            this.riskStatus = riskStatus;
        }

        public String getQuickPayment() {
            return quickPayment;
        }

        public SelectParams setQuickPayment(String quickPayment) {
            this.quickPayment = quickPayment;
            return this;
        }

		public String getTeamEntryId() {
			return teamEntryId;
		}

		public void setTeamEntryId(String teamEntryId) {
			this.teamEntryId = teamEntryId;
		}
    }

    public void setAddress(AddRequireItem item, String s2, MerchantRequireItem mri) {
        if (mri.getItemName().equals(s2)) {
            mri.setContent(ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, item.getPhotoAddress(), new Date(64063065600000L)));
        }
    }

    //商户详情和审核查询 修改页面信息查询  tiangh20170114
    @RequestMapping(value = "/selectDetailInfoToUpdate.do")
    @ResponseBody
    public Object selectDetailInfoToUpdate(String ids) {
        Map<String, Object> maps = new HashMap<>();

        //商户业务产品
        MerchantBusinessProduct mbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(ids));

        List<MerchantServiceRate> listmr = new ArrayList<MerchantServiceRate>();
        List<MerchantServiceQuota> listmsq = new ArrayList<MerchantServiceQuota>();
        List<MerchantRequireItem> listmri = new ArrayList<MerchantRequireItem>();
        List<MerchantInfo> mi = null;
        List<TerminalInfo> tis = null;
        List<MerchantService> listms = null;
        MerchantServiceRate msr = new MerchantServiceRate();
        MerchantServiceQuota msq = new MerchantServiceQuota();
        List<ExaminationsLog> listel = null;
        try {

            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String entityId = principal.getUserEntityInfo().getEntityId();    // 当前登陆代理商
            if (!agentInfoService.merchantIsDirectBelongToAgent(ids, entityId)) {
                throw new AgentWebException("该商户不属于当前登陆代理商的审核失败直营商户或不是一级代理商,无权进行修改");
            }
            List<String> listStr = merchantBusinessProductService.querySerivceId(mbp.getBpId());
            if (mbp.getMerchantNo() != null) {
                tis = terminalInfoService.selectAllInfoBymerNoAndBpId(mbp.getMerchantNo(), mbp.getBpId());
                mi = merchantInfoService.selectByMertId(mbp.getMerchantNo());
                if (mi.size() == 0) {
                    maps.put("msg", "没有该商户~");
                    maps.put("bols", false);
                    return maps;
                }
//				mi.get(0).setIdCardNo("**************"+(mi.get(0).getIdCardNo()).substring(14));
//				mi.get(0).setMobilephone("*******"+mi.get(0).getMobilephone().substring(7,11));
                if (StringUtils.isNotBlank(mi.get(0).getBusinessType())) {
                    mi.get(0).setSysName(merchantInfoService.selectSysDictByKey(mi.get(0).getBusinessType()).getSysName());
                    if (mi.get(0).getIndustryType() != null) {
                        mi.get(0).setTwoSysName(merchantInfoService.selectSysDictByKey(mi.get(0).getIndustryType()).getSysName());
                    }
                }
            }
            if (mbp.getMerchantNo() != null) {
                msr.setMerchantNo(mbp.getMerchantNo());
                msr.setUseable("1");
                for (String str : listStr) {
                    ServiceInfo sis = serviceProService.queryServiceInfo(Long.valueOf(str));
                    if (sis.getFixedRate() == 1) {
                        List<ServiceRate> srlist = serviceProService.getServiceAllRate(sis.getServiceId(), mi.get(0).getOneAgentNo());
                        for (ServiceRate serviceRate : srlist) {
                            String oneRate = serviceProService.profitExpression(serviceRate);
                            MerchantServiceRate msrs = new MerchantServiceRate();
                            msrs.setServiceId(sis.getServiceId().toString());
                            msrs.setServiceName(sis.getServiceName());
                            msrs.setCardType(serviceRate.getCardType());
                            msrs.setHolidaysMark(serviceRate.getHolidaysMark());
                            msrs.setRateType(serviceRate.getRateType());
                            msrs.setCapping(serviceRate.getCapping());
                            msrs.setRate(serviceRate.getRate());
                            msrs.setSafeLine(serviceRate.getSafeLine());
                            msrs.setSingleNumAmount(serviceRate.getSingleNumAmount());
                            msrs.setLadder1Max(serviceRate.getLadder1Max());
                            msrs.setLadder1Rate(serviceRate.getLadder1Rate());
                            msrs.setLadder2Max(serviceRate.getLadder2Max());
                            msrs.setLadder2Rate(serviceRate.getLadder2Rate());
                            msrs.setLadder3Max(serviceRate.getLadder3Max());
                            msrs.setLadder3Rate(serviceRate.getLadder3Rate());
                            msrs.setLadder3Max(serviceRate.getLadder4Max());
                            msrs.setLadder4Rate(serviceRate.getLadder4Rate());
                            msrs.setOneRate(oneRate);
                            msrs.setMerRate(merchantServiceRateService.profitExpression(msrs));
                            String ss = String.valueOf(sis.getFixedRate());
                            msrs.setFixedMark(ss);
                            listmr.add(msrs);
                        }
                    } else {
                        List<MerchantServiceRate> listmrs = merchantServiceRateService.selectByMertIdAndSerivceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceRate merchantServiceRate : listmrs) {
                            //查询服务管控表的费率
                            ServiceRate sr = new ServiceRate();
                            sr.setAgentNo(mi.get(0).getOneAgentNo());
                            sr.setServiceId(Long.valueOf(merchantServiceRate.getServiceId()));
                            sr.setCardType(merchantServiceRate.getCardType());
                            sr.setHolidaysMark(merchantServiceRate.getHolidaysMark());
                            sr.setRateType(merchantServiceRate.getRateType());

                            //查询出一级代理商的费率
                            String oneRate = serviceProService.profitExpression(serviceProService.queryServiceRate(sr));
                            //判断是否固定
                            if (sis.getFixedRate() == 0) {
                                merchantServiceRate.setMerRate(merchantServiceRateService.profitExpression(merchantServiceRate));
                                merchantServiceRate.setFixedMark("0");
                                merchantServiceRate.setOneRate(oneRate);
                            } else {
                                merchantServiceRate.setFixedMark("1");
                                merchantServiceRate.setOneRate(oneRate);
                                merchantServiceRate.setMerRate(oneRate);
                            }
                            listmr.add(merchantServiceRate);
                        }
                    }
                }

            }
            List<ServiceQuota> sqlist1 = new ArrayList<ServiceQuota>();
            if (mbp.getMerchantNo() != null) {
                msq.setMerchantNo(mbp.getMerchantNo());
                for (String str : listStr) {
                    ServiceInfo sis = serviceProService.queryServiceInfo(Long.valueOf(str));
                    if (sis.getFixedQuota() == 1) {
                        List<ServiceQuota> sqlist = serviceProService.getServiceAllQuota(sis.getServiceId(), mi.get(0).getOneAgentNo());
                        for (ServiceQuota serviceQuota : sqlist) {
                            MerchantServiceQuota msqs = new MerchantServiceQuota();
                            msqs.setServiceId(sis.getServiceId().toString());
                            msqs.setServiceName(sis.getServiceName());
                            msqs.setCardType(serviceQuota.getCardType());
                            msqs.setHolidaysMark(serviceQuota.getHolidaysMark());
                            msqs.setSingleCountAmount(serviceQuota.getSingleCountAmount());
                            msqs.setSingleMinAmount(serviceQuota.getSingleMinAmount());
                            msqs.setSingleDayAmount(serviceQuota.getSingleDayAmount());
                            msqs.setSingleDaycardAmount(serviceQuota.getSingleDaycardAmount());
                            msqs.setSingleDaycardCount(serviceQuota.getSingleDaycardCount());
                            String ss = String.valueOf(sis.getFixedQuota());
                            msqs.setFixedMark(ss);
                            listmsq.add(msqs);
                        }
                    } else {
                        List<MerchantServiceQuota> listmsqs = merchantServiceQuotaService.selectByMertIdAndServiceId(mbp.getMerchantNo(), str);
                        for (MerchantServiceQuota merchantServiceQuota : listmsqs) {
                            //查询服务限额表的费率
                            ServiceQuota sq = new ServiceQuota();
                            sq.setAgentNo(mi.get(0).getOneAgentNo());
                            sq.setServiceId(Long.valueOf(merchantServiceQuota.getServiceId()));
                            sq.setCardType(merchantServiceQuota.getCardType());
                            sq.setHolidaysMark(merchantServiceQuota.getHolidaysMark());

                            //查询出一级代理商的限额
                            sq = serviceProService.queryServiceQuota(sq);
                            sqlist1.add(sq);
                            //判断是否固定
                            if (sis.getFixedQuota() == 0) {
                                merchantServiceQuota.setFixedMark("0");
                            } else {
                                merchantServiceQuota.setFixedMark("1");
                            }
                            listmsq.add(merchantServiceQuota);
                        }
                    }
                }

            }
            if (mbp.getMerchantNo() != null) {
                listms = merchantServiceProService.selectByMerId(mbp.getMerchantNo());
            }
            List<MerchantRequireItem> listmris = new ArrayList<MerchantRequireItem>();
            if (mbp.getMerchantNo() != null) {
                List<String> listStrs = businessRequireItemService.findByProduct(mbp.getBpId());
                //判断1图片、2文件
                Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
                for (String string : listStrs) {
                    MerchantRequireItem mri = new MerchantRequireItem();
                    mri = merchantRequireItemService.selectByMriId(string, mbp.getMerchantNo());
                    if (mri == null) {
                        continue;
                    }
                    mri.setMerBpId(mbp.getId().toString());
                    if (mri.getExampleType() != null) {
                        if (mri.getExampleType().equals("1")) {//照片
                            if (mri.getStatus().equals("2") && mri.getContent() != null) {//等于2,审核失败,正常显示,否则显示示例照片
                                String content = mri.getContent();
                                String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
                                mri.setContent(newContent);
                                listmris.add(mri);
                                continue;
                            } else {
                                //显示示例照片,从add_require_item表中查询出来把默认示例照片设置进去
                                List<AddRequireItem> items = merchantRequireItemService.selectPhotoAddress();
                                for (int i = 7; i < items.size(); i++) {
                                    AddRequireItem item = items.get(i);
                                    switch (i) {
                                        case 7:
                                            setAddress(item, "手持身份证", mri);
                                            break;
                                        case 8:
                                            setAddress(item, "身份证正面", mri);
                                            break;
                                        case 9:
                                            setAddress(item, "身份证反面", mri);
                                            break;
                                        case 10:
                                            setAddress(item, "银行卡正面", mri);
                                            break;
                                        case 11:
                                            setAddress(item, "营业执照", mri);
                                            break;
                                        case 12:
                                            setAddress(item, "门头照", mri);
                                            break;
                                        case 13:
                                            setAddress(item, "店内照", mri);
                                            break;
                                        case 15:
                                            setAddress(item, "活体照片", mri);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                listmris.add(mri);
                                continue;
                            }
                        }
                        if (mri.getExampleType().equals("2")) {
                            String content = mri.getContent();
                            String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, content, expiresDate);
                            mri.setContent(newContent);
                        }
                    }
                    listmri.add(mri);
                }
            }
            String str1 = "";
            String str2 = "";
//			Pattern p = Pattern.compile("\\d+(\\d{4})");
            for (MerchantRequireItem list : listmri) {
//				System.out.println(list.getMriId()+"========");
                if (list.getMriId().equals("3")) {
//					Matcher matcher = p.matcher(list.getContent());
//					String content = matcher.replaceAll("***************$1");
//					list.setContent(content);
                    list.setContent(list.getContent());
                }
                if (list.getMriId().equals("6")) {
//					list.setContent("**************"+list.getContent().substring(14));
                    list.setContent(list.getContent());
                }
                if (list.getMriId().equals("2") || list.getMriId().equals("6")) {
                    str1 += list.getItemName() + ":" + list.getContent() + ",";
                }
                if (list.getMriId().equals("3") || list.getMriId().equals("4")) {
                    str2 += list.getItemName() + ":" + list.getContent() + ",";
                }
            }
            if (listmris.size() > 0) {
                for (MerchantRequireItem lists : listmris) {
                    if (lists.getMriId().equals("9")) {
                        lists.setRemark(str1);
                        continue;
                    }
                    if (lists.getMriId().equals("11")) {
                        lists.setRemark(str2);
                        continue;
                    }
                    lists.setRemark("");
                }
            }
            //审核记录
            listel = examinationsLogService.selectByMerchantId(mbp.getId().toString());
            maps.put("mbp", mbp);
            maps.put("mi", mi.get(0));
            maps.put("listmr", listmr);
            maps.put("tis", tis);
            maps.put("listmsq", listmsq);
            maps.put("sqlist", sqlist1);
            maps.put("listel", listel);
            maps.put("listmri", listmri);
            maps.put("listmris", listmris);
            maps.put("serviceMgr", listms);
            maps.put("bols", true);
        } catch (AgentWebException e) {
            log.error("详情查询失败----", e);
            maps.put("bols", false);
            maps.put("msg", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("详情查询失败----", e);
            maps.put("bols", false);
            maps.put("msg", "详情查询失败");
        }
        return maps;
    }

    //商户汇总309tgh====
    @RequestMapping(value = "/selectMechant.do")
    @ResponseBody
    public Integer selectMechant(@RequestParam("info") String param) throws Exception {
        Integer num = 0;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
            String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
            if(StringUtils.isNotBlank(accessTeamId)){
                selectParams.setMerTeamId(accessTeamId);
            }
            //xm tgh是否包含下级无效20170207
            if ("0".equals(selectParams.getAgentNode())) {//包含下级
                if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                    AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                    if (ais != null) {
                        selectParams.setAgentName(null);
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                    }
                } else {
                    selectParams.setAgentNode(null);
                }
            } else {
                selectParams.setAgentNode(null);
            }
            num = merchantBusinessProductService.selectMechant(selectParams, principal.getUserEntityInfo().getEntityId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败----", e);
        }
        return num;
    }
    
    @RequestMapping(value="/threeMerchantQuery.do")
	@ResponseBody
	public ResponseBean threeMerchantQuery(@RequestBody SelectParams selectParams,
								@RequestParam(defaultValue = "1") int pageNo,
								@RequestParam(defaultValue = "10") int pageSize) throws Exception{	
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginAgentNo = principal.getUserEntityInfo().getEntityId();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        String selectAgentNo = selectParams.getAgentName();
        // 权限检查
        boolean auth = agentInfoService.isAuth(loginAgentNo, selectAgentNo);
		if (!auth) {
			 return new ResponseBean(new ArrayList<>(), 0);
		}
		AgentInfo selectAgent = agentInfoService.selectByagentNo(selectParams.getAgentName());
		// 身份替换
		loginAgent = selectAgent;
		loginAgentNo = selectAgentNo;
        try {
            if ("0".equals(selectParams.getAgentNode())) {//包含下级
                if (StringUtils.isNotBlank(selectParams.getAgentName())) {
                    AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
                    if (ais != null) {
                        selectParams.setAgentName(null);
                        selectParams.setAgentNode(ais.getAgentNode() + "%");
                    } else {
                        return new ResponseBean(null, 0);
                    }
                } else {
                    selectParams.setAgentNode(null);
                }
            } else {
                selectParams.setAgentNode(null);
            }
            Page<MerchantBusinessProduct> page = new Page<>(pageNo, pageSize);
            List<MerchantBusinessProduct> mbplist = merchantBusinessProductService.selectByParam(page, selectParams, loginAgent.getAgentNo());
            for (MerchantBusinessProduct merchantBusinessProduct : mbplist) {
                /**
            	 * 机构可以查看所有商户的手机号码，大盟主只能查看直营商户的手机号码，非直营商户的手机号码打上掩码
            	 */
            	if ("11".equals(loginAgent.getAgentType())) {
            		//PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
            		String merchantNo = merchantBusinessProduct.getMerchantNo();
            		Map<String,String> merchantInfoMap = perAgentService.selectByMerchantNo(merchantNo);
            		log.info("用商户号 === >" + merchantNo + "查询到pa_mer_info表 ==>" + merchantInfoMap);
//            		if (loginAgent.getAgentLevel() != 1 && !loginAgentNo.equals(merchantBusinessProduct.getAgentNo())) {
            		/*if (loginAgent.getAgentLevel() != 1 && !(paUserInfo.getUserCode()).equals(merchantInfoMap == null ? "" : merchantInfoMap.get("user_code"))) {
            			merchantBusinessProduct.setMobilePhone(StringUtil.isBlank(merchantBusinessProduct.getMobilePhone()) ? "" : merchantBusinessProduct.getMobilePhone().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
            		}*/
				}else{
            		/*if (!agentFunctionService.queryFunctionValue(loginAgent, "001")) {
            			merchantBusinessProduct.setMobilePhone(CommonUtil.replaceMask(merchantBusinessProduct.getMobilePhone(), "^(.{4}).*?$", "$1*******"));
            		}*/
            	}
            	merchantBusinessProduct.setMobilePhone(CommonUtil.replaceMask(merchantBusinessProduct.getMobilePhone(), "(?<=[\\d]{3})\\d(?=[\\d]{3})", "*"));
                if (merchantBusinessProduct.getAgentNo().equals(loginAgentNo)) {
                    merchantBusinessProduct.setShowReplace("1");
                }
                
               // Map<String, Object> map = merchantInfoService.getMerGroupByTeamId(merchantBusinessProduct.getMerTeamId());
               // merchantBusinessProduct.setMerGroup((String)map.get("team_name"));
            }
            return new ResponseBean(mbplist, page.getTotalCount());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模糊查询失败失败----", e);
            return new ResponseBean(e);
        }
	}
	
	@RequestMapping(value="/threeSelectMechant.do")
	@ResponseBody
	public Integer threeSelectMechant(@RequestParam("info") String param) throws Exception{
		 Integer num = 0;
	        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        try {
	            SelectParams selectParams = JSON.parseObject(param, SelectParams.class);
	            String loginAgentNo = principal.getUserEntityInfo().getEntityId();
	            String selectAgentNo = selectParams.getAgentName();
	            boolean auth = agentInfoService.isAuth(loginAgentNo, selectAgentNo);
	            if (!auth) {
	            	return 0;
	            }
				loginAgentNo = selectAgentNo;
	            //xm tgh是否包含下级无效20170207
	            if ("0".equals(selectParams.getAgentNode())) {//包含下级
	                if (StringUtils.isNotBlank(selectParams.getAgentName())) {
	                    AgentInfo ais = agentInfoService.selectByagentNo(selectParams.getAgentName());
	                    if (ais != null) {
	                        selectParams.setAgentName(null);
	                        selectParams.setAgentNode(ais.getAgentNode() + "%");
	                    }
	                } else {
	                    selectParams.setAgentNode(null);
	                }
	            } else {
	                selectParams.setAgentNode(null);
	            }
	            num = merchantBusinessProductService.selectMechant(selectParams, loginAgentNo);
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error("查询失败----", e);
	        }
	        return num;
	}
    
}
