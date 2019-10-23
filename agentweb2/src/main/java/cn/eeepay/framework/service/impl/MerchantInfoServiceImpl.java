package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

@Service("merchantInfoService")
public class MerchantInfoServiceImpl implements MerchantInfoService {
    private static final Logger log = LoggerFactory.getLogger(MerchantInfoServiceImpl.class);

    @Resource
    private BusinessProductDefineDao businessProductDefineDao;

    @Resource
    private MerchantInfoDao merchantInfoDao;

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private TerminalInfoDao terminalInfoDao;

    @Resource
    private SeqService seqService;

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private RiskRuleService riskRuleService;

    @Resource
    private MerchantRequireItemService merchantRequireItemService;

    @Resource
    private BusinessRequireItemService businessRequireItemService;

    @Resource
    private MerchantServiceRateService merchantServiceRateService;

    @Resource
    private ServiceProService serviceProService;

    @Resource
    private TerminalInfoService terminalInfoService;

    @Resource
    private AuditorManagerService auditorManagerService;


    @Resource
    private GatherCodeService gatherCodeService;

    @Autowired
    private AcqMerchantInfoMapper acqMerchantInfoMapper;

    @Autowired
    private AcqMerchantFileInfoMapper acqMerchantFileInfoMapper;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private AcqMerchantInfoLogMapper acqMerchantInfoLogMapper;

    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private HardwareProductDao hardwareProductDao;

    @Override
    public MerchantInfo selectByPrimaryKey(Long id) {
        return merchantInfoDao.selectByPrimaryKey(id);
    }

//	@Override
//	public int updateByPrimaryKeySelective(MerchantInfo record) {
//		return merchantInfoDao.updateByPrimaryKeySelective(record);
//	}

    @Override
    public int updateByPrimaryKey(MerchantInfo record) {
        return merchantInfoDao.updateByPrimaryKey(record);
    }

    @Override
    public List<MerchantInfo> selectAllInfo() {
        return merchantInfoDao.selectAllInfo();
    }

    @Override
    public List<MerchantInfo> selectByMertId(String mertId) {
        return merchantInfoDao.selectByMertId(mertId);
    }

    @Override
    public int updateByMerId(MerchantInfo record) {
        return merchantInfoDao.updateByMerId(record);
    }

    @Override
    public List<MerchantInfo> selectAllInfoByTermianl() {
        return merchantInfoDao.selectAllInfoByTermianl();
    }

    @Override
    public List<ServiceRate> getServiceRatedByParams(String one_agent_no, String bp_id) {
        List<ServiceRate> rateList = merchantInfoDao.getServiceRatedByParams(one_agent_no, bp_id);
        for (ServiceRate serviceRate : rateList) {
            serviceRate.setMerRate(serviceProService.profitExpression(serviceRate));
            serviceRate.setOneMerRate(serviceProService.profitExpression(serviceRate));
        }
        return rateList;
    }

    @Override
    public List<ServiceQuota> getServiceQuotaByParams(String one_agent_no, String bp_id) {
        return merchantInfoDao.getServiceQuotaByParams(one_agent_no, bp_id);
    }

    @Override
    public String getMerchantNo(String mcc) {
        String merchantNoFron = "2" + mcc;
        String merchantNoBack = seqService.createKey("merchant_no_seq", new BigInteger("1000000000"));
        String merchantNo = merchantNoFron + merchantNoBack;
        return merchantNo;
    }

