package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.daoSuperBank.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.ZxProductOrderService;
import cn.eeepay.framework.util.*;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service("superBankService")
public class SuperBankServiceImpl implements SuperBankService{
	private static final Logger log = LoggerFactory.getLogger(SuperBankServiceImpl.class);
	private static final Pattern pattern=Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	private static final DecimalFormat format=new DecimalFormat("0.00");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private UserInfoSuperBankDao userInfoSuperBankDao;
	
	@Resource
	private CreditCardBonusDao creditCardBonusDao;
	
	@Resource
    private LoanCompanyDao loanCompanyDao;

	@Resource
	private AgentInfoService agentInfoService;

	@Resource
	private ZxProductOrderService zxProductOrderService;

    @Resource
    private OrderMainDao orderMainDao;
    
	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;
	
	@Resource
    private UserProfitDao userProfitDao;
	
	@Resource
    private LoanSourceDao loanSourceDao;
	
	@Resource
    private OrgInfoDao orgInfoDao;
	
	@Resource
	private SysDictDao sysDictDao;

	@Resource
    private CreditcardSourceDao creditcardSourceDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;
	
	@Resource
	private UserInfoDao userInfoDao;
	
	@Resource
	private UserEntityInfoDao userEntityInfoDao;
	
	@Resource
	private UserRoleDao userRoleDao;
	
	@Resource
    private LotteryOrderDao lotteryOrderDao;

	@Resource
	private RankingRecordDao rankingRecordDao;

	@Resource
	private RankingPushRecordDao rankingPushRecordDao;

	@Resource
	private RankingRecordDetailDao rankingRecordDetailDao;
	
	@Resource
    private CarOrderDao carOrderDao;

	@Resource
	private InsuranceCompanyDao insuranceCompanyDao;

	@Resource
	private BonusConfDao bonusConfDao;

	@Resource
	private MposMachinesDao mposMachinesDao;

	@Resource
	private MposOrderDao mposOrderDao;


	@Resource
	private MposActiveOrderDao mposActiveOrderDao;

	@Resource
	private MposTradeOrderDao mposTradeOrderDao;

	@Resource
	private MposMerchantTradeCountDao mposMerchantTradeCountDao;

	@Resource
	private MposProductTypeDao mposProductTypeDao;


	@Override
	public List<UserInfoSuperBank> selectByCondions(UserInfoSuperBank userInfoSuperBank, Page<UserInfoSuperBank> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = secondUserNodeConditions(userInfoSuperBank);
		List<UserInfoSuperBank> list = userInfoSuperBankDao.selectByCondions(userInfoSuperBank,entityId,page);
		for (UserInfoSuperBank info : list) {
			info.setIsAgent(principal.getUserEntityInfo().getIsAgent());
//			String phone = info.getPhone();
//			info.setPhone(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
		}
		return list;
	}

