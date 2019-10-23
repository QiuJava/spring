package cn.eeepay.framework.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.Result;

public interface SuperBankService {
	 
	/**
	 * 用户管理查询
	 * @param userInfoSuperBank
	 * @param page
	 * @return
	 */
	List<UserInfoSuperBank> selectByCondions(UserInfoSuperBank userInfoSuperBank, Page<UserInfoSuperBank> page);

	/**
	 * 查询所有银行家组织
	 * @return
	 */
	List<OrgInfo> getOrgInfoList();

	/**
	 * 用户管理导出
	 * @param userInfoSuperBank
	 * @param agentNo
	 * @return
	 */
	List<UserInfoSuperBank> exportUserInfoSuperBank(UserInfoSuperBank userInfoSuperBank, String agentNo);

	/**
	 * 用户管理详情
	 * @param userId
	 * @return
	 */
	Map<String, Object> selectDetail(String userId);

	/**
	 * 获取所有用户
	 * @param userCode
	 * @return
	 */
	List<UserInfoSuperBank> selectUserInfoList(String userCode);

	 /**
     * 条件分页查询订单
     * @param baseInfo
     * @param page
     * @return
     */
    List<OrderMain> selectOrderPage(OrderMain baseInfo, Page<OrderMain> page);

    /**
     * 订单数据汇总
     * @param baseInfo
     * @return
     */
    OrderMainSum selectOrderSum(OrderMain baseInfo);

	/**
     * 导出超级银行家代理授权订单
     * @param order
     */
    void exportAgentOrder(HttpServletResponse response, OrderMain order);

	 /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    OrderMain selectOrderDetail(String orderNo);

	/**
	 * 保险订单详情
	 * @param orderNo
	 * @return
	 */
	OrderMain selectInsuranceOrderDetail(String orderNo);

    /**
     * 导出超级银行家办理信用卡订单
     * @param order
     */
	void exportCreditOrder(HttpServletResponse response, OrderMain order);

	 /**
     * 获取所有的贷款机构
     * @return
     */
    List<LoanSource> getLoanList();
    
    /**
     * 导出超级银行家贷款订单
     * @param order
     */
    void exportLoanOrder(HttpServletResponse response, OrderMain order);

    /**
     * 导出超级银行家收款订单
     * @param order
     */
	void exportReceiveOrder(HttpServletResponse response, OrderMain order);

	/**
	 * 导出超级银行家保险订单
	 * @param response
	 * @param order
	 */
	void exportInsuranceOrder(HttpServletResponse response, OrderMain order);

	/**
	 * 超级银行家还款订单导出
	 * @param response
	 * @param order
	 */
	void exportRepayOrder(HttpServletResponse response, OrderMain order);

	 /**
     * 分润明细订单查询
     * @param baseInfo
     * @param page
     * @return
     */
    List<UserProfit> selectProfitDetailPage(UserProfit baseInfo, Page<UserProfit> page) throws UnsupportedEncodingException ;
    /**
     * 分润明细汇总
     * @param baseInfo
     * @return
     */
    OrderMainSum selectProfitDetailSum(UserProfit baseInfo);

	/**
	 * 获取所有的银行
	 * @return
	 */
	List<CreditcardSource> banksList();

	/**
	 * 查询贷款奖金配置
	 * @param conf
	 * @param page
	 * @return
	 */
	List<LoanBonusConf> getLoanBonusConfList(LoanBonusConf conf, Page<LoanBonusConf> page);

	/**
	 * 超级银行家OEM信息
	 * @return
	 */
	Result orgInfoDetail();

	/**
	 * 信用卡总奖金查询
	 * @param creditCardConf
	 * @param page
	 * @return
	 */
	Result getCreditBonusConf(CreditCardBonus creditCardConf, Page<CreditCardBonus> page);

	/**
	 * 用户管理统计用户数量
	 * @param userInfoSuperBank
	 * @return
	 */
	UserInfoSuperBank selectTotal(UserInfoSuperBank userInfoSuperBank);

	/**
	 * 开二级代理后台
	 * @param userInfoSuperBank
	 * @return
	 */
	int updateOpenTwoAgent(UserInfoSuperBank userInfoSuperBank);

	/**
	 * 订单分润明细导出
	 * @param response
	 * @param order
	 */
	void exportProfitDetail(HttpServletResponse response, UserProfit order);

	/**
     * 导出超级银行家积分超级兑订单
     * @param order
     */
	void exportOpenCredit(HttpServletResponse response, OrderMain order);

	/**
     * 导出超级银行家开通办理信用卡订单
     * @param order
     */
	void exportSuperExchangeOrder(HttpServletResponse response, OrderMain order);

	/**
	 * 查询开发配置明细
	 * @return
	 */
	Result selectDevelopmentConfiguration();
	
	/**
    * 查询彩票订单
    * @param info
    * @param page
    * @return
    */
  	public List<LotteryOrder> qryLotteryOrder(LotteryOrder info, Page<LotteryOrder> page);
	
  	/**
   	 * 彩票订单汇总信息
   	 * @param info
   	 * @return
   	 */
   	public LotteryOrder qrySumOrder(LotteryOrder info);
  	
