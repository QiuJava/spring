package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.WithDrawExtraCheck;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AgentInfoService {
	public AgentInfo getCurAgentInfo();
	public String getCurAgentNo();

	 public AgentInfo selectByName(String name);
	
	 public List<AgentInfo> selectAllInfo(String agentNo);
	 
	 public List<AgentInfo> selectAllInfoByTeamId(String teamId);
	 
	 /**
	  * 根据代理商id查询所有的子代理商
	 * @param integer 
	  * @param agentNode: 如果类似0-20:查询所有子代理商，且包括自己
	  *                   如果类似0-20-:查询所有子代理商，不包括自己
	  * @return
	  */
	 public List<AgentInfo> selectChildAgentByAgentNode(String agentNode, Integer integer);
	 
	 public  AgentInfo selectByagentNo(String agentNo);
	 
	 public AgentInfo selectByParentId(String parentId);

	/**
	 * 设置安全密码
	 * @param principal
	 * @return
	 */
	 Integer updateSafePassword(String password, UserLoginInfo principal);

	 /**
	  * 查询代理商所代理的业务产品gw
	  * @param agentNo
	  * @return
	  */
	 public List<BusinessProductDefine> queryAgentProduct(String agentNo);
	 
	 /**
	  * 根据登录用户获取代理商信息
	  * @return
	  */
	 public AgentInfo selectByPrincipal();
	 
	/**
	 * 根据登录用户获取代理商信息 9楼
	 * @param agentNo
	 * @return
	 */
	public AgentInfo selectByPrincipalApi(String agentNo);

	public List<AgentShareRule> getAgentShareInfos(String agentNo);

	public Map<String,Object> getAgentServices(Map<String, Object> json);
	
	public List<ServiceRate> getServiceRate(List<Integer> bpIds,String agentId); 
	
//	public List<ServiceQuota> getServiceQuota(List<Integer> bpIds,String agentId);

	public void saveAgentInfo(JSONObject json);

	void setShareRule(AgentShareRule share);

	public Map<String, Object> queryAgentInfoList(Map<String, Object> params, Page<AgentInfo> page);

	public Map<String,Object> queryAgentProducts(String agentNo,Integer teamId);

	public Map<String, Object> queryAgentInfoDetail(String agentNo, Integer teamId);
	
	public List<BusinessProductDefine> queryMerProduct(String agentNo,String Product);

	public String delAgent(String agentNo);

	public Map<String, Object> queryAgentInfoDetail(String agentNo, Integer teamId, boolean b);

	public void profitExpression(AgentShareRule rule);

	public String updateAgent(String data);
//	public String updateAgentNoSafePhone(String data);

	public Map<String, Object> getNewAgentServices(Map<String, Object> map);

	List<JoinTable> selectProductByTeamId(Integer id);

	public Map<String, Object> queryMyInfo(String agentNo, String oneAgentNo, Integer teamId);

	public String getOneAgentNo(String agentNo);

	public Map<String, Object> getMyAccount(String entityId);
	
//	public Map<String, Object> getHappySendMyAccount(String entityId);

	//20170112 tiangh
	public AgentInfo selectLevelOne(Integer userId);

	//227tgh
	public int updateRestPwd(String mobilephone, String teamId);

	//330tgh
//	public boolean findFunctionManage(String functionNumber);
	/*
	 * 只有一级代理商才会显示,不用开关控制
	 */
	public boolean findFunctionManage();
	public boolean findSuperPush(String functionNumber);

	public Map<String, Object> findActivityIsSwitch(String functionNumber,String agentNo);

	public List<Map<String, String>> selectSubjectNo();

	/**
	 * 欢乐送补贴提现 tgh331
	 * @param money
	 * @return
	 */
	public Map<String, Object> UpdateWithDrawCash(String money);

	/**
	 * 代理商欢乐送补贴提现手续费
	 * @param serviceId
	 * @return
	 */
	public Map<String, Object> getSingleNumAmount(String serviceId);

	/**
	 * 代理商账户余额提现tgh415
	 * @param money
	 * @return
	 */
	public Map<String, Object> UpdateTakeBalance(String money);

	/**
	 * 信用卡余额提现
	 * 超级银行家分润账号提现 superBank
	 * @param money
	 * @return
	 */
	Map<String,Object> UpdateTakeReplayBalance(String money,String type);
	/**
	 * 代理商分润日结报表tgh418
	 * @param param
	 */
	public Map<String, Object> selectAllShareList(String param);

	/***
	 * 代理商预冻结明细查询
	 * @param json
	 * @return
	 * @author ZengJA
	 * @date 2017-07-06 16:10:48
	 */
    Map<String, Object> preliminaryFreezeQuery(JSONObject json);

	/***
	 * 分润预调账明细
	 * @param json
	 * @return
	 * @author rpc
	 * @date 2019-09-16 10:27:48
	 */
	Map<String, Object> profitAdvanceQuery(String url,JSONObject json);

	/**
	 * 预冻结明细导出
	 * @param json
	 * @return
	 * @author ZengJA
	 * @date 2017-07-06 16:10:55
	 */
	String preliminaryFreezeExport(JSONObject json);
	/**
	 * 分润预调账明细导出
	 * @param json
	 * @return
	 * @author rpc
	 * @date 2019-09-16 16:23:55
	 */
	String profitAdvanceExport(String url,JSONObject json);

	/**
	 * 解冻明细查询
	 * @param json
	 * @return
	 * @author ZengJA
	 * @date 2017-07-07 17:48:42
	 */
	Map<String, Object> unFreezeQuery(JSONObject json);

	Map<String, Object> preliminaryAdjustQuery(JSONObject json);

	String preliminaryAdjustExport(JSONObject json);

	/**
	 * 解冻明细导出
	 * @param json
	 * @return
	 * @author ZengJA
	 * @date 2017-07-07 17:48:45
	 */
	String unFreezeExport(JSONObject json);

    /**
	 * 提现手续费计算tgh424
	 * @param money
	 * @param map1
	 * @return
	 */
	public Map<String, Object> getRateSingleNumAmount(String money, Map<String, Object> map1);


	/**
	 * 修改代理商的分润日结功能
	 * @param parentAgentNo 	当前登陆的代理商
	 * @param childrenAgentNo	需要被修改的代理商
	 * @param profitSwitch		分润日结功能
	 */
    void updateProfitSwitch(String parentAgentNo, String childrenAgentNo, int profitSwitch);

	/**
	 * 批量修改代理商的分润日结功能
	 * @param parentAgentNo 	当前登陆的代理商
	 * @param childrenAgentNos	需要被修改的代理商
	 * @param profitSwitch		分润日结功能
	 */
    void batchUpdateProfitSwitch(String parentAgentNo, List<String> childrenAgentNos, int profitSwitch);
	/**
	 * 批量修改代理商推广功能
	 * @param parentAgentNo 	当前登陆的代理商
	 * @param childrenAgentNos	需要被修改的代理商
	 * @param promotionSwitch		推广功能
	 */
	void batchUpdatePromotionSwitch(String parentAgentNo, List<String> childrenAgentNos, int promotionSwitch);
	/**
	 * 批量修改代理商欢乐返返现功能
	 * @param parentAgentNo 	当前登陆的代理商
	 * @param childrenAgentNos	需要被修改的代理商
	 * @param cashBackSwitch		欢乐返返现功能
	 */
	void batchUpdateCashBackSwitch(String parentAgentNo, List<String> childrenAgentNos, int cashBackSwitch);
	/**
	 * 根据业务id获取服务费率,需要考虑业务组
	 * @param bpIds	 业务id
	 * @param parentAgentNo 上级代理商编号
	 * @return 服务费率
	 */
    List<ServiceRate> getNewAgentServicesByBpId(List<String> bpIds, String parentAgentNo);

	/**
	 * 获取交易分润明细
	 * @param paramBean 参数
	 * @return 交易分润明细集合
	 */
	ProfitDaySettleDetailBean profitDaySettleDetailList(ProfitDaySettleDetailParamBean paramBean);

	/**
	 * 获取交易分润明细列表,用于导出
	 * @param paramBean 参数
	 * @return 交易分润明细集合
	 */
	List<ProfitDaySettleDetailBean.DataList> exportProfitDaySettleDetailList(ProfitDaySettleDetailParamBean paramBean);

	/**
	 * 查询当前代理商以及直接下级代理商
	 * @param entityId
	 * @return
	 */
	List<AgentInfo> selectSelfAndDirectChildren(String entityId);

	/**
	 * 开启/关闭代理商业务产品
	 *
     * @param loginAgentNo 登陆代理商编号
     * @param agentNo    代理商编号
     * @param bpId        业务产品编号
     * @param status    状态
     * @return 更新状态
	 */
	boolean updateAgentProStatus(String loginAgentNo, String agentNo, String bpId, String status);

	/**
	 * 每日分润报表,获取"分润调账统计"和"分润冻结统计"的数据
	 * @param agentNo 代理商编号
	 * @return
	 */
    Map<String, Object> findAgentProfitCollection(String agentNo);

	/**
	 * 查询该商户的是否属于登陆的代理商的商户
	 * @param merchantBpId 商户进件Id
	 * @param loginAgentNo 登陆代理商
	 * @return
	 */
    boolean merchantIsBelongToAgent(String merchantBpId, String loginAgentNo);
	/**
	 * 查询该商户的是否属于登陆的代理商的直营商户,且审核状态为失败的
	 * @param merchantBpId 商户进件Id
	 * @param loginAgentNo 登陆代理商
	 * @return
	 */
    boolean merchantIsDirectBelongToAgent(String merchantBpId, String loginAgentNo);
	/**
	 * 查询该机具的是否属于登陆的代理商的商户
	 * @param terminalId 商户进件Id
	 * @param loginAgentNo 登陆代理商
	 * @return
	 */
    boolean terminalIsBelongToAgent(String terminalId, String loginAgentNo);

	/**
	 * 设置登陆代理商的bpId为默认的费率
	 * @param bpId			需要设置为默认费率的业务产品
	 * @param loginAgentNo	登陆代理商
	 * @return	更新状态
	 */
	boolean updateDefaultFlagSwitch(String bpId, String loginAgentNo);

	/**
	 * 修改代理商的推广功能
	 * @param parentAgentNo 	当前登陆的代理商
	 * @param childrenAgentNo	需要被修改的代理商
	 * @param promotionSwitch		分润日结功能
	 */
	void updatePromotionSwitch(String parentAgentNo, String childrenAgentNo, int promotionSwitch);

	/**
	 * 修改代理商的欢乐返返现功能
	 * @param parentAgentNo 	当前登陆的代理商
	 * @param childrenAgentNo	需要被修改的代理商
	 * @param cashBackSwitch		欢乐返返现功能
	 */
	void updateCashBackSwitch(String parentAgentNo, String childrenAgentNo, int cashBackSwitch);


	/**
	 * 红包余额查询
	 * @return
	 */
	Map<String, Object> selectBalance();
	/**
	 * 红包收支明细查询
	 * @param info
	 * @return
	 */
	Map<String, Object> selectRedEnvelopesDetails(RedEnvelopesDetails info,@Param("page")Page<RedEnvelopesDetails> page);

	/**
	 * 红包收支明细导出
	 * @param redEnvelopesDetails
	 * @return
	 */
	List<RedEnvelopesDetails> exportRedEnvelopesDetails(RedEnvelopesDetails redEnvelopesDetails);

	/**
	 * 红包余额提现,默认全部提现,提现到银行家分润账户余额中
	 * @param redBalance
	 * @return
	 */
	Map<String, Object> updateWithdrawRedBalance(String redBalance);

    List<AgentInfo> selectDirectChildren(String entityId);

    /**
     * 查询当前登录用户是否是开发平台
     * @param entityId
     * @return
     */
    Integer selectIsOpen(String entityId);

	/**
	 * 积分兑换余额提现
	 * @param money	 提现金额
	 */
	boolean takeRedemBalance(String money);

	/**
	 * 新增代理商 欢乐返活动查询t
	 * @return
	 */
	List<AgentActivity> selectHappyBackList();

	/**
	 * 查询下发返现金额及税额百分比
	 * @param activityTypeNo
	 * @return
	 */
	Map<String, Object> selectByActivityTypeNo(String activityTypeNo);

	/**
	 * 积分兑换激活版余额提现
	 * @param money	 提现金额
	 */
	boolean takeRedemActiveBalance(String money);

	/**
	 * 通用的提现服务
	 * @param moneyStr		提现金额
	 * @param subjectNo		提现科目编号
	 * @param subType		出款子类型
	 */
	boolean commonWithdrawCash(String moneyStr, String subjectNo, String subType);

	/**
	 * 通用的提现服务
	 * @param moneyStr		提现金额
	 * @param subjectNo		提现科目编号
	 * @param subType		出款子类型
	 * @param check		提现额外需要检验的信息,如果抛异常,表示校验失败
	 */
	boolean commonWithdrawCash(String moneyStr, String subjectNo, String subType, WithDrawExtraCheck check);
	
	/**
	 * 通用的提现服务
	 * @param moneyStr		提现金额
	 * @param subjectNo		提现科目编号
	 * @param subType		出款子类型
	 * @param check		提现额外需要检验的信息,如果抛异常,表示校验失败
	 */
	boolean commonWithdrawCashApi(String moneyStr, String subjectNo, String subType,String entityId,String userId, WithDrawExtraCheck check);

	/**
	 * 查询默认总开关的状态
	 * @return
	 */
	Map<String,Object> selectDefaultStatus();

	/**
	 * 查询当前代理商有没有差异化设置开关
	 * @param entityId
	 * @return
	 */
	Map<String,Object> selectAccountStatus(String entityId);

	/**
	 * 根据手机号查询代理商
	 * @param mobilephone
	 * @return
	 */
    AgentInfo selectByMobilephoneAndTeamId(String mobilephone, String teamId);

	/**
	 * 机具款项账户余额提现
	 * @param moneyStr
	 * @param subjectNo
	 * @param subType
	 * @return
	 */
	Map<String,Object> takeTerminalBalance(String moneyStr, String subjectNo, String subType);

	/**
	 * 通道金额不足,关闭开关
	 * @param valueOf
	 */
	Integer updateWithdrawSwitch(Integer valueOf);

	/**
	 * 打开状态的可用出款通道条数
	 * @param serviceType
	 * @return
	 */
	List<Map<String, Object>> selectByServiceType(String serviceType);

	/**
	 * 查询所有直属代理商
	 * @param entityId
	 * @return
	 */
	List<Map<String, Object>> selectAllInfoBelong(String entityId);
	
	/**
	 * 获取boss配置的授权代理商
	 * 
	 * @param entityId
	 * @return
	 */
	public List<AgentInfo> getConfigInfo(String entityId);
	
	/**
	 * 判断当前用户是否拥有查看选择代理商的权限
	 * @param currAgentNo
	 * @param selectAgentNo
	 * @return
	 */
	public boolean isAuth(String currAgentNo, String selectAgentNo);
	
	/**
	 * 判断用户是否是一级代理商
	 * @param userName
	 * @return
	 */
	public boolean isOneAgent(String agentNo);

	public AgentInfo getOneAgentByAgentNo(String entityId);

	public void updateFullPrizeSwitch(String entityId, String agentNo, int fullPrizeSwitch);

	public void updateNotFullDeductSwitch(String entityId, String agentNo, int notFullDeductSwitch);

	public int updateSafePhoneByAgentNo(String agentNo, String checkphone);

	public String getSafePhone(String agentNo);

	Integer updateAgentByIdCardNo(String idCardNo,String agentNo);

	List<AgentActivity> getAgentActivity();
	
	public boolean getFunctionSwitch(AgentInfo agent,String functionNumber);


    AgentActivity selectByAgentNoAndActivityType(String agentNo, String activityTypeNo);

	Map<String, Object> selectHappyBackDefaultParam(String s);

	int insertAgentActivity(List<AgentActivity> happyBackList);

	int updateAgentActivityStatus(Long id, Boolean status);

	int updateAgentActivityStatusByAgentNode(String agentNode, boolean status, String activityTypeNo);

    int selectUpdateAgentStatusByActivityTypeNo(String activityTypeNo);

    AgentActivity selectAgentActivityByAgentNoAndTypeNo(String agentNo, String activityTypeNo);

	/**
	 * 获取欢乐返子类型与组织的关系map
	 * @return key 欢乐返子类型, value 欢乐返子类型对应的组织id
	 */
	Map<String, List<String>> getActivityTypeNoAndTeamIdMap(String agentNo);
}

