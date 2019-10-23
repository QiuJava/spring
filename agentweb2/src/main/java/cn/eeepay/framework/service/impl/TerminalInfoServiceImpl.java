package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.daoPerAgent.PerAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.peragent.PaChangeLog;
import cn.eeepay.framework.model.peragent.PaTerInfo;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("TerminalInfoService")
public class TerminalInfoServiceImpl implements TerminalInfoService {
	private static final Logger log = LoggerFactory.getLogger(TerminalInfoServiceImpl.class);

    @Resource
    public TerminalInfoDao terminalInfoDao;
    @Resource
    public TerminalInfoReadDao terminalInfoReadDao;
    @Resource
    private PaTerInfoDao paTerInfoDao;
    @Resource
    private PerAgentDao perAgentDao;
    @Resource
    public AgentInfoService agentInfoService;
    @Resource
    public PerMerService perMerService;
    @Resource
    private PerAgentService perAgentService;
    @Resource
    private ActivityDetailDao activityDetailDao;
    @Resource
    private PaChangeLogService paChangeLogService;
    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private HardwareProductDao hardwareProductDao;

    @Resource
    private MerchantBusinessProductDao merchantBusinessProductDao;

    @Resource
    private MerchantInfoDao merchantInfoDao;

    @Resource
    private ZQMerchantDao zQMerchantDao;
    @Resource
    private  AgentTerminalOperateService agentTerminalOperateService;

    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<TerActivityCheck> selectTerActivityCheck(Page<TerActivityCheck> page,TerActivityCheck terActivityCheck) {
        AgentInfo entityInfo = setParams(terActivityCheck);
        List<TerActivityCheck> list = terminalInfoDao.selectTerActivityCheck(page, terActivityCheck);
        if (list.size() > 0){
            List<HardwareProduct> hardwareProductList = hardwareProductDao.selectAllInfo(entityInfo.getAgentNo(), entityInfo.getAgentOem());
            for (TerActivityCheck activityCheck : list) {
                String agentNo = activityCheck.getAgentNo();//机具所属代理商编号
                AgentInfo agentInfo = agentInfoService.selectByagentNo(agentNo);
                activityCheck.setAgentName(agentInfo.getAgentName());
                if (agentInfo.getAgentLevel() != 1){
                    String agentNode = agentInfo.getAgentNode();
                    String agentNodeTwo = agentNode.substring(0, StringUtils.ordinalIndexOf(agentNode,"-",3) + 1);
                    AgentInfo agentInfoLevel2 = agentInfoDao.selectByAgentNode(agentNodeTwo);//二级代理商
                    activityCheck.setAgentNoTwo(agentInfoLevel2.getAgentNo());
                    activityCheck.setAgentNameTwo(agentInfoLevel2.getAgentName());
                }
                activityCheck.setBpName(terminalInfoDao.selectBpName(activityCheck.getBpId()));
                activityCheck.setActivityTypeNo(terminalInfoDao.selectActivityName(activityCheck.getActivityTypeNo()));
                // 已达标 1：考核天数为正数且激活状态为已激活(成为已达标状态后达标状态不会再变更) 激活天数显示为已达标
                if (StringUtils.isNotBlank(activityCheck.getDueDays())){
                    Integer dueDays = Integer.valueOf(activityCheck.getDueDays());
                    String status = activityCheck.getStatus();
                    if (dueDays > 0 && "1".equals(status)){
                        activityCheck.setDueDays("已达标");
                    }
                }
                String type = activityCheck.getType();
                for (HardwareProduct hardwareProduct : hardwareProductList) {
                    String hpId = hardwareProduct.getHpId().toString();
                    String typeName = hardwareProduct.getTypeName();
                    if (type.equals(hpId)) {
                        activityCheck.setType(typeName + hardwareProduct.getVersionNu());
                        break;
                    }
                }
            }
        }
        return list;
    }