	/**导出彩票代购订单*/
    public void exportLotteryOrder(HttpServletResponse response, LotteryOrder baseInfo);

    List<RankingRecordInfo> queryRankingRecord(RankingRecordInfo baseInfo, Page<LotteryOrder> page);

	Map<String,Object> queryRankingRecordSum(RankingRecordInfo baseInfo);

	List<RankingPushRecordInfo>  queryRankingPushRecordPage(RankingPushRecordInfo baseInfo, Page<RankingPushRecordInfo> page);

	String selectRankingPushRecordTotalMoneySum(RankingPushRecordInfo baseInfo);

	String selectRankingPushRecordPushTotalMoneySum(RankingPushRecordInfo baseInfo);

	void exportRankingPushRecord(HttpServletResponse response,RankingPushRecordInfo baseInfo);

	RankingRecordInfo getRankingRecordById(String recordId);

	List<RankingRecordDetailInfo> queryRankingRecordDetailPage(String recordId, Page<RankingRecordDetailInfo> page);

	String queryRankingRecordDetailUserTotalAmountSum(String recordId);
	
	Result carOrderDetail(String orderNo);
	
	CarOrder getCarOrders(CarOrder order,Page<CarOrder> page);
	
	void exportCarOrder(HttpServletResponse response, CarOrder order);

    List<InsuranceCompany> getCompanyNickNameList();



	/**
     * 导出超级银行家征信记录
     * @param order
     */
    void exportInquiryOrder(HttpServletResponse response, ZxProductOrder order);

	/**
	 * 兑换机构
	 * @return
	 */
	List<BonusConf> getBonusConfList();

	/**
	 * 查找机具列表
	 * @param baseInfo
	 * @param page
	 * @return
	 */
	List<MposMachines> selectMposMachinesList(MposMachines baseInfo,Page<MposMachines> page);

	/**
	 * 导出机具列表
	 * @param response
	 * @param mposMachines
	 */
	void mposMachinesExport(HttpServletResponse response, MposMachines mposMachines);



	List<MposOrder>  selectMposOrderList(MposOrder baseInfo, Page<MposOrder> page);

	MposOrder mposOrderDetail(String orderNo);

	void exportMposOrder(HttpServletResponse response, MposOrder mposOrder);

	List<MposMachines> selectMposMachinesByOrderId(Long id);

	MposOrderSum selectMposOrderSum(MposOrder baseInfo);

	Result mposOrderShip(MposOrder mposOrder, List<String> snList);

	int mposOrderUpdate(MposOrder mposOrder);

	/**
	 * mpos明细订单查询
	 * @param baseInfo
	 * @param page
	 * @return
	 */
	List<UserProfit> selectMposProfitDetailPage(UserProfit baseInfo, Page<UserProfit> page) throws UnsupportedEncodingException ;

	/**
	 * 导出mpos分润详情
	 * @param order
	 */
	void mposProfitDetailExprort(HttpServletResponse response, UserProfit order);


	/**
	 * 获取Mpos激活信息
	 * @param mposActiveOrder
	 * @param page
	 * @return
	 */
	List<MposActiveOrder> selectMposActiveOrderList(MposActiveOrder mposActiveOrder, Page<MposActiveOrder> page);

	/**
	 * 获取Mpos激活汇总信息
	 * @param mposActiveOrder
	 * @return
	 */
	MposActiveOrder selectMposActiveOrderSum(MposActiveOrder mposActiveOrder);

	/**
	 * 导出Mpos激活信息
	 * @param response
	 * @param mposActiveOrder
	 */
	void exportMposActiveOrder(HttpServletResponse response, MposActiveOrder mposActiveOrder);

	/**
	 * 获取Mpos交易信息
	 * @param mposTradeOrder
	 * @param page
	 * @return
	 */
	List<MposTradeOrder> selectMposTradeOrderList(MposTradeOrder mposTradeOrder, Page<MposTradeOrder> page);

	/**
	 * 获取Mpos交易汇总信息
	 * @param mposTradeOrder
	 * @return
	 */
	MposTradeOrder selectMposTradeOrderSum(MposTradeOrder mposTradeOrder);

	/**
	 * 导出Mpos交易信息
	 * @param response
	 * @param mposTradeOrder
	 */
	void exportMposTradeOrder(HttpServletResponse response, MposTradeOrder mposTradeOrder);

	/**
	 * 获取商户交易数据汇总信息
	 * @param mposMerchantTradeCount
	 * @param page
	 * @return
	 */
	List<MposMerchantTradeCount> selectMposMerchantTradeCountList(MposMerchantTradeCount mposMerchantTradeCount, Page<MposMerchantTradeCount> page);

	/**
	 * 导出商户交易数据汇总信息
	 * @param response
	 * @param mposMerchantTradeCount
	 */
	void exportMposMerchantTradeCount(HttpServletResponse response, MposMerchantTradeCount mposMerchantTradeCount);

	/**
	 * 查询所有的mpos设备类型列表
	 * @return
	 */
	List<MposProductType> getMposProductTypeListAll();

	/**
	 * 根据订单编号查询交易
	 * @return
	 */
    MposTradeOrder selectMposTradeOrderDetail(String orderNo);
}