    /**
     * 商户进件流程
     */
    @Override
    public Map<String, Object> insertMerchantInfo(JSONObject json) {
        String msg = "商户进件异常";
        int i = 0;
        Map<String, Object> jsonMap1 = new HashMap<String, Object>();
        MerchantInfo merInfo = json.getObject("infos", MerchantInfo.class);
        items itemStr = json.getObject("item", items.class);

        //在商户类型选择为“个人”和“个体商户”时需校验“法人姓名”和“开户名”是否一致，
        // 如不一致则提示：法人姓名和开户名不一致，请重新输入
        if ("1".equals(merInfo.getMerchantType()) || "2".equals(merInfo.getMerchantType())) {
            if (!merInfo.getLawyer().equals(itemStr.getAccountName())) {
                msg = "法人姓名和开户名不一致，请重新输入";
                throw new RuntimeException(msg);
            }
        }
        //根据boss后台-风控管理-风控规则管理中的114编号风控规则
        // 设置的禁止进件地区屏蔽商户进件中对于的地区选项
        Map<String, Object> riskRuleMap = riskRuleService.getRiskRule("114");
        if (riskRuleMap != null) {
            String provinces = (String) riskRuleMap.get("rules_provinces");
            String citys = (String) riskRuleMap.get("rules_city");
            if (provinces.contains(merInfo.getProvince()) && citys.contains(merInfo.getCity())) {

            }else{
                msg = "该地区暂不支持进件";
                throw new RuntimeException(msg);
            }
        }


        String bpId = json.getJSONObject("mbp").getString("bpId");
        String teamIdByBpiD = businessProductDefineDao.selectTeamIdByBpId(bpId);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mobilephone = merInfo.getMobilephone();
        String merName = merInfo.getMerchantName().trim();
        String merNo = getMerchantNo(merInfo.getIndustryType());
        if (merNo.length() != 15) {
            msg = "生成商户号失败";
            throw new RuntimeException(msg);
        }
        AgentInfo ais = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
        if ("11".equals(ais.getAgentType())) {//人人代理
            merInfo.setRecommendedSource("3");
        } else {
            merInfo.setRecommendedSource("0");
        }
        merInfo.setAgentNo(ais.getAgentNo());
        merInfo.setMerchantName(merName);
        merInfo.setSaleName(ais.getAgentNo());
        merInfo.setOneAgentNo(ais.getOneLevelId());
        merInfo.setMerchantNo(merNo);
        merInfo.setStatus("1");
        merInfo.setParentNode(ais.getAgentNode());
        merInfo.setCreator(ais.getAgentNo());
//			merInfo.setTeamId(principal.getTeamId());
        merInfo.setTeamId(teamIdByBpiD);
        merInfo.setCreateTime(new Date());
        String addr = merInfo.getProvince() + "-" + merInfo.getCity() + "-" + merInfo.getDistrict() + "-" + merInfo.getAddress();
        //merInfo.setProvince("" );
       // merInfo.setCity("");
        //merInfo.setAddress("");
        //商户业务产品
        MerchantBusinessProduct mbp = json.getObject("mbp", MerchantBusinessProduct.class);
        mbp.setSaleName(principal.getRealName());
        mbp.setMerchantNo(merNo);
        int isApprove = merchantInfoDao.isApprove(ais.getOneLevelId());//假设O为一级代理商
        if (isApprove == 1) {
            mbp.setStatus("1");//待一审核
        }
        if (isApprove == 0) {
            mbp.setStatus("2");//待平台审核
            List<AuditorManager> auditorList = auditorManagerService.findAllInfo(mbp.getBpId());//审核人员查询
            if (auditorList.size() == 0) {
                mbp.setAuditorId(null);
            } else {
                Random r = new Random();
                int randint = r.nextInt(auditorList.size());
                mbp.setAuditorId(auditorList.get(randint).getAuditorId());
            }
        }
        i = merchantInfoDao.insertMer(merInfo);//商户信息
        if (i != 1) {
            msg = "商户信息插入失败";
            throw new RuntimeException(msg);

        }
        List<GatherCode> gatherCodeInfoList = new ArrayList<>();
        //机具分配
        List<TerminalInfo> shuzu = JSON.parseArray(json.getJSONArray("shuzu").toJSONString(), TerminalInfo.class);
//        for (TerminalInfo string : shuzu) {//业务修改,不支持多个机具同时进件,只有一个sn
        TerminalInfo terminalInfo = shuzu.get(0);
        String sn = terminalInfo.getSn();
        TerminalInfo checkAgentSn = terminalInfoService.checkAgentSn(principal.getUserEntityInfo().getEntityId(), sn);//当前登录代理商
        if (checkAgentSn == null) {
            msg = "所输入的机具不存在";
            throw new RuntimeException(msg);
        }
        // 盛POS和超级盛POS区分,写入子级组织ID team_entry_id
        String teamEntryId = merchantInfoDao.selectTeamEntryId(sn);
        merInfo.setTeamEntryId(teamEntryId);
        i = merchantInfoDao.insertMer(merInfo);//商户信息
        if (i != 1) {
            msg = "商户信息插入失败";
            throw new RuntimeException(msg);

        }
        // 判断绑定的机具是否是超级推机具，是的话需要调用一次高伟的绑定超级推机具业务处理的接口
        if (merchantInfoDao.selectSuperPushTerminal(teamIdByBpiD,sn) > 0){
            //超级推机具,调用绑定机具接口
            log.info("<<<<<<<<<<<<<<<<<<<<"+merNo+"超级推商户进件接口");
            String signKey = sysConfigDao.getStringValueByKey("CORE_KEY");
            String coreUrl = sysDictDao.SelectServiceId("CORE_URL");
            String result = ClientInterface.cjtMerToCjtMer(coreUrl, merNo, sn, signKey);
            log.info("调用绑定超级推机具接口返回结果====> {}",result);
        }

        //人人代理机具
        Map<String, Object> paTerInfoMap = merchantInfoDao.selectFromPaTerInfo(principal.getUserEntityInfo().getEntityId(), sn);
        if (paTerInfoMap != null && "6".equals(ais.getAgentType())) {
            //调用伟哥接口
            log.info("===========进入人人代理商户进件=========");
            String result = ClientInterface.saveMerchantInfo(sn, merNo);
            log.info("====接口返回 result = " + result + "======");
        }

        // 如果为超级推机具，则调用高伟的接口  代理商进件时只能绑定一个机具
        TerminalInfo info = terminalInfoDao.selectBySn(sn);
        log.info("判断是否为超级推机具");
        if(StringUtils.isNotBlank(hardwareProductDao.selectSuperPushHardwareByHpId(info.getType()))){
            jsonMap1.put("superPush", true);
            jsonMap1.put("sn", sn);
            jsonMap1.put("merchantNo", merInfo.getMerchantNo());
        }

        //写入收款码信息开始gw
        log.info("===>开始写入收款码信息开始");
        TerminalInfo checkJhSnInfo = terminalInfoService.querySn(sn);
        String psamNo = checkJhSnInfo.getPsamNo();
        psamNo = psamNo.substring(0, 2);
        if ("jh".equals(psamNo)) {
            String collection_code = checkJhSnInfo.getCollectionCode();
            GatherCode gatherCodeInfo = new GatherCode();
            gatherCodeInfo.setGatherCode(collection_code);
            gatherCodeInfo.setGatherName("初始化收银台");
            gatherCodeInfo.setDeviceSn(sn);
            gatherCodeInfo.setMerchantNo(merNo);
            gatherCodeInfo.setStatus(2);
            gatherCodeInfo.setMaterialType(1);
            gatherCodeInfoList.add(gatherCodeInfo);
        }
        checkAgentSn.setBpId(mbp.getBpId());
        checkAgentSn.setMerchantNo(merInfo.getMerchantNo());
        checkAgentSn.setOpenStatus("2");
        checkAgentSn.setStartTime(new Date());
        i = terminalInfoService.updateBundlingItem(checkAgentSn);
        if (i != 1) {
            msg = "机具分配失败";
            throw new RuntimeException(msg);
        }
//        }
        //写入收款码信息开始gw
        if (gatherCodeInfoList.size() > 0) {
            i = gatherCodeService.insertBatch(gatherCodeInfoList);
            if (i != gatherCodeInfoList.size()) {
                msg = "写入收款码失败";
                throw new RuntimeException(msg);
            }
        }
        log.info("===>开始写入收款码信息结束");
        //商户进件资料
        List<String> nums = JSON.parseArray(json.getJSONArray("nums").toJSONString(), String.class);
        List<String> content = JSON.parseArray(json.getJSONArray("content").toJSONString(), String.class);
        //items itemStr=json.getObject("item",items.class);
        MerchantRequireItem merchantRequireItem = new MerchantRequireItem();
        merchantRequireItem.setMerchantNo(merInfo.getMerchantNo());
        merchantRequireItem.setStatus("0");
        List<String> strList = businessRequireItemService.findByProduct(mbp.getBpId());
        for (String string : strList) {
            merchantRequireItem.setMriId(string);
            if (string.equals("1")) {
                if (itemStr.getAccountType().equals("3")) {
                    itemStr.setAccountType("对公");
                } else {
                    itemStr.setAccountType("对私");
                }
                merchantRequireItem.setContent(itemStr.getAccountType());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件账户类型插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            if (string.equals("2")) {
                merchantRequireItem.setContent(itemStr.getAccountName());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件开户名插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            if (string.equals("3")) {
                merchantRequireItem.setContent(itemStr.getAccountNo());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件开户账号插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            if (string.equals("4")) {
//						merchantRequireItem.setContent(itemStr.getAccountHangName());
                merchantRequireItem.setContent(itemStr.getBankName());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件开户行全称插入失败";
                    throw new RuntimeException(msg);

                }
                continue;
            }
            if (string.equals("5")) {
                merchantRequireItem.setContent(itemStr.getUnionAccountNo());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件联行行号插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            if (string.equals("6")) {
                merchantRequireItem.setContent(merInfo.getIdCardNo());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件开户身份证插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            if (string.equals("7")) {
                merchantRequireItem.setContent(addr.trim());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件经营地址插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            if (string.equals("15")) {
                merchantRequireItem.setContent(itemStr.getAccountAddress());
                i = merchantRequireItemService.insertInfo(merchantRequireItem);
                if (i != 1) {
                    msg = "商户进件开户地区插入失败";
                    throw new RuntimeException(msg);
                }
                continue;
            }
            for (int j = 0; j < nums.size(); j++) {
                if (string.equals(nums.get(j))) {
                    merchantRequireItem.setContent(content.get(j));
                    i = merchantRequireItemService.insertInfo(merchantRequireItem);
                    if (i != 1) {
                        msg = "商户进件资料图片插入失败";
                        throw new RuntimeException(msg);
                    }
                    break;
                }
            }
        }
        log.info("商户进件资料 i=" + i);

        //商户服务
        List<ServiceInfo> listService = JSON.parseArray(json.getJSONArray("listService").toJSONString(), ServiceInfo.class);
        for (ServiceInfo serviceInfo : listService) {
            MerchantService merService = new MerchantService();
            merService.setBpId(mbp.getBpId());
            merService.setMerchantNo(merNo);
            merService.setServiceId(serviceInfo.getServiceId().toString());
            merService.setCreateDate(new Date());
            String status = String.valueOf(serviceInfo.getServiceStatus());
            merService.setStatus(status);
            i = merchantInfoDao.insertMerService(merService);
            if (i != 1) {
                msg = "商户服务插入失败";
                throw new RuntimeException(msg);
            }
        }
        log.info("商户服务 i=" + i);
        //商户限额
        List<ServiceQuota> msq = JSON.parseArray(json.getJSONArray("listMsq").toJSONString(), ServiceQuota.class);
        for (ServiceQuota serviceQuota : msq) {
            if (serviceQuota.getFiexdQuota() == 0) {
                MerchantServiceQuota msqs = new MerchantServiceQuota();
                msqs.setMerchantNo(merNo);
                msqs.setServiceId(serviceQuota.getServiceId().toString());
                msqs.setCardType(serviceQuota.getCardType());
                msqs.setHolidaysMark(serviceQuota.getHolidaysMark());
                msqs.setSingleCountAmount(serviceQuota.getSingleCountAmount());
                msqs.setSingleDayAmount(serviceQuota.getSingleDayAmount());
                msqs.setSingleDaycardAmount(serviceQuota.getSingleDaycardAmount());
                msqs.setSingleDaycardCount(serviceQuota.getSingleDaycardCount());
                msqs.setSingleMinAmount(serviceQuota.getSingleMinAmount());
                i = merchantInfoDao.insertMerQuota(msqs);
                if (i != 1) {
                    msg = "商户限额插入失败";
                    throw new RuntimeException(msg);

                }
            }
        }
        log.info("商户限额 i=" + i);
        //商户费率
        List<ServiceRate> msr = JSON.parseArray(json.getJSONArray("listMsr").toJSONString(), ServiceRate.class);
        for (ServiceRate serviceRate : msr) {
            if (serviceRate.getFixedRate() == 0) {
                MerchantServiceRate msrs = new MerchantServiceRate();
                msrs.setMerchantNo(merNo);
                msrs.setServiceId(serviceRate.getServiceId().toString());
                msrs.setCardType(serviceRate.getCardType());
                msrs.setHolidaysMark(serviceRate.getHolidaysMark());
                msrs.setRateType(serviceRate.getRateType());
                msrs.setMerRate(serviceRate.getMerRate());
                MerchantServiceRate msrs1 = merchantServiceRateService.setMerchantServiceRate(msrs);
                msrs.setCapping(msrs1.getCapping());
                msrs.setRate(msrs1.getRate());
                msrs.setSafeLine(msrs1.getSafeLine());
                msrs.setSingleNumAmount(msrs1.getSingleNumAmount());
                msrs.setLadder1Max(msrs1.getLadder1Max());
                msrs.setLadder1Rate(msrs1.getLadder1Rate());
                msrs.setLadder2Max(msrs1.getLadder2Max());
                msrs.setLadder2Rate(msrs1.getLadder2Rate());
                msrs.setLadder3Max(msrs1.getLadder3Max());
                msrs.setLadder3Rate(msrs1.getLadder3Rate());
                msrs.setLadder4Max(msrs1.getLadder4Max());
                msrs.setLadder4Rate(msrs1.getLadder4Rate());
                i = merchantInfoDao.insertMerRate(msrs);
                if (i != 1) {
                    msg = "商户费率插入失败";
                    throw new RuntimeException(msg);

                }
            }
        }
        log.info("商户费率 i=" + i);

        //======手机端注册,商户进件======tiangh=======================
        //	String teamId = principal.getTeamId();
        log.info("=========业务产品带过来的teamId: " + teamIdByBpiD);
        Map<String, Object> map = merchantInfoDao.getMerMobilephone(mobilephone, teamIdByBpiD);
        UserEntityInfo userEntityInfo = null;
        if (map != null) {
            int row = 0;
            String userId = (String) map.get("userid_m");
            if (!StringUtils.isNotBlank(userId)) {//USERID不存在
                String id = (String) map.get("user_id");
                merchantInfoDao.insertMerchantUserEntity(merNo, id);
            } else {//USERID存在
                if (merchantInfoDao.selectFromSuperPuserUserByUserId(userId) != null) {//判断超级推用户表是否已经存在
                    throw new RuntimeException("微创业用户请在商户APP完善资料");
                }
                String userName = (String) map.get("user_name");
                String entity_id = (String) map.get("entity_id_m");
                if (userName == null || userName.equals("")) {
                    row = merchantInfoDao.updateUserName(merName, userId);
                    if (row != 1) {
                        msg = "用户名更新失败";
                        throw new RuntimeException(msg);
                    }
                }
                if (entity_id == null || entity_id.equals("")) {
                    row = merchantInfoDao.updateEntity(merNo, userId);
                    if (row != 1) {
                        msg = "用户编号更新失败";
                        throw new RuntimeException(msg);
                    }
                } else {
                    msg = "手机号用户已存在";
                    throw new RuntimeException(msg);
                }
            }
        } else {
            userEntityInfo = new UserEntityInfo();
            userEntityInfo.setEntityId(merNo);
            userEntityInfo.setManage("1");
            userEntityInfo.setStatus("1");
            userEntityInfo.setUserType("2");
            userEntityInfo.setApply("2");
            UserInfo userInfo = new UserInfo();
            userInfo.setMobilephone(mobilephone);
//				userInfo.setTeamId(principal.getTeamId());//先假设还未从前台传
            userInfo.setTeamId(teamIdByBpiD);
            userInfo.setUserName(merName);
            String userId = seqService.createKey(Constants.USER_NO_SEQ, new BigInteger(Constants.USER_VALUE));
            String possword = Md5.md5Str("123456{" + userInfo.getMobilephone() + "}");
            userInfo.setPassword(possword);
            userInfo.setUserId(userId);

            int row = merchantInfoDao.insertUserInfo(userInfo);
            if (row != 1) {
                msg = "用户信息插入失败";
                throw new RuntimeException(msg);
            }

            userEntityInfo.setUserId(userId);
            //228
            int entityrow = merchantInfoDao.insertAgentUserEntity(userEntityInfo);
            if (entityrow != 1) {
                msg = "商户用户信息插入失败";
                throw new RuntimeException(msg);
            }
        }

        //=====================================================


//			UserEntityInfo userEntityInfo = new UserEntityInfo();
//			userEntityInfo.setEntityId(merNo);
//			userEntityInfo.setManage("1");
//			userEntityInfo.setStatus("1");
//			userEntityInfo.setUserType("2");
//			userEntityInfo.setApply("2");		
//			UserInfo userInfo = new UserInfo();
//			userInfo.setMobilephone(mobilephone);
//			userInfo.setTeamId(principal.getTeamId());//先假设还未从前台传
//			userInfo.setUserName(merName);
//			UserInfo checkUser = merchantInfoDao.getMobilephone(merNo,principal.getTeamId());
//			if(checkUser == null){//没有用户信息但没有应用信息
//				String userId = seqService.createKey(Constants.USER_NO_SEQ, new BigInteger(Constants.USER_VALUE));
//				String possword = Md5.md5Str("123456{"+userInfo.getMobilephone()+"}");	
//				userInfo.setPassword(possword);
//				userInfo.setUserId(userId);		
//				try{
//					int row =  merchantInfoDao.insertUserInfo(userInfo);
//					if(row !=1){	
//						throw new RuntimeException(msg);	
//					}	
//				}catch(Exception e){
//					msg = "用户信息插入失败";
//					throw new RuntimeException(msg);	
//				}
//				
//				userEntityInfo.setUserId(userId);
//			}else{
//				userEntityInfo.setUserId(checkUser.getUserId());
//				userInfo.setPassword(checkUser.getPassword());
//			}		
//				int entityrow = merchantInfoDao.insertAgentUserEntity(userEntityInfo);
//				if(entityrow != 1){
//					msg = "商户用户信息插入失败";
//					throw new RuntimeException(msg);	
//				}
//				msg =Constants.MER_ADD_SUCCESS;


        //V2代理商web端商户进件成功后，根据商户经营地址归属对应集群，
        // 集群归属逻辑与商户APP客户端一致，既有地区集群的。
        merchantInfoDao.updateMerGroupCity(merInfo.getMerchantNo());
        //开设商户账户与事务无关

        try {
            String acc = ClientInterface.createMerchantAccount(merInfo.getMerchantNo());
            if (JSONObject.parseObject(acc).getBooleanValue("status")) {
                merchantInfoDao.updateMerCountBymerNo(merInfo.getMerchantNo(), 1);
            } else {
                jsonMap1.put("bols", false);
                jsonMap1.put("msg", "开设商户账户失败");
                return jsonMap1;
            }
        } catch (Exception e) {
            jsonMap1.put("bols", false);
            jsonMap1.put("msg", "开设商户账户失败");
            return jsonMap1;
        }
        SysDict sysDict=sysDictService.getByKey("SENSORS_STATUS");
        if(sysDict!=null&&sysDict.getSysValue().equals("1")){
            Map<String, Object> merMap=merchantInfoDao.getUserByMerNo(merNo);
            List<Map<String, Object>> list=merchantInfoDao.getBpHpByMerNo(merNo);
            if(merMap!=null){
                try {
                    //根据身份证计算性别/生日
                    String gender = "未知";//性别
                    String age = "未知";//年龄
                    if(merMap.get("id_card_no")!=null){
                        String id_card_no = String.valueOf(merMap.get("id_card_no"));//身份证号码
                        if (StringUtils.isNotBlank(id_card_no) && id_card_no.length() == 18) {
                            age = String.format("%s-%s-%s", id_card_no.substring(6, 10), id_card_no.substring(10, 12), id_card_no.substring(12, 14));
                            try {
                                int sex = Integer.valueOf(id_card_no.substring(16, 17));
                                gender = (sex & 1) == 1 ? "男" : "女";
                            } catch (Exception e) {
                                gender = "未知";
                            }
                        }
                    }
                    merMap.put("teamEntryId",teamEntryId);
                    merMap.put("source_system ","YS");
                    merMap.put("merchant_type",merInfo.getMerchantType());
                    merMap.put("gender",gender);
                    merMap.put("birthday",age);
                    merMap.put("birthyear",age.split("-")[0]);
                    String token = sysConfigDao.getStringValueByKey("SENSORS_TOKEN");
                    String apiUrl = sysConfigDao.getStringValueByKey("SENSORS_URL");
                    String projectName = sysConfigDao.getStringValueByKey("SENSORS_PROJECT");
                    ClientInterface.getRegisterSource(apiUrl,token,projectName,merMap,list);
                } catch (Exception e) {
                    log.error("调用神策注册接口异常",e);
                }
            }
        }


        log.info("进件完成 i=" + i);
        jsonMap1.put("bols", true);
        jsonMap1.put("msg", Constants.MER_ADD_SUCCESS);
        return jsonMap1;
    }

    public static class items {
        private String accountName;
        private String accountType;
        private String accountHangName;
        private String accountNo;
        private String unionAccountNo;
        private String accountAddress;
        private String bankName;

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountHangName() {
            return accountHangName;
        }

        public void setAccountHangName(String accountHangName) {
            this.accountHangName = accountHangName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getUnionAccountNo() {
            return unionAccountNo;
        }

        public void setUnionAccountNo(String unionAccountNo) {
            this.unionAccountNo = unionAccountNo;
        }

        public String getAccountAddress() {
            return accountAddress;
        }

        public void setAccountAddress(String accountAddress) {
            this.accountAddress = accountAddress;
        }

    }


    @Override
    public List<ServiceInfo> getServiceInfoByParams(String agent_no, String bp_id) {
        return merchantInfoDao.getServiceInfoByParams(agent_no, bp_id);
    }

    @Override
    public List<SysDict> getMerTypeMcc(String syskey, String parentId) {
        return merchantInfoDao.getMerTypeMcc(syskey, parentId);
    }

    @Override
    public List<SysDict> getMerAllMcc(String syskey) {
        return merchantInfoDao.getMerAllMcc(syskey);
    }

    @Override
    public List<MerchantInfo> queryCardMerInfo(String cardNo, String teamId) {
        return merchantInfoDao.queryCardMerInfo(cardNo, teamId);
    }

    @Override
    public List<BusinessProductHardware> queryHardWare(String bpId) {
        return merchantInfoDao.queryHardWare(bpId);
    }

    @Override
    public List<MerchantInfo> selectByNameInfoByTermianl() {
        return merchantInfoDao.selectByNameInfoByTermianl();
    }

    @Override
    public SysDict selectSysDictByKey(String key) {
        return merchantInfoDao.selectSysDictByKey(key);
    }

    @Override
    public List<SysDict> selectTwoInfoByParentId(String ParentId) {
        return merchantInfoDao.selectTwoInfoByParentId(ParentId);
    }

    @Override
    public List<MerchantInfo> queryPhoneMerInfo(String phone, String teamId) {
        return merchantInfoDao.queryPhoneMerInfo(phone, teamId);
    }

    @Override
    public List<SysDict> selectOneInfo() {
        return merchantInfoDao.selectOneInfo();
    }

    @Override
    public MerchantInfo selectByNameInfo(String merName) {
        return merchantInfoDao.selectByNameInfo(merName);
    }

    @Override
    public List<MerchantInfo> selectByNameAllInfo(String merName) {
        return merchantInfoDao.selectByNameAllInfo(merName);
    }

    @Override
    public String findBlacklist(String rollNo, String rollType, String rollBelong) {
        return merchantInfoDao.findBlacklist(rollNo, rollType, rollBelong);
    }

    @Override
    public MerchantInfo selectMn(String merchantNo) {
        return merchantInfoDao.selectMn(merchantNo);
    }

    @Override
    public MerchantInfo selectMp(String phone, String teamId) {
        return merchantInfoDao.selectMp(phone, teamId);
    }

    @Override
    public int countMerchantByIdCardAndTeamId(String idCardNo, String teamId) {
        return merchantInfoDao.countMerchantByIdCardAndTeamId(idCardNo, teamId);
    }

    @Override
    public int countMerchantPhone(String mobilePhone, String bpId) {
        String bpIdTeamId = businessProductDefineDao.selectTeamIdByBpId(bpId);
        return merchantInfoDao.countMerchantPhone(mobilePhone, bpIdTeamId);
    }

    @Override
    public List<Map<String, Object>> getMerTeamsByAgentNo(String agentNo) {
        return merchantInfoDao.findMerTeamsByAgentNo(agentNo);
    }

    @Override
    public Map<String, Object> getMerGroupByTeamId(String merTeamId) {
        return merchantInfoDao.findMerGroupByTeamId(merTeamId);
    }


    @Transactional
    @Override
    public void saveAcqInfo(AcqMerchantInfo info, List<String> urlList) {
        Date cur = new Date();
        // 生成进件编号
        UUID randomUUID = UUID.randomUUID();
        String intoNo = randomUUID.toString().substring(0, 8) + new Date().getTime();
        info.setAcqIntoNo(intoNo);
        info.setIntoSource("5");
        info.setCreateTime(cur);
        info.setAuditStatus(1);
        // 获取一级代理商
        String agentNo = info.getAgentNo();
        String oneAgentNo = agentInfoService.getOneAgentNo(agentNo);
        info.setOneAgentNo(oneAgentNo);

        int record = acqMerchantInfoMapper.insertSelective(info);
        if (record != 1) {
            throw new RuntimeException("保存异常");
        }
        AcqMerchantFileInfo fileInfo = new AcqMerchantFileInfo();
        fileInfo.setAcqIntoNo(intoNo);
        fileInfo.setCreateTime(cur);
        for (String url : urlList) {
            fileInfo.setFileUrl(url);
            int indexOf = url.indexOf("_");
            fileInfo.setFileType(url.substring(0, indexOf));
            acqMerchantFileInfoMapper.insertSelective(fileInfo);
        }
    }

    @Override
    public void page(Page<AcqMerchantInfo> page, AcqMerQo qo) {
        acqMerchantInfoMapper.page(page, qo);
    }

    @Override
    public AcqMerchantInfo getAcqMerInfoDetail(Long id) {
        AcqMerchantInfo info = acqMerchantInfoMapper.findAcqMerInfoDetail(id);
        List<AcqMerchantFileInfo> fileList = info.getFileList();
        for (AcqMerchantFileInfo acqMerchantFileInfo : fileList) {
            String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, acqMerchantFileInfo.getFileUrl(), new Date(64063065600000L));
            acqMerchantFileInfo.setFileUrl(url);
        }
        String key = "sys_mcc";
        try {
            // 获取经营范围
            SysDict sysDictOne = sysDictService.findSysDictByKeyValue(key, info.getOneScope());
            SysDict sysDictTwo = sysDictService.findSysDictByKeyValue(key, info.getTwoScope());
            info.setScope(sysDictOne.getSysName() + " - " + sysDictTwo.getSysName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取经营范围异常");
        }
        // 获取审核记录
        List<AcqMerchantInfoLog> auditList = acqMerchantInfoLogMapper.findAuditListByIntoId(info.getId());
        info.setAuditList(auditList);
        return info;
    }

    @Transactional
    @Override
    public void updateAcqInfo(AcqMerchantInfo info, List<String> urlList) {
        // 编辑完进入待审核状态
        info.setAuditStatus(1);
        info.setUpdateTime(new Date());
        // 更新进件信息
        int record = acqMerchantInfoMapper.updateByPrimaryKeySelective(info);
        if (record != 1) {
            throw new RuntimeException("修改异常");
        }
        // 更新资质文件信息
        for (String url : urlList) {
            acqMerchantFileInfoMapper.updateUrl(info.getAcqIntoNo(), url.substring(0, url.indexOf("_")), url);
        }


    }

    @Override
    public Map<String, Object> getUserByMerNo(String merchantNo){
        return merchantInfoDao.getUserByMerNo(merchantNo);
    }

    @Override
    public List<Map<String, Object>> getBpHpByMerNo(String merchantNo){
        return merchantInfoDao.getBpHpByMerNo(merchantNo);
    }

    @Override
    public String selectRecommendedSource(String merchantNo) {
        return merchantInfoDao.selectRecommendedSource(merchantNo);
    }

    @Override
    public  List<Map<String, String>> batchSelectRecommendedSource(List<String> merchantNos) {
        return merchantInfoDao.batchSelectRecommendedSource(merchantNos);
    }
	@Override
	public int getByMerchantName(String merchantName) {
		return merchantInfoDao.getByMerchantName(merchantName);
	}

    @Override
    public void merToCjtMer(String sn, String merchantNo) {
        try {
                log.info("超级推进件调用core");
                String url = sysDictService.getStringValueByKey("CORE_URL") + Constants.CJT_REGIST;
                String signKey = sysConfigDao.getStringValueByKey("CORE_KEY");

                Map<String, String> requestMap = new HashMap<>();
                requestMap.put("merchantNo", merchantNo);
                requestMap.put("sn", sn);
                String signData = CommonUtil.sortASCIISign(requestMap, signKey);
                requestMap.put("signData", signData);

                String str = HttpUtils.sendPostRequest(url, requestMap);
                log.info("超级推进件处理结果：" + str);
            } catch (Exception e) {
                log.error("超级推机具处理异常",e);
            }
    }

    @Override
    public String selectParentNodeByAgentNo(String agentNo) {
        return merchantInfoDao.selectParentNodeByAgentNo(agentNo);
    }
}
