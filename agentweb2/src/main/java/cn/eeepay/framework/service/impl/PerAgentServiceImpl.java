package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.ActivityDetailDao;
import cn.eeepay.framework.dao.PaTerInfoDao;
import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.daoPerAgent.PerAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.PaAfterSale;
import cn.eeepay.framework.model.PaCashBackDetail;
import cn.eeepay.framework.model.PaSnBack;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.peragent.PaChangeLog;
import cn.eeepay.framework.model.peragent.PaTerInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.PaChangeLogService;
import cn.eeepay.framework.service.PerAgentService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * @author MXG
 * create 2018/07/11
 */
@Service
public class PerAgentServiceImpl implements PerAgentService {

    @Resource
    private PerAgentDao perAgentDao;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private PaChangeLogService paChangeLogService;
    @Resource
    private PaTerInfoDao paTerInfoDao;
    
    @Resource
    private ActivityDetailDao activityDetailDao;
    
    @Resource
    private TerminalInfoDao terminalInfoDao;

    private Logger log = LoggerFactory.getLogger(PerAgentServiceImpl.class);
    //接口调用成功
    private static final String SUCCESS = "200";

    @Override
    public List<PaUserInfo> selectAllList(PaUserInfo info) {
        return perAgentDao.selectAllList(info);
    }

    @Override
    public List<PaUserInfo> selectChildPaUser(PaUserInfo info){return perAgentDao.selectChildPaUser(info);};

    @Override
    public long countUserFromMer(PaUserInfo info) {
        return perAgentDao.countUserFromMer(info);
    }

    @Override
    public Map<String, String> selectByAgentNo(String entityId) {
        return perAgentDao.selectByAgentNo(entityId);
    }

    @Override
    public int updatePassword(String agentNo, String newPwd) {
        return perAgentDao.updatePassword(agentNo, newPwd);
    }

    @Override
    public Map<String, Object> updateShareLevel(String param) {
        Map<String, Object> result = new HashMap<>();
        //调接口
        List<SysDict> sysDicts = sysDictService.selectByKey("ALLAGENT_SERVICE_URL");
        String url = sysDicts.get(0).getSysValue()+"/user/changelevel";
        String response = HttpUtils.sendPost(url, param,"UTF-8");
        log.info("调整分润等级接口返回response=====>" + response + "======");
        if(StringUtils.isBlank(response)){
            result.put("status", false);
            result.put("msg", "调整分润等级失败");
            return result;
        }
        JSONObject responseObject = JSONObject.parseObject(response);
        String status = responseObject.getString("status");
        String msg = responseObject.getString("msg");
        String data = responseObject.getString("data");
        if(SUCCESS.equals(status)){
            result.put("status", true);
        }else {
            result.put("status", false);
            result.put("msg", msg);
        }
        return result;
    }