	private String secondUserNodeConditions(UserInfoSuperBank userInfoSuperBank) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		//当前用户登录用户的二级节点为空且是OEM默认用户,查询条件添加当前用户的二级代理节点(设置成当前登录用户的二级代理节点)
		String secondUserNode = principal.getSecondUserNode();
		//如果是一级代理商就直接根据页面传过来的值作为条件
		if (agentInfo.getAgentLevel() == 1 && "0".equals(principal.getUserEntityInfo().getIsAgent()) && StringUtils.isNotBlank(secondUserNode)) {
//			String telNo = principal.getTelNo();
//			String isoem = userInfoSuperBankDao.isOem(telNo,secondUserNode);
//			if ("1".equals(isoem)) {
				if (StringUtils.isBlank(userInfoSuperBank.getSecondUserNode())) {
					userInfoSuperBank.setSecondUserNode(secondUserNode);
				}
//			}
		}
		return entityId;
	}

	@Override
	public List<OrgInfo> getOrgInfoList() {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userInfoSuperBankDao.getOrgInfoList(principal.getUserEntityInfo().getEntityId());//代理商编号就对应组织ID
	}
	@Override
	public List<BonusConf> getBonusConfList() {
		return bonusConfDao.getBonusConfList();
	}

	@Override
	public List<UserInfoSuperBank> exportUserInfoSuperBank(UserInfoSuperBank userInfoSuperBank, String agentNo) {
		String entityId = secondUserNodeConditions(userInfoSuperBank);
		return userInfoSuperBankDao.exportUserInfoSuperBank(userInfoSuperBank,entityId);
	}

	@Override
	public Map<String, Object> selectDetail(String userId) {
		Map<String,Object> map = new HashMap<>();
		UserInfoSuperBank userInfoSuperBank = userInfoSuperBankDao.selectDetail(userId);
		//根据商户号找到进件ID
		String bpId = sysDictDao.SelectServiceId("SUPER_BANK_BP_ID");
		Map<String, Object> selectMerBpInfo = merchantBusinessProductDao.selectMerBpInfo(userInfoSuperBank.getReceiveUserNo(),bpId);
		if (selectMerBpInfo != null) {
			userInfoSuperBank.setItemId(selectMerBpInfo.get("id").toString());//进件编号
		}
		String userCode = userInfoSuperBank.getUserCode();
		String totalAmount = userInfoSuperBankDao.selectTotalAmount(userCode);
		if (StringUtils.isNotBlank(totalAmount)) {
			userInfoSuperBank.setTotalAmount(new BigDecimal(totalAmount));
		}else{
			userInfoSuperBank.setTotalAmount(new BigDecimal("0.00"));
		}
		userInfoSuperBank.setUserType(returnUserType(userInfoSuperBank.getUserType()));
		String idCardNo = userInfoSuperBank.getIdCardNo();
		if (StringUtils.isNotBlank(idCardNo)) {
			userInfoSuperBank.setIdCardNo("**************" + idCardNo.substring(idCardNo.length()-4, idCardNo.length()));
		}
		userInfoSuperBank.setStatusAgent("1".equals(userInfoSuperBank.getStatusAgent()) ? "已开通" : "未开通");
		userInfoSuperBank.setPayBack("1".equals(userInfoSuperBank.getPayBack()) ? "已退款" : "未退款");
		if (StringUtils.isNotBlank(userInfoSuperBank.getStatusRepayment())) {
			String statusRepayment = showStatusRepayment(userInfoSuperBank.getStatusRepayment());
			userInfoSuperBank.setStatusRepayment(statusRepayment);
		}else{
			userInfoSuperBank.setStatusRepayment("未开通");
		}
		if (StringUtils.isNotBlank(userInfoSuperBank.getStatusReceive())) {
			String statusReceive = showStatusReceive(userInfoSuperBank.getStatusReceive());
			userInfoSuperBank.setStatusReceive(statusReceive);
		}else{
			userInfoSuperBank.setStatusReceive("未开通");
		}
		if(StringUtils.isNotBlank(userInfoSuperBank.getQrCode())){
			userInfoSuperBank.setQrCode(getImgUrl(userInfoSuperBank.getQrCode()));
        }
		String oneCode = userInfoSuperBank.getTopOneCode();
		String twoCode = userInfoSuperBank.getTopTwoCode();
		String threeCode = userInfoSuperBank.getTopThreeCode();
		UserInfoSuperBank one = userInfoSuperBankDao.selectDetailByUserNode(oneCode);
		UserInfoSuperBank two = userInfoSuperBankDao.selectDetailByUserNode(twoCode);
		UserInfoSuperBank three = userInfoSuperBankDao.selectDetailByUserNode(threeCode);
		if (one != null) {
			userInfoSuperBank.setTopOneUserName(one.getUserName());
			userInfoSuperBank.setTopOneUserType(returnUserType(one.getUserType()));
			userInfoSuperBank.setTopOneNickName(one.getNickName() == null ? "" : one.getNickName());
		}
		if (two != null) {
			userInfoSuperBank.setTopTwoUserName(two.getUserName());
			userInfoSuperBank.setTopTwoUserType(returnUserType(two.getUserType()));
			userInfoSuperBank.setTopTwoNickName(two.getNickName() == null ? "" : two.getNickName());
		}
		if (three != null) {
			userInfoSuperBank.setTopThreeUserName(three.getUserName());
			userInfoSuperBank.setTopThreeUserType(returnUserType(three.getUserType()));
			userInfoSuperBank.setTopThreeNickName(three.getNickName() == null ? "" : three.getNickName());
		}
		//下级代理数量
		Integer agentCount = userInfoSuperBankDao.selectAgentCount(userCode);
		//直营用户数量
		Integer notAgentCount = userInfoSuperBankDao.selectNotAgentCount(userCode);
		//用户结算卡
		UserCard userCard = userInfoSuperBankDao.selectUserCard(userCode);
		if(userCard != null){
            log.info("结算卡:" + userCard +"==");
            String cardNo = userCard.getCardNo();
            String accountIdNo = userCard.getAccountIdNo();
            if (StringUtils.isNotBlank(cardNo)) {
                userCard.setCardNo("***********" + cardNo.substring(cardNo.length()-4, cardNo.length()));
            }
            if (StringUtils.isNotBlank(accountIdNo)) {
            	userCard.setAccountIdNo("**************" + accountIdNo.substring(accountIdNo.length()-4, accountIdNo.length()));
            }
            if(StringUtils.isNotBlank(userCard.getPositivePhoto())){
                userCard.setPositivePhoto(getImgUrl(userCard.getPositivePhoto()));
            }
        }
		userInfoSuperBank.setAddress(userInfoSuperBank.getOpenProvince() == null ? "" : userInfoSuperBank.getOpenProvince() +
										userInfoSuperBank.getOpenCity() == null ? "" : userInfoSuperBank.getOpenCity() +
												userInfoSuperBank.getOpenRegion() == null ? "" : userInfoSuperBank.getOpenRegion());
//		userInfoSuperBank.setPhone(userInfoSuperBank.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
		map.put("userInfoSuperBank", userInfoSuperBank);
		map.put("agentCount", agentCount);
		map.put("notAgentCount", notAgentCount);
		map.put("userCard", userCard);
		return map;
	}

	/**
	 * 显示收款业务状态
	 * @param status
	 * @return
	 */
	private String showStatusReceive(String status) {
		String returnString = "";
		switch (status) {
		case "1":
			returnString = "待审核";
			break;
		case "2":
			returnString = "审核失败";
			break;
		case "3":
			returnString = "开通成功";
			break;
		default:
			returnString = "未开通";
			break;
		}
		return returnString;
	}

	/**
	 * 显示还款业务状态
	 * @param status
	 * @return
	 */
	private String showStatusRepayment(String status) {
		String returnString = "";
		switch (status) {
		case "1":
			returnString = "已开通";
			break;
		case "2":
			returnString = "已付款";
			break;
		default:
			returnString = "未开通";
			break;
		}
		return returnString;
	}
	
	@Override
	public List<UserInfoSuperBank> selectUserInfoList(String userCode) {
        return userInfoSuperBankDao.selectUserInfoList(userCode);
	}

	@Override
	public List<OrderMain> selectOrderPage(OrderMain baseInfo, Page<OrderMain> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		//当前用户登录用户的二级节点为空且是OEM默认用户,查询条件添加当前用户的二级代理节点(设置成当前登录用户的二级代理节点)
		String secondUserNode = principal.getSecondUserNode();
		//如果是一级代理商且是代理商 就直接根据页面传过来的值作为条件
		if (agentInfo.getAgentLevel() == 1 && "0".equals(principal.getUserEntityInfo().getIsAgent()) && StringUtils.isNotBlank(secondUserNode)) {
//			String telNo = principal.getTelNo();
//			String isoem = userInfoSuperBankDao.isOem(telNo,secondUserNode);
//			if ("1".equals(isoem)) {
				if (StringUtils.isBlank(baseInfo.getSecondUserNode())) {
					baseInfo.setSecondUserNode(secondUserNode);
				}
//			}
		}
		baseInfo.setEntityId(entityId);
		orderMainDao.selectOrderPage(baseInfo, page);
        List<OrderMain> list = page.getResult();
        filterOrderParam(list);
        return list;
	}


	@Override
	public OrderMainSum selectOrderSum(OrderMain baseInfo) {
		return orderMainDao.selectOrderSum(baseInfo);
	}
	/**
     * 过滤订单里面的数据
     * @param list
     */
	public void filterOrderParam(List<OrderMain> list){
		Map<String, String> repayTransStatusMap = getTransStatusMap();
        Map<String, String> userNameMap = getUserNameMap();
        Map<String, String> orderStatusMap = getOrderStatusMap();
        Map<String, String> accountStatusMap = getAccountStatusMap();
        Map<String, String> userTypeMap = getUserTypeMap();
        Map<String, String> payMethodMap = getPayMethodMap();
        Map<String, String> orderTypeMap = getOrderTypeMap();
        if(list != null && list.size() > 0){
        	log.info("====开始扣率去百分号计算===在结束之前报错说明数据有问题,没有带百分号====");
            for(OrderMain order: list){
            	String userCode = order.getUserCode();
            	UserInfoSuperBank userInfoSuperBank = userInfoSuperBankDao.selectAddress(userCode);
            	if (userInfoSuperBank == null) {
            		log.info("请处理数据问题,根据userCode" + userCode + "没查询到数据");
				}
            	log.info("根据订单userCode在用户表查询到用户为========" + userInfoSuperBank + "====");
            	String incomeType = userInfoSuperBankDao.selectIncomeType(order.getOrderNo());
            	UserInfoSuperBank infoSuperBank = userInfoSuperBankDao.selectAddress(userCode);
            	if (infoSuperBank == null) {
            		log.info("请处理数据问题,根据userCode" + userCode + "没查询到数据");
            	}
            	//保存了百分号,计算之前先去掉百分号
            	String loanBankRate = order.getLoanBankRate() == null ? "0" : order.getLoanBankRate().substring(0, order.getLoanBankRate().length()-1);
//            	String loanOrgRate = order.getLoanOrgRate() == null ? "0" : order.getLoanOrgRate().substring(0, order.getLoanOrgRate().length()-1);
            	String companyBonusConf = order.getCompanyBonusConf() == null ? "0" : order.getCompanyBonusConf();
            	BigDecimal subtract = new BigDecimal(loanBankRate).subtract(new BigDecimal(companyBonusConf));
            	String loanToBrankRate = subtract + "%";
            	order.setLoanToBrankRate(loanToBrankRate);
            	BigDecimal loanAmount = order.getLoanAmount();
            	if (loanAmount != null) {
            		order.setLoanToBrankTotalAmount(loanAmount.multiply(subtract).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)+"");
				}
            	BigDecimal creditcardBankBonus = order.getCreditcardBankBonus();
            	if (creditcardBankBonus != null && order.getTotalBonus() != null) {
            		order.setBankTotalAmount(creditcardBankBonus.subtract(order.getTotalBonus()));
				}
            	
            	order.setOrderType(orderTypeMap.get(order.getOrderType()));
            	order.setPayMethod(payMethodMap.get(order.getPayMethod()));
            	order.setUserName(userNameMap.get(order.getUserCode()));
                order.setShareUserName(userNameMap.get(order.getShareUserCode()));
                order.setOneUserName(userNameMap.get(order.getOneUserCode()));
                order.setTwoUserName(userNameMap.get(order.getTwoUserCode()));
                order.setThrUserName(userNameMap.get(order.getThrUserCode()));
                order.setFouUserName(userNameMap.get(order.getFouUserCode()));
                order.setOneUserType(userTypeMap.get(order.getOneUserType()));
                order.setTwoUserType(userTypeMap.get(order.getTwoUserType()));
                order.setThrUserType(userTypeMap.get(order.getThrUserType()));
                order.setFouUserType(userTypeMap.get(order.getFouUserType()));
                order.setStatus(orderStatusMap.get(order.getStatus()));
                order.setAccountStatus(accountStatusMap.get(order.getAccountStatus()));
                order.setAccountStatus2(accountStatusMap.get(order.getAccountStatus2()));
                order.setRepayTransStatus(repayTransStatusMap.get(order.getRepayTransStatus()));
                BigDecimal repayTransfeeAdd = order.getRepayTransfeeAdd() == null ? new BigDecimal("0") : order.getRepayTransfeeAdd();
                order.setTransRate(new BigDecimal(order.getTransRate() == null ? "0" : order.getTransRate()).setScale(2, RoundingMode.HALF_UP) + "% +" + repayTransfeeAdd + "元");
                if (userInfoSuperBank != null) {
                	String openProvince = userInfoSuperBank.getOpenProvince() == null ? "" : userInfoSuperBank.getOpenProvince();
                	String openCity = userInfoSuperBank.getOpenCity() == null ? "" : userInfoSuperBank.getOpenCity();
                	String openRegion = userInfoSuperBank.getOpenRegion() == null ? "" : userInfoSuperBank.getOpenRegion();
                	String remark = userInfoSuperBank.getRemark();
                	order.setOpenProvince(openProvince);
                	order.setOpenCity(openCity);
                	order.setOpenRegion(openRegion);
                	order.setRemark(remark);
                	order.setAddress(openProvince+openCity+openRegion);
				}
            	order.setIncomeType(incomeType);
            	if (infoSuperBank != null) {
            		order.setSharePhone(infoSuperBank.getPhone());
				}
            	String orderIdNo = order.getOrderIdNo();
            	if (StringUtils.isNotBlank(orderIdNo)) {
            		order.setOrderIdNo(orderIdNo.replaceAll("(\\d{4})\\d{10}(\\d{3})","$1**********$2"));
				}
            	changeType(order);
            }
            log.info("====结束扣率去百分号计算=======");
        }
    }

	/**
	 * 类型显示转换
	 * @param order
	 */
	private void changeType(OrderMain order) {
		String loanType = order.getLoanType();
		if(StringUtils.isNotBlank(loanType)){
			switch (loanType) {
				case "1":
					loanType = "有效注册";
					break;
				case "2":
					loanType = "有效借款";
					break;
				case "3":
					loanType = "授信成功";
					break;
				default:
					break;
			}
		}
		String profitType = order.getProfitType();
		if (StringUtils.isNotBlank(profitType)){
			switch (profitType) {
				case "1":
					loanType = "固定奖金";
					break;
				case "2":
					loanType = "按比例发放";
					break;
				default:
					break;
			}
		}
	}
    /**
     * 将所有userInfo的userCode和userName组成map
     * @return
     */
    public Map<String, String> getUserNameMap(){
        Map<String, String> map = new HashMap<>();
        List<UserInfoSuperBank> userList = userInfoSuperBankDao.getAllList();
        if(userList != null && userList.size() > 0){
            for(UserInfoSuperBank userInfo: userList){
                map.put(
                        userInfo.getUserCode() == null?"":userInfo.getUserCode(),
                        userInfo.getUserName() == null?"":userInfo.getUserName());
            }
        }
        map.put("plate", "默认");
        return map;
    }
    /**
     * 订单状态集合转换为map
     * @return
     */
    public Map<String, String> getOrderStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "已创建");
        map.put("2", "待支付");
        map.put("3", "待审核");
        map.put("4", "已完成");
        map.put("5", "已完成");
        map.put("6", "订单失败");
        map.put("7", "已办理过");
        map.put("9", "回收站");
        return map;
    }
    /**
     * 支付方式
     * @return
     */
    public Map<String, String> getPayMethodMap(){
    	Map<String, String> map = new HashMap<>();
    	map.put("1", "微信");
    	map.put("2", "支付宝");
    	map.put("3", "快捷");
    	return map;
    }
    public Map<String, String> getTransStatusMap(){
    	Map<String, String> map = new HashMap<>();
    	map.put("3", "成功");
    	map.put("4", "失败");
    	map.put("6", "终止");
    	return map;
    }
	public String returnUserType(String userType){
		switch (userType) {
		case "10":
			userType = "普通用户";
			break;
		case "20":
			userType = "专员";
			break;
		case "30":
			userType = "经理";
			break;
		case "40":
			userType = "银行家";
			break;
		case "50":
			userType = "OEM";
			break;
		case "60":
			userType = "平台";
			break;
		default:
			userType = "";
			break;
		}
		return userType;
	}
	/**
     * 入账状态集合转换为map
     * @return
     */
    public Map<String, String> getAccountStatusMap(){
        Map<String, String> map = new HashMap<>();
        map.put("0", "待入账");
        map.put("1", "已记账");
        map.put("2", "记账失败");
        return map;
    }
    /**
     * 订单状态集合转换为map
     * @return
     */
    public Map<String, String> getUserTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("10", "用户");
        map.put("20", "专员");
        map.put("30", "经理");
        map.put("40", "银行家");
        map.put("50", "OEM");
        map.put("60", "平台");
        return map;
    }

    /**
     * 导出代理授权订单
     * @param order
     */
    @Override
    public void exportAgentOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "代理授权订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgName", String.valueOf(item.getOrgId()));
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("status", item.getStatus());
                map.put("refundStatus", "1".equals(item.getRefundStatus()) ? "是" : "否");
                map.put("refundDate", item.getRefundDateStr());
                map.put("refundMsg", item.getRefundMsg());
                map.put("incomeType", item.getIncomeType());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("sharePhone", item.getSharePhone());
                map.put("price", String.valueOf(item.getPrice()));
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("payDate", item.getPayDateStr());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit() == null ? "" : item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            addSumMap(order, data);

            String[] cols = new String[]{
                    "orderNo","orgName","secondUserNode","status",
                    "refundStatus","refundDate","refundMsg","incomeType",
                    "userCode","userName","sharePhone"
                    ,"price","totalBonus",
                    "createDate","payDate","payOrderNo",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgName","orgProfit","plateProfit","accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","二级代理节点","订单状态",
                    "已达标退款","退款时间","退款原由","退款订单号",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "售价","发放总奖金",
                    "创建时间","支付时间","关联支付订单",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润","品牌代理成本","记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家代理授权订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public OrderMain selectOrderDetail(String orderNo) {
		if(StringUtils.isBlank(orderNo)){
            return null;
        }
        OrderMain orderMain = orderMainDao.selectOrderDetail(orderNo);
		if ("0".equals(orderMain.getProfitStatus2())) {
			orderMain.setProfitStatus2("未完成");
		}else if ("1".equals(orderMain.getProfitStatus2())){
			orderMain.setProfitStatus2("已完成");
		}
        List<OrderMain> list = new ArrayList<>();
        list.add(orderMain);
        filterOrderParam(list);
        return list.get(0);
	}
    @Override
    public OrderMain selectInsuranceOrderDetail(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            return null;
        }
        OrderMain orderMain = orderMainDao.selectInsuranceOrderDetail(orderNo);
        List<OrderMain> list = new ArrayList<>();
        list.add(orderMain);
        filterOrderParam(list);
        return list.get(0);
    }

	/**
	 * 办理信用卡订单导出
	 * @param response
	 * @param order
	 */
	@Override
	public void exportCreditOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "办理信用卡订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgName", item.getOrgName());
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("status", item.getStatus());
                String profitStatus2 = item.getProfitStatus2();
                if ("0".equals(profitStatus2)) {
                	profitStatus2 = "未完成";
				}else if ("1".equals(profitStatus2)) {
					profitStatus2 = "已完成";
				}
                map.put("profitStatus2", profitStatus2);
                map.put("bankName", item.getBankName());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("sharePhone", item.getSharePhone());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo() == null ? "" : item.getOrderIdNo().replaceAll("(\\d{4})\\d{10}(\\d{3})","$1**********$2"));
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("totalBonus2", String.valueOf(item.getTotalBonus2()));
                map.put("createDate", item.getCreateDateStr());
                map.put("payDate", item.getPayDateStr());
                map.put("payDate2", item.getPayDate2Str());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("oneUserProfit2", String.valueOf(item.getOneUserProfit2() == null ? "" : item.getOneUserProfit2()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("twoUserProfit2", String.valueOf(item.getTwoUserProfit2() == null ? "" : item.getTwoUserProfit2()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("thrUserProfit2", String.valueOf(item.getThrUserProfit2() == null ? "" : item.getThrUserProfit2()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("fouUserProfit2", String.valueOf(item.getFouUserProfit2() == null ? "" : item.getFouUserProfit2()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("orgProfit2", String.valueOf(item.getOrgProfit2() == null ? "" : item.getOrgProfit2()));
//                map.put("plateProfit", String.valueOf(item.getPlateProfit() == null ? "" : item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("accountStatus2", item.getAccountStatus2());
				map.put("openProvince", item.getOpenProvince());
				map.put("openCity", item.getOpenCity());
				map.put("openRegion", item.getOpenRegion());
                data.add(map);
            }
            creditOrderaddSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgName","secondUserNode","status","profitStatus2","bankName",
                    "userCode","userName","sharePhone",
                    "orderName","orderPhone", "orderIdNo","totalBonus","totalBonus2",
                    "createDate","payDate","payDate2",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit","oneUserProfit2",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit","twoUserProfit2",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit","thrUserProfit2",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit","fouUserProfit2",
                    "orgName","orgProfit","orgProfit2","accountStatus","accountStatus2",
					"openProvince","openCity","openRegion"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","二级代理节点","订单状态","首刷分润状态","发卡银行名称",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "订单姓名","订单手机号","订单证件号","发卡品牌发放总奖金","首刷品牌发放总奖金",
                    "创建时间","发卡分润时间","首刷分润时间",
                    "一级编号","一级名称","一级身份","一级分润发卡","一级分润首刷",
                    "二级编号","二级名称","二级身份","二级分润发卡","二级分润首刷",
                    "三级编号","三级名称","三级身份","三级分润发卡","三级分润首刷",
                    "四级编号","四级名称","四级身份","四级分润发卡","四级分润首刷",
                    "品牌商名称","品牌商分润发卡","品牌商分润首刷","发卡记账状态","首刷记账状态",
					"省","市","区"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家办理信用卡订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public List<LoanSource> getLoanList() {
		return loanSourceDao.getLoanList();
	}

	@Override
	public List<InsuranceCompany> getCompanyNickNameList() {
		return insuranceCompanyDao.getCompanyNickNameList();
	}

	@Override
	public void exportLoanOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "贷款订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
            	changeType(item);
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orderType", item.getOrderType());
                map.put("orgName", String.valueOf(item.getOrgId()));
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("status", item.getStatus());
                map.put("loanName", item.getLoanName());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo() == null ? "" : item.getOrderIdNo().replaceAll("(\\d{4})\\d{10}(\\d{3})","$1**********$2"));
                map.put("loanAmount", String.valueOf(item.getLoanAmount() == null ? "" : item.getLoanAmount()));
                String profitType = item.getProfitType() == null ? "" : item.getProfitType();
                switch (profitType) {
                case "1":
                	profitType = "固定奖金";
                	break;
                case "2":
                	profitType = "按比例发放";
                	break;
				default:
					break;
				}
                map.put("profitType", profitType);
                String loanType = item.getLoanType() == null ? "" : item.getLoanType();
                switch (loanType) {
	            case "1":
	            	loanType = "有效注册";
					break;
				case "2":
					loanType = "有效借款";
					break;
				case "3":
					loanType = "授信成功";
					break;
				default:
					break;
                }
                map.put("loanType", loanType);
//                map.put("loanToBrankRate", item.getLoanToBrankRate());
//                map.put("loanToBrankTotalAmount", item.getLoanToBrankTotalAmount());
                map.put("loanOrgBonus", item.getLoanOrgBonus());
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
//                map.put("loanPushPro", item.getLoanPushPro());
//                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDateStr", item.getCreateDateStr());
                map.put("completeDateStr", item.getCompleteDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit() == null ? "" : item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            addSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orderType","orgName","secondUserNode","status","loanName",
                    "userCode","userName","sharePhone",
                    "orderName","orderPhone", "orderIdNo",
                    "loanAmount","profitType","loanType",
                    "loanOrgBonus","totalBonus",
//                    "loanPushPro", "totalBonus",
                    "createDateStr","completeDateStr",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgName","orgProfit",
//                    "plateProfit",
                    "accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","订单类型","所属组织","二级代理节点","订单状态","放款机构",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "订单姓名","订单手机号","订单证件号",
                    "贷款金额","奖金模式","奖励要求",
                    "品牌发放总奖金扣率","品牌发放总奖金",
//                    "发放比例","发放奖金",
                    "创建时间","订单完成时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润",
//                    "平台分润",
                    "记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家贷款订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public void exportInsuranceOrder(HttpServletResponse response, OrderMain order) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
			List<OrderMain> list = selectOrderPage(order, page);
			int size = 1;
			ExcelExport export = new ExcelExport(size);
			String fileName = "保险订单"+sdf.format(new Date())+export.getFileSuffix(size);
			String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			Map<String,String> map = null;
			for(OrderMain item: list){
				changeType(item);
				map = new HashMap<>();
				map.put("orderNo", item.getOrderNo());
				map.put("orgName",item.getOrgName());
				map.put("status",item.getStatus());
				map.put("productName",item.getProductName());
				if(item.getProductType()==null){
					item.setProductType("");
				}
				String productType = "";
				switch (item.getProductType()) {
					case "1":
						productType = "健康险";
						break;
					case "2":
						productType = "人寿险";
						break;
					case "3":
						productType = "车险";
						break;
					case "4":
						productType = "财产险";
						break;
					default:
						break;
				}
				String bonusSettleTime = "";
				if(item.getBonusSettleTime()==null){
					item.setBonusSettleTime("");
				}
				switch (item.getBonusSettleTime()) {
					case "1":
						bonusSettleTime = "实时";
						break;
					case "2":
						bonusSettleTime = "周结";
						break;
					case "3":
						bonusSettleTime = "月结";
						break;
					default:
						break;
				}
				map.put("productType",productType);
				map.put("companyNickName",item.getCompanyNickName());
				map.put("createDate",item.getCreateDateStr());
				map.put("price",String.valueOf(item.getPrice()==null ?"":item.getPrice()));
				map.put("bonusSettleTime",bonusSettleTime);
				map.put("userCode",item.getUserCode());
				map.put("userName",item.getUserName());
				map.put("sharePhone",item.getSharePhone());
				map.put("totalBonus",String.valueOf(item.getTotalBonus()));
				map.put("oneUserCode", item.getOneUserCode());
				map.put("oneUserName", item.getOneUserName());
				map.put("oneUserType", item.getOneUserType());
				map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
				map.put("twoUserCode", item.getTwoUserCode());
				map.put("twoUserName", item.getTwoUserName());
				map.put("twoUserType", item.getTwoUserType());
				map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
				map.put("thrUserCode", item.getThrUserCode());
				map.put("thrUserName", item.getThrUserName());
				map.put("thrUserType", item.getThrUserType());
				map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
				map.put("fouUserCode", item.getFouUserCode());
				map.put("fouUserName", item.getFouUserName());
				map.put("fouUserType", item.getFouUserType());
				map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
				map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
				map.put("accountStatus", item.getAccountStatus());
				map.put("openProvince", item.getOpenProvince());
				map.put("openCity", item.getOpenCity());
				map.put("openRegion", item.getOpenRegion());
				map.put("remark", item.getRemark());
				data.add(map);
			}

			//添加合计
		/*	OrderMainSum orderMainSum = selectOrderSum(order);
			if(orderMainSum != null){
				data.add(new HashMap<String, String>());
				Map<String ,String> sumMap = new HashMap<>();
				BigDecimal orgProfitSum = orderMainSum.getOrgProfitSum();
				sumMap.put("orgProfitSum", "品牌分润汇总:" + (orgProfitSum == null ? "0.00" : String.valueOf(orgProfitSum)));
				sumMap.put("totalItems", "销售总量:" + String.valueOf(list.size()));
				data.add(sumMap);
			}
*/
			String[] cols = new String[]{
					"orderNo", "orgName", "status", "productName",
					"productType", "companyNickName", "createDate", "price","bonusSettleTime",
					"userCode", "userName", "sharePhone", "totalBonus",
					"oneUserCode", "oneUserName", "oneUserType", "oneUserProfit",
					"twoUserCode", "twoUserName", "twoUserType", "twoUserProfit",
					"thrUserCode", "thrUserName", "thrUserType", "thrUserProfit",
					"fouUserCode", "fouUserName", "fouUserType", "fouUserProfit",
					"orgName", "orgProfit", "accountStatus",
					"openProvince", "openCity", "openRegion", "remark"};
			String[] colsName = new String[]{
					"订单ID","所属组织","订单状态","产品名称",
					"保险种类","保险公司别称","订单创建时间", "保单金额","奖金结算时间",
					"贡献人ID","贡献人名称", "贡献人手机号", "品牌发放总奖金",
					"一级编号","一级名称","一级身份","一级分润",
					"二级编号","二级名称","二级身份","二级分润",
					"三级编号","三级名称","三级身份","三级分润",
					"四级编号","四级名称","四级身份","四级分润",
					"品牌商名称","品牌商分润", "记账状态",
					"省","市","区","备注"};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出超级银行家保险订单异常", e);
		} finally {
			try {
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
     * 添加合计
     * @param order
     * @param data
     */
    private void addSumMap(OrderMain order, List<Map<String, String>> data) {
        OrderMainSum orderMainSum = selectOrderSum(order);
        if(orderMainSum != null){
            data.add(new HashMap<String, String>());
            Map<String ,String> sumMap = new HashMap<>();
            BigDecimal totalBonusSum = orderMainSum.getTotalBonusSum();
            BigDecimal orgProfitSum = orderMainSum.getOrgProfitSum();
            sumMap.put("orderNo", "奖励总金额:" + (totalBonusSum == null ? "0.00" : String.valueOf(totalBonusSum)));
            sumMap.put("secondUserNode", "品牌分润汇总:" + (orgProfitSum == null ? "0.00" : String.valueOf(orgProfitSum)));
            data.add(sumMap);
        }
    }


    /**
     * 办理信用卡订单导出汇总
     * @param order
     * @param data
     */
    private void creditOrderaddSumMap(OrderMain order, List<Map<String, String>> data) {
    	OrderMainSum orderMainSum = selectOrderSum(order);
    	if(orderMainSum != null){
    		data.add(new HashMap<String, String>());
    		Map<String ,String> sumMap = new HashMap<>();
    		BigDecimal orgProfitSum = orderMainSum.getOrgProfitSum();
    		BigDecimal orgProfitSum2 = orderMainSum.getOrgProfitSum2();
    		sumMap.put("orderNo", "发卡品牌分润汇总:" + (orgProfitSum == null ? "0.00" : String.valueOf(orgProfitSum)));
    		sumMap.put("secondUserNode", "首刷品牌分润汇总:" + (orgProfitSum2 == null ? "0.00" : String.valueOf(orgProfitSum2)));
    		data.add(sumMap);
    	}
    }
    /**
     * 还款订单导出汇总
     * @param order
     * @param data
     */
    private void addSumRepayMap(OrderMain order, List<Map<String, String>> data) {
    	OrderMainSum orderMainSum = selectOrderSum(order);
    	if(orderMainSum != null){
    		data.add(new HashMap<String, String>());
    		Map<String ,String> sumMap = new HashMap<>();
    		BigDecimal receiveAmountSum = orderMainSum.getReceiveAmountSum();
    		BigDecimal repaymentAmountSum = orderMainSum.getRepaymentAmountSum();
    		BigDecimal orgProfitSum = orderMainSum.getOrgProfitSum();
    		sumMap.put("orderNo", "目标还款金额汇总:" + (receiveAmountSum == null ? "0.00" : String.valueOf(receiveAmountSum)));
    		sumMap.put("secondUserNode", "实际还款金额汇总:" + (repaymentAmountSum == null ? "0.00" : String.valueOf(repaymentAmountSum)));
    		sumMap.put("userCode", "品牌分润汇总:" + (orgProfitSum == null ? "0.00" : String.valueOf(orgProfitSum)));
    		data.add(sumMap);
    	}
    }

	@Override
	public void exportReceiveOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "收款订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgName", item.getOrgName());
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("status", item.getStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("sharePhone", item.getSharePhone());
                map.put("orderName", item.getOrderName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("orderIdNo", item.getOrderIdNo() == null ? "" : item.getOrderIdNo().replaceAll("(\\d{4})\\d{10}(\\d{3})","$1**********$2"));
                map.put("receiveAgentId", item.getReceiveAgentId());
                map.put("receiveAmount", String.valueOf(item.getReceiveAmount()));
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("payMethod", item.getPayMethod());
                map.put("transRate", item.getTransRate());
                map.put("loanOrgRate", item.getLoanOrgRate());
                map.put("loanOrgBonus", item.getLoanOrgBonus());
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDateStr", item.getCreateDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit() == null ? "" : item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            addSumMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgName","secondUserNode","status",
                    "userCode","userName","sharePhone",
                    "orderName","orderPhone", "orderIdNo",
                    "receiveAgentId","receiveAmount","payOrderNo",
                    "payMethod","transRate","loanOrgRate","loanOrgBonus",
                    "totalBonus","createDateStr",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgName","orgProfit","plateProfit","accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","二级代理节点","订单状态",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "订单姓名","订单手机号","订单证件号",
                    "收款商户ID","收款金额","关联订单号",
                    "支付方式","商户费率","品牌代理成本扣率","品牌发放总奖金扣率",
                    "品牌发放总奖金","创建时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润","品牌代理成本","记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家收款订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
	@Override
    public void exportRepayOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "还款订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgName", String.valueOf(item.getOrgId()));
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("status", item.getStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("sharePhone", item.getSharePhone());
//                map.put("orderName", item.getOrderName());
//                map.put("orderIdNo", item.getOrderIdNo() == null ? "" : item.getOrderIdNo().replaceAll("(\\d{4})\\d{10}(\\d{3})","$1**********$2"));
                map.put("repaymentAgentId", item.getRepaymentAgentId());
                map.put("payOrderNo", item.getPayOrderNo());
                map.put("repayTransStatus", item.getRepayTransStatus());
                map.put("receiveAmount", String.valueOf(item.getReceiveAmount()));
                map.put("repaymentAmount", String.valueOf(item.getRepaymentAmount()));
                map.put("transRate", String.valueOf(item.getTransRate() == null ? "" : item.getTransRate()));
                map.put("repayTransfee", String.valueOf(item.getRepayTransfee() == null ? "" : item.getRepayTransfee()));
                map.put("loanOrgRate", String.valueOf(item.getLoanOrgRate() == null ? "" : item.getLoanOrgRate()));
                map.put("loanOrgBonus", String.valueOf(item.getLoanOrgBonus() == null ? "" : item.getLoanOrgBonus()));
                map.put("totalBonus", String.valueOf(item.getTotalBonus() == null ? "" : item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgName", item.getOrgName());
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
//                map.put("plateProfit", String.valueOf(item.getPlateProfit() == null ? "" : item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                map.put("openProvince", item.getOpenProvince());
                map.put("openCity", item.getOpenCity());
                map.put("openRegion", item.getOpenRegion());
                map.put("remark", item.getRemark());
                data.add(map);
            }
            addSumRepayMap(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgName","secondUserNode","status",
                    "userCode","userName","sharePhone",
//                    "orderName", "orderIdNo",
                    "repaymentAgentId","payOrderNo","repayTransStatus","receiveAmount","repaymentAmount","transRate","repayTransfee",
                    "loanOrgRate","loanOrgBonus",
                    "totalBonus","createDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgName","orgProfit",
//                    "plateProfit",
                    "accountStatus",
                    "openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","二级代理节点","订单状态",
                    "贡献人ID","贡献人名称","贡献人手机号",
//                    "订单姓名","订单证件号",
                    "还款商户ID","关联还款订单号","还款状态","目标还款金额","实际还款金额","用户还款费率","还款手续费",
                    "品牌还款代理费率","品牌发放总奖金扣率","品牌发放总奖金","创建时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商名称","品牌商分润",
//                    "平台分润",
                    "记账状态",
                    "省","市","区","备注"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家还款订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
    @Override
    public void exportProfitDetail(HttpServletResponse response, UserProfit order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<UserProfit> page = new Page<>(0, Integer.MAX_VALUE);
            List<UserProfit> list = selectProfitDetailPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "订单分润详情"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(UserProfit item: list){
                map = new HashMap<>();
                map.put("id", String.valueOf(item.getId()));
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("orgName", item.getOrgName());
                map.put("orderType", item.getOrderType());
                map.put("status", item.getStatus());
                map.put("orderNo", item.getOrderNo());
                map.put("userCode", item.getUserCode());
                map.put("shareNiceName", item.getShareNickName());
                map.put("userName", item.getUserName());
                map.put("shareUserPhone", item.getShareUserPhone());
                map.put("totalProfit", String.valueOf(item.getTotalProfit() == null ? "" : item.getTotalProfit()));
                map.put("userName", item.getUserName());
                map.put("userCode", item.getUserCode());
                map.put("userType", item.getUserType());
                map.put("userProfit", String.valueOf(item.getUserProfit() == null ? "" : item.getUserProfit()));
                map.put("profitLevel", String.valueOf(item.getProfitLevel() == null ? "" : item.getProfitLevel()));
                map.put("createDate", item.getCreateDateStr());
                map.put("accountStatus", item.getAccountStatus());
                data.add(map);
            }
            //添加合计
            OrderMainSum orderMainSum = selectProfitDetailSum(order);
            Map<String,String> sumMap = new HashMap<>();
            sumMap.put("id", "奖励总金额：" + orderMainSum.getTotalBonusSum() + "元");
            sumMap.put("orgId", "收益汇总：" + orderMainSum.getProfitSum() + "元");
            data.add(sumMap);
            String[] cols = new String[]{
                    "id","orgId","secondUserNode","orgName","orderType",
                    "status","orderNo",
                    "userCode","shareNiceName","userName","shareUserPhone",
                    "totalProfit",
                    "userName","userCode", "userType","profitLevel","userProfit",
                    "createDate","accountStatus"};
            String[] colsName = new String[]{
                    "分润明细ID","所属组织","二级代理节点","品牌商名称","订单类型",
                    "订单状态","订单编号",
                    "贡献人ID","贡献人昵称","贡献人名称","贡献人手机号",
                    "总奖金",
                    "收益人姓名","收益人ID","收益人身份","当前分润层级","收益人分润",
                    "创建时间","记账状态"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家分润明细异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
    @Override
    public void exportOpenCredit(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "开通办理信用卡订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();
                map.put("orderNo", item.getOrderNo());
                map.put("orgId", String.valueOf(item.getOrgId()));
                map.put("secondUserNode", item.getSecondUserNode());
                map.put("orgName", String.valueOf(item.getOrgId()));
                map.put("status", item.getStatus());
                map.put("userCode", item.getUserCode());
                map.put("userName", item.getUserName());
                map.put("orderPhone", item.getOrderPhone());
                map.put("price", String.valueOf(item.getPrice()));
                map.put("totalBonus", String.valueOf(item.getTotalBonus()));
                map.put("createDate", item.getCreateDateStr());
                map.put("payDate", item.getPayDateStr());
                map.put("oneUserCode", item.getOneUserCode());
                map.put("oneUserName", item.getOneUserName());
                map.put("oneUserType", item.getOneUserType());
                map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));
                map.put("twoUserCode", item.getTwoUserCode());
                map.put("twoUserName", item.getTwoUserName());
                map.put("twoUserType", item.getTwoUserType());
                map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));
                map.put("thrUserCode", item.getThrUserCode());
                map.put("thrUserName", item.getThrUserName());
                map.put("thrUserType", item.getThrUserType());
                map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));
                map.put("fouUserCode", item.getFouUserCode());
                map.put("fouUserName", item.getFouUserName());
                map.put("fouUserType", item.getFouUserType());
                map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
                map.put("plateProfit", String.valueOf(item.getPlateProfit() == null ? "" : item.getPlateProfit()));
                map.put("accountStatus", item.getAccountStatus());
                data.add(map);
            }
            addOrderSumMap2(order, data);//添加合计
            String[] cols = new String[]{
                    "orderNo","orgId","secondUserNode","orgName","status",
                    "userCode","userName","orderPhone",
                    "price","totalBonus",
                    "createDate","payDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName", "twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit","plateProfit","accountStatus"};
            String[] colsName = new String[]{
                    "订单ID","所属组织","二级代理节点","组织名称","订单状态",
                    "贡献人ID","贡献人名称","贡献人手机号",
                    "售价","品牌发放总奖金",
                    "创建时间","支付时间","关联支付订单",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "品牌商分润","平台分润","记账状态"};
            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家办理信用卡订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void exportSuperExchangeOrder(HttpServletResponse response, OrderMain order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        OutputStream ouputStream = null;
        try {
            Page<OrderMain> page = new Page<>(0, Integer.MAX_VALUE);
            List<OrderMain> list = selectOrderPage(order, page);
            int size = 1;
            ExcelExport export = new ExcelExport(size);
            String fileName = "积分兑换订单"+sdf.format(new Date())+export.getFileSuffix(size);
            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
            Map<String,String> map = null;
            for(OrderMain item: list){
                map = new HashMap<>();

				map.put("orderNo", item.getOrderNo());			//订单ID
				map.put("orgId", String.valueOf(item.getOrgId()));			//所属组织
				map.put("orgName", item.getOrgName());			//组织名称
				map.put("status", item.getStatus());			//订单状态
				map.put("bankName", item.getBankName());		//兑换机构别称
				map.put("orderName", item.getOrderName());		//兑换产品名称
				map.put("loanAmount", String.valueOf(item.getLoanAmount()));		//兑换积分数
				map.put("price", String.valueOf(item.getPrice()));			//兑换价格
				map.put("payOrderNo", item.getPayOrderNo());		//关联订单号
				map.put("createDate", item.getCreateDateStr());		//订单创建时间
				map.put("userCode", item.getUserCode());		//贡献人ID
				map.put("userName", item.getUserName());		//贡献人名称
				map.put("sharePhone", item.getSharePhone());	//贡献人手机号
				map.put("totalBonus", String.valueOf(item.getTotalBonus()));		//品牌发放总奖金
				map.put("oneUserCode", item.getOneUserCode());		//一级编号
				map.put("oneUserName", item.getOneUserName());		//一级名称
				map.put("oneUserType", item.getOneUserType());		//一级身份
				map.put("oneUserProfit", String.valueOf(item.getOneUserProfit() == null ? "" : item.getOneUserProfit()));	//一级分润
				map.put("twoUserCode", item.getTwoUserCode());		//二级编号
				map.put("twoUserName", item.getTwoUserName());		//二级名称
				map.put("twoUserType", item.getTwoUserType());		//二级身份
				map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit() == null ? "" : item.getTwoUserProfit()));	//二级分润
				map.put("thrUserCode", item.getThrUserCode());		//三级编号
				map.put("thrUserName", item.getThrUserName());		//三级名称
				map.put("thrUserType", item.getThrUserType());		//三级身份
				map.put("thrUserProfit", String.valueOf(item.getThrUserProfit() == null ? "" : item.getThrUserProfit()));	//三级分润
				map.put("fouUserCode", item.getFouUserCode());		//四级编号
				map.put("fouUserName", item.getFouUserName());		//四级名称
				map.put("fouUserType", item.getFouUserType());		//四级身份
				map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));	//四级分润
				map.put("orgName",item.getOrgName());			//品牌商名称
				map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));		//品牌商分润
				map.put("accountStatus", item.getAccountStatus());	//记账状态
				map.put("openProvince", item.getOpenProvince());	//省
				map.put("openCity", item.getOpenCity());		//市
				map.put("openRegion", item.getOpenRegion());		//区
				map.put("remark", item.getRemark());			//备注
                data.add(map);
            }
            String[] cols = new String[]{
					"orderNo", "orgId", "orgName", "status",
					"bankName", "orderName", "loanAmount", "price",
					"payOrderNo", "createDate", "userCode", "userName",
					"sharePhone", "totalBonus", "oneUserCode", "oneUserName",
					"oneUserType", "oneUserProfit", "twoUserCode", "twoUserName",
					"twoUserType" , "twoUserProfit", "thrUserCode", "thrUserName",
					"thrUserType", "thrUserProfit", "fouUserCode", "fouUserName",
					"fouUserType", "fouUserProfit", "orgName", "orgProfit",
					"accountStatus", "openProvince", "openCity", "openRegion", "remark"};


            String[] colsName = new String[]{
					"订单ID", "所属组织", "组织名称", "订单状态",
					"兑换机构别称", "兑换产品名称", "兑换积分数", "兑换价格",
					"关联订单号", "订单创建时间", "贡献人ID", "贡献人名称",
					"贡献人手机号", "品牌发放总奖金", "一级编号", "一级名称",
					"一级身份", "一级分润", "二级编号", "二级名称", "二级身份",
					"二级分润", "三级编号", "三级名称", "三级身份", "三级分润",
					"四级编号", "四级名称", "四级身份", "四级分润", "品牌商名称",
					"品牌商分润", "记账状态", "省", "市", "区", "备注"};


            ouputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出超级银行家积分兑换订单异常", e);
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
    public List<UserProfit> selectProfitDetailPage(UserProfit baseInfo, Page<UserProfit> page) throws UnsupportedEncodingException {
		String entityId = seconeUserNode(baseInfo);
		baseInfo.setEntityId(entityId);
        userProfitDao.selectProfitDetailPage(baseInfo, page);
        List<UserProfit> list = page.getResult();
        filterProfitDetailParam(list);
        return list;
    }

	private String seconeUserNode(UserProfit baseInfo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		//当前用户登录用户的二级节点为空且是OEM默认用户,查询条件添加当前用户的二级代理节点(设置成当前登录用户的二级代理节点)
		String secondUserNode = principal.getSecondUserNode();
		//如果是一级代理商就直接根据页面传过来的值作为条件
		if (agentInfo.getAgentLevel() == 1 && "0".equals(principal.getUserEntityInfo().getIsAgent()) && StringUtils.isNotBlank(secondUserNode)) {
//			String telNo = principal.getTelNo();
//			String isoem = userInfoSuperBankDao.isOem(telNo,secondUserNode);
//			if ("1".equals(isoem)) {
				if (StringUtils.isBlank(baseInfo.getSecondUserNode())) {
					baseInfo.setSecondUserNode(secondUserNode);
					baseInfo.setOEM("50");//用户不能看OEM的分润
				}
//			}
		}
		return entityId;
	}
	
	@Override
    public OrderMainSum selectProfitDetailSum(UserProfit baseInfo) {
		String entityId = seconeUserNode(baseInfo);
		baseInfo.setEntityId(entityId);
        OrderMainSum orderMainSum = new OrderMainSum();
        OrderMainSum totalSum = userProfitDao.selectTotalProfitSum(baseInfo);//奖励总金额
        OrderMainSum userSum = userProfitDao.selectUserProfitSum(baseInfo);//收益汇总
        orderMainSum.setTotalBonusSum(totalSum != null ? totalSum.getTotalBonusSum() : new BigDecimal(0));
        orderMainSum.setProfitSum(userSum != null ? userSum.getProfitSum() : new BigDecimal(0));
        return orderMainSum;
    }

    @Override
    public List<CreditcardSource> banksList() {
        List<CreditcardSource>  list = creditcardSourceDao.allBanksList();
        return list;
    }

    /**
     * 过滤分润详情列表里面的数据
     * @param list
     * @throws UnsupportedEncodingException 
     */
    private void filterProfitDetailParam(List<UserProfit> list) throws UnsupportedEncodingException {
        Map<String, String> orderStatusMap = getOrderStatusMap();
        Map<String, String> orderTypeMap = getOrderTypeMap();
        Map<String, String> accountStatusMap = getAccountStatusMap();
        Map<Long, String> orgNameMap = getOrgNameMap();
        Map<String, String> userTypeMap = getUserTypeMap();
        if(list != null && list.size() > 0){
            for(UserProfit order: list){
                order.setStatus(orderStatusMap.get(order.getStatus()));
                order.setOrderType(orderTypeMap.get(order.getOrderType()));
                order.setAccountStatus(accountStatusMap.get(order.getAccountStatus()));
                order.setOrgName(orgNameMap.get(order.getOrgId()));
                if("50".equals(order.getUserType())){
                    order.setUserName(order.getOrgName());
                }
                if("60".equals(order.getUserType())){
                    order.setUserName("默认");
                }
                order.setUserType(userTypeMap.get(order.getUserType()));
                if(StringUtils.isNotBlank(order.getShareNickName())){
                   order.setShareNickName(URLDecoder.decode(order.getShareNickName(), "utf-8"));
                }
            }
        }
    }
    /**
     * 订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款 5贷款
      * @return
     */
    public Map<String, String> getOrderTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("1", "代理授权");
        map.put("2", "信用卡申请");
        map.put("3", "收款");
        map.put("4", "信用卡还款");
        map.put("5", "贷款注册");
        map.put("6", "贷款批贷");
        map.put("7", "还款");
        map.put("8", "彩票代购");
		map.put("101", "购机补贴");
		map.put("102", "Mpos激活奖励");
		map.put("103", "Mpos激活返现");
		map.put("104", "Mpos交易");
        return map;
    }
    /**
     * 将所有orgInfo的orgId和orgName组成map
     * @return
     */
    public Map<Long, String> getOrgNameMap(){
        Map<Long, String> map = new HashMap<>();
        List<OrgInfo> list = orgInfoDao.getOrgInfoList();
        if(list != null && list.size() > 0){
            for(OrgInfo info: list){
                map.put(
                        info.getOrgId() == null? -1L : info.getOrgId(),
                        info.getOrgName() == null?"" : info.getOrgName());
            }
        }
        return map;
    }
    
    public List<OrgInfo> selectOrgInfoPage(OrgInfo baseInfo, Page<OrgInfo> page) {
        List<OrgInfo> list = orgInfoDao.selectOrgInfoPage(baseInfo, page);
        List<OrgInfo> pageList = page.getResult();
        if(pageList != null && pageList.size() > 0){
            String imgUrl = "";
            for (OrgInfo item: pageList){
                imgUrl = getImgUrl(item.getOrgLogo());
                item.setOrgLogoUrl(imgUrl);
            }
        }
        return list;
    }

    /**
     * 将图片的地址转换为阿里云的地址
     * @param imgUrl
     * @return
     */
    public String getImgUrl(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 60*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_TEMP_TUCKET, imgUrl, date);
        return imgUrl;
    }
    @Override
	public List<LoanBonusConf> getLoanBonusConfList(LoanBonusConf conf,Page<LoanBonusConf> page) {
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	conf.setEntityId(principal.getUserEntityInfo().getEntityId());
		return loanCompanyDao.getLoanBonusConf(conf, page);
	}
    @Override
    public Result orgInfoDetail() {
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Result result = new Result();
        String orgId = principal.getUserEntityInfo().getEntityId();
        OrgInfo orgInfo = orgInfoDao.selectDetail(Long.valueOf(orgId));
        if(orgInfo != null){
            String orgLogo = CommonUtil.getImgUrlAgent(orgInfo.getOrgLogo());
            String sealUrl = CommonUtil.getImgUrlAgent(orgInfo.getAuthorizedUnitSeal());
            String memberCenterUrl = CommonUtil.getImgUrlAgent(orgInfo.getMemberCenterLogo());
            String startPageUrl = CommonUtil.getImgUrlAgent(orgInfo.getStartPage());
            String appLogoUrl = CommonUtil.getImgUrlAgent(orgInfo.getAppLogo());
            String publicQrUrl = CommonUtil.getImgUrlAgent(orgInfo.getPublicQrCode());
            String shareMessageUrl = CommonUtil.getImgUrlAgent(orgInfo.getShareMessageLogo());
            String shareTempalte1Url = CommonUtil.getImgUrlAgent(orgInfo.getShareTemplateImage1());
            String shareTempalte2Url = CommonUtil.getImgUrlAgent(orgInfo.getShareTemplateImage2());
            String shareTempalte3Url = CommonUtil.getImgUrlAgent(orgInfo.getShareTemplateImage3());
            changeUiStatus(orgInfo);
            orgInfo.setOrgLogoUrl(orgLogo);
            orgInfo.setAuthorizedUnitSealUrl(sealUrl);
            orgInfo.setMemberCenterLogoUrl(memberCenterUrl);
            orgInfo.setStartPageUrl(startPageUrl);
            orgInfo.setAppLogoUrl(appLogoUrl);
            orgInfo.setPublicQrCodeUrl(publicQrUrl);
            orgInfo.setShareMessageLogoUrl(shareMessageUrl);
            orgInfo.setShareTemplateImage1Url(shareTempalte1Url);
            orgInfo.setShareTemplateImage2Url(shareTempalte2Url);
            orgInfo.setShareTemplateImage3Url(shareTempalte3Url);
            if(orgInfo.getTradeFeeRate() != null){
                orgInfo.setTradeFeeRateStr(orgInfo.getTradeFeeRate() + "%");
            }
            if(orgInfo.getTradeSingleFee() != null){
                orgInfo.setTradeSingleFeeStr(orgInfo.getTradeSingleFee() + "元");
            }
            if(orgInfo.getWithdrawFeeRate() != null){
                orgInfo.setWithdrawFeeRateStr(orgInfo.getWithdrawFeeRate() + "%");
            }
            if(orgInfo.getWithdrawSingleFee() != null){
                orgInfo.setWithdrawSingleFeeStr(orgInfo.getWithdrawSingleFee() + "元");
            }
            String receiveKjCost = orgInfo.getReceiveKjCost();
            if (receiveKjCost != null && !receiveKjCost.contains("%")) {
				orgInfo.setReceiveKjCost(receiveKjCost + "%");
			}
            String receivePushCost = orgInfo.getReceivePushCost();
            if (receivePushCost != null && !receivePushCost.contains("%")) {
            	orgInfo.setReceivePushCost(receivePushCost + "%");
            }
            String repaymentCost = orgInfo.getRepaymentCost();
            if (repaymentCost != null && !repaymentCost.contains("%")) {
            	orgInfo.setRepaymentCost(repaymentCost + "%");
            }
            String repaymentPushCost = orgInfo.getRepaymentPushCost();
            if (repaymentPushCost != null && !repaymentPushCost.contains("%")) {
            	orgInfo.setRepaymentPushCost(repaymentPushCost + "%");
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(orgInfo);
        } else {
            result.setMsg(orgId + "找不到对应的超级银行家组织");
        }
        return result;
    }

	private void changeUiStatus(OrgInfo orgInfo) {
		if (orgInfo.getUiStatus() != null) {
			switch (orgInfo.getUiStatus()) {
			case "0":
				orgInfo.setUiStatus("0超级银行家(自用)");
				break;
			case "1":
				orgInfo.setUiStatus("金色");
				break;
			case "2":
				orgInfo.setUiStatus("橙色");
				break;
			case "3":
				orgInfo.setUiStatus("黄色");
				break;
			default:
				orgInfo.setUiStatus("");
				break;
			}
		}
	}
    
    @Override
	public Result getCreditBonusConf(CreditCardBonus creditCardconf,Page<CreditCardBonus> page) {
		Result result = new Result();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		creditCardconf.setEntityId(principal.getUserEntityInfo().getEntityId());
		creditCardBonusDao.getCreditCardConf(creditCardconf, page);
		result.setData(page);
		return result;
	}
    
    @Override
    public Result selectDevelopmentConfiguration() {
    	Result result = new Result();
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String entityId = principal.getUserEntityInfo().getEntityId();
    	OrgInfo orgInfo = orgInfoDao.selectDetail(Long.valueOf(entityId));//1446有测试数据
    	if (orgInfo != null) {
    		List<OrgInfoOpenConf> list = orgInfoDao.selectDevelopmentConfiguration(entityId);
    		List<OrgInfoOpenConf> infoList = orgInfoDao.selectFunctionUrl();
    		for (OrgInfoOpenConf conf : list) {
    			String functionId = conf.getFunctionId().toString();
    			for (OrgInfoOpenConf orgInfoOpenConf : infoList) {
    				String functionIdConf = orgInfoOpenConf.getId().toString();
    				if (functionId.equals(functionIdConf)) {
    					switch (functionId) {
    					case "1"://首页
    						orgInfo.setFirstPage(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnableFirstPage(Integer.valueOf(conf.getIsEnable()));
    						break;
    					case "2"://办理信用卡
    						orgInfo.setCreditCard(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnableCreditCard(Integer.valueOf(conf.getIsEnable()));
    						break;
    					case "3"://贷款
    						orgInfo.setHandleLoan(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnable4(Integer.valueOf(conf.getIsEnable()));
    						break;
    					case "4"://订单管理,订单查询
    						orgInfo.setQueryOrder(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnableQueryOrder(Integer.valueOf(conf.getIsEnable()));
    						break;
//    					case "5":
//    						orgInfo.setPeccancy(orgInfoOpenConf.getFunctionUrl());
//    						break;
//    					case "6":
//    						orgInfo.setRepayCard(orgInfoOpenConf.getFunctionUrl());
//    						orgInfo.setIsEnable6(Integer.valueOf(conf.getIsEnable()));
//    						break;
    					case "7"://商户收款
    						orgInfo.setMerchantReceivables(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnable8(Integer.valueOf(conf.getIsEnable()));
    						break;
    					case "8"://个人信息
    						orgInfo.setPersonalInfomation(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnablePersonalInfomation(Integer.valueOf(conf.getIsEnable()));
    						break;
    					case "9"://办卡查询
    						orgInfo.setQueryProgress(orgInfoOpenConf.getFunctionUrl());
    						orgInfo.setIsEnableQueryProgress(Integer.valueOf(conf.getIsEnable()));
    						break;
    					default:
    						break;
    					}
					}
				}
			}
    		result.setData(orgInfo);
    		result.setStatus(true);
		}
    	return result;
    }

	@Override
	public UserInfoSuperBank selectTotal(UserInfoSuperBank userInfoSuperBank) {
		UserInfoSuperBank superBankNum = new UserInfoSuperBank();
		String entityId = secondUserNodeConditions(userInfoSuperBank);
		List<Map<String, Object>> list = userInfoSuperBankDao.selectTotal(userInfoSuperBank,entityId);
		for (Map<String, Object> map : list) {
			Integer num = Integer.valueOf(map.get("num").toString());
			switch (map.get("userType").toString()) {
			case "10":
				superBankNum.setOrdinaryUserNum(num);
				break;
			case "20":
				superBankNum.setCommissionerUserNum(num);
				break;
			case "30":
				superBankNum.setManagerUserNum(num);
				break;
			case "40":
				superBankNum.setBankerUserNum(num);
				break;
			default:
				break;
			}
		}
		return superBankNum;
	}

	@Override
	public int updateOpenTwoAgent(UserInfoSuperBank userInfoSuperBank) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo agentInfo = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
		String phone = userInfoSuperBank.getPhone();
		String secondUserNode = userInfoSuperBank.getSecondUserNode();
		Long orgId = userInfoSuperBank.getOrgId();
		UserInfo userInfo = userInfoDao.selectUserInfo(phone,orgId);
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		String password = passEnc.encodePassword(sysDictDao.SelectServiceId("INITIAL_PWD"), phone);
		int i = 0;
		UserInfo secondUserNodeInfo = userInfoDao.selectBySecondUserNode(secondUserNode);
		if (secondUserNodeInfo == null) {
			if (userInfo != null) {
				//更新 secondUserNode
				log.info("找到数据,执行更新二级代理节点操作");
				i = userInfoDao.updateSecondUserNode(userInfo.getUserId(),secondUserNode);
				i += 2;
			}else{
				//新增用户
				UserInfo info = new UserInfo();
				info.setMobilephone(phone);
				info.setPassword(password);
				info.setSecondUserNode(secondUserNode);
//				info.setTeamId(Constants.TEAM_ID);
				info.setTeamId(agentInfo.getTeamId().toString());
				info.setUserName(userInfoSuperBank.getUserName());
				info.setStatus("1");//有效状态
				info.setUserId(String.format("1%018d", userInfoDao.selectUserNoSeq()));
				info.setCreateTime(new Date());
				info.setUpdatePwdTime(new Date());
				i += userInfoDao.insert(info);
				log.info("保存user_info表 i = " + i);
				
				//保存 entity 表
				UserLoginInfo Principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				UserEntityInfo userEntityInfo = new UserEntityInfo();
				userEntityInfo.setUserId(info.getUserId());
				userEntityInfo.setUserType("1");
				userEntityInfo.setApply("1");
				userEntityInfo.setManage("1");
				userEntityInfo.setStatus("1");
				userEntityInfo.setIsAgent("0");
				userEntityInfo.setEntityId(Principal.getUserEntityInfo().getEntityId());
				i += userEntityInfoDao.insertUserEntity(userEntityInfo);
				log.info("保存entity表 i += " + i);
				
				//保存角色
				UserEntityInfo ue = userEntityInfoDao.findAgentUserEntityInfoByUserId(info.getUserId());
				i += userRoleDao.insertUserRole(ue.getId(), userInfoDao.selectSuperBankRole());
				log.info("保存角色表 i += " + i);
			}
		}else{
			//如果存在,就修改手机号,密码
			log.info("根据二级代理节点查到数据,修改手机号和密码");
			i = userInfoDao.updatePhoneBySecondUserNode(secondUserNode,phone,password);
			i += 2;
		}
		return i;
	}
	/**
     * 添加合计
     * @param order
     * @param data
     */
    private void addOrderSumMap2(OrderMain order, List<Map<String, String>> data) {
        OrderMainSum orderMainSum = selectOrderSum(order);
        if(orderMainSum != null){
            data.add(new HashMap<String, String>());
            Map<String ,String> sumMap = new HashMap<>();
            sumMap.put("orgId", "平台分润汇总:" + String.valueOf(orderMainSum.getPlateProfitSum()));
            sumMap.put("status", "品牌分润汇总:" + String.valueOf(orderMainSum.getOrgProfitSum()));
            data.add(sumMap);
        }
    }
    
    public void exportLotteryOrder(HttpServletResponse response, LotteryOrder baseInfo) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
	    OutputStream ouputStream = null;
	        try {
	            Page<LotteryOrder> page = new Page<>(0, Integer.MAX_VALUE);
	            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    		String orgId = principal.getUserEntityInfo().getEntityId();
	        	
	    		baseInfo.setOrgId(orgId);//设置只能查自己所属组织的订单
	            List<LotteryOrder> list = lotteryOrderDao.selectLotteryOrder(baseInfo, page);
	            
	            ListDataExcelExport export = new ListDataExcelExport();
	            
	            String fileName = "彩票代购订单"+sdf.format(new Date())+"."+export.getFileSuffix();
	            String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
	            response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
	            response.setContentType("application/vnd.ms-excel;charset=utf-8");
	            List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
	            Map<String,String> map = null;
	            for(LotteryOrder item: list){
	                map = new HashMap<>();
	                formatData(item);
	                map.put("orderNo",item.getOrderNo());
	                map.put("deviceJno",item.getDeviceJno());
	                map.put("orgId",item.getOrgId());
	                map.put("orgName",item.getOrgName());
	                map.put("orderStatus",item.getOrderStatus());
	                map.put("userCode",item.getUserCode());
	                map.put("userName",item.getUserName());
	                map.put("phone",item.getPhone());
	                map.put("price",item.getPrice());
	                map.put("profitType",item.getProfitType());
	                map.put("awardRequire",item.getAwardRequire());
	                map.put("loanBankRate",item.getLoanBankRate());
	                map.put("totalBonus", item.getTotalBonus());
	                map.put("loanOrgRate",item.getLoanOrgRate());
	                map.put("loanOrgBonus",item.getLoanOrgBonus());
	                map.put("betTime",item.getBetTime());
	                map.put("lotteryType",item.getLotteryType());
	                map.put("issue",item.getIssue());
	                map.put("redeemTime",item.getRedeemTime());
	                map.put("awardAmount",item.getAwardAmount());
	                map.put("isBigPrize",item.getIsBigPrize());
	                map.put("redeemFlag",item.getRedeemFlag());
	                map.put("oneCode",item.getOneCode());
	                map.put("oneName",item.getOneName());
	                map.put("oneRole",item.getOneRole());
	                map.put("oneProfit",item.getOneProfit());
	                map.put("twoCode", item.getTwoCode());
	                map.put("twoName",item.getTwoName());
	                map.put("twoRole",item.getTwoRole());
	                map.put("twoProfit",item.getTwoProfit());
	                map.put("threeCode",item.getThreeCode());
	                map.put("threeName",item.getThreeName());
	                map.put("threeRole",item.getThreeRole());
	                map.put("threeProfit",item.getThreeProfit());
	                map.put("fourCode",item.getFourCode());
	                map.put("fourName",item.getFourName());
	                map.put("fourRole",item.getFourRole());
	                map.put("fourProfit",item.getFourProfit());
	                map.put("orgName",item.getOrgName());
	                map.put("orgProfit",item.getOrgProfit());
	                //map.put("plateProfit",item.getPlateProfit());
	                map.put("accountStatus",item.getAccountStatus());
	                map.put("openProvince",item.getOpenProvince());
	                map.put("openCity",item.getOpenCity());
	                map.put("openRegion",item.getOpenRegion());
	                map.put("remark",item.getRemark());
	                map.put("loanAmount",item.getLoanAmount());
	                map.put("createDate",item.getCreateDate());
	                data.add(map);
	            }
	            //addOrderSumMap(order, data);//添加合计
	            String[] cols = new String[]{
	                    "orderNo","deviceJno","orgId","orgName","orderStatus",
	                    "userCode","userName","phone",
	                    "loanAmount","profitType","awardRequire","loanBankRate","price",
	                    "loanOrgRate","totalBonus","betTime","lotteryType","issue",
	                    "redeemTime","awardAmount",
	                    "isBigPrize",
	                    "redeemFlag","oneCode","oneName","oneRole",
	                    "oneProfit","twoCode", "twoName","twoRole",
	                    "twoProfit","threeCode","threeName","threeRole",
	                    "threeProfit","fourCode","fourName","fourRole","fourProfit",
	                    "orgName","orgProfit","accountStatus",
	                    "openProvince","openCity","openRegion","createDate","remark"};
	            String[] colsName = new String[]{
	            		"订单ID","投注设备流水号","所属组织","组织名称","订单状态","贡献人ID",
	            		"贡献人名称","贡献人手机号","付款金额","奖金方式","奖励要求","彩票机构给品牌扣率",
	            		"品牌奖金","品牌发放总资金扣率","品牌发放总奖金","投注时间","彩种","投注期号",
	            		"兑奖时间","中奖总金额","大奖标志","兑奖状态","一级编号","一级名称","一级身份",
	            		"一级分润","二级编号","二级名称","二级身份","二级分润","三级编号","三级名称",
	            		"三级身份","三级分润","四级编号","四级名称","四级身份","四级分润","品牌商名称",
	            		"品牌商分润","记账状态","省","市","区","创建时间","备注"};
	            ouputStream = response.getOutputStream();
	            export.export(cols, colsName, data, response.getOutputStream());
	        } catch (Exception e) {
	            log.error("导出彩票代购订单异常", e);
	        } finally {
	            try {
	                ouputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	 }

	@Override
	public LotteryOrder qrySumOrder(LotteryOrder info) {
		return lotteryOrderDao.selectOrderSum(info);
	}
    
    @Override
   	public List<LotteryOrder> qryLotteryOrder(LotteryOrder info, Page<LotteryOrder> page) {
       	
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
    	
		info.setOrgId(orgId);//设置只能查自己所属组织的订单
    	
       	lotteryOrderDao.selectLotteryOrder(info, page);
       	
       	List<LotteryOrder> list = page.getResult();
       	for (LotteryOrder order : list) {
       		formatData(order);
   		}
       	page.setResult(list);
   		return list;
   	}
    
    public void formatData(LotteryOrder order){
		if("0".equals(order.getAccountStatus())){//0待入账；1已记账；2记账失败
			order.setAccountStatus("待入账");
		}else if("1".equals(order.getAccountStatus())){
			order.setAccountStatus("已记账");
		}else if("2".equals(order.getAccountStatus())){
			order.setAccountStatus("记账失败");
		}
		
		//1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败 7已办理过  9已关闭
		if("1".equals(order.getOrderStatus())){
			order.setOrderStatus("已创建");
		}else if("5".equals(order.getOrderStatus())){
			order.setOrderStatus("已完成");
		}else if("6".equals(order.getOrderStatus())){
			order.setOrderStatus("订单失败");
		}
		
		//1-固定奖金，2-按比例发放
		if("1".equals(order.getProfitType())){
			order.setProfitType("固定奖金");
		}else if("2".equals(order.getProfitType())){
			order.setProfitType("按比例发放");
		}
		
		String normal = "普通用户";
		String twoZero 	  = "专员";
		String threeZero  = "经理";
		String fourZero = "银行家";
		
		if("10".equals(order.getOneRole())){
			order.setOneRole(normal);
		}else if("20".equals(order.getOneRole())){
			order.setOneRole(twoZero);
		}else if("30".equals(order.getOneRole())){
			order.setOneRole(threeZero);
		}else if("40".equals(order.getOneRole())){
			order.setOneRole(fourZero);
		}
		
		if("10".equals(order.getTwoRole())){
			order.setTwoRole(normal);
		}else if("20".equals(order.getTwoRole())){
			order.setTwoRole(twoZero);
		}else if("30".equals(order.getTwoRole())){
			order.setTwoRole(threeZero);
		}else if("40".equals(order.getTwoRole())){
			order.setTwoRole(fourZero);
		}
		
		if("10".equals(order.getThreeRole())){
			order.setThreeRole(normal);
		}else if("20".equals(order.getThreeRole())){
			order.setThreeRole(twoZero);
		}else if("30".equals(order.getThreeRole())){
			order.setThreeRole(threeZero);
		}else if("40".equals(order.getThreeRole())){
			order.setThreeRole(fourZero);
		}
		
		if("10".equals(order.getFourRole())){
			order.setFourRole(normal);
		}else if("20".equals(order.getFourRole())){
			order.setFourRole(twoZero);
		}else if("30".equals(order.getFourRole())){
			order.setFourRole(threeZero);
		}else if("40".equals(order.getFourRole())){
			order.setFourRole(fourZero);
		}
		
		//重新计算
		//loanBankRate
		//彩票机构给品牌扣率 = 总-公司截留配率
		//品牌奖金 = 品牌扣率 * 总
		try{
			String companyConf = order.getCompanyBonusConf();
			String totalConf = order.getLoanBankRate();
			
			if(companyConf!=null&&totalConf!=null&&companyConf.contains("%")&&totalConf.contains("%")){
				String comp = companyConf.substring(0,companyConf.indexOf("%"));//公司截留率
				String total = totalConf.substring(0,totalConf.indexOf("%"));//总率
				
				BigDecimal orgGetRate = new BigDecimal(total).subtract(new BigDecimal(comp));// = 公司发放奖金配率
				
				order.setLoanBankRate(orgGetRate.toString() + "%");//品牌收到的总奖金配率
				String amount = order.getLoanAmount();
				order.setPrice(orgGetRate.multiply(new BigDecimal(amount)).divide(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP).toString());
			}
		}catch(Exception e){
			e.printStackTrace();
			log.info("--------转换费率出错------",e);
		}
	}


	@Override
	public List<RankingRecordInfo> queryRankingRecord(RankingRecordInfo baseInfo, Page<LotteryOrder> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(orgId);	//设置只能查自己所属组织的排行榜
    	return rankingRecordDao.queryRankingRecord(baseInfo,page);
	}

	@Override
	public Map<String,Object> queryRankingRecordSum(RankingRecordInfo baseInfo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(orgId);	//设置只能查自己所属组织的排行榜
		return rankingRecordDao.queryRankingRecordSum(baseInfo);
	}

	@Override
	public List<RankingPushRecordInfo> queryRankingPushRecordPage(RankingPushRecordInfo baseInfo, Page<RankingPushRecordInfo> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(orgId);	//设置只能查自己所属组织的排行榜
    	return rankingPushRecordDao.queryRankingPushRecordPage(baseInfo,page);
	}

	@Override
	public String selectRankingPushRecordTotalMoneySum(RankingPushRecordInfo baseInfo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(orgId);	//设置只能查自己所属组织的排行榜
		return rankingPushRecordDao.selectRankingPushRecordTotalMoneySum(baseInfo);
	}

	@Override
	public String selectRankingPushRecordPushTotalMoneySum(RankingPushRecordInfo baseInfo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(orgId);	//设置只能查自己所属组织的排行榜
		baseInfo.setPushStatus("1");//状态为已发送状态
		return rankingPushRecordDao.selectRankingPushRecordTotalMoneySum(baseInfo);
	}

	@Override
	public void exportRankingPushRecord(HttpServletResponse response, RankingPushRecordInfo baseInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		OutputStream outputStream = null;
		try {
			Page<RankingPushRecordInfo> page = new Page<>(0, Integer.MAX_VALUE);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String orgId = principal.getUserEntityInfo().getEntityId();

			baseInfo.setOrgId(orgId);//设置只能查自己所属组织的订单

			List<RankingPushRecordInfo> list = queryRankingPushRecordPage(baseInfo, page);

			ListDataExcelExport export = new ListDataExcelExport();

			String fileName = "用户奖金发放记录" + sdf.format(new Date()) +"."+ export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			Map<String,String> map = null;
			for (RankingPushRecordInfo item : list) {
				map = new HashMap<>();
				String rankingType = "";
				String accountStatus = "";
				if(item.getRankingType()!=null){
					if("0".equals(item.getRankingType())){
						rankingType = "周榜";
					}else if ("1".equals(item.getRankingType())){
						rankingType = "月榜";
					}else if ("2".equals(item.getRankingType())){
						rankingType = "年榜";
					}
				}

				if(item.getAccountStatus()!=null){
					if("0".equals(item.getAccountStatus())){
						accountStatus = "未记账";
					}else if ("1".equals(item.getAccountStatus())){
						accountStatus = "已记账";
					}else if ("2".equals(item.getAccountStatus())){
						accountStatus = "记账失败";
					}
				}

				map.put("orderNo", item.getOrderNo());         //获奖订单号
				map.put("rankingNo", item.getRankingNo());       //排行榜编号
				map.put("batchNo", item.getBatchNo());         //期号
				map.put("ruleNo", item.getRuleNo());          //规则编号
				map.put("rankingName", item.getRankingName());     //排行榜名称
				map.put("rankingType", rankingType);     //排行榜类型
				map.put("orgId", item.getOrgName());           //所属组织
				map.put("showNo", item.getShowNo());          //排名
				map.put("userName", item.getUserName());        //用户姓名
				map.put("nickName", org.apache.commons.lang3.StringUtils.isNotEmpty(item.getNickName())?URLDecoder.decode(item.getNickName(), "utf-8"):"");        //微信昵称
				map.put("userCode", item.getUserCode());        //用户ID
				map.put("phone", item.getPhone());           //手机号
				map.put("rankingData", item.getRankingData());     //统计总额
				map.put("rankingLevel", item.getRankingLevel());    //获奖等级
				map.put("rankingMoney", item.getRankingMoney()==null?"0.00":item.getRankingMoney().toString());//获奖金额
				map.put("accountStatus", accountStatus);   //记账状态
				map.put("removeRemark", item.getRemoveRemark());    //移除说明
				map.put("pushTime", item.getPushTime()==null?"":sdf2.format(item.getPushTime()));          //发放时间
				map.put("removeTime", item.getRemoveTime()==null?"":sdf2.format(item.getRemoveTime()));        //移除时间
				map.put("startTime", item.getStartTime()==null?"":sdf2.format(item.getStartTime()));        //统计记录开始时间
				map.put("endTime", item.getEndTime()==null?"":sdf2.format(item.getEndTime()));        //统计记录结束时间
				data.add(map);
			}
			String[] cols = new String[]{
					"orderNo","rankingNo","batchNo","ruleNo","rankingName","rankingType",
					"orgId", "showNo","userName","nickName","userCode","phone", "rankingData",
					"rankingLevel","rankingMoney","accountStatus","removeRemark",
					"pushTime","removeTime","startTime","endTime"};
			String[] colsName = new String[]{
					"获奖订单号", "排行榜编号", "期号", "规则编号",
					"排行榜名称", "统计周期", "所属组织", "排名", "用户姓名", "微信昵称",
					"用户ID", "手机号", "统计总额","获奖等级","获奖金额","记账状态",
					"移除说明","发放时间","移除时间","统计开始时间","统计结束时间"};
			outputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出排行榜 用户奖金发放记录异常", e);
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public RankingRecordInfo getRankingRecordById(String recordId) {
		RankingRecordInfo baseInfo = new RankingRecordInfo();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(orgId);	//设置只能查自己所属组织的排行榜
		baseInfo.setId(recordId);
		return rankingRecordDao.getRankingRecordById(baseInfo);
	}

	@Override
	public List<RankingRecordDetailInfo> queryRankingRecordDetailPage(String recordId, Page<RankingRecordDetailInfo> page) {
		
		List<RankingRecordDetailInfo> initList = rankingRecordDetailDao.queryRankingRecordDetailPage(recordId,page);
		
		if(initList.size()>0 && initList.get(0).getRankingIndex()!=null && !"".equals(initList.get(0).getRankingIndex())){
			return initList ;  //如果已排名不做重新排名
		}
		
		//查询时根据统计总额倒序，成为专员时间正序排序
		Page<RankingRecordDetailInfo> pageAll = new Page<RankingRecordDetailInfo>(1,Integer.MAX_VALUE);
		List<RankingRecordDetailInfo> list = rankingRecordDetailDao.queryRankingRecordDetailPage(recordId,pageAll);
		
		int index = 0;
		int flagIndex = 0;
		boolean flag = false;
		
		List<RankingRecordDetailInfo> notRemove = new ArrayList<RankingRecordDetailInfo>();
		List<RankingRecordDetailInfo> hadRemove = new ArrayList<RankingRecordDetailInfo>();
		
		for(int i=0;i < list.size();i++){
			RankingRecordDetailInfo rd = list.get(i);

			if("2".equals(rd.getStatus())){//已移除
				hadRemove.add(rd);
			}else{
				notRemove.add(rd);
			}
			
			rd.setRankingIndex(index+1+"");   //排名
			if("2".equals(rd.getStatus()) && !flag){//已移除
				flagIndex = index;
				flag = true;
			}

			if(!"2".equals(rd.getStatus()) && flag){//非移除
				index = flagIndex;
				flag = false;
				rd.setRankingIndex(index+1+"");   //重新设置排名
			}

			index++;
		}
		
		for (RankingRecordDetailInfo detailInfo : notRemove) {
			rankingRecordDetailDao.updSort(detailInfo);
		}
		for(RankingRecordDetailInfo detailInfo : hadRemove) {
			rankingRecordDetailDao.updSort(detailInfo);
		}
		
		return rankingRecordDetailDao.queryRankingRecordDetailPage(recordId,page);//重新查询，防止获取不到排名
	}

	@Override
	public String queryRankingRecordDetailUserTotalAmountSum(String recordId) {
		return rankingRecordDetailDao.queryRankingRecordDetailUserTotalAmountSum(recordId);
	}
	
	
	@Override
	public CarOrder getCarOrders(CarOrder order, Page<CarOrder> page) {
		
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgId = principal.getUserEntityInfo().getEntityId();
        order.setOrgId(orgId);//当前登录代理商所属组织
        
		carOrderDao.findCarOrderByPage(order, page);
		
		return carOrderDao.orderSum(order);
	}

	@Override
	public Result carOrderDetail(String orderNo) {
		Result result = new Result();
		CarOrder carOrder = carOrderDao.carOrderDetail(orderNo);
		if(carOrder == null){
			result.setStatus(false);
			result.setMsg("查询失败");
			return result;
		}
		result.setData(carOrder);
		result.setMsg("查询成功");
		result.setStatus(true);
		return result;
	}
	
	@Override
	public void exportCarOrder(HttpServletResponse response, CarOrder order) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OutputStream outputStream = null;
        try {
            Page<CarOrder> page = new Page<CarOrder>(0, Integer.MAX_VALUE);
            
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String orgId = principal.getUserEntityInfo().getEntityId();
            order.setOrgId(orgId);//当前登录代理商所属组织
            
            List<CarOrder> carList = carOrderDao.findCarOrderByPage(order, page);
            
            ExcelExport export = new ExcelExport(1);
            String fileName = "违章代缴订单" + sdf.format(new Date()) + export.getFileSuffix(1);
            
            String fileNameFormat = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            Map<String, String> map = null;
            
            String wzType = "";
            String oneUserType = "";
            String twoUserType = "";
            String thrUserType = "";
            String fourUserType = "";
            
            for (CarOrder carOrder : carList) {
                map = new HashMap<>();
                
                map.put("orderNo", carOrder.getOrderNo());
                if("1".equals(carOrder.getViolationType())){
                	wzType = "扣分单";
                }else{
                	wzType = "非扣分单";
                }
                map.put("violationType", wzType);
                map.put("score", carOrder.getScore());
                map.put("violationTime", carOrder.getViolationTime()==null?"":sdf2.format(carOrder.getViolationTime()));
                map.put("violationCity", carOrder.getViolationCity());
                map.put("carNum", carOrder.getCarNum());
                map.put("receiveAmount", carOrder.getReceiveAmount());
                map.put("price", carOrder.getPrice());
                map.put("totalBonus", carOrder.getTotalBonus());
                map.put("orgName", carOrder.getOrgName());
                map.put("status", "订单成功");
                map.put("userCode", carOrder.getUserCode());
                map.put("userName", carOrder.getUserName());
                map.put("phone", carOrder.getPhone());
                map.put("createDate", carOrder.getCreateDate()==null?"":sdf2.format(carOrder.getCreateDate()));
                map.put("completeDate", carOrder.getCompleteDate()==null?"":sdf2.format(carOrder.getCompleteDate()));
                map.put("oneUserCode", carOrder.getOneUserCode());
                map.put("oneUserName", carOrder.getOneUserName());
                if("20".equals(carOrder.getOneUserType())){
                	oneUserType = "专员";
                }else if("30".equals(carOrder.getOneUserType())){
                	oneUserType = "经理";
                }else if("40".equals(carOrder.getOneUserType())){
                	oneUserType = "银行家";
                }
                map.put("oneUserType", oneUserType);
                map.put("oneUserProfit", carOrder.getOneUserProfit());
                map.put("twoUserCode", carOrder.getTwoUserCode());
                map.put("twoUserName", carOrder.getTwoUserName());
                if("20".equals(carOrder.getTwoUserType())){
                	twoUserType = "专员";
                }else if("30".equals(carOrder.getTwoUserType())){
                	twoUserType = "经理";
                }else if("40".equals(carOrder.getTwoUserType())){
                	twoUserType = "银行家";
                }
                map.put("twoUserType", twoUserType);
                map.put("twoUserProfit", carOrder.getTwoUserProfit());
                map.put("thrUserCode", carOrder.getThrUserCode());
                map.put("thrUserName", carOrder.getThrUserName());
                if("20".equals(carOrder.getThrUserType())){
                	thrUserType = "专员";
                }else if("30".equals(carOrder.getThrUserType())){
                	thrUserType = "经理";
                }else if("40".equals(carOrder.getThrUserType())){
                	thrUserType = "银行家";
                }
                map.put("thrUserType", thrUserType);
                map.put("thrUserProfit", carOrder.getThrUserProfit());
                map.put("fouUserCode", carOrder.getFouUserCode());
                map.put("fouUserName", carOrder.getFouUserName());
                if("20".equals(carOrder.getFouUserType())){
                	fourUserType = "专员";
                }else if("30".equals(carOrder.getFouUserType())){
                	fourUserType = "经理";
                }else if("40".equals(carOrder.getFouUserType())){
                	fourUserType = "银行家";
                }
                map.put("fouUserType", fourUserType);
                map.put("fouUserProfit", carOrder.getFouUserProfit());
                map.put("orgProfit", carOrder.getOrgProfit());
                map.put("plateProfit", carOrder.getPlateProfit());
                if("0".equals(carOrder.getAccountStatus())){
                	map.put("accountStatus", "未记账");
                }else if("1".equals(carOrder.getAccountStatus())){
                	map.put("accountStatus", "记账成功");
                }else{
                	map.put("accountStatus", "记账失败");
                }
                
                map.put("openProvince", carOrder.getOpenProvince());
                map.put("openCity", carOrder.getOpenCity());
                map.put("openRegion", carOrder.getOpenRegion());
                map.put("remark", carOrder.getRemark());
                
                data.add(map);
            }

            String[] cols = new String[]{"orderNo","violationType","score","violationTime","violationCity","carNum","receiveAmount","price","totalBonus","orgName","status","userCode","userName","phone","createDate","completeDate","oneUserCode","oneUserName","oneUserType","oneUserProfit","twoUserCode","twoUserName","twoUserType","twoUserProfit","thrUserCode","thrUserName","thrUserType","thrUserProfit","fouUserCode","fouUserName","fouUserType","fouUserProfit","orgProfit","plateProfit","accountStatus","openProvince","openCity","openRegion","remark"};
            String[] colsName = new String[]{"订单ID","违章类型","扣分","违章时间","违章城市","车牌号","订单总额","银行家项目总分润","发放总奖金","所属组织","订单状态","贡献人ID","贡献人名称","贡献人手机号","创建时间","订单完成时间","一级编号","一级名称","一级身份","一级分润","二级编号","二级名称","二级身份","二级分润","三级编号","三级名称","三级身份","三级分润","四级编号","四级名称","四级身份","四级分润","品牌商分润","平台分润","记账状态","省","市","区","备注"};
            outputStream = response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出违章代缴订单记录异常", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	@Override
    public void exportInquiryOrder(HttpServletResponse response, ZxProductOrder order) {
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
            OutputStream ouputStream = null;
            try {
                Page<ZxProductOrder> page = new Page<>(0, Integer.MAX_VALUE);
                List<ZxProductOrder> zxProductOrders = zxProductOrderService.selectByPage(order, page);

                ListDataExcelExport export = new ListDataExcelExport();
    			String fileName = "超级银行家征信订单" + sdf.format(new Date()) +"."+ export.getFileSuffix();
                String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
                Map<String,String> map = null;
                for(ZxProductOrder item: zxProductOrders){
                    map = new HashMap<>();
                    map.put("yhjOrderNo", item.getYhjOrderNo());
                    map.put("orderNo",item.getOrderNo());
                    map.put("status",item.getStatus());
                    map.put("payMethod", item.getPayMethod());
                    map.put("orgName", item.getOrgName());
                    map.put("generationTimeStr",item.getGenerationTimeStr() );
                    map.put("reportNo", item.getReportNo());
                    map.put("reportType", item.getReportType());
                    map.put("productName", item.getProductName());
                    map.put("price", String.valueOf(item.getPrice()));
                    map.put("recordName", item.getRecordName());
                    map.put("recordPhone", item.getRecordPhone());
                    map.put("recordIdNo", item.getRecordIdNo());
                    map.put("contactPhone", item.getContactPhone());
                    map.put("userCode", item.getUserCode());
                    map.put("userName", item.getUserName());
                    map.put("shareUserPhone",item.getShareUserPhone());
                    map.put("totalBonus", String.valueOf(item.getTotalBonus()==null?"":item.getTotalBonus()));
                    map.put("accountStatus", item.getAccountStatus());
                    map.put("profitDateStr", item.getProfitDateStr());
                    map.put("createTimeStr", item.getCreateTimeStr());
                    map.put("expiryTimeStr", item.getExpiryTimeStr());
                    map.put("refundDateStr", item.getRefundDateStr());
                    map.put("oneUserCode", item.getOneUserCode());
                    map.put("oneUserName", item.getOneUserName());
                    map.put("oneUserType", item.getOneUserType());
                    map.put("oneUserProfit", String.valueOf(item.getOneUserProfit()==null?"":item.getOneUserProfit()));
                    map.put("twoUserCode", item.getTwoUserCode());
                    map.put("twoUserName", item.getTwoUserName());
                    map.put("twoUserType", item.getTwoUserType());
                    map.put("twoUserProfit",String.valueOf( item.getTwoUserProfit()==null?"":item.getTwoUserProfit()));
                    map.put("thrUserCode", item.getThrUserCode());
                    map.put("thrUserName", item.getThrUserName());
                    map.put("thrUserType", item.getThrUserType());
                    map.put("thrUserProfit", String.valueOf(item.getThrUserProfit()==null?"":item.getThrUserProfit()));
                    map.put("fouUserCode", item.getFouUserCode());
                    map.put("fouUserName", item.getFouUserName());
                    map.put("fouUserType", item.getFouUserType());
                    map.put("fouUserProfit", String.valueOf(item.getFouUserProfit() == null ? "" : item.getFouUserProfit()));
                    map.put("orgProfit", String.valueOf(item.getOrgProfit() == null ? "" : item.getOrgProfit()));
//                    map.put("zxCostPrice", String.valueOf(item.getZxCostPrice()==null?"":item.getZxCostPrice()));
//                    map.put("plateProfit", String.valueOf(item.getPlateProfit()==null?"":item.getPlateProfit()));
                    map.put("openProvince", item.getOpenProvince());
                    map.put("openCity", item.getOpenCity());
                    map.put("openRegion", item.getOpenRegion());
                    map.put("remark", item.getRemark());
                    data.add(map);
                }

                String[] cols = new String[]{
                        "yhjOrderNo","orderNo","status","payMethod","orgName","generationTimeStr",
                        "reportNo","reportType","productName",
                        "price","recordName","recordPhone","recordIdNo",
                        "contactPhone","userCode",
                        "userName","shareUserPhone","totalBonus","accountStatus","profitDateStr","createTimeStr","expiryTimeStr","refundDateStr",
                        "oneUserCode","oneUserName","oneUserType",
                        "oneUserProfit","twoUserCode","twoUserName","twoUserType",
                        "twoUserProfit","thrUserCode",
                        "thrUserName","thrUserType","thrUserProfit","fouUserCode","fouUserName",
                        "fouUserType","fouUserProfit","orgProfit",
//                        "zxCostPrice","plateProfit",
                        "openProvince","openCity","openRegion",
                        "remark"
                };
                String[] colsName = new String[]{
                        "订单ID ","征信订单ID","订单状态","支付方式","所属组织","报告生成时间",
                       "报告编号","报告类型名称","报告名称",
                        "报告价格","订单姓名","订单手机号","订单身份证号",
                        "联系人手机号","贡献人ID",
                        "贡献人名称 ","贡献人手机号","发放总奖金额","记账状态","订单分润时间","订单生成时间","订单失效时间","退款成功时间",
                        "一级编号","一级名称","一级身份",
                        "一级分润","二级编号","二级名称","二级身份",
                        "二级分润","三级编号",
                        "三级名称 ","三级身份","三级分润","四级编号","四级名称",
                        "四级身份","四级分润","品牌商分润",
//                        "征信系统给银行家成本","平台分润",
                        "省","市","区",
                        "备注"
                };
                ouputStream = response.getOutputStream();
                export.export(cols, colsName, data, response.getOutputStream());
            } catch (Exception e) {
                log.error("导出超级银行家用户管理异常", e);

            } finally {
                try {
                    ouputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	@Override
	public List<MposMachines> selectMposMachinesList(MposMachines baseInfo, Page<MposMachines> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(Long.valueOf(entityId));
		return mposMachinesDao.selectList(baseInfo,page);
	}

	@Override
	public void mposMachinesExport(HttpServletResponse response, MposMachines mposMachines) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();

		OutputStream ouputStream = null;
		try {
			Page<MposMachines> page = new Page<>(0, Integer.MAX_VALUE);
			mposMachines.setOrgId(Long.valueOf(entityId));
			List<MposMachines> mposMachinesList = mposMachinesDao.selectList(mposMachines, page);

			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "Mpos机具管理" + sdf.format(new Date()) +"."+ export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			Map<String,String> map = null;
			for(MposMachines item: mposMachinesList){
				map = new HashMap<>();
				map.put("snNo", item.getSnNo());  //SN号
				map.put("productTypeName", item.getProductTypeName());  //硬件产品种类
				map.put("v2MerchantCode", item.getV2MerchantCode());  //V2商户编号
				map.put("v2MerchantName", item.getV2MerchantName());  //V2商户简称
				map.put("purchaserUserCode", item.getPurchaserUserCode());  //采购者编号
				map.put("purchaserUserPhone", item.getPurchaserUserPhone());  //采购者手机号
				map.put("orgName", item.getOrgName());  //所属组织
				String status = "";
				if(item.getStatus()!= null) {
					switch (item.getStatus()) {
						case 1:{
							status="已入库";
							break;
						}
						case 2:{
							status="已分配";
							break;
						}
						case 3:{
							status="已发货";
							break;
						}
						case 4:{
							status="已启用";
							break;
						}
						case 5:{
							status="已激活";
							break;
						}
						default:{
							break;
						}
					}
				}
				map.put("status", status);  //机具状态  //[{value:"1",text:"已入库"},{value:"2",text:"已分配"},{value:"3",text:"已发货"},{value:"4",text:"已启用"},{value:"5",text:"已激活"}]'},
				map.put("shipDate", item.getShipDate()==null?"":sdf2.format(item.getShipDate()));  //发货时间
				map.put("enabledDate", item.getEnabledDate()==null?"":sdf2.format(item.getEnabledDate()));  //启动时间
				map.put("orderNo", item.getOrderNo());  //订单编号
				data.add(map);
			}

			String[] cols = new String[]{
					"snNo","productTypeName","v2MerchantCode","v2MerchantName","purchaserUserCode","purchaserUserPhone",
					"orgName","status","shipDate", "enabledDate","orderNo"
			};
			String[] colsName = new String[]{
					"SN号 ","硬件产品种类","V2商品编号","V2商户简称","采购者编号","采购者手机号",
					"所属组织","机具状态","发货时间","启用时间","订单编号"

			};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出Mpos机具异常", e);

		} finally {
			try {
				ouputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<MposMachines> selectMposMachinesByOrderId(Long orderId) {
		return mposMachinesDao.selectMposMachinesByOrderId(orderId);
	}


	@Override
	public List<MposOrder> selectMposOrderList(MposOrder baseInfo, Page<MposOrder> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(Long.valueOf(entityId));
        List<MposOrder> mposOrdersList = mposOrderDao.selectList(baseInfo,page);
        List<MposOrder> list = page.getResult();
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                MposOrder mposOrder = list.get(i);
                if(org.apache.commons.lang3.StringUtils.isNotBlank(mposOrder.getImgUrl())){
                    mposOrder.setImgUrl(CommonUtil.getImgUrlAgent(mposOrder.getImgUrl()));
                }
                list.set(i,mposOrder);
            }
            page.setResult(list);
        }
		return mposOrdersList;
	}

	@Override
	public MposOrder mposOrderDetail(String orderNo) {
		return mposOrderDao.findByOrderNo(orderNo);
	}

	@Override
	public MposOrderSum selectMposOrderSum(MposOrder baseInfo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		baseInfo.setOrgId(Long.valueOf(entityId));
		return mposOrderDao.selectMposOrderSum(baseInfo);
	}

	@Override
	public void exportMposOrder(HttpServletResponse response, MposOrder mposOrder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		OutputStream ouputStream = null;
		try {
			Page<MposOrder> page = new Page<>(0, Integer.MAX_VALUE);
			mposOrder.setOrgId(Long.valueOf(entityId));
			List<MposOrder> list = mposOrderDao.selectList(mposOrder, page);
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "Mpos订单" + sdf.format(new Date()) +"." + export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			Map<String,String> userTypeMap = new HashMap<>();
			userTypeMap.put("10","普通用户");
			userTypeMap.put("20","专员");
			userTypeMap.put("30","经理");
			userTypeMap.put("40","银行家");
			Map<String, String> map = null;
			for (MposOrder item : list) {
				map = new HashMap<>();
				map.put("orderNo", item.getOrderNo());//订单编号
				map.put("orgName", item.getOrgName());//所属组织
				map.put("userCode", item.getUserCode());//采购者编号
				map.put("userName", item.getUserName());//采购者名称
				map.put("goodNo", item.getGoodNo());//商品编号
				map.put("goodTitle", item.getGoodTitle());//商品标题
				map.put("typeName", item.getTypeName());//所在商品种类
				map.put("productType", item.getProductType());//设备类型
				map.put("goodSinglePrice", item.getGoodSinglePrice() == null ? "" : item.getGoodSinglePrice().toString());//商品销售价
				map.put("buyNum", String.valueOf(item.getBuyNum()));//购买数量(台)
				map.put("goodTotalPrice", item.getGoodTotalPrice()==null ? "":item.getGoodTotalPrice().toString());//商品总价
				map.put("totalPrice", item.getTotalPrice()==null ?"":item.getTotalPrice().toString());//订单总金额
				map.put("shipFee", item.getShipFee()==null?"":item.getShipFee().toString());//运费 cellFilter:'currency:""'},
				String shipWay = "";
				if(item.getShipWay()!=null) {
					switch (item.getShipWay()) {
						case 1: {
							shipWay = "快递配送";
							break;
						}
						case 2: {
							shipWay = "线下自提";
							break;
						}
						default: {
							break;
						}
					}
				}
				map.put("shipWay", shipWay);    //配送方式 'formatDropping:[{value:"1",text:"快递配送"},{value:"2",text:"线下自提"}]
				String needShipFee="";
				if(item.getNeedShipFee()!=null) {
					switch (item.getNeedShipFee()) {
						case 1: {
							needShipFee = "是";
							break;
						}
						case 2: {
							needShipFee = "否";
							break;
						}
						default: {
							break;
						}
					}
				}
				map.put("needShipFee", needShipFee);//是否包邮 cellFilter:'formatDropping:[{value:"1",text:"是"},{value:"2",text:"否"}]'},
				String shipper = "";
				if(item.getShipper()!=null) {
					switch (item.getShipper()) {
						case 1: {
							shipper = "平台发货";
							break;
						}
						case 2: {
							shipper = "组织发货";
							break;
						}

						default: {
							break;
						}
					}
				}
				map.put("shipper", shipper);//订单发货方  formatDropping:[{text:"平台发货",value:"1"},{text:"组织发货",value:"2"}]'},
				map.put("receiverName", item.getReceiverName());//收货人姓名
				map.put("receiverPhone", item.getReceiverPhone());//收货人手机号
				map.put("receiverAddr", item.getReceiverAddr());//收货人地址
				map.put("remark", item.getRemark());//备注
				map.put("createDate", item.getCreateDate()==null?"":sdf2.format(item.getCreateDate()));//创建时间  cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
				String status = "";
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getStatus())) {
					switch (item.getStatus()) {
						case "1": {
							status = "待付款";
							break;
						}
						case "2": {
							status = "待发货";
							break;
						}
						case "3": {
							status = "待收货";
							break;
						}
						case "4": {
							status = "已收货";
							break;
						}
						case "9": {
							status = "已关闭";
							break;
						}
						default: {
							break;
						}
					}
				}
				map.put("status", status);//订单状态  formatDropping:[{text:"待付款",value:"1"},{text:"待发货",value:"2"},{text:"待收货",value:"3"},{text:"已收货",value:"4"},{text:"已关闭",value:"9"}]'},
				String payStatus = "";
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getPayStatus())) {
					switch (item.getPayStatus()) {
						case "1": {
							payStatus = "未支付";
							break;
						}
						case "2": {
							payStatus = "已支付";
							break;
						}
						default: {
							break;
						}
					}
				}
				map.put("payStatus", payStatus);//支付状态 formatDropping:[{text:"未支付",value:"1"},{text:"已支付",value:"2"}]'},
				String payMethod = "";
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getPayMethod())) {
					switch (item.getPayMethod()) {
						case "1": {
							payMethod = "微信";
							break;
						}
						case "2": {
							payMethod = "支付宝";
							break;
						}
						case "3": {
							payMethod = "快捷";
							break;
						}
						case "4": {
							payMethod = "红包账户";
							break;
						}
						case "5": {
							payMethod = "分润账户";
							break;
						}
						default: {
							break;
						}
					}
				}
				map.put("payMethod", payMethod);//支付方式  formatDropping:[{text:"微信",value:"1"},{text:"支付宝",value:"2"},{text:"快捷",value:"3"},{text:"红包账户",value:"4"},{text:"分润账户",value:"5"}]'},
				map.put("payDate", item.getPayDate()==null?"":sdf2.format(item.getPayDate()));//支付时间  cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
				map.put("payChannelNo", item.getPayChannelNo());//支付商户号
				map.put("payChannel", item.getPayChannelName());//支付通道
				map.put("payOrderNo", item.getPayOrderNo());//关联支付流水号
				map.put("receiveDate", item.getReceiveDate()==null?"":sdf2.format(item.getReceiveDate()));//确认收货时间  cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
				map.put("toOrgAmount", item.getToOrgAmount()==null?"":item.getToOrgAmount().toString());//转组织贷款金额
				String toOrgStatus="";
				if(item.getToOrgStatus()!=null) {
					switch (item.getToOrgStatus()) {
						case 1: {
							toOrgStatus = "未转账";
							break;
						}
						case 2: {
							toOrgStatus = "已转账";
							break;
						}
						case 3: {
							toOrgStatus = "转账失败";
							break;
						}

						default: {
							break;
						}
					}
				}
				map.put("toOrgStatus", toOrgStatus);//贷款转账状态   cellFilter:'formatDropping:[{text:"未转账",value:"1"},{text:"已转账",value:"2"}]'},
				map.put("totalBonus", item.getTotalBonus()==null ? "":item.getTotalBonus().toString());//采购机具总奖励包
				String accountStatus = "";
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getAccountStatus())) {
					switch (item.getAccountStatus()) {
						case "0": {
							accountStatus = "待入账";
							break;
						}
						case "1": {
							accountStatus = "已记账";
							break;
						}
						case "2": {
							accountStatus = "记账失败";
							break;
						}
						default: {
							break;
						}
					}
				}
				map.put("oneUserCode", item.getOneUserCode());//一级编号
				map.put("oneUserName", item.getOneUserName());//一级名称
				map.put("oneUserType", userTypeMap.get(item.getOneUserType()));//一级身份
				map.put("oneUserProfit", item.getOneUserProfit()==null?"":item.getOneUserProfit().toString());//一级分润
				map.put("twoUserCode", item.getTwoUserCode());//二级编号
				map.put("twoUserName", item.getTwoUserName());//二级名称
				map.put("twoUserType", userTypeMap.get(item.getTwoUserType()));//二级身份
				map.put("twoUserProfit", item.getTwoUserProfit()==null?"":item.getTwoUserProfit().toString());//二级分润
				map.put("thrUserCode", item.getThrUserCode());//三级编号
				map.put("thrUserName", item.getThrUserName());//三级名称
				map.put("thrUserType", userTypeMap.get(item.getThrUserType()));//三级身份
				map.put("thrUserProfit", item.getThrUserProfit()==null?"":item.getThrUserProfit().toString());//三级分润
				map.put("fouUserCode", item.getFouUserCode());//四级编号
				map.put("fouUserName", item.getFouUserName());//四级名称
				map.put("fouUserType", userTypeMap.get(item.getFouUserType()));//四级身份
				map.put("fouUserProfit", item.getFouUserProfit()==null?"":item.getFouUserProfit().toString());//四级分润
				map.put("orgProfit", item.getOrgProfit()==null?"":item.getOrgProfit().toString());//组织分润
				map.put("accountStatus", accountStatus);//奖励入账状态  cellFilter:'formatDropping:[{text:"待入账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}]'},
				map.put("completeDate", item.getCompleteDate()==null?"":sdf2.format(item.getCompleteDate()));//入账时间
				map.put("provinceName", item.getProvinceName());//采购者所属省
				map.put("cityName", item.getCityName());//采购者所属市
				map.put("districtName", item.getDistrictName());//采购者所属区
				map.put("userRemark", item.getUserRemark());//采购者所属区
				data.add(map);
			}
			String[] cols = new String[]{
					"orderNo","orgName","userCode","userName",
					"goodNo","goodTitle","typeName",
					"productType","goodSinglePrice","buyNum","goodTotalPrice",
					"totalPrice","shipFee","shipWay","needShipFee",
					"shipper","receiverName","receiverPhone","receiverAddr",
					"remark","createDate","status","payStatus",
					"payMethod","payDate","payChannelNo","payChannel",
					"payOrderNo","receiveDate","toOrgAmount","toOrgStatus", "totalBonus",
					"oneUserCode","oneUserName","oneUserType","oneUserProfit",
					"twoUserCode","twoUserName","twoUserType","twoUserProfit",
					"thrUserCode","thrUserName","thrUserType","thrUserProfit",
					"fouUserCode","fouUserName","fouUserType","fouUserProfit",
					"orgProfit",
					"accountStatus","completeDate","provinceName","cityName","districtName","userRemark"

			};
			String[] colsName = new String[]{
					"订单编号","所属组织","采购者编号","采购者名称",
					"商品编号","商品标题","所在商品种类",
					"设备类型","商品销售价","购买数量(台)","商品总价",
					"订单总金额","运费","配送方式","是否包邮",
					"订单发货方","收货人姓名","收货人手机号","收货人地址",
					"下单备注","创建时间","订单状态","支付状态",
					"支付方式","支付时间","支付商户号","支付通道",
					"关联支付流水号","确认收货时间","转组织货款金额","货款转账状态","采购机具总奖励包",
					"一级编号","一级名称","一级身份","一级分润",
					"二级编号","二级名称","二级身份","二级分润",
					"三级编号","三级名称","三级身份","三级分润",
					"四级编号","四级名称","四级身份","四级分润",
					"组织分润",
					"奖励入账状态","奖励入账时间","采购者所属省","采购者所属市","采购者所属区","采购人备注"


			};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出超级银行家Mpos订单异常", e);
		} finally {
			try {
				if (ouputStream != null) {
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Result mposOrderShip(MposOrder mposOrder, List<String> snList) {
		Result result = new Result();
		UserLoginInfo loginInfo = CommonUtil.getLoginUser();
		if(snList!=null && snList.size()>0){
			for (String sn :snList){
				MposMachines mposMachines = mposMachinesDao.selectBySn(sn);
				if(mposMachines==null){
					result.setStatus(false);
					result.setMsg("机具不存在");
					return result;
				}
				if(mposMachines.getStatus()==null || mposMachines.getStatus() != 2){
					result.setStatus(false);
					result.setMsg("机具状态不是已分配状态");
					return result;
				}
				if(!mposOrder.getOrgId().equals(mposMachines.getOrgId())){
					result.setStatus(false);
					result.setMsg("机具"+sn+"不属于当前组织，不能发货,");
					return result;
				}
			}
			for (String sn : snList) {
				mposMachinesDao.mposMachinesShip(mposOrder.getId(), 3, sn, loginInfo.getUsername(), new Date(),mposOrder.getUserCode());
			}
			mposOrder.setStatus("3");       //待收货
			mposOrder.setUpdateBy(loginInfo.getUsername());
			mposOrder.setUpdateDate(new Date());
			mposOrder.setShipDate(new Date());
			int num = mposOrderDao.update(mposOrder);
			if(num>0){

				result.setStatus(true);
				result.setMsg("发货成功");
			}
		}else {
			result.setStatus(false);
			result.setMsg("sn号为空");
		}
		return result;
	}

	@Override
	public int mposOrderUpdate(MposOrder mposOrder){
    	return mposOrderDao.update(mposOrder);
	}

	@Override
	public List<UserProfit> selectMposProfitDetailPage(UserProfit baseInfo, Page<UserProfit> page) throws UnsupportedEncodingException {
		String entityId = seconeUserNode(baseInfo);
		baseInfo.setEntityId(entityId);
		userProfitDao.selectMposProfitDetailPage(baseInfo, page);
		List<UserProfit> list = page.getResult();
		filterProfitDetailParam(list);
		return list;
	}

	/**
	 * 超级银行家mpos分润详情导出
	 * @param response
	 * @param order
	 */
	@Override
	public void mposProfitDetailExprort(HttpServletResponse response, UserProfit order) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			String entityId = seconeUserNode(order);
			order.setEntityId(entityId);
			Page<UserProfit> page = new Page<>(0, Integer.MAX_VALUE);
			List<UserProfit> list = selectMposProfitDetailPage(order, page);
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "mpos分润明细"+sdf.format(new Date())+"."+export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
			Map<String,String> map = null;
			for(UserProfit item: list){
				map = new HashMap<>();
				map.put("id", String.valueOf(item.getId()));
				map.put("orgId", String.valueOf(item.getOrgId()));
				map.put("orgName", item.getOrgName());
				map.put("orderType", item.getOrderType());
				map.put("status", item.getStatus());
				map.put("orderNo", item.getOrderNo());
				map.put("shareUserCode", item.getShareUserCode());
				map.put("shareNickName", item.getShareNickName());
				map.put("userName", item.getUserName());
				map.put("shareUserPhone", item.getShareUserPhone());
				map.put("totalProfit", String.valueOf(item.getTotalProfit()));
				map.put("userName", item.getUserName());
				map.put("userCode", item.getUserCode());
				map.put("userType", item.getUserType());
				map.put("userProfit", String.valueOf(item.getUserProfit()));
				map.put("createDate", item.getCreateDateStr());
				map.put("accountStatus", item.getAccountStatus());
				map.put("profitLevel", String.valueOf(item.getProfitLevel()));
				map.put("openProvince", item.getOpenProvince());
				map.put("openCity", item.getOpenCity());
				map.put("openRegion", item.getOpenRegion());
				map.put("remark", item.getRemark());
				map.put("shareUserRemark", item.getShareUserRemark());
				data.add(map);
			}

			String[] cols = new String[]{
					"id","orgId","orgName","orderType",
					"status","orderNo",
					"shareUserCode","shareNickName","userName","shareUserPhone",
					"totalProfit",
					"userName","userCode", "userType","userProfit","profitLevel",
					"createDate","accountStatus",
					"openProvince","openCity","openRegion","remark","shareUserRemark"};
			String[] colsName = new String[]{
					"分润明细ID","所属组织","品牌商名称","订单类型",
					"订单状态","订单编号",
					"贡献人ID","贡献人昵称","贡献人名称","贡献人手机号",
					"总奖金",
					"收益人姓名","收益人ID","收益人身份","收益人分润","当前分润层级",
					"创建时间","记账状态",
					"省","市","区","备注","贡献人备注"};
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出超级银行家mpos分润明细异常", e);
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<MposActiveOrder> selectMposActiveOrderList(MposActiveOrder mposActiveOrder,
														   Page<MposActiveOrder> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		mposActiveOrder.setOrgId(Long.valueOf(entityId));
		return mposActiveOrderDao.selectMposActiveOrderList(mposActiveOrder,page);
	}

	@Override
	public MposActiveOrder selectMposActiveOrderSum(MposActiveOrder mposActiveOrder) {
		MposActiveOrder mposActiveOrderSum = mposActiveOrderDao.selectMposActiveOrderSum(mposActiveOrder);
		if(mposActiveOrderSum != null) {
			BigDecimal activeReturnBonusSum = mposActiveOrderSum.getActiveReturnBonusSum();
			BigDecimal totalBonusSum = mposActiveOrderSum.getTotalBonusSum();
			if(activeReturnBonusSum != null) {
				if(totalBonusSum != null) {
					mposActiveOrderSum.setTotalSubsidy(activeReturnBonusSum.add(totalBonusSum));
				}else {
					mposActiveOrderSum.setTotalSubsidy(activeReturnBonusSum);
				}
			}else {
				mposActiveOrderSum.setTotalSubsidy(totalBonusSum);
			}
		}

		return mposActiveOrderSum;
	}

	@Override
	public void exportMposActiveOrder(HttpServletResponse response, MposActiveOrder mposActiveOrderParam) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			mposActiveOrderParam.setOrgId(Long.valueOf(entityId));
			Page<MposActiveOrder> page = new Page<>(0, Integer.MAX_VALUE);
			List<MposActiveOrder> list = mposActiveOrderDao.selectMposActiveOrderList(mposActiveOrderParam,page);
			List<Map<String, String>> data = new ArrayList<>() ;
			setMposActiveOrderData(list,data);

            String[] cols = new String[]{
                    "orderNo","v2MerchantCode","v2MerchantName","v2MerchantPhone","snNo",
                    "typeName","createDate","registerDate",
                    "userCode","userName","phone","orgName",
                    "status","activeDate","activeReturnBonus",
                    "totalBonus","accountStatus","completeDate",
                    "oneUserCode","oneUserName","oneUserType","oneUserProfit",
                    "twoUserCode","twoUserName","twoUserType","twoUserProfit",
                    "thrUserCode","thrUserName","thrUserType","thrUserProfit",
                    "fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit",
                    "provinceName","cityName","districtName","remark"
            };
            String[] colsName = new String[]{
                    "订单编号","V2商户编号","V2商户名称","商户手机号","进件机具SN",
                    "设备类型","创建时间","进件时间",
                    "所属采购者ID","所属采购者姓名","采购者手机号","所属组织",
                    "活动状态","激活时间","激活返现金额",
                    "激活奖金包","补贴入账状态","补贴发放时间",
                    "一级编号","一级名称","一级身份","一级分润",
                    "二级编号","二级名称","二级身份","二级分润",
                    "三级编号","三级名称","三级身份","三级分润",
                    "四级编号","四级名称","四级身份","四级分润",
                    "组织分润",
                    "省","市","区","备注"
            };
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "Mpos激活信息"+sdf.format(new Date())+"."+export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出Mpos激活信息异常!", e);
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建Mpos激活信息导出数据集合
	 * @param list
	 * @param data
	 */
	private void setMposActiveOrderData(List<MposActiveOrder> list,List<Map<String, String>> data) {
        Map<String, String> userTypeMap = getUserTypeMap();
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("1", "未激活");
        statusMap.put("2", "已激活");
        statusMap.put("3", "已返现");

        // 记账状态
        Map<String, String> accountStatusMap = new HashMap<>();
        accountStatusMap.put("0", "待入账");
        accountStatusMap.put("1", "已入账");
        accountStatusMap.put("2", "记账失败");

        for(MposActiveOrder item: list){
            Map<String,String> map = new HashMap<>();
            map.put("orderNo", item.getOrderNo());// 订单编号
            map.put("v2MerchantCode", item.getV2MerchantCode()); // V2商户编号
            map.put("v2MerchantName", item.getV2MerchantName()); // V2商户名称
            map.put("v2MerchantPhone", item.getV2MerchantPhone()); // 商户手机号
            map.put("snNo", item.getSnNo()); // 进件机具SN
            map.put("typeName", item.getTypeName()); // 设备类型名称
            map.put("createDate",item.getCreateDate()== null?"":DateUtil.getLongFormatDate(item.getCreateDate())); // 创建时间
            map.put("registerDate",item.getRegisterDate()== null?"":DateUtil.getLongFormatDate(item.getRegisterDate())); // 进件时间
            map.put("status", statusMap.get(item.getStatus())); // 活动状态
            map.put("activeDate", item.getActiveDate()== null?"":DateUtil.getLongFormatDate(item.getActiveDate())); // 激活时间
            map.put("userCode", item.getUserCode()); // 所属采购者ID
            map.put("userName", item.getUserName()); // 所属采购者姓名
            map.put("phone", item.getPhone()); // 采购者手机号
            map.put("orgName", item.getOrgName()); // 所属组织
            map.put("activeReturnBonus", item.getActiveReturnBonus() == null ? "":"￥"+item.getActiveReturnBonus().toPlainString()); // 激活返现金额
            map.put("totalBonus", item.getTotalBonus() == null ? "":"￥"+item.getTotalBonus().toPlainString()); // 激活奖金包
            map.put("accountStatus", accountStatusMap.get(item.getAccountStatus())); // 补贴入账状态
            map.put("completeDate", item.getCompleteDate()== null?"":DateUtil.getLongFormatDate(item.getCompleteDate())); // 补贴发放时间/完成时间
            map.put("oneUserCode", item.getOneUserCode()); // 一级编号
            map.put("oneUserName", item.getOneUserName()); // 一级名称
            map.put("oneUserType", userTypeMap.get(item.getOneUserType())); // 一级身份
            map.put("oneUserProfit", item.getOneUserProfit() == null ? "":item.getOneUserProfit().toPlainString()); // 一级分润
            map.put("twoUserCode", item.getTwoUserCode()); // 二级编号
            map.put("twoUserName", item.getTwoUserName()); // 二级名称
            map.put("twoUserType", userTypeMap.get(item.getTwoUserType())); // 二级身份
            map.put("twoUserProfit", item.getTwoUserProfit() == null ? "":item.getTwoUserProfit().toPlainString()); // 二级分润
            map.put("thrUserCode", item.getThrUserCode()); // 三级编号
            map.put("thrUserName", item.getThrUserName()); // 三级名称
            map.put("thrUserType", userTypeMap.get(item.getThrUserType())); // 三级身份
            map.put("thrUserProfit", item.getThrUserProfit() == null ? "":item.getThrUserProfit().toPlainString()); // 三级分润
            map.put("fouUserCode", item.getFouUserCode()); // 四级编号
            map.put("fouUserName", item.getFouUserName()); // 四级名称
            map.put("fouUserType", userTypeMap.get(item.getFouUserType())); // 四级身份
            map.put("fouUserProfit", item.getFouUserProfit() == null ? "":item.getFouUserProfit().toPlainString()); // 四级分润
            map.put("orgProfit", item.getOrgProfit() == null ? "":"￥"+item.getOrgProfit().toPlainString()); // 组织分润
            map.put("provinceName", item.getProvinceName()); // 省
            map.put("cityName", item.getCityName()); // 市
            map.put("districtName", item.getDistrictName()); // 区
            map.put("remark", item.getRemark()); // 备注

            data.add(map);
        }
	}

	@Override
	public List<MposTradeOrder> selectMposTradeOrderList(MposTradeOrder mposTradeOrder, Page<MposTradeOrder> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		mposTradeOrder.setOrgId(Long.valueOf(entityId));
		return mposTradeOrderDao.selectMposTradeOrderList(mposTradeOrder,page);
	}

	@Override
	public MposTradeOrder selectMposTradeOrderSum(MposTradeOrder mposTradeOrder) {
		return mposTradeOrderDao.selectMposTradeOrderSum(mposTradeOrder);
	}

	@Override
	public void exportMposTradeOrder(HttpServletResponse response, MposTradeOrder mposTradeOrder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			mposTradeOrder.setOrgId(Long.valueOf(entityId));
			Page<MposTradeOrder> page = new Page<>(0, Integer.MAX_VALUE);
			List<MposTradeOrder> list = mposTradeOrderDao.selectMposTradeOrderList(mposTradeOrder,page);
			List<Map<String, String>> data = new ArrayList<>() ;
			setMposTradeOrderListData(list,data);

			String[] cols = new String[] {
					"orderNo","orgName","userCode","phone","v2OrderNo","v2MerchantCode","v2MerchantName",
					"v2MerchantPhone", "snNo","productType","tradeAmount","tradeDate",
					"settleCycle","transType", "receiveType","actualPaymentFee","settleStatus","transStatus",
					"status", "accountStatus",
					"orgTradeProfit","orgPaymentProfit","totalBonusConf",
					"oneUserCode","oneUserName","oneUserType","oneUserProfit",
					"twoUserCode","twoUserName","twoUserType","twoUserProfit",
					"thrUserCode","thrUserName","thrUserType","thrUserProfit",
					"fouUserCode","fouUserName","fouUserType","fouUserProfit",
                    "orgProfit",
					"provinceName","cityName","districtName"
			};
			String[] colsName = new String[]{
					"订单编号","所属组织","所属采购者编号","采购者手机号","V2交易订单号","V2商户编号","V2商户名称",
					"商户手机号","机具sn号","设备类型","交易金额","交易时间",
					"结算周期","交易类型","收款类型","实际出款手续费","结算状态","交易状态",
					"订单状态","入账状态",
					"交易-OEM分润金额","出款-OEM分润金额","交易总奖金包",
					"一级编号","一级名称","一级身份","一级分润",
					"二级编号","二级名称","二级身份","二级分润",
					"三级编号","三级名称","三级身份","三级分润",
					"四级编号","四级名称","四级身份","四级分润",
                    "组织分润",
					"省","市","区"
			};
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "Mpos交易信息"+sdf.format(new Date())+"."+export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出Mpos交易信息异常!", e);
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 创建Mpos交易信息导出数据集合
	 * @param list
	 * @param data
	 */
	private void setMposTradeOrderListData(List<MposTradeOrder> list,List<Map<String, String>> data) {
		Map<String, String> userTypeMap = getUserTypeMap();

		// 结算周期 1-T0 2-T1
		Map<Integer, String> settleCycleMap = new HashMap<>();
		settleCycleMap.put(1, "T0");
		settleCycleMap.put(2, "T1");

		// 收款类型 1-标准类 2-vip类
		Map<Integer, String> receiveTypeMap = new HashMap<>();
		receiveTypeMap.put(1, "标准类");
		receiveTypeMap.put(2, "vip类");

		// 计算分润状态 0为计算失败 1为计算成功
		Map<String, String> profitStatusMap = new HashMap<>();
		profitStatusMap.put("0", "计算失败");
		profitStatusMap.put("1", "计算成功");

		// 交易类型：（1刷卡支付 2微信支付 3支付宝 4云闪付 5取现 6银联二维码 7快捷支付 8测试交易）  1 POS，2 支付宝，3 微信，4 快捷，5 银联二维码
		Map<String, String> transTypeMap = new HashMap<>();
		transTypeMap.put("1", "POS");
		transTypeMap.put("2", "支付宝");
		transTypeMap.put("3", "微信");
		transTypeMap.put("4", "快捷");
		transTypeMap.put("5", "银联二维码");


		//记账状态;0待入账；1已记账；2记账失败
		Map<String, String> accountStatusMap = new HashMap<>();
		accountStatusMap.put("0", "待入账");
		accountStatusMap.put("1", "已记账");
		accountStatusMap.put("2", "记账失败");

		// 交易状态:0.初始化,1.成功,2.失败
		Map<String, String> transStatusMap = new HashMap<>();
		transStatusMap.put("0", "初始化");
		transStatusMap.put("1", "成功");
		transStatusMap.put("2", "失败");

		// '订单状态:1待付款 2待发货 3待收获 4已收货 9已关闭'
		Map<String, String> statusMap = new HashMap<>();
		statusMap.put("1", "待付款");
		statusMap.put("2", "待发货");
		statusMap.put("3", "待收货");
		statusMap.put("4", "已收货");
		statusMap.put("9", "已关闭");

        //结算状态
        Map<String, String> settleStatusMap = new HashMap<>();
        settleStatusMap.put("0", "未提交");
        settleStatusMap.put("1", "已提交");
        settleStatusMap.put("2", "提交失败");
        settleStatusMap.put("3", "超时");
        settleStatusMap.put("4", "交易成功");
        settleStatusMap.put("5", "交易失败");
        settleStatusMap.put("6", "未知");

		for(MposTradeOrder item: list){

			Map<String,String> map = new TreeMap<>();
			map.put("orderNo", item.getOrderNo()); // 订单编号
			map.put("v2OrderNo", item.getV2OrderNo()); // V2交易订单号
			map.put("v2MerchantCode", item.getV2MerchantCode()); // V2商户编号
			map.put("v2MerchantName", item.getV2MerchantName()); // V2商户名称
			String phoneMask="";
			if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getV2MerchantPhone())){
				phoneMask = mask(item.getV2MerchantPhone(),3,4);
			}
			map.put("v2MerchantPhone", phoneMask); // 商户手机号
			map.put("snNo", item.getSnNo()); // 机具sn号
			map.put("productType", item.getProductTypeName()); // 设备类型
			map.put("tradeAmount", item.getTradeAmount() == null ? "":"￥"+item.getTradeAmount().toPlainString()); //交易金额
			map.put("tradeDate", DateUtil.getLongFormatDate(item.getTradeDate())); // 交易时间
			map.put("settleCycle", settleCycleMap.get(item.getSettleCycle())); // 结算周期
			map.put("transType", transTypeMap.get(item.getTransType())); // 交易类型
			map.put("receiveType", receiveTypeMap.get(item.getReceiveType())); // 收款类型
			map.put("actualPaymentFee", item.getActualPaymentFee() == null ? "":"￥"+item.getActualPaymentFee().toPlainString()); // 实际出款手续费
			map.put("settleStatus", settleStatusMap.get(item.getSettleStatus())); // 结算状态
			map.put("transStatus", transStatusMap.get(item.getTransStatus())); // 交易状态
			map.put("status", statusMap.get(item.getStatus())); // 订单状态
			map.put("accountStatus", accountStatusMap.get(item.getAccountStatus())); // 入账状态
			map.put("userCode", item.getUserCode()); // 所属采购者编号
			map.put("phone", item.getPhone()); // 采购者手机号
			map.put("orgName", item.getOrgName()); // 所属组织
			map.put("orgTradeProfit", item.getOrgTradeProfit() == null ? "":"￥"+item.getOrgTradeProfit().toPlainString()); // 交易-OEM分润金额
			map.put("orgPaymentProfit", item.getOrgPaymentProfit() == null ? "":"￥"+item.getOrgPaymentProfit().toPlainString()); // 出款-OEM分润金额
			map.put("totalBonusConf", item.getTotalBonusConf() == null ? "":"￥"+item.getTotalBonusConf().toPlainString()); // 交易总奖金包
			map.put("oneUserCode", item.getOneUserCode()); // 一级编号
			map.put("oneUserName", item.getOneUserName()); // 一级名称
			map.put("oneUserType", userTypeMap.get(item.getOneUserType())); // 一级身份
			map.put("oneUserProfit", String.valueOf(item.getOneUserProfit())); // 一级分润
			map.put("twoUserCode", item.getTwoUserCode()); // 二级编号
			map.put("twoUserName", item.getTwoUserName()); // 二级名称
			map.put("twoUserType", userTypeMap.get(item.getTwoUserType())); // 二级身份
			map.put("twoUserProfit", String.valueOf(item.getTwoUserProfit())); // 二级分润
			map.put("thrUserCode", item.getThrUserCode()); // 三级编号
			map.put("thrUserName", item.getThrUserName()); // 三级名称
			map.put("thrUserType", userTypeMap.get(item.getThrUserType())); // 三级身份
			map.put("thrUserProfit", String.valueOf(item.getThrUserProfit())); // 三级分润
			map.put("fouUserCode", item.getFouUserCode()); // 四级编号
			map.put("fouUserName", item.getFouUserName()); // 四级名称
			map.put("fouUserType", userTypeMap.get(item.getFouUserType())); // 四级身份
			map.put("fouUserProfit", String.valueOf(item.getFouUserProfit())); //4级分润
			map.put("orgProfit", String.valueOf(item.getOrgProfit())); //4级分润
			map.put("provinceName", item.getProvinceName()); //省
			map.put("cityName", item.getCityName()); //市
			map.put("districtName", item.getDistrictName()); //区

			data.add(map);
		}
	}


	@Override
	public List<MposMerchantTradeCount> selectMposMerchantTradeCountList(MposMerchantTradeCount mposMerchantTradeCount,
																		 Page<MposMerchantTradeCount> page) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		mposMerchantTradeCount.setOrgId(Long.valueOf(entityId));
		return mposMerchantTradeCountDao.selectMposMerchantTradeCountList(mposMerchantTradeCount,page);
	}

	@Override
	public void exportMposMerchantTradeCount(HttpServletResponse response,
											 MposMerchantTradeCount mposMerchantTradeCount) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		OutputStream ouputStream = null;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			mposMerchantTradeCount.setOrgId(Long.valueOf(entityId));
			Page<MposMerchantTradeCount> page = new Page<>(0, Integer.MAX_VALUE);
			List<MposMerchantTradeCount> list = mposMerchantTradeCountDao.selectMposMerchantTradeCountList(mposMerchantTradeCount,page);
			List<Map<String, String>> data = new ArrayList<>() ;
			for(MposMerchantTradeCount item: list){
				Map<String,String> map = new TreeMap<>();

				map.put("v2MerchantCode", item.getV2MerchantCode());
				map.put("v2MerchantName", item.getV2MerchantName());
				String phoneMask="";
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getV2MerchantPhone())){
					phoneMask = mask(item.getV2MerchantPhone(),3,4);
				}
				map.put("v2MerchantPhone", phoneMask);
				map.put("snNo", item.getSnNo());
				map.put("registerDate", DateUtil.getLongFormatDate(item.getRegisterDate()));
				map.put("userCode", item.getUserCode());
				map.put("userName", item.getUserName());
				map.put("phone", item.getPhone());
				map.put("orgName", item.getOrgName());
				map.put("totalTradeAmount", item.getTotalTradeAmount() == null ? "":"￥"+item.getTotalTradeAmount().toPlainString());
				map.put("monthTradeAmount", item.getMonthTradeAmount() == null ? "":"￥"+item.getMonthTradeAmount().toPlainString());
				map.put("near30TradeAmount", item.getNear30TradeAmount() == null ? "":"￥"+item.getNear30TradeAmount().toPlainString());

				data.add(map);
			}

			String[] cols = new String[] {
					"v2MerchantCode","v2MerchantName","v2MerchantPhone","snNo",
					"registerDate","userCode","userName","phone",
					"orgName","totalTradeAmount","monthTradeAmount","near30TradeAmount"
			};
			String[] colsName = new String[]{
					"V2商户编号","V2商户名称","商户手机号","进件机具SN",
					"进件时间","所属采购者ID","所属采购者姓名","采购者手机号",
					"所属组织","累计交易金额","本月交易金额","近30天交易金额"
			};
			ListDataExcelExport export = new ListDataExcelExport();
			String fileName = "商户交易数据汇总信息"+sdf.format(new Date())+"."+export.getFileSuffix();
			String fileNameFormat = new String(fileName.getBytes(),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出商户交易数据汇总信息异常!", e);
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public MposTradeOrder selectMposTradeOrderDetail(String orderNo) {
		return mposTradeOrderDao.selectMposTradeOrderDetail(orderNo);
	}

	@Override
	public List<MposProductType> getMposProductTypeListAll() {
		return mposProductTypeDao.getMposProductTypeListAll();
	}

	/**
	 * 给数字打码加*
	 * @param num  数字
	 * @param front 显示前几位
	 * @param end 显示末几位
	 * @return
	 */
	public static String mask(String num, int front, int end) {
		//身份证不能为空
		if (org.apache.commons.lang3.StringUtils.isEmpty(num)) {
			return null;
		}
		//需要截取的长度不能大于要截取字符的长度
		if ((front + end) > num.length()) {
			return null;
		}
		//需要截取的不能小于0
		if (front < 0 || end < 0) {
			return null;
		}
		//计算*的数量
		int asteriskCount = num.length() - (front + end);
		StringBuffer asteriskStr = new StringBuffer();
		for (int i = 0; i < asteriskCount; i++) {
			asteriskStr.append("*");
		}
		String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
		return num.replaceAll(regex, "$1" + asteriskStr + "$3");
	}
}