    private AgentInfo setParams(TerActivityCheck terActivityCheck) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo entityInfo = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
        terActivityCheck.setEntityNode(entityInfo.getAgentNode());
//        String agentNoTwo = terActivityCheck.getAgentNoTwo();
        if ("1".equals(terActivityCheck.getHasChild())){
//            if (StringUtils.isNotBlank(agentNoTwo)){
//                terActivityCheck.setAgentNodeTwo(agentInfoService.selectByagentNo(agentNoTwo).getAgentNode());
//            }
            String agentNoBelong = terActivityCheck.getAgentNo();
            if (StringUtils.isNotBlank(agentNoBelong)){
                terActivityCheck.setAgentNode(agentInfoService.selectByagentNo(agentNoBelong).getAgentNode());
            }
        }
        return entityInfo;
    }

    @Override
    public void exportTerActivityCheck(HttpServletResponse response, TerActivityCheck terActivityCheck) {
        log.info("========进入活动考核机具导出===========");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        OutputStream ouputStream = null;
        try{
            AgentInfo entityInfo = setParams(terActivityCheck);
            List<TerActivityCheck> list = terminalInfoDao.exportTerActivityCheck(terActivityCheck);
            List<HardwareProduct> hardwareProductList = hardwareProductDao.selectAllInfo(entityInfo.getAgentNo(), entityInfo.getAgentOem());
            ExcelExport export = new ExcelExport(2);
            String fileName = "活动考核机具"+sdf.format(new Date())+".xlsx";
            String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<>() ;
            Map<String,String> map = null;
            for (TerActivityCheck info : list) {
                map = new HashMap<>();
                String agentNo = info.getAgentNo();//机具所属代理商编号
                AgentInfo agentInfo = agentInfoService.selectByagentNo(agentNo);
                map.put("agentName",agentInfo.getAgentName());
                if (agentInfo.getAgentLevel() != 1){
                    String agentNode = agentInfo.getAgentNode();
                    String agentNodeTwo = agentNode.substring(0, StringUtils.ordinalIndexOf(agentNode,"-",3) + 1);
                    AgentInfo agentInfoLevel2 = agentInfoDao.selectByAgentNode(agentNodeTwo);//二级代理商
                    map.put("agentNoTwo",agentInfoLevel2.getAgentNo());
                    map.put("agentNameTwo",agentInfoLevel2.getAgentName());
                }
                map.put("sn",info.getSn());
                String type = info.getType();
                for (HardwareProduct hardwareProduct : hardwareProductList) {
                    String hpId = hardwareProduct.getHpId().toString();
                    String typeName = hardwareProduct.getTypeName();
                    if (type.equals(hpId)) {
                        map.put("type",typeName + hardwareProduct.getVersionNu());
                        break;
                    }
                }
                map.put("bpName",terminalInfoDao.selectBpName(info.getBpId()));
                map.put("agentNo",info.getAgentNo());
                map.put("checkTime",info.getCheckTime() == null ? "" : sdf1.format(info.getCheckTime()));
                map.put("dueDays",info.getDueDays() == null ? "" : info.getDueDays());
                // 已达标 1：考核天数为正数且激活状态为已激活(成为已达标状态后达标状态不会再变更) 激活天数显示为已达标
                if (StringUtils.isNotBlank(info.getDueDays())){
                    Integer dueDays = Integer.valueOf(info.getDueDays());
                    String status = info.getStatus();
                    if (dueDays > 0 && "1".equals(status)){
                        map.put("dueDays","已达标");
                    }
                }
                map.put("status","1".equals(info.getStatus()) ? "已激活" : "未激活");
                switch(info.getOpenStatus()){
                    case "1":
                        map.put("openStatus","已分配");
                        break;
                    case "2":
                        map.put("openStatus","已使用");
                        break;
                    default:
                        break;
                }
                map.put("activityTypeNo",terminalInfoDao.selectActivityName(info.getActivityTypeNo()));
                data.add(map);
            }
            String[] cols = new String[]{"sn","type","bpName","agentNoTwo","agentNameTwo","agentNo", "agentName",
                    "checkTime","dueDays","status","openStatus","activityTypeNo"};
            String[] colsName = new String[]{
                    "SN号","硬件产品种类","业务产品","二级代理商编号","二级代理商名称","所属代理商编号","所属代理商名称",
                    "考核日期", "考核剩余天数","机具激活状态","机具状态","欢乐返子类型"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch(Exception e){
            log.error("活动考核机具列表导出异常", e);
        }finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int insert(TerminalInfo record) {
        return terminalInfoDao.insert(record);
    }

    @Override
    public int insertSelective(List<TerminalInfo> record) {
        int i = 0;
        for (TerminalInfo terminalInfo : record) {
            if (selectBySameData(terminalInfo) == null)
                i += terminalInfoDao.insertSelective(terminalInfo);
            else
                continue;
        }
        return i;
    }

    @Override
    public int updateByPrimaryKey(TerminalInfo record) {
        return terminalInfoDao.updateByPrimaryKey(record);
    }

    @Override
    public List<TerminalInfo> selectAllInfo(Page<TerminalInfo> page) {
        return terminalInfoDao.selectAllInfo(page);
    }

    @Override
    public TerminalInfo selectObjInfo(Long id,String agentNo) {
        return terminalInfoDao.selectObjInfo(id,agentNo);
    }

    @Override
    public List<TerminalInfo> selectByParam(Page<TerminalInfo> page, TerminalInfo terminalInfo) {
        List<TerminalInfo> list = terminalInfoReadDao.selectByParam(page, terminalInfo);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String entityId = principal.getUserEntityInfo().getEntityId();
        AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
        List<HardwareProduct> hardwareProductList = hardwareProductDao.selectAllInfo(entityId, entityInfo.getAgentOem());
        //人人代理,一级且下发机具对应user_code等于当前登录代理商的user_code才有下发机具功能
        //查询登录代理商对应的人人代理用户信息
        PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(entityId);
        if ("11".equals(entityInfo.getAgentType())) {

            for (TerminalInfo info : list) {
                if (info == null) {
                    continue;
                }
                Integer status = info.getStatus();
                info.setStatus(0);
                if (info.getSendLock() != null && info.getSendLock() == 1) {//锁定状态
                    continue;
                }
                //String userCode = terminalInfoDao.selectFromPaTerInfoBySn(info.getSn());
                String userCode = info.getUserCode();
                PaUserInfo mInfo = perAgentDao.selectByUserCode(userCode);

                if ("1".equals(paUserInfo.getUserType())) {
                   if ("1".equals(status + "")) {
                        if (paUserInfo.getUserCode().equals(userCode)) {
                            info.setStatus(1);//设置可下发
                        }
                    } else if ("2".equals(status + "")) {
                        if (mInfo.getParentId().startsWith(paUserInfo.getUserCode())) {
                            info.setStatus(2);//设置可回收
                        }
                    }
               } else if ("2".equals(paUserInfo.getUserType())) {
                    if ("2".equals(status + "")||"1".equals(status + "")) {
                        if (paUserInfo.getUserCode().equals(userCode)) {
                            info.setStatus(1);//设置可下发
                        }
                    }
                }

            }

           /* if (entityInfo.getAgentLevel() != 1) {
                for (TerminalInfo info : list) {
                    //info.setStatus(1);
                }
            } else {
                for (TerminalInfo info : list) {
                    String userCode = terminalInfoDao.selectFromPaTerInfoBySn(info.getSn());
                    if (!entityMap.get("user_code").equals(userCode)) {
                        info.setStatus(1);
                    }
                }
            }*/
        }
        for (TerminalInfo info : list) {
        	info.setBelongAgent(true);
        	if (entityInfo.getAgentLevel() == 2 && !entityId.equals(info.getAgentNo())) {//用来判断机具是否属于当前登录代理商所有
        		info.setBelongAgent(false);
        	}
            if (info.getUserCode() != null) {
                Map<String, Object> map = perMerService.selectRealNameByUserCode(info.getUserCode());
                if (map != null) {
                    //info.setUserCode(paUserMap.get("user_code"));
                    info.setRealName((String) map.get("real_name"));
                }
            }
            String type = info.getType();
            for (HardwareProduct hardwareProduct : hardwareProductList) {
            	String hpId = hardwareProduct.getHpId().toString();
            	String typeName = hardwareProduct.getTypeName();
            	if (type.equals(hpId)) {
					info.setType(typeName + hardwareProduct.getVersionNu());
					break;
				}
			}

            //MerchantInfo merchantInfo = merchantInfoDao.selectByMerchantNo(info.getMerchantNo());

            //查询超级推机具
            String id = hardwareProductDao.selectSuperPushHardwareByHpId(type);
            info.setSuperPush(StringUtils.isNotBlank(id)?"1":"0");

        }

        return list;
    }

    @Override
    public List<TerminalInfo> selectByAddParam(TerminalInfo terminalInfo) {
        return terminalInfoDao.selectByAddParam(terminalInfo);
    }

    @Override
    public List<TerminalInfo> selectUserCodeLists(String agentNode) {
        List<TerminalInfo> list = terminalInfoDao.selectUserCodeLists(agentNode);
        if (list != null) {
            for (TerminalInfo info : list) {
                if (info != null && info.getUserCode() != null) {
                    Map<String, Object> map = perMerService.selectRealNameByUserCode(info.getUserCode());
                    if (map != null) {
                        //info.setUserCode(paUserMap.get("user_code"));
                        info.setRealName((String) map.get("real_name"));
                    }
                }

            }
        }

        return list;
    }

    @Override
    public int updateSolutionById(TerminalInfo record) {
        return terminalInfoDao.solutionById(record);
    }

    @Override
    public int recoverById(PaUserInfo paUserInfo, String id) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        PaTerInfo changePre = paTerInfoDao.selectById(id);
        int i = terminalInfoDao.recoverById(paUserInfo, id);
        if(changePre != null){
            PaTerInfo changeAfter = paTerInfoDao.selectById(id);
            PaChangeLog log = new PaChangeLog(JSONObject.toJSONString(changePre), JSONObject.toJSONString(changeAfter),
                    "机具回收", "代理商系统，代理商编号：" + loginAgent.getAgentNo(),"recoverById");
            paChangeLogService.insert(log);
        }
        return i;
    }

   /* @Override
    public int updateUnbundlingById(TerminalInfo record) {
        return terminalInfoDao.unbundlingById(record);
    }*/

    /*@Override
    public int updateBundlingById(TerminalInfo record) {
        return terminalInfoDao.bundlingById(record);
    }*/

    @Override
    public TerminalInfo selectBySameData(TerminalInfo record) {
        return terminalInfoDao.selectBySameData(record);
    }

    @Override
    public TerminalInfo checkSn(String agentNo, String sn, String status) {
        return terminalInfoDao.checkSn(agentNo, sn, status);
    }

    @Override
    public TerminalInfo querySn(String sn) {
        return terminalInfoDao.querySn(sn);
    }

    @Override
    public TerminalInfo checkAgentSn(String agentNo, String sn) {
        return terminalInfoDao.checkAgentSn(agentNo, sn);
    }

    @Override
    public int updateBundlingItem(TerminalInfo record) {
        return terminalInfoDao.bundlingItem(record);
    }

    @Override
    public List<TerminalInfo> selectAllInfoBymerNoAndBpId(String merNo, String bpId) {
        return terminalInfoDao.selectAllInfoBymerNoAndBpId(merNo, bpId);
    }

    @Override
    public List<Map<String, String>> selectAllActivityType() {
        return terminalInfoDao.selectAllActivityType();
    }

    @Override
    public Map<String, Object> UpdateTerminalInfoBySn(String snStart1, String snEnd1, String agentNo, String userCode) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        AgentInfo ais = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String agentId = principal.getUserEntityInfo().getEntityId();
        AgentInfo entityInfo = agentInfoService.selectByagentNo(agentId);
        if ("11".equals(entityInfo.getAgentType())) {
            ais = new AgentInfo();
            //查询登录代理商对应的人人代理用户信息
            PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(agentId);
            PaUserInfo info = perAgentDao.selectByUserCode(userCode);
            if (info != null) {
                if ("1".equals(paUserInfo.getUserType())) {
                    if (info.getUserNode().startsWith(paUserInfo.getUserNode())) {
                        ais.setUserCode(info.getUserCode());
                        ais.setAgentNo(info.getAgentNo());
                        ais.setAgentNode(info.getAgentNode());
                        entityInfo.setUserCode(paUserInfo.getUserCode());
                        entityInfo.setUserType(paUserInfo.getUserType());
                    } else {
                        throw new RuntimeException("下发失败:用户非直营大盟主或者盟主");
                    }
                } else if ("2".equals(paUserInfo.getUserType())) {
                    if (info.getParentId().equals(paUserInfo.getUserCode())) {
                        ais.setUserCode(info.getUserCode());
                        ais.setAgentNo(info.getAgentNo());
                        ais.setAgentNode(info.getAgentNode());
                        entityInfo.setUserCode(paUserInfo.getUserCode());
                        entityInfo.setUserType(paUserInfo.getUserType());
                    } else {
                        throw new RuntimeException("下发失败:用户非直营大盟主或者盟主");
                    }
                } else {
                    throw new RuntimeException("下发失败:登录用户非机构或者大盟主");
                }

            }

            //ais.setUserCode(perAgentDao.selectByAgentNo(agentNo).get("user_code"));
            //entityInfo.setUserCode(perAgentDao.selectByAgentNo(agentId).get("user_code"));

        } else {
            ais = agentInfoService.selectByagentNo(agentNo);
        }
        List<TerminalInfo> terminalInfo1 = terminalInfoDao.selectTerminalInfoBySn(snStart1, snEnd1);
        int num = 0;
        int count = 0;
        for (TerminalInfo terminalInfo : terminalInfo1) {
            if (!"1".equals(terminalInfo.getOpenStatus()) || !agentId.equals(terminalInfo.getAgentNo())) {
                throw new RuntimeException("下发失败:存在不可下发机具");
            }
            String id = hardwareProductDao.selectSuperPushHardwareByHpId(terminalInfo.getType());
            if(StringUtils.isNotBlank(id)){
                log.info("下发失败:超级推机具不能下发，机具号：" + terminalInfo.getTerminalId());
                throw new RuntimeException("下发失败:超级推机具不能下发，机具号：" + terminalInfo.getTerminalId());
            }
            terminalInfo.setStartTime(new Date());
            terminalInfo.setOpenStatus("1");
            terminalInfo.setAgentNo(ais.getAgentNo());
            terminalInfo.setAgentNode(ais.getAgentNode());
            terminalInfo.setDownDate(new Date());
            terminalInfo.setReceiptDate(new Date());
            num += this.updateSolutionById(terminalInfo);

            addAgentActivity(terminalInfo.getSn(), agentNo);

            if (num>0){
                Date date = new Date();
                //记录机具下发日期
                agentTerminalOperateService.insertAgentTerminalOperation(entityInfo.getAgentNo(),terminalInfo.getSn(),"1","2",date);
                //记录机具接收日期
                agentTerminalOperateService.insertAgentTerminalOperation(agentNo,terminalInfo.getSn(),"1","1",date);
            }

            if ("11".equals(entityInfo.getAgentType())) {
            	String sn = terminalInfo.getSn();
            	Map<String, Object> paTerInfoMap = terminalInfoDao.selectPaTerInfo(sn);
            	if (paTerInfoMap != null && "1".equals(paTerInfoMap.get("callback_lock").toString())) {
                    log.info("锁定状态机具 sn ==> " + sn + "=======不可以再下发==");
                    throw new AgentWebException("机具已被锁定,不可以再下发");
				}
                /*if ("1".equals(entityInfo.getUserType())) {
                    count += terminalInfoDao.UpdateTerminalInfoBySn(ais.getAgentNo(), ais.getUserCode(), sn, entityInfo.getUserCode());
                } else if ("2".equals(entityInfo.getUserType())) {
                    count += terminalInfoDao.UpdateTerminalInfoBySn(ais.getAgentNo(), ais.getUserCode(), sn, entityInfo.getUserCode());
                }*/
                PaTerInfo changePre = paTerInfoDao.selectBySnWithUserCodeWithCallbackLock(sn,entityInfo.getUserCode(),"0");
                count += terminalInfoDao.UpdateTerminalInfoBySn(ais.getAgentNo(), ais.getUserCode(), sn, entityInfo.getUserCode());
                if(changePre != null){
                    PaTerInfo changeAfter = paTerInfoDao.selectBySnWithUserCodeWithCallbackLock(sn,ais.getUserCode(),"0");
                    PaChangeLog paChangeLog = new PaChangeLog(JSONObject.toJSONString(changePre), JSONObject.toJSONString(changeAfter),
                            "机具批量下发",
                            "代理商系统，代理商编号：" + agentInfoService.selectByPrincipal().getAgentNo(),"UpdateTerminalInfoBySn");
                    paChangeLogService.insert(paChangeLog);
                }
            }
        }
        if ("11".equals(entityInfo.getAgentType()) && count != num) {
            throw new AgentWebException("下发失败");
        }
        jsonMap.put("count", count);
        jsonMap.put("num", num);
        return jsonMap;
    }


    @Override
    public int updateTer(String agentNo, String userCode, String sn, String entityUserCode) {
        return terminalInfoDao.UpdateTerminalInfoBySn(agentNo, userCode, sn, entityUserCode);
    }

    /*@Override
    public int updateTer2(String agentNo, String userCode, String sn, String entityUserCode) {
        return terminalInfoDao.UpdateTerminalInfoBySn2(agentNo, userCode, sn, entityUserCode);
    }*/

    @Override
    public String selectAgentNode(String sn) {
        return terminalInfoDao.selectAgentNode(sn);
    }

    @Override
    public Map<String, Object> selectPaTerInfo(String sn) {
        return terminalInfoDao.selectPaTerInfo(sn);
    }


    @Override
    public List<TerminalInfo> selectByUserCode(Page<PaOrder> page, String userCode, TerminalInfo info) {
        return terminalInfoDao.selectByUserCode(page, userCode, info);
    }


    public List<TerminalInfo> selectByOrderNo(Page<TerminalInfo> page, String orderNo, String agentNode) {
        return terminalInfoDao.selectByOrderNo(page, orderNo, agentNode);
    }

    @Override
    public List<TerminalInfo> queryTerminalByOrder(String userCode, String snStart, String snEnd) {
        return terminalInfoDao.queryTerminalByOrder(userCode, snStart, snEnd);
    }

	@Override
	public Map<String, Object> untiedById(Long id) {
		Map<String, Object> map = new HashMap<>();
		/**
		 *  1、操做项新增解绑操作，只有机具状态为“已使用”才显示该操作，否则不显示
			2、点击解绑时，判断该商户是否已只绑定了一台机具，如果是的话则判断商户是否参加过活动，如已经参加过则提示：该商户已经参加过活动，机具暂时不可解绑
			3、如只绑定了一台机具，且还未参与过活动则显示如下弹层，解绑成功后提示：解绑成功
			4、如绑定多台机具则参加过活动的机具不允许解绑，未参加过活动的机具可以解绑，如已经参加过则提示：该商户已经参加过活动，机具暂时不可解绑
			不用区分绑定一台和多台,逻辑一样
		 */
		TerminalInfo terminalInfo = terminalInfoDao.selectById(id);
		String merchantNo = terminalInfo.getMerchantNo();
		//商户是否绑定机具,用商户号在机具表查;
		List<TerminalInfo> list = terminalInfoDao.selectByMerchantNo(merchantNo);
		//商户是否参加过活动,用商户号在活动表查(是否有数据);
		String sn = terminalInfo.getSn();
		TerminalInfo info = terminalInfoDao.querySn(sn);
		String typeName = terminalInfoDao.selectHardNameByType(info.getType());
		if (typeName.contains("收款码") || typeName.contains("激活码")) {
			map.put("status", false);
			map.put("msg", "收款码不可解绑");
			return map;
		}
		if (list != null) {
		    if (activityDetailDao.selectCountBySnMerchantNo(sn,merchantNo) > 0){
                map.put("status", false);
                map.put("msg", "机具已参加欢乐返且已激活,不能解绑");
                return map;
            }
			if (activityDetailDao.selectCountBySn(sn,merchantNo) > 0) {
				map.put("status", false);
				map.put("msg", "该商户已经参加过活动，机具暂时不可解绑");
				return map;
			}else{
				//弹框,调接口解绑,解绑成功
		        String bpId = terminalInfo.getBpId();
		        String type = "1";//解绑
		        String coreUrl = "";
		        SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
		        if(coreUrlDict != null){
		            coreUrl = coreUrlDict.getSysValue() + Constants.BIND_OR_UNBIND;
		        }

		        String acqEnname = "YS_ZQ";
		        MerchantBusinessProduct mbp = merchantBusinessProductDao.selectMerBusPro(merchantNo, bpId);
		        ZQMerchantInfo zqMerchantInfo = null;
		        String mbpId = null;
		        if (mbp != null) {
		        	mbpId = String.valueOf(mbp.getId());
		        	zqMerchantInfo = getZqMerchant(merchantNo, mbpId, acqEnname);
		        	if(zqMerchantInfo != null && "3".equals(zqMerchantInfo.getSyncStatus())){
		        		map.put("msg", "该商户号" + merchantNo + "处于审核中状态，不能解绑");
		        		return map;
		        	}
		        }
		        if(zqMerchantInfo != null && "1".equals(zqMerchantInfo.getSyncStatus())){
		        	//检查商户在科目224101001的余额，如果余额大于0，不能解绑，解绑提示‘商户账户余额大于0，不能解绑’；
					String subjectNo = "224101001";
					String balanceMsg = ClientInterface.getMerchantAccountBalance(merchantNo, subjectNo);
					if(StringUtils.isBlank(balanceMsg)){
						map.put("msg", "sn:" + sn + ",merNo:" + merchantNo + ",调用账户接口查询余额失败");
						return map;
					}
					JSONObject balanceJson = JSONObject.parseObject(balanceMsg);
					if(balanceJson.getBoolean("status")){
						AccountInfo accountInfo = JSONObject.parseObject(balanceMsg,AccountInfo.class);
						if(accountInfo != null && accountInfo.getBalance().compareTo(BigDecimal.ZERO) > 0){
							map.put("msg", "sn:" + sn + ",merNo:" + merchantNo + ",商户账户余额大于0，不能解绑");
							return map;
						}
					} else{
						map.put("msg", "sn:" + sn + ",merNo:" + merchantNo + ",调用账户接口查询余额失败");
						return map;
					}
					String returnStr = ClientInterface.bindingOrUnBindTerminal(coreUrl, sn, merchantNo, mbpId, type);
					if(StringUtils.isNotBlank(returnStr)){
						JSONObject json = JSONObject.parseObject(returnStr);
						JSONObject headJson = JSONObject.parseObject(json.getString("header"));
						if(!headJson.getBoolean("succeed")){
							map.put("status", false);
							map.put("msg", "sn:" + sn + ",调用接口解绑失败," + headJson.getString("errMsg"));
							return map;
						}
					}
		        }
		        Integer count = terminalInfoDao.updateUnbundlingById(id);
		        if (count > 0) {
		        	map.put("status", true);
		        	map.put("msg", "解绑成功");
		        	return map;
				}
			}
		}
		map.put("status", false);
    	map.put("msg", "没有绑定机具");
		return map;
	}

	/**
     * 获取直清商户
     * @param merNo
     * @param mbpId
     * @param acqEnname
     * @return
     */
    private ZQMerchantInfo getZqMerchant(String merNo, String mbpId, String acqEnname) {
    	ZQMerchantInfo zqMerchantInfo = zQMerchantDao.selectByMerMbpAcq(merNo, mbpId, acqEnname);
        return zqMerchantInfo;
    }

	@Override
	public Map<String, Object> updateBindingTerminal(String merchantNo,String sn,String id,String bpId) {
		Map<String,Object> map = new HashMap<>();
		TerminalInfo terminalInfo = terminalInfoDao.selectById(Long.valueOf(id));
		String coreUrl = "";
		SysDict coreUrlDict = sysDictDao.getByKey("CORE_URL");
		if(coreUrlDict != null){
			coreUrl = coreUrlDict.getSysValue() + Constants.BIND_OR_UNBIND;
		}
		String type = "0";//绑定
		/**
		 * 一个商户总共不能绑定超过3台机具，点击确定后校验该商户已经绑定了多少台机具，如加上增绑的机具超过3台则提示：绑定失败，该商户绑定机具数量已经超限。
			如增绑的机具与商户的的直系所属代理不一致则提示：增绑机具失败，新机具的直系所属代理商与商户不一致
			增绑的机具绑定的活动可以和原注册时绑定的机具活动不一致，但商户只能参加一次活动，以注册时绑定的机具活动为准，新增绑的机具不参与活动

			boss
			1）机具必须在acq_terminal_store有记录，才可以绑定，否则，给与提示并返回（status的判断与维护，由core那边处理）；
		 	* 2）商户业务产品必须在zq_merchant_info.sync_status为“1同步成功”，report_status为“1已报备”，才可以调用绑定接口，
		 	* 若接口返回成功，则进行内部绑定，否则，给与提示并返回；
		 */
		List<TerminalInfo> list = terminalInfoDao.selectByMerchantNo(merchantNo);
		MerchantInfo merchantInfo = merchantInfoDao.selectMn(merchantNo);
		List<ActivityDetail> activityList = activityDetailDao.selectByMerchantNo(merchantNo);
		TerminalInfo info = terminalInfoDao.querySn(sn);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String entityId = principal.getUserEntityInfo().getEntityId();
        AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
		if ("11".equals(entityInfo.getAgentType())) {
			//判断机具是否被锁定
        	Map<String, Object> paTerInfoMap = terminalInfoDao.selectPaTerInfo(sn);
        	if (paTerInfoMap != null && "1".equals(paTerInfoMap.get("callback_lock").toString())) {
        		map.put("result", false);
        		map.put("message", "机具已被锁定,不可以再下发");
                log.info("锁定状态机具 sn ==> " + sn + "=======不可以再下发==");
                return map;
			}
		}
		if (list.size() >= 3 && info != null && sn.equals(info.getPsamNo())) {
			map.put("status", false);
        	map.put("msg", "绑定失败，该商户绑定机具数量已经超限");
        	return map;
		}
		if (!terminalInfo.getAgentNo().equals(merchantInfo.getAgentNo())) {
			map.put("status", false);
        	map.put("msg", "增绑机具失败，新机具的直系所属代理商与商户不一致");
        	return map;
		}
		if (activityList.size() > 1) {
			map.put("status", false);
        	map.put("msg", "商户只能参加一次活动");
        	return map;
		}
		String acqEnname = "YS_ZQ";
		//检查商户是否在银盛报备
		MerchantBusinessProduct mbp = merchantBusinessProductDao.selectMerBusPro(merchantNo, bpId);
		ZQMerchantInfo zqMerchantInfo = null;
		String mbpId = null;
		if (mbp != null) {
			mbpId = String.valueOf(mbp.getId());
			zqMerchantInfo = checkSyncMer(merchantNo, mbpId, acqEnname);
			if(zqMerchantInfo != null && "3".equals(zqMerchantInfo.getSyncStatus())){
				map.put("status", false);
				map.put("msg", "商户同步状态为审核中，不能绑定");
				return map;
			}
		}
        if (!merchantInfo.getAgentNo().equals(terminalInfo.getAgentNo())) {
            map.put("status", false);
        	map.put("msg", "商户" + merchantNo + "和机具" + sn + "不属于同一个代理商，不能绑定;");
			log.info("商户{}和机具{}不属于同一个代理商，不能绑定",merchantNo, sn);
			return map;
		}
        boolean syncTerStatus = checkExistsAcqTer(sn, acqEnname);
        terminalInfo.setMerchantNo(merchantNo);
        terminalInfo.setBpId(bpId);
		terminalInfo.setOpenStatus("2");
		if (zqMerchantInfo != null && "1".equals(zqMerchantInfo.getSyncStatus())){
            if(!syncTerStatus){
                map.put("status", false);
            	map.put("msg", "机具" + sn + "在" + acqEnname +"找不到记录，不能绑定");
                log.info("机具{}在{}找不到记录，不能绑定",sn, acqEnname);
                return map;
            } else {
            	String returnStr = ClientInterface.bindingOrUnBindTerminal(coreUrl, sn, merchantNo, mbpId, type);
            	if(StringUtils.isNotBlank(returnStr)){
            		JSONObject json = JSONObject.parseObject(returnStr);
            		JSONObject headJson = JSONObject.parseObject(json.getString("header"));
            		if(!headJson.getBoolean("succeed")){
            			map.put("status", false);
            			map.put("msg","sn:" + sn + ",调用接口绑定失败," + headJson.getString("errMsg") +";");
            			return map;
            		}
            	} else {
            		map.put("status", false);
            		map.put("msg", "sn:" + sn + ",调用接口失败;");
            		return map;
            	}
            }
		}
		Integer count = terminalInfoDao.updateBundlingBySn(terminalInfo);
		if (count < 1) {
			map.put("status", false);
			map.put("msg", "绑定失败");
			return map;
		}
		map.put("status", true);
		map.put("msg", "绑定成功");
		return map;
	}

	/**
	 * 检查商户是否同步成功
	 * @param merNo
	 * @param mbpId
	 * @param acqEnname
	 * @return
	 */
	private ZQMerchantInfo checkSyncMer(String merNo, String mbpId, String acqEnname) {
        ZQMerchantInfo zqMerchantInfo = zQMerchantDao.selectByMerMbpAcq(merNo, mbpId, acqEnname);
		return zqMerchantInfo;
	}
	/**
	 * 检查机具是否报备
	 * @param sn
	 * @param acqEnname
	 * @return
	 */
	private boolean checkExistsAcqTer(String sn, String acqEnname) {
		Map<String,Object> acqTer = terminalInfoDao.selectBySnAcq(sn, acqEnname);
		if(acqTer != null){
			return true;
        }
		return false;
	}
	@Override
	public List<Map<String, String>> selectAllMerchantInfo() {
		List<Map<String,String>> list = new ArrayList<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
        AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
        if (entityInfo.getAgentLevel() == 1) {
        	list = terminalInfoDao.selectAllMerchantInfo1(entityInfo.getAgentNode());
		}
        if (entityInfo.getAgentLevel() == 2) {
        	list = terminalInfoDao.selectAllMerchantInfo2(entityId);
		}
		return list;
	}

    @Override
    public String selectMerchantNameByMerchantNo(String merchantNo) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        String merchantName = null;
        if(loginAgent.getAgentLevel() == 1){
            merchantName = terminalInfoDao.selectMerchantNameByMerchantNo1(merchantNo,loginAgent.getAgentNode());
        }
        if(loginAgent.getAgentLevel() == 2){
            merchantName = terminalInfoDao.selectMerchantNameByMerchantNo2(merchantNo, loginAgent.getAgentNo());
        }
        return merchantName;
    }


	@Override
	public List<TerminalInfo> selectByParamThree(Page<TerminalInfo> page, TerminalInfo terminalInfo, String selectAgentNo,
			String currAgentNo) {

		// 查询出授权的代理商节点
	/*	List<AgentInfo> list = agentInfoService.getConfigInfo(currAgentNo);
		if (list!=null && list.size()>0) {*/

			List<TerminalInfo> listInfo = terminalInfoDao.selectByParamThree(page, terminalInfo ,selectAgentNo);
			 AgentInfo entityInfo = agentInfoService.selectByagentNo(currAgentNo);
	        PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(currAgentNo);
	        if ("11".equals(entityInfo.getAgentType())) {

	            for (TerminalInfo info : listInfo) {
	                if (info == null) {
	                    continue;
	                }
	                Integer status = info.getStatus();
	                info.setStatus(0);
	                if (info.getSendLock() != null && info.getSendLock() == 1) {//锁定状态
	                    continue;
	                }
	                String userCode = info.getUserCode();
	                PaUserInfo mInfo = perAgentDao.selectByUserCode(userCode);
	                if ("1".equals(paUserInfo.getUserType())) {
	                    if ("1".equals(status + "")) {
	                        if (paUserInfo.getUserCode().equals(userCode)) {
	                            info.setStatus(1);//设置可下发
	                        }
	                    } else if ("2".equals(status + "")) {
	                        if (mInfo.getParentId().startsWith(paUserInfo.getUserCode())) {
	                            info.setStatus(2);//设置可回收
	                        }
	                    }
	                } else if ("2".equals(paUserInfo.getUserType())) {
	                    if ("2".equals(status + "")||"1".equals(status + "")) {
	                        if (paUserInfo.getUserCode().equals(userCode)) {
	                            info.setStatus(1);//设置可下发
	                        }
	                    }
	                }


	            }
	        }
	        for (TerminalInfo info : listInfo) {
	        	info.setBelongAgent(true);
	        	if (entityInfo.getAgentLevel() == 2 && !currAgentNo.equals(info.getAgentNo())) {//用来判断机具是否属于当前登录代理商所有
	        		info.setBelongAgent(false);
	        	}
	            if (info.getUserCode() != null) {
	                Map<String, Object> map = perMerService.selectRealNameByUserCode(info.getUserCode());
	                if (map != null) {
	                    info.setRealName((String) map.get("real_name"));
	                }
	            }

	        }
	        return listInfo;
		/*}
		return new ArrayList<>();*/
	}

    @Override
    public MerchantInfo selectMerchantDetail(String merchantNo){
	    return terminalInfoDao.selectMerchantDetail(merchantNo);
    }

    @Override
    public HardwareProduct selectHardwareProductBySn(String sn){
        return terminalInfoDao.selectHardwareProductBySn(sn);
    }


	@Override
	public List<HardwareAcvitityType> getActivityType(List<TerminalInfo> list) {
		UserLoginInfo loginUser = CommonUtil.getLoginUser();
		String curAgentNo = loginUser.getUserEntityInfo().getEntityId();
		return terminalInfoDao.findActivityType(list,curAgentNo);
	}


	@Override
	public int updateActivity(String snNo, String activityTypeNo) {
		TerminalInfo  sn = terminalInfoDao.findTerminalInfoBySn(snNo);
		UserLoginInfo loginUser = CommonUtil.getLoginUser();
		String curAgentNo = loginUser.getUserEntityInfo().getEntityId();
		String agentNo = sn.getAgentNo();
		if (!agentNo.equals(curAgentNo)) {
			throw new AgentWebException("非直营机具，不支持更改活动");
		}else {
			// 判断 该子类型是否是改机具的硬件类型对应的子类型
			long count = terminalInfoDao.countActivityHardwareBySn(sn.getSn(),activityTypeNo);
			// 判断该机具的活动子类型是可被代理商修改的
			if (sn.getUpdateAgentStatus() == null|| sn.getUpdateAgentStatus() != 1 || sn.getOpenStatus().equals("2") || count != 1) {
				throw new AgentWebException("机具不支持绑定该活动");
			}else {
				List<String> orgIdList = terminalInfoDao.findOrgIdByTypeNo(activityTypeNo);

				// 拿到机具对应的所属组织
				String orgId = terminalInfoDao.findOrgIdBySn(sn.getSn());
				if (orgIdList.contains(orgId)) {
					return terminalInfoDao.updateActivity(sn.getSn(),activityTypeNo);
				}else {
					throw new AgentWebException("活动与机具所属组织不一致");
				}
			}
		}
	}

	@Override
	public UpdateActivityBatchResult updateActivityBatch(List<TerminalInfo> snList, String activityTypeNo) {
		UpdateActivityBatchResult result = new UpdateActivityBatchResult();
		Integer errorCount = 0;
		Integer successCount = 0;
		ArrayList<SnVo> list = new ArrayList<>();
		UserLoginInfo loginUser = CommonUtil.getLoginUser();
		String curAgentNo = loginUser.getUserEntityInfo().getEntityId();
		for (TerminalInfo sn : snList) {
			SnVo snVo = new SnVo();
			snVo.setSn(sn.getSn());
			// 判断该机具是否是自营
			String agentNo = sn.getAgentNo();
			if (!agentNo.equals(curAgentNo)) {
				snVo.setErrMsg("非直营机具，不支持更改活动");
				errorCount ++;
				list.add(snVo);
			}else {
				// 判断 该子类型是否是改机具的硬件类型对应的子类型
				long count = terminalInfoDao.countActivityHardwareBySn(sn.getSn(),activityTypeNo);
				// 判断该机具的活动子类型是可被代理商修改的
				if (sn.getUpdateAgentStatus() == null|| sn.getUpdateAgentStatus() != 1 || sn.getOpenStatus().equals("2") || count != 1) {
					snVo.setErrMsg("机具不支持绑定该活动");
					errorCount ++;
					list.add(snVo);
				}else {
                    // 拿到机具对应的所属组织
                    String orgId = terminalInfoDao.findOrgIdBySn(sn.getSn());
                    // 是否有子组织
                    String teamEntryId = terminalInfoDao.getTeamEntryIdBySn(sn.getSn());
                    List<String> orgIdList = null;
                    if(StringUtils.isNotBlank(teamEntryId)){
                        orgIdList = terminalInfoDao.findOrgIdByTypeNoWithSonTeam(activityTypeNo,teamEntryId);
                    }else{
                        orgIdList = terminalInfoDao.findOrgIdByTypeNo(activityTypeNo);
                    }
					if (orgIdList!=null && orgIdList.contains(orgId)) {
						terminalInfoDao.updateActivity(sn.getSn(),activityTypeNo);
						successCount++;
					}else {
                        if(StringUtils.isNotBlank(teamEntryId)){
                            snVo.setErrMsg("活动所属不同组织,不能更改");
                        } else{
                            snVo.setErrMsg("活动与机具所属组织不一致");
                        }
                        errorCount ++;
						list.add(snVo);
					}
				}
			}

		}
		result.setErrorlist(list);
		result.setErrorCount(errorCount);
		result.setSuccessCount(successCount);
		return result;
	}

	@Override
	public List<HardwareAcvitityType> getActivityType(String sn) {
		TerminalInfo  info = terminalInfoDao.findTerminalInfoBySn(sn);
		ArrayList<TerminalInfo> arrayList = new ArrayList<>();
		arrayList.add(info);
        UserLoginInfo loginUser = CommonUtil.getLoginUser();
        String curAgentNo = loginUser.getUserEntityInfo().getEntityId();

        //根据sn获取team_entry_id,若果有,则去除不同子组织的活动,否,走原逻辑
        String teamEntryId = terminalInfoDao.getTeamEntryIdBySn(sn);
        if(StringUtils.isNotBlank(teamEntryId)) {
            return terminalInfoDao.findActivityTypeWithSonTeam(arrayList, curAgentNo,teamEntryId);
        }else{
            return terminalInfoDao.findActivityType(arrayList, curAgentNo);
        }


	}

    @Override
    public void exportTerinalInfo(HttpServletResponse response, HttpServletRequest request, TerminalInfo terminalInfo) {
	    log.info("jinrule");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        OutputStream ouputStream = null;
        try{
           /* Page<TerminalInfo> page = new Page<>(0,Integer.MAX_VALUE);
            List<TerminalInfo> terminalInfos = this.selectByParam(page, terminalInfo);*/
            List<TerminalInfo> terminalInfos = terminalInfoReadDao.exportSelectByParam(terminalInfo);
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String entityId = principal.getUserEntityInfo().getEntityId();
            AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
            List<HardwareProduct> hardwareProductList = hardwareProductDao.selectAllInfo(entityId, entityInfo.getAgentOem());
            ExcelExport export = new ExcelExport(2);
            String fileName = "机具列表"+sdf.format(new Date())+".xlsx";
            String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for (TerminalInfo info : terminalInfos) {
                map = new HashMap<String, String>();
                map.put("id",info.getId().toString());
                map.put("sn",info.getSn());
                map.put("merchantName",info.getMerchantName());
                map.put("agentName",info.getAgentName());
                map.put("terminalId",info.getTerminalId());
                String openStatus = info.getOpenStatus();
                if ("0".equals(openStatus)){
                    openStatus="已入库";
                }else if ("1".equals(openStatus)){
                    openStatus="已分配";
                }else if ("2".equals(openStatus)){
                    openStatus="已使用";
                }
                map.put("openStatus",openStatus);
                map.put("startTime",info.getStartTime()==null?"":sdf1.format(info.getStartTime()));
                map.put("createTime",info.getCreateTime()==null?"":sdf1.format(info.getCreateTime()));
                String activityTypes = "";
                if (StringUtils.isNotEmpty(info.getActivityType())){
                    String[] activityTypeArray = info.getActivityType().split(",");
                    if (activityTypeArray.length>0){
                        List<Map<String, String>> maps = terminalInfoDao.selectAllActivityType();
                        for (Map<String, String> stringStringMap : maps) {
                            for (String activityType : activityTypeArray) {
                                if (activityType.equals(stringStringMap.get("sys_value"))){
                                    if (StringUtils.isNotBlank(activityTypes)){
                                        activityTypes+=","+stringStringMap.get("sys_name");
                                    }else {
                                        activityTypes+=stringStringMap.get("sys_name");
                                    }
                                }
                            }
                        }

                    }
                }
                map.put("activityType",activityTypes);
                String type = info.getType();
                for (HardwareProduct hardwareProduct : hardwareProductList) {
                    String hpId = hardwareProduct.getHpId().toString();
                    String typeName = hardwareProduct.getTypeName();
                    if (type.equals(hpId)) {
                        info.setType(typeName + hardwareProduct.getVersionNu());
                        break;
                    }
                }
                map.put("type",info.getType());
                map.put("teamName",info.getTeamName());
                map.put("activityTypeNoName",info.getActivityTypeNoName());
                map.put("receiptDate",info.getReceiptDate()==null?"":sdf1.format(info.getReceiptDate()));
                map.put("downDate",info.getDownDate()==null?"":sdf1.format(info.getDownDate()));
                data.add(map);
            }
            String[] cols = new String[]{
                    "id","sn","merchantName","agentName",
                    "terminalId","openStatus", "startTime","createTime","activityType","type",
                    "teamName","activityTypeNoName","receiptDate",
                    "downDate"};
            String[] colsName = new String[]{
                    "序号","机具SN号","商户简称","所分配代理商",
                    "终端号","机具状态","启用时间","入库时间", "机具活动类型","硬件产品种类",
                    "所属组织","欢乐返子类型","接收日期",
                    "下发日期"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());

        }catch(Exception e){
            e.printStackTrace();
            log.error("导出机具列表异常", e);
        }finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TerminalInfo selectBySn(String sn) {
        return terminalInfoDao.selectBySn(sn);
    }

    /**
     * 判断代理商是否具有机具所对应的欢乐返活动子类型，没有则添加
     * @param sn
     * @param agentNo
     */
    @Override
    public void addAgentActivity(String sn, String agentNo) {
        TerminalInfo info = selectBySn(sn);
        String activityTypeNo = info.getActivityTypeNo();
        if(info != null && "7".equals(info.getActivityType()) && org.apache.commons.lang3.StringUtils.isNotBlank(info.getActivityTypeNo())){
            //为该代理商及其链条上的所有代理商增加欢乐返子类型
            AgentInfo agent = agentInfoService.selectByagentNo(agentNo);
            String[] agentNos = agent.getAgentNode().split("-");
            /*String agentNode = "0-";
            if(agentNos.length > 1){
                List<AgentActivity> happyBackList = new ArrayList<>();
                for (int i = 1; i < agentNos.length; i++) {
                    // 判断代理商是否具有该欢乐返子类型
                    AgentActivity aa = agentInfoService.selectByAgentNoAndActivityType(agentNos[i], activityTypeNo);
                    agentNode = agentNode + agentNos[i] + "-";
                    if(null == aa){
                        AgentActivity activity = new AgentActivity();
                        activity.setActivityTypeNo(activityTypeNo);
                        activity.setAgentNo(agentNos[i]);
                        activity.setAgentNode(agentNode);
                        activity.setStatus(true);
                        // 如果为一级代理商
                        if(i == 1){
                            Map<String, Object> map = agentInfoService.selectHappyBackDefaultParam(activityTypeNo);
                            activity.setCashBackAmount((BigDecimal)map.get("cash_back_amount"));
                            activity.setTaxRate(BigDecimal.valueOf(1));
                            activity.setRepeatRegisterAmount((BigDecimal)map.get("repeat_register_amount"));
                            activity.setRepeatRegisterRatio(BigDecimal.valueOf(1));
                            activity.setFullPrizeAmount((BigDecimal)map.get("full_amount"));
                            activity.setRepeatFullPrizeAmount((BigDecimal)map.get("repeat_full_amount"));
                            activity.setNotFullDeductAmount((BigDecimal)map.get("empty_amount"));
                            activity.setRepeatNotFullDeductAmount((BigDecimal)map.get("repeat_empty_amount"));
                        }else {
                            activity.setCashBackAmount(new BigDecimal(0));
                            activity.setTaxRate(new BigDecimal(0));
                            activity.setRepeatRegisterAmount(new BigDecimal(0));
                            activity.setRepeatRegisterRatio(new BigDecimal(0));
                            activity.setFullPrizeAmount(new BigDecimal(0));
                            activity.setRepeatFullPrizeAmount(new BigDecimal(0));
                            activity.setNotFullDeductAmount(new BigDecimal(0));
                            activity.setRepeatNotFullDeductAmount(new BigDecimal(0));
                        }
                        happyBackList.add(activity);
                    }else if(!aa.isStatus()){// 如果具有该活动且状态为关闭，则打开
                        agentInfoService.updateAgentActivityStatus(aa.getId(), true);
                    }
                }
                if(happyBackList.size() > 0){
                    agentInfoService.insertAgentActivity(happyBackList);
                }
            }*/

            int length = agentNos.length;
            if(length > 2){
                List<AgentActivity> happyBackList = new ArrayList<>();
                // 接收机具的代理商是否具有欢乐返活动且状态为打开
                AgentActivity a1 = agentInfoService.selectByAgentNoAndActivityType(agentNo, activityTypeNo);
                if(null == a1){
                    String parentAgentNo = agentNos[length - 2];
                    AgentActivity a2 = agentInfoService.selectByAgentNoAndActivityType(parentAgentNo, activityTypeNo);
                    if(null != a2){//上级有机具对应的欢乐返子类型且状态为打开
                        AgentActivity activity = new AgentActivity();
                        activity.setActivityTypeNo(activityTypeNo);
                        activity.setAgentNo(agentNo);
                        activity.setAgentNode(agent.getAgentNode());
                        // 是否启用与上级保持一致
                        activity.setStatus(a2.isStatus());

                        activity.setCashBackAmount(new BigDecimal(0));
                        activity.setTaxRate(new BigDecimal(0));
                        activity.setRepeatRegisterAmount(new BigDecimal(0));
                        activity.setRepeatRegisterRatio(new BigDecimal(0));
                        activity.setFullPrizeAmount(new BigDecimal(0));
                        activity.setRepeatFullPrizeAmount(new BigDecimal(0));
                        activity.setNotFullDeductAmount(new BigDecimal(0));
                        activity.setRepeatNotFullDeductAmount(new BigDecimal(0));

                        happyBackList.add(activity);
                        agentInfoService.insertAgentActivity(happyBackList);
                    }
                }else if(!a1.isStatus()){
                    agentInfoService.updateAgentActivityStatus(a1.getId(), true);
                }

            }

        }
    }

    @Override
    public int countAgentNodeAndActivityTypeNo(String agentNode, String activityTypeNo) {
        return terminalInfoDao.countAgentNodeAndActivityTypeNo(agentNode, activityTypeNo);
    }
}