    @Override
    public Map<String, Object> queryUserByParam(PaUserInfo info, int pageNo, int pageSize) {
        Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent != null) {
            if(info != null ){
                if(info.getAgentNode() == null){
                    info.setAgentNode(loginAgent.getAgentNode());
                }
            }else {
                info = new PaUserInfo();
                info.setAgentNode(loginAgent.getAgentNode());
            }
        }else {
            msg.put("status", false);
            msg.put("msg", "请先登录");
            return msg;
        }
        //查询登录代理商对应的人人代理用户信息
        PaUserInfo paUserInfo = selectUserByAgentNo(loginAgent.getAgentNo());
        String loginUserCode = paUserInfo.getUserCode();
        info.setLoginUserCode(loginUserCode);
        Page<PaUserInfo> page = new Page<>(pageNo, pageSize);
        perAgentDao.selectPaUserByParam(page, info);
        if(page.getResult().size() == 0){
            msg.put("status", true);
            msg.put("page", page);
            return msg;
        }
        Map<String,Object> map = perAgentDao.selectFromPaBrand(loginAgent.getAgentOem());
        if(page.getResult().size() > 0){
            List<PaUserInfo> list = page.getResult();
            for (PaUserInfo userInfo : list) {
            	String parentId = userInfo.getParentId();
            	String userCode = userInfo.getUserCode();
            	//隐藏手机号,四期要求去掉
//              userInfo.setMobile(userInfo.getMobile().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
                userInfo.setMerTotal(perAgentDao.selectMerTotal(userCode));
                userInfo.setAllyTotal(perAgentDao.selectAllyTotal(userCode));
                userInfo.setAllMerTotal(perAgentDao.selectAllMerTotal(userInfo.getUserNode()));
//                Map<String,String> paUserAccountMap = perAgentDao.selectFromPaUserAccount(userCode);
//                Integer allMerTotal = 0;
//                if (paUserAccountMap != null) {
//                	allMerTotal = Integer.valueOf(paUserAccountMap.get("team_act_num")) - Integer.valueOf(paUserAccountMap.get("act_num"));
//				}
//                userInfo.setAllMerTotal(allMerTotal);
                if (loginUserCode.equals(parentId) && loginAgent.getAgentLevel() == 1) {
        			userInfo.setAgentStatus(map.get("one_agent_status").toString());
        		}else if (loginUserCode.equals(parentId) && loginAgent.getAgentLevel() == 2) {
        			userInfo.setAgentStatus(map.get("two_agent_status").toString());
        		}
            }
        }
        //统计大盟主、盟主数量
        msg.put("userTotal", perAgentDao.countUser(info));//盟主总数
        msg.put("bigUserTotal", perAgentDao.countBigUser(info));//大盟主总数
        //统计由商家成为盟友的数量
        long merUserTotal = countUserFromMer(info);
        msg.put("status", true);
        msg.put("page", page);
        msg.put("merUserTotal", merUserTotal);
        msg.put("maxAgentLevel", loginAgent.getAgentShareLevel());//登录代理商的分润最高可调级别
        return msg;
    }

    @Override
    public void exportPerAgentUser(PaUserInfo info, HttpServletResponse response, HttpServletRequest request) throws Exception {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent != null) {
            if(info != null ){
                if(info.getAgentNode() == null){
                    info.setAgentNode(loginAgent.getAgentNode());
                }else{}
            }else {
                info.setAgentNode(loginAgent.getAgentNode());
            }
        }else{}
        List<PaUserInfo> list =  perAgentDao.selectAllList(info);
        PaUserInfo paUserInfo = selectUserByAgentNo(loginAgent.getAgentNo());
        String loginUserCode = paUserInfo.getUserCode();
        Map<String,Object> map = perAgentDao.selectFromPaBrand(loginAgent.getAgentOem());
        if(list.size() > 0){
            for (PaUserInfo userInfo : list) {
            	String parentId = userInfo.getParentId();
            	String userCode = userInfo.getUserCode();
            	//隐藏手机号 四期要求去掉
//              userInfo.setMobile(userInfo.getMobile().replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
                userInfo.setMerTotal(perAgentDao.selectMerTotal(userCode));
                userInfo.setAllyTotal(perAgentDao.selectAllyTotal(userCode));
                userInfo.setAllMerTotal(perAgentDao.selectAllMerTotal(userInfo.getUserNode()));
//                Map<String,String> paUserAccountMap = perAgentDao.selectFromPaUserAccount(userCode);
//                Integer allMerTotal = 0;
//                if (paUserAccountMap != null) {
//                	allMerTotal = Integer.valueOf(paUserAccountMap.get("team_act_num")) - Integer.valueOf(paUserAccountMap.get("act_num"));
//				}
//                userInfo.setAllMerTotal(allMerTotal);
                if (loginUserCode.equals(parentId) && loginAgent.getAgentLevel() == 1) {
        			userInfo.setAgentStatus(map.get("one_agent_status").toString());
        		}else if (loginUserCode.equals(parentId) && loginAgent.getAgentLevel() == 2) {
        			userInfo.setAgentStatus(map.get("two_agent_status").toString());
        		}
            }
        }
        export(list, response, request);
    }
    @Override
	public Map<String, Object> selectPaCashBackDetail(PaCashBackDetail info, int pageNo, int pageSize) {
    	log.info("===进入盟友活动返现明细查询=======");
    	Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent == null){
            msg.put("status", false);
            msg.put("msg", "请登录后再操作");
            return msg;
        }
        //查询登录代理商对应的人人代理用户信息
        PaUserInfo paUserInfo = selectUserByAgentNo(loginAgent.getAgentNo());
        String loginUserCode = paUserInfo.getUserCode();
        info.setLoginUserCode(loginUserCode);
        info.setLoginUserNode(paUserInfo.getUserNode());
        info.setAgentLevel(loginAgent.getAgentLevel());
        Page<PaCashBackDetail> page = new Page<>(pageNo, pageSize);
        perAgentDao.selectPaCashBackDetail(page, info);
        List<Map<String,Object>> listMap = activityDetailDao.selectActivityTypeNo();
        List<PaCashBackDetail> list = page.getResult();
        for (PaCashBackDetail detail : list) {
        	for (Map<String, Object> map : listMap) {
				if (map.get("activity_type_no").toString().equals(detail.getActivityTypeNo())) {
					detail.setActivityTypeNo(map.get("activity_type_name").toString());
					break;
				}
			}
		}
        BigDecimal cashBackAmount0 = perAgentDao.sumCashBackAmount(info,"0");//未入账
        BigDecimal cashBackAmount1 = perAgentDao.sumCashBackAmount(info,"1");//已入账
        cashBackAmount0 = cashBackAmount0 == null ? new BigDecimal("0") : cashBackAmount0;
        cashBackAmount1 = cashBackAmount1 == null ? new BigDecimal("0") : cashBackAmount1;
        msg.put("cashBackAmount0", cashBackAmount0);
        msg.put("cashBackAmount1", cashBackAmount1);
        msg.put("cashBackAmountTotal", cashBackAmount0.add(cashBackAmount1));
        msg.put("status", true);
        msg.put("page", page);
        return msg;
	}
    @Override
	public void exportCashBackDetail(PaCashBackDetail info, HttpServletResponse response, HttpServletRequest request) throws Exception {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if (loginAgent == null) {
			throw new AgentWebException("请登录后再操作");
		}
		PaUserInfo paUserInfo = selectUserByAgentNo(loginAgent.getAgentNo());
        String loginUserCode = paUserInfo.getUserCode();
        info.setLoginUserCode(loginUserCode);
        info.setLoginUserNode(paUserInfo.getUserNode());
        info.setAgentLevel(loginAgent.getAgentLevel());
        List<PaCashBackDetail> list =  perAgentDao.exportCashBackDetail(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "盟友活动返现明细"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size() < 1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("activeOrder",null);
            maps.put("realName",null);
            maps.put("userCode",null);
            maps.put("activityCode",null);
            maps.put("activityTypeNo",null);
            maps.put("transAmount", null);
            maps.put("cashBackAmount", null);
            maps.put("entryStatus",null);
            maps.put("createTime",null);
            maps.put("entryTime",null);
            data.add(maps);
        }else{
        	List<Map<String,Object>> listMap = activityDetailDao.selectActivityTypeNo();
            for (PaCashBackDetail detail : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("activeOrder",detail.getActiveOrder());
                maps.put("realName",detail.getRealName());
                maps.put("userCode",detail.getUserCode());
                String activityCode = detail.getActivityCode();
                switch (activityCode) {
				case "008":
					activityCode = "欢乐返-循环送";
					break;
				case "009":
					activityCode = "欢乐返";
					break;
				default:
					break;
				}
                maps.put("activityCode",activityCode);
                for (Map<String, Object> map : listMap) {
    				if (map.get("activity_type_no").toString().equals(detail.getActivityTypeNo())) {
    					detail.setActivityTypeNo(map.get("activity_type_name").toString());
    					break;
    				}
    			}
                maps.put("activityTypeNo",detail.getActivityTypeNo());
                maps.put("transAmount", detail.getTransAmount() == null ? "" : detail.getTransAmount().toString());
                maps.put("cashBackAmount", detail.getCashBackAmount() == null ? "" : detail.getCashBackAmount().toString());
                String entryStatus = detail.getEntryStatus();
                switch (entryStatus) {
				case "0":
					entryStatus = "未入账";
					break;
				case "1":
					entryStatus = "已入账";
					break;
				default:
					break;
				}
                maps.put("entryStatus",entryStatus);
                maps.put("createTime",detail.getCreateTime() == null ? "" : sdf1.format(detail.getCreateTime()));
                maps.put("entryTime",detail.getEntryTime() == null ? "" : sdf1.format(detail.getEntryTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"activeOrder","realName","userCode","activityCode","activityTypeNo","transAmount","cashBackAmount","entryStatus","createTime","entryTime"};
        String[] colsName = new String[]{"激活订单号","盟主姓名","盟主编号","欢乐返类型","欢乐返子类型","交易金额(元)","返盟主金额(元)","返现入账状态","激活日期","入账日期"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("SN回拨记录导出失败!",e);
        }finally {
            if(ouputStream != null){
                ouputStream.close();
            }
        }
	}

    /**
     * 根据代理商编号查询出人人代理用户信息
     * @param agentNo
     * @return
     */
    @Override
    public PaUserInfo selectUserByAgentNo(String agentNo) {
        return perAgentDao.selectUserByAgentNo(agentNo);
    }

    @Override
    public List<Map> getShareLevelList() {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        return perAgentDao.getShareLevelList(loginAgent.getAgentOem(), loginAgent.getAgentShareLevel());
    }


    /**
     * 生成表格
     * @param list
     * @param response
     * @param request
     * @throws Exception
     */
    private void export(List<PaUserInfo> list, HttpServletResponse response, HttpServletRequest request) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "人人代理用户"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("userCode",null);
            maps.put("realName",null);
            maps.put("nickName",null);
            maps.put("userType",null);
            maps.put("merTotal",null);
            maps.put("allyTotal",null);
            maps.put("allMerTotal",null);
            maps.put("mobile", null);
            //maps.put("grade", null);
            maps.put("levelShow", null);
            maps.put("parentId",null);
            maps.put("parentName",null);
            maps.put("parentType",null);
            maps.put("isBindCard",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            for (PaUserInfo user : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userCode", user.getUserCode()==null?"":user.getUserCode());
                maps.put("realName", user.getRealName()==null?"":user.getRealName());
                maps.put("nickName", user.getNickName());
                maps.put("userType", "2".equals(user.getUserType())?"大盟主":"盟主");
                maps.put("merTotal", user.getMerTotal()==null?"0":user.getMerTotal().toString());
                maps.put("allyTotal", user.getAllyTotal()==null?"0":user.getAllyTotal().toString());
                maps.put("allMerTotal", user.getAllMerTotal().toString());
                maps.put("mobile", user.getMobile()==null?"":user.getMobile());
                //盟主称号
                /*String grade = user.getGrade();
                String gradeValue="";
                switch (grade){
                    case "0":gradeValue="普通盟主";
                        break;
                    case "1":gradeValue="黄金盟主";
                        break;
                    case "2":gradeValue="铂金盟主";
                        break;
                    case "3":gradeValue="黑金盟主";
                        break;
                    case "4":gradeValue="钻石盟主";
                        break;
                }
                maps.put("grade", gradeValue);*/
                maps.put("levelShow", user.getLevelShow()==null?"":user.getLevelShow());
                maps.put("vipLevelShow", user.getVipLevelShow());
                maps.put("parentId", user.getAgentNo()==null?"":user.getParentId());
                maps.put("parentName", user.getParentName()==null?"":user.getParentName());
                //所属上级类型
                String parentType = user.getParentType();
                maps.put("parentType", "1".equals(parentType)?"机构":("2".equals(parentType)?"大盟主":"盟主"));
                String isBindCard = user.getIsBindCard();
                maps.put("isBindCard",isBindCard==null ? "" : ("1".equals(isBindCard) ? "已认证" : "未认证"));
                maps.put("createTime", user.getCreateTime()==null?"":sdf1.format(user.getCreateTime()));
                data.add(maps);
            }
        }

        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"userCode","realName","nickName","userType","merTotal","allyTotal","allMerTotal","mobile","levelShow","vipLevelShow",
        		"parentId","parentName","parentType","isBindCard","createTime"};
        String[] colsName = new String[]{"用户编号","用户姓名","昵称","用户类型","直营商户（家）","发展盟主（名）","盟友商户激活数","注册手机","标准分润比例","VIP分润比例",
                "所属上级编号","所属上级名称","所属上级类型","是否认证","注册日期"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("人人代理用户导出失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

	@Override
	public PaUserInfo selectByUserCode(String userCode) {
		return perAgentDao.selectByUserCode(userCode);
	}

	@Override
	public Map<String,Object> selectPaUserCardByUserCode(String userCode) {
		return perAgentDao.selectPaUserCardByUserCode(userCode);
	}

	@Override
	public Integer updateCanProfitChange(String canProfitChange, String userCode) {
		return perAgentDao.updateCanProfitChange(canProfitChange,userCode);
	}

	@Override
	public Integer selectStatus() {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		return perAgentDao.selectStatus(loginAgent.getAgentOem());
	}

	@Override
	public Map<String, Object> selectPaAfterSale(PaAfterSale info, int pageNo, int pageSize) {
        Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent == null) {
            msg.put("status", false);
            msg.put("msg", "请先登录");
            return msg;
        }
        PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
        info.setUserCode(paUserInfo.getUserCode());
        info.setUserNode(paUserInfo.getUserNode());
        Page<PaAfterSale> page = new Page<>(pageNo, pageSize);
        perAgentDao.selectPaAfterSale(page, info);
        List<PaAfterSale> list = page.getResult();
        if(list.size() == 0){
            msg.put("status", true);
            msg.put("page", page);
            return msg;
        }
        for (PaAfterSale paAfterSale : list) {
        	String applyImg = paAfterSale.getApplyImg();
        	if (StringUtils.isNotBlank(applyImg)) {
        		String[] applyImgs = applyImg.split(",");
        		for (int i = 0; i < applyImgs.length; i++) {
        			switch (i) {
        			case 0:
        				paAfterSale.setApplyImg1(CommonUtil.getImgUrlAgent(applyImgs[0]));
        				break;
        			case 1:
        				paAfterSale.setApplyImg2(CommonUtil.getImgUrlAgent(applyImgs[1]));
        				break;
        			case 2:
        				paAfterSale.setApplyImg3(CommonUtil.getImgUrlAgent(applyImgs[2]));
        				break;
        			default:
        				break;
        			}
        		}
			}
        	String dealImg = paAfterSale.getDealImg();
        	if (StringUtils.isNotBlank(dealImg)) {
        		String[] dealImgs = dealImg.split(",");
        		for (int i = 0; i < dealImgs.length; i++) {
        			switch (i) {
        			case 0:
        				paAfterSale.setDealImg1(CommonUtil.getImgUrlAgent(dealImgs[0]));
        				break;
        			case 1:
        				paAfterSale.setDealImg2(CommonUtil.getImgUrlAgent(dealImgs[1]));
        				break;
        			case 2:
        				paAfterSale.setDealImg3(CommonUtil.getImgUrlAgent(dealImgs[2]));
        				break;
        			default:
        				break;
        			}
        		}
			}
		}
        msg.put("status", true);
        msg.put("page", page);
        msg.put("waitHandleTotal", perAgentDao.countWaitHandleTotal(info));//waitHandleTotal
        msg.put("waitHandleTotalTreeDays", perAgentDao.countWaitHandleTotalTreeDays(info));//waitHandleTotalTreeDays
        msg.put("waitHandleTotalSevenDays", perAgentDao.countWaitHandleTotalSevenDays(info));//waitHandleTotalSevenDays
        return msg;
    }

	@Override
	public void exportAfterSale(PaAfterSale info, HttpServletResponse response, HttpServletRequest request) throws Exception {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if (loginAgent == null) {
			throw new AgentWebException();
		}
        PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
        info.setUserCode(paUserInfo.getUserCode());
        info.setUserNode(paUserInfo.getUserNode());
        List<PaAfterSale> list =  perAgentDao.exportAfterSale(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "申购售后订单"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size() < 1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("payOrder",null);
            maps.put("saleType",null);
            maps.put("applyDesc",null);
            maps.put("status", null);
            maps.put("handler", null);
            maps.put("dealDesc", null);
            maps.put("applyTime",null);
            maps.put("dealTime",null);
            data.add(maps);
        }else{
            for (PaAfterSale paAfterSale : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",paAfterSale.getOrderNo());
                maps.put("payOrder",paAfterSale.getPayOrder());
                String saleType = paAfterSale.getSaleType();
                switch (saleType) {
				case "1":
					saleType = "收到的商品存在损坏";
					break;
				case "2":
					saleType = "发错货/漏发货";
					break;
				case "3":
					saleType = "未收到货";
					break;
				case "4":
					saleType = "其他";
					break;
				default:
					break;
				}
                maps.put("saleType",saleType);
                maps.put("applyDesc",paAfterSale.getApplyDesc());
                String status = paAfterSale.getStatus();
                switch (status) {
				case "0":
					status = "待机构处理";
					break;
				case "1":
					status = "待平台处理";
					break;
				case "2":
					status = "已处理";
					break;
				case "3":
					status = "已取消";
					break;
				default:
					break;
				}
                maps.put("status", status);
                maps.put("handler", "1".equals(paAfterSale.getHandler()) ? "机构" : "平台");
                maps.put("dealDesc", paAfterSale.getDealDesc());
                maps.put("applyTime",paAfterSale.getApplyTime() == null ? "" : sdf1.format(paAfterSale.getApplyTime()));
                maps.put("dealTime",paAfterSale.getDealTime() == null ? "" : sdf1.format(paAfterSale.getDealTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","payOrder","saleType","applyDesc","status","handler","dealDesc","applyTime","dealTime"};
        String[] colsName = new String[]{"售后编号","关联订单编号","售后类型","售后说明","售后状态","处理人","处理结果","提交日期","处理日期"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("申购售后订单导出失败!",e);
        }finally {
            if(ouputStream != null){
                ouputStream.close();
            }
        }
	}

	@Override
	public Map<String, Object> selectSnBackByParam(PaSnBack baseInfo, int pageNo, int pageSize) {
		Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent == null) {
            msg.put("status", false);
            msg.put("msg", "请先登录");
            return msg;
        }
        PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
        String userCode = paUserInfo.getUserCode();
        baseInfo.setUserNode(paUserInfo.getUserNode());
        baseInfo.setParentId(userCode);
        baseInfo.setEntityLevel(loginAgent.getAgentLevel());
        Page<PaSnBack> page = new Page<>(pageNo, pageSize);
        perAgentDao.selectSnBackByParam(page, baseInfo);
        List<PaSnBack> list = page.getResult();
        if(list.size() == 0){
            msg.put("status", true);
            msg.put("page", page);
            return msg;
        }else{
        	for (PaSnBack paSnBack : list) {
        		if (userCode.equals(paSnBack.getUserCode())) {//自己是申请人
            		paSnBack.setUserCodeStatus("0");
				}else if (userCode.equals(paSnBack.getReceiveUserCode())) {//自己是接收人
					paSnBack.setUserCodeStatus("1");
				}
        		paSnBack.setBackCount(perAgentDao.selectSnBackCount(paSnBack.getOrderNo()));
			}
        }
        msg.put("status", true);
        msg.put("page", page);
		return msg;
	}
	
	@Override
	public void exportSnBack(PaSnBack info, HttpServletResponse response, HttpServletRequest request) throws Exception {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if (loginAgent == null) {
			throw new AgentWebException();
		}
        PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
        String userCode = paUserInfo.getUserCode();
        info.setParentId(userCode);
        info.setUserNode(paUserInfo.getUserNode());
        info.setEntityLevel(loginAgent.getAgentLevel());
        List<PaSnBack> list =  perAgentDao.exportSnBack(info);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "SN号回拨记录"+sdf.format(new Date())+".xls" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size() < 1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("backCount",null);
            maps.put("userCode",null);
            maps.put("receiveUserCode",null);
            maps.put("receiveUserType", null);
            maps.put("beLongUserCode", null);
            maps.put("status",null);
            maps.put("createTime",null);
            maps.put("lastUpdateTime",null);
            data.add(maps);
        }else{
            for (PaSnBack paSnBack : list) {
//            	if (userCode.equals(paSnBack.getUserCode())) {//自己是申请人
//            		paSnBack.setUserCodeStatus("0");
//				}else if (userCode.equals(paSnBack.getReceiveUserCode())) {//自己是接收人
//					paSnBack.setUserCodeStatus("1");
//				}
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",paSnBack.getOrderNo());
                maps.put("backCount",perAgentDao.selectSnBackCount(paSnBack.getOrderNo()).toString());
                maps.put("userCode",paSnBack.getUserCode());
                maps.put("receiveUserCode",paSnBack.getReceiveUserCode());
                maps.put("receiveUserType", showReceiveUserType(paSnBack.getReceiveUserType()));
                maps.put("beLongUserCode", paSnBack.getBeLongUserCode());
                maps.put("status",showStatus(paSnBack.getStatus()));
                maps.put("createTime",paSnBack.getCreateTime() == null ? "" : sdf1.format(paSnBack.getCreateTime()));
                maps.put("lastUpdateTime",paSnBack.getLastUpdateTime() == null ? "" : sdf1.format(paSnBack.getLastUpdateTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","backCount","userCode","receiveUserCode","receiveUserType","beLongUserCode","status","createTime","lastUpdateTime"};
        String[] colsName = new String[]{"回拨单号","回拨数量","回拨盟主编号","接收盟主编号","接收盟主类型","所属机构编号","回拨状态","回拨日期","处理日期"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("SN回拨记录导出失败!",e);
        }finally {
            if(ouputStream != null){
                ouputStream.close();
            }
        }
	}

	@Override
	public Integer updateStatus(String orderNo,String status) {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		String agentNo = loginAgent.getAgentNo();
		String agentNode = loginAgent.getAgentNode();
		PaUserInfo paUserInfo = perAgentDao.selectUserByAgentNo(loginAgent.getAgentNo());
		List<String> snList = perAgentDao.selectSnFromPaBackSN(orderNo);
		if ("1".equals(status)) {//接收,修改所属人,状态
			String saleStauts = "1";
			if (loginAgent.getAgentLevel() != 1) {
				saleStauts = "2";
			}
			Integer count1 = 0;
			Integer count2 = 0;
			for (String sn : snList) {
                PaTerInfo changePre = paTerInfoDao.selectBySn(sn);
				count1 += terminalInfoDao.updateLockStatusToHave(sn,agentNo,paUserInfo.getUserCode(),saleStauts);
				count2 += terminalInfoDao.updateBelongAgent(agentNo,agentNode,sn);
                if(changePre != null){
                    PaTerInfo changeAfter = paTerInfoDao.selectBySn(sn);
                    PaChangeLog changeLog = new PaChangeLog(JSONObject.toJSONString(changePre), JSONObject.toJSONString(changeAfter),
                            "机具回拨","代理商系统，代理商编号：" + loginAgent.getAgentNo(),"updateLockStatusToHave");
                    paChangeLogService.insert(changeLog);
                }
			}
			return perAgentDao.updateStatus(orderNo,status) + count1 + count2;
		}else{
			Integer count3 = 0;
			for (String sn : snList) {
                PaTerInfo changePre = paTerInfoDao.selectBySn(sn);
				count3 += terminalInfoDao.updateLockStatus(sn);
				if(changePre != null){
                    PaTerInfo changeAfter = paTerInfoDao.selectBySn(sn);
                    PaChangeLog paChangeLog = new PaChangeLog(JSONObject.toJSONString(changePre),JSONObject.toJSONString(changeAfter),
                            "将对应sn号的机具改为不锁定","代理商系统，代理商编号：" + loginAgent.getAgentNo(),"updateLockStatus");
                    paChangeLogService.insert(paChangeLog);
                }
			}
			return perAgentDao.updateStatus(orderNo,status) + count3;
		}
	}

	@Override
	public List<Map<String,Object>> selectSnByOrder(String orderNo) {
		return perAgentDao.selectSnByOrder(orderNo);
	}

	@Override
	public Map<String, Object> selectFromPaTransInfo(String orderNo) {
		return perAgentDao.selectFromPaTransInfo(orderNo);
	}
	

	@Override
	public Integer updateNowAfterSale(JSONObject jsonObject) {
		String orderNo = jsonObject.getString("orderNo");
    	String dealDesc = jsonObject.getString("dealDesc");
    	String dealImg1 = jsonObject.getString("dealImg1");
    	String dealImg2 = jsonObject.getString("dealImg2");
    	String dealImg3 = jsonObject.getString("dealImg3");
    	StringBuilder dealImg = new StringBuilder();
    	if (StringUtils.isNotBlank(dealImg1)) {
    		dealImg.append(dealImg1);
		}
    	if (StringUtils.isNotBlank(dealImg2)) {
    		dealImg.append(",").append(dealImg2);
    	}
    	if (StringUtils.isNotBlank(dealImg3)) {
    		dealImg.append(",").append(dealImg3);
    	}
		return perAgentDao.updateNowAfterSale(orderNo,dealDesc,dealImg == null ? "" : dealImg.toString());
	}

	@Override
	public Map<String, Object> selectFromPaMerInfoByMerchantNo(String merchantNo) {
		return perAgentDao.selectFromPaMerInfoByMerchantNo(merchantNo);
	}


    /**
	 * 回拨状态显示
	 * @param status
	 */
	private String showStatus(String status) {
		switch (status) {
		case "0":
			status = "等待接收";
			break;
		case "1":
			status = "回拨成功";
			break;
		case "2":
			status = "拒绝接收";
			break;
		case "3":
			status = "已取消";
			break;
		case "4":
			status = "处理中";
			break;
		default:
			status = "";
			break;
		}
		return status;
	}
	private String showReceiveUserType(String status) {
		switch (status) {
		case "1":
			status = "机构";
			break;
		case "2":
			status = "大盟主";
			break;
		case "3":
			status = "盟主";
			break;
		default:
			status = "";
			break;
		}
		return status;
	}

    @Override
    public List<Integer> selectShareLevelList(String agentOem, String agentShareLevel) {
        return perAgentDao.selectShareLevelList(agentOem, agentShareLevel);
    }

    @Override
    public String selectPaTerminalBackByOrderNo(String userNode, String orderNo) {
        return perAgentDao.selectPaTerminalBackByOrderNo(userNode, orderNo);
    }

	@Override
	public String selectMd5Key(String key) {
		return perAgentDao.selectMd5Key(key);
	}

	@Override
	public Map<String, String> selectByMerchantNo(String merchantNo) {
		return perAgentDao.selectByMerchantNo(merchantNo);
	}
	
}
