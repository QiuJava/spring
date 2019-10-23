package cn.eeepay.framework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.model.MerchantIncomeBean;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.UserCouponBean;
import cn.eeepay.framework.util.ReadOnlyDataSource;

@ReadOnlyDataSource
public interface ActivityDetailDao {
	
	/**
	 * 判断机具是否参加活动
	 * @param sn
	 * @return
	 */
	@Select(" select count(*) from activity_detail ad inner join collective_trans_order cto  on ad.active_order = cto.order_no "
			+ " where cto.device_sn = #{sn} and cto.trans_status = 'SUCCESS' and ad.status != '1' and ad.merchant_no = #{merchantNo} ")
	@ResultType(Integer.class)
	Integer selectCountBySn(@Param("sn")String sn,@Param("merchantNo")String merchantNo);

	@Select(" select count(*) from activity_detail where status != '1' and merchant_no = #{merchantNo} and active_sn = #{sn}")
	@ResultType(Integer.class)
	Integer selectCountBySnMerchantNo(@Param("sn")String sn,@Param("merchantNo")String merchantNo);

	@Select(" select * from activity_detail where merchant_no = #{merchantNo} ")
	@ResultType(List.class)
	List<ActivityDetail> selectByMerchantNo(@Param("merchantNo")String merchantNo);
	
	@Select(" SELECT agent_service_id FROM activity_config where activity_code = #{activityCode} ")
	@ResultType(String.class)
	String getServiceId(@Param("activityCode")String activityCode);
	
	/**
	 * 查询所有欢乐返子类型编号名称
	 * @return
	 */
	@Select(" SELECT activity_type_no,activity_type_name FROM activity_hardware_type ")
	@ResultType(List.class)
	List<Map<String, Object>> selectActivityTypeNo();
	/**
	 * 当前登录代理商的返现明细
	 * @param adId
	 * @return
	 */
	@Select("SELECT * FROM cash_back_detail where agent_no = #{agentNo} and ad_id = #{adId} and amount_type = 1")
	@ResultType(ActivityDetail.class)
	ActivityDetail selectCashBackDetail(@Param("agentNo")String agentNo, @Param("adId")Integer adId);
	
	@Select(" SELECT t.* FROM terminal_info t WHERE t.activity_type = '6' AND EXISTS "
			+ " ( SELECT 1  FROM agent_info a WHERE a.mobilephone = #{phone} AND t.agent_node LIKE CONCAT(a.agent_node,'%')) ")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectTermi(@Param("phone")String phone);
	
	
	@SelectProvider(type = SqlProvider.class, method = "selectActivityDetail")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectActivityDetail(@Param("page") Page<ActivityDetail> page,@Param("loginAgentNode")String loginAgentNode,@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackDetail")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectHappyBackDetail(@Param("page") Page<ActivityDetail> page,
											   @Param("loginAgent")AgentInfo loginAgent,
											   @Param("activityDetail")ActivityDetail activityDetail);
	
	
	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackDetailJoin")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectHappyBackDetailJoin(@Param("page") Page<ActivityDetail> page,
											   @Param("loginAgent")AgentInfo loginAgent,
											   @Param("activityDetail")ActivityDetail activityDetail,@Param("agentNo")String entityId);
	
	
	
	@SelectProvider(type = SqlProvider.class, method = "selectTotalMoney")
	@ResultType(Map.class)
    Map<String,Object> selectTotalMoney(@Param("activityDetail")ActivityDetail activityDetail, @Param("loginAgent")AgentInfo loginAgent);
	/**
	 * 分页查询注册法和登陆返的券数据
	 * @param userCouponBean    查询条件
     * @param currentAgentNo	当前登陆的代理商
     *@param page                分页信息  @return	查询数据
	 */
    List<UserCouponBean> listUserCouponsByPage(@Param("bean") UserCouponBean userCouponBean,
											   @Param("currentAgentNo") String currentAgentNo,
											   @Param("page") Page<UserCouponBean> page);

	/**
	 * 按条件统计注册返,签到返的 赠送金额 可用金额 已使用金额 过期金额的数据
	 * @param userCouponBean    查询条件
	 * @param currentAgentNo    当前登陆的代理商
	 */
    UserCouponBean countUserCoupons(@Param("bean")UserCouponBean userCouponBean,
									@Param("currentAgentNo") String currentAgentNo);

    List<MerchantIncomeBean> listMerchantIncome(@Param("bean") MerchantIncomeBean bean, Page<MerchantIncomeBean> page);

    public class SqlProvider {
		
		public String selectTotalMoney(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT("IFNULL(SUM(ad.trans_total), 0) totalTransTotal, " +
                            "IFNULL(SUM(IF(ad.status = 7, ad.empty_amount, 0)),0) totalEmptyAmount," +
                            "IFNULL(SUM(IF(ad.status = 8, ad.empty_amount, 0)),0) totalAdjustmentAmount," +
                            "IFNULL(SUM(IF(ad.status = 9, ad.full_amount, 0)),0) totalFullAmount");
					FROM("activity_detail ad ");
					JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					
					WHERE(" ad.activity_code IN ('008','009', '021') " +
							"AND ai.agent_node like concat(#{loginAgent.agentNode}, '%') ");
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" ad.active_order=#{activityDetail.activeOrder}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
						WHERE(" ad.activity_code=#{activityDetail.activityCode}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
						WHERE(" mi.team_entry_id=#{activityDetail.activityCode}");
					}
					if (StringUtils.isNotBlank(activityDetail.getTeamEntryId())) {
						WHERE(" ad.activity_type_no=#{activityDetail.teamEntryId}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
						WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getCashBackAmount())){
						WHERE(" ad.cash_back_amount=#{activityDetail.cashBackAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentNode())) {
//						activityDetail.setAgentN(activityDetail.getAgentN() + "%");
//						WHERE(" (ai.agent_name like #{activityDetail.agentN} or ai.agent_no like #{activityDetail.agentN})");
                        if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
                            WHERE(" ai.agent_node = #{activityDetail.agentNode}");
                        }else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
                            WHERE(" ai.agent_node like CONCAT(#{activityDetail.agentNode}, '_%') ");
                        } else {    //所有商户
                            WHERE("ai.agent_node like CONCAT(#{activityDetail.agentNode}, '%')");
                        }
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if (activityDetail.getBillingStatus()!=null) {
						WHERE(" ad.billing_status=#{activityDetail.billingStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinBillingTime())){
						WHERE(" ad.billing_time >= #{activityDetail.minBillingTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxBillingTime())){
						WHERE(" ad.billing_time <= #{activityDetail.maxBillingTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationTimeStart())) {
						WHERE(" ad.liquidation_time>=concat(#{activityDetail.liquidationTimeStart}, ' 00:00:00')");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationTimeEnd())) {
						WHERE(" ad.liquidation_time<=concat(#{activityDetail.liquidationTimeEnd}, ' 23:59:59')");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinStandardTime())){
						WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxStandardTime())){
						WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinMinusAmountTime())){
						WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxMinusAmountTime())){
						WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinAddAmountTime())){
						WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxAddAmountTime())){
						WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
					}
                    if (StringUtils.isNotBlank(activityDetail.getMobilephone())){
                        WHERE(" mi.mobilephone = #{activityDetail.mobilephone}");
                    }
					//激活订单号、商户编号/名称 不限制时间
					if (StringUtils.isBlank(activityDetail.getActiveOrder())
							|| (StringUtils.isBlank(activityDetail.getMerchantN()))) {
						if (StringUtils.isNotBlank(activityDetail.getActiveTimeStart())) {
							WHERE(" ad.active_time>=concat(#{activityDetail.activeTimeStart}, ' 00:00:00')");
						}
						if (StringUtils.isNotBlank(activityDetail.getActiveTimeEnd())) {
							WHERE(" ad.active_time<=concat(#{activityDetail.activeTimeEnd}, ' 23:59:59')");
						}
					}
					if (activityDetail.getTransTotal()!=null) {
						WHERE(" ad.trans_total=#{activityDetail.transTotal}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
						WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
						WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
					}
                    if(StringUtils.isNotBlank(activityDetail.getMinCumulateTransAmount())){
                        WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
                    }
                    if (StringUtils.isNotBlank(activityDetail.getMaxCumulateTransAmount())){
                        WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
                    }
                    if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
                        WHERE(" ad.is_standard = #{activityDetail.isStandard}");
                    }
					if(StringUtils.isNotBlank(activityDetail.getMerTeamId()) && (!"-1".equals(activityDetail.getMerTeamId()))){
						WHERE(" mi.team_id=#{activityDetail.merTeamId}");
					}
					if (activityDetail.getRepeatRegister()!=null){
						if (activityDetail.getRepeatRegister().equals(2)){
							WHERE(" EXISTS " +
									"(  " +
									"SELECT 1 FROM merchant_info mi2  " +
									"JOIN activity_detail ad2 ON ad2.merchant_no  = mi2.merchant_no " +
									"WHERE mi2.parent_node LIKE CONCAT(#{loginAgent.agentNode}, '%')  " +
									"AND ad2.repeat_register = '1' " +
									"AND mi.id_card_no = mi2.id_card_no " +
									") ");
						}else {
							WHERE(" ad.repeat_register = #{activityDetail.repeatRegister}");
						}

					}
				}
			}.toString();
			return sql;
		}
		public String selectHappyBackDetail(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT("ad.id,ad.agent_node,ad.active_order,ad.active_time,ad.activity_code," +
									"ad.billing_status,ad.billing_time,mi.merchant_name,mi.merchant_no," +
									"ad.enter_time,ad.trans_total,cto.merchant_fee,ad.status,ai.agent_no,ai.agent_name," +
							// 		"(SELECT a.agent_name FROM agent_info a WHERE a.agent_no = ai.one_level_id) oneAgentName," +
							 		"ai.one_level_id oneAgentNo,ad.liquidation_status,ad.liquidation_time,ad.account_check_status," +
									"ad.account_check_time,ad.cash_back_amount,ad.cumulate_trans_amount, ad.end_cumulate_time, " +
									"ad.cumulate_amount_minus, ad.cumulate_amount_add, ad.empty_amount, ad.full_amount, ad.is_standard, " +
									"ad.overdue_time, ad.min_overdue_time,ad.standard_time, ad.minus_amount_time, ad.add_amount_time," +
									"ad.activity_type_no,ai.cash_back_switch,ad.repeat_register,ti.team_name merGroup "
//									 + ", (SELECT DISTINCT a_i.team_name FROM app_info a_i WHERE a_i.team_id = mi.team_id) AS merGroup"
							);
					FROM("activity_detail ad ");
					JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
                    LEFT_OUTER_JOIN("team_info ti ON ti.team_id = mi.team_id");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");

					WHERE(" ad.activity_code IN ('008','009','021') " +
							"and ai.agent_node like concat(#{loginAgent.agentNode}, '%')");
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" ad.active_order=#{activityDetail.activeOrder}");
					}
					if (StringUtils.isNotBlank(activityDetail.getTeamEntryId())) {
						WHERE(" mi.team_entry_id=#{activityDetail.teamEntryId}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
						WHERE(" ad.activity_code=#{activityDetail.activityCode}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityTypeNo())) {
						WHERE(" ad.activity_type_no=#{activityDetail.activityTypeNo}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
						WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getCashBackAmount())){
						WHERE(" ad.cash_back_amount=#{activityDetail.cashBackAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentNode())) {
                        if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
                            WHERE(" ai.agent_node = #{activityDetail.agentNode} ");
                        }else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
                            WHERE(" ai.agent_node like CONCAT(#{activityDetail.agentNode}, '_%')");
                        } else {    //所有商户
                            WHERE(" ai.agent_node like CONCAT(#{activityDetail.agentNode}, '%') ");
                        }
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if (activityDetail.getBillingStatus()!=null) {
						WHERE(" ad.billing_status=#{activityDetail.billingStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinBillingTime())){
						WHERE(" ad.billing_time >= #{activityDetail.minBillingTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxBillingTime())){
						WHERE(" ad.billing_time <= #{activityDetail.maxBillingTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationTimeStart())) {
						WHERE(" ad.liquidation_time>=concat(#{activityDetail.liquidationTimeStart}, ' 00:00:00')");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationTimeEnd())) {
						WHERE(" ad.liquidation_time<=concat(#{activityDetail.liquidationTimeEnd}, ' 23:59:59')");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinStandardTime())){
						WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxStandardTime())){
						WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinMinusAmountTime())){
						WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxMinusAmountTime())){
						WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinAddAmountTime())){
						WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxAddAmountTime())){
						WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
					}
					//激活订单号、商户编号/名称 不限制时间
					if (StringUtils.isBlank(activityDetail.getActiveOrder())
							|| (StringUtils.isBlank(activityDetail.getMerchantN()))) {
						if (StringUtils.isNotBlank(activityDetail.getActiveTimeStart())) {
							WHERE(" ad.active_time>=concat(#{activityDetail.activeTimeStart}, ' 00:00:00')");
						}
						if (StringUtils.isNotBlank(activityDetail.getActiveTimeEnd())) {
							WHERE(" ad.active_time<=concat(#{activityDetail.activeTimeEnd}, ' 23:59:59')");
						}
					}
					if (activityDetail.getTransTotal()!=null) {
						WHERE(" ad.trans_total=#{activityDetail.transTotal}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
						WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
						WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
					}
					if(StringUtils.isNotBlank(activityDetail.getMinCumulateTransAmount())){
						WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxCumulateTransAmount())){
						WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
						WHERE(" ad.is_standard = #{activityDetail.isStandard}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMobilephone())){
						WHERE(" mi.mobilephone = #{activityDetail.mobilephone}");
					}
					if(StringUtils.isNotBlank(activityDetail.getMerTeamId()) && (!"-1".equals(activityDetail.getMerTeamId()))){
						WHERE(" mi.team_id=#{activityDetail.merTeamId}");
					}
					if (activityDetail.getRepeatRegister()!=null){
						if (activityDetail.getRepeatRegister().equals(2)){
							WHERE(" EXISTS " +
									"(  " +
									"SELECT 1 FROM merchant_info mi2  " +
									"JOIN activity_detail ad2 ON ad2.merchant_no  = mi2.merchant_no " +
									"WHERE mi2.parent_node LIKE CONCAT(#{loginAgent.agentNode}, '%')  " +
									"AND ad2.repeat_register = '1' " +
									"AND mi.id_card_no = mi2.id_card_no " +
									") ");
						}else {
							WHERE(" ad.repeat_register = #{activityDetail.repeatRegister}");
						}

					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
		}

		
		public String selectHappyBackDetailJoin(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT(" ad.id,ad.agent_node,ad.active_order,ad.active_time,ad.activity_code,ad.billing_status,ad.billing_time,mi.merchant_name,mi.merchant_no,ad.enter_time,ad.trans_total,cto.merchant_fee,"
							+ "ad.status,ai.agent_no,ai.agent_name,(SELECT a.agent_name FROM agent_info a WHERE a.agent_no = ai.one_level_id) oneAgentName,"
							+ "ai.one_level_id oneAgentNo,ad.liquidation_status,ad.liquidation_time,ad.account_check_status,ad.account_check_time,ad.cash_back_amount,"
							+ "ad.cumulate_trans_amount, ad.end_cumulate_time, ad.cumulate_amount_minus, ad.cumulate_amount_add, ad.empty_amount,ad.full_amount,cbd2.cash_back_amount empty_amount_join, cbd.cash_back_amount full_amount_join, ad.is_standard, ad.overdue_time, ad.min_overdue_time,"
							+ "ad.standard_time, ad.minus_amount_time, ad.add_amount_time,ad.activity_type_no,ai.cash_back_switch,ad.repeat_register,"
							+ "(SELECT DISTINCT a_i.team_name FROM app_info a_i WHERE a_i.team_id = mi.team_id) AS merGroup"
							);
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_node = ad.agent_node ");
//					LEFT_OUTER_JOIN("agent_info aiP ON aiP.agent_no = ai.parent_id ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
//					LEFT_OUTER_JOIN("activity_hardware ah ON ah.id = ad.activity_id ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					
					LEFT_OUTER_JOIN("cash_back_detail cbd ON cbd.ad_id=ad.id AND cbd.agent_no=#{agentNo} AND cbd.amount_type='2' ");
					LEFT_OUTER_JOIN("cash_back_detail cbd2 ON cbd2.ad_id=ad.id AND cbd2.agent_no=#{agentNo} AND cbd2.amount_type='3' ");
					
					WHERE(" ad.activity_code IN ('008','009','021') " +
							"and ad.agent_node like concat(#{loginAgent.agentNode}, '%')");
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" ad.active_order=#{activityDetail.activeOrder}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
						WHERE(" ad.activity_code=#{activityDetail.activityCode}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityTypeNo())) {
						WHERE(" ad.activity_type_no=#{activityDetail.activityTypeNo}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
						WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getCashBackAmount())){
						WHERE(" ad.cash_back_amount=#{activityDetail.cashBackAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentNode())) {
                        if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
                            WHERE(" ai.agent_node = #{activityDetail.agentNode} ");
                        }else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
                            WHERE(" ai.agent_node like CONCAT(#{activityDetail.agentNode}, '_%')");
                        } else {    //所有商户
                            WHERE(" ai.agent_node like CONCAT(#{activityDetail.agentNode}, '%')");
                        }
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if (activityDetail.getBillingStatus()!=null) {
						WHERE(" ad.billing_status=#{activityDetail.billingStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinBillingTime())){
						WHERE(" ad.billing_time >= #{activityDetail.minBillingTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxBillingTime())){
						WHERE(" ad.billing_time <= #{activityDetail.maxBillingTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationTimeStart())) {
						WHERE(" ad.liquidation_time>=concat(#{activityDetail.liquidationTimeStart}, ' 00:00:00')");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationTimeEnd())) {
						WHERE(" ad.liquidation_time<=concat(#{activityDetail.liquidationTimeEnd}, ' 23:59:59')");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinStandardTime())){
						WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxStandardTime())){
						WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinMinusAmountTime())){
						WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxMinusAmountTime())){
						WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMinAddAmountTime())){
						WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxAddAmountTime())){
						WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
					}
					//激活订单号、商户编号/名称 不限制时间
					if (StringUtils.isBlank(activityDetail.getActiveOrder())
							|| (StringUtils.isBlank(activityDetail.getMerchantN()))) {
						if (StringUtils.isNotBlank(activityDetail.getActiveTimeStart())) {
							WHERE(" ad.active_time>=concat(#{activityDetail.activeTimeStart}, ' 00:00:00')");
						}
						if (StringUtils.isNotBlank(activityDetail.getActiveTimeEnd())) {
							WHERE(" ad.active_time<=concat(#{activityDetail.activeTimeEnd}, ' 23:59:59')");
						}
					}
					if (activityDetail.getTransTotal()!=null) {
						WHERE(" ad.trans_total=#{activityDetail.transTotal}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
						WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
						WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
					}
					if(StringUtils.isNotBlank(activityDetail.getMinCumulateTransAmount())){
						WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMaxCumulateTransAmount())){
						WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
						WHERE(" ad.is_standard = #{activityDetail.isStandard}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMobilephone())){
						WHERE(" mi.mobilephone = #{activityDetail.mobilephone}");
					}
					if(StringUtils.isNotBlank(activityDetail.getMerTeamId()) && (!"-1".equals(activityDetail.getMerTeamId()))){
						WHERE(" mi.team_id=#{activityDetail.merTeamId}");
					}
					if (activityDetail.getRepeatRegister()!=null){
						if (activityDetail.getRepeatRegister().equals(2)){
							WHERE(" EXISTS " +
									"(  " +
									"SELECT 1 FROM merchant_info mi2  " +
									"JOIN activity_detail ad2 ON ad2.merchant_no  = mi2.merchant_no " +
									"WHERE mi2.parent_node LIKE CONCAT(#{loginAgent.agentNode}, '%')  " +
									"AND ad2.repeat_register = '1' " +
									"AND mi.id_card_no = mi2.id_card_no " +
									") ");
						}else {
							WHERE(" ad.repeat_register = #{activityDetail.repeatRegister}");
						}

					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
	}	
		
//    public class SqlProvider {

		
		public String selectActivityDetail(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT(" ad.id,ad.active_time,ad.enter_time,ad.trans_total,ad.frozen_amout,ad.status,ad.cash_time,"
							+ "ad.active_order,ad.cash_order,ad.check_status,ad.discount_status,"
//							+ "sd1.sys_name checkStatusStr,"
//							+ "sd2.sys_name statusStr,"
							+ "ai.agent_no,ai.agent_name,"
							+ "aiP.agent_no parentNo,aiP.agent_name parentName,"
							+ "mi.merchant_name,"
							+ "ah.target_amout,"
//							+ "cto.acq_enname,"
							+ "cto.merchant_fee"
//							+ " ,st.id settleTransferId,st.out_amount merchantOutAmount,st.fee_amount merchantFeeAmount"
							);
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_node = ad.agent_node ");
					LEFT_OUTER_JOIN("agent_info aiP ON aiP.agent_no = ai.parent_id ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("activity_hardware ah ON ah.id = ad.activity_id ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					//优化tgh502
//					LEFT_OUTER_JOIN("settle_transfer st on st.status=4 and st.trans_id=ad.cash_order and st.settle_type='2' and st.correction = '0' ");
//					LEFT_OUTER_JOIN("sys_dict sd1 on sd1.sys_value=ad.check_status and sd1.sys_key='CHECK_STATUS'");
//					LEFT_OUTER_JOIN("sys_dict sd2 on sd2.sys_value=ad.status and sd2.sys_key='ACTIVITY_STATUS'");
					WHERE(" ad.activity_code = '002' AND ad.agent_node like CONCAT(#{loginAgentNode}, '%')");
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
						WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
					}
//					if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
//						activityDetail.setAgentN(activityDetail.getAgentN() + "%");
//						WHERE(" (ai.agent_name like #{activityDetail.agentN} or ai.agent_no like #{activityDetail.agentN})");
//					}
					if (StringUtils.isNotBlank(activityDetail.getAgentNode())) {
						if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
							WHERE(" (ai.agent_node = #{activityDetail.agentNode} )");
						}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentNode}, '_%'))");
						} else {    //所有商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentNode}, '%') )");
						}
					}
					if (activityDetail.getCheckStatus()!=null) {
						WHERE(" ad.check_status=#{activityDetail.checkStatus}");
					}
					if (activityDetail.getDiscountStatus()!=null && activityDetail.getDiscountStatus()!=-1) {
						WHERE(" ad.discount_status=#{activityDetail.discountStatus}");
					}
					if (activityDetail.getFrozenAmout()!=null) {
						WHERE(" ad.frozen_amout=#{activityDetail.frozenAmout}");
					}
					if (StringUtils.isNotBlank(activityDetail.getActiveTimeStart())) {
						WHERE(" ad.active_time>=concat(#{activityDetail.activeTimeStart}, ' 00:00:00')");
					}
					if (StringUtils.isNotBlank(activityDetail.getActiveTimeEnd())) {
						WHERE(" ad.active_time<=concat(#{activityDetail.activeTimeEnd}, ' 23:59:59')");
					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
		}
		
	}
    
    @Select("SELECT a_h_t.activity_type_no AS activityTypeNo,a_h_t.activity_type_name AS activityTypeName FROM activity_hardware_type a_h_t WHERE a_h_t.activity_code =#{activityCode} AND a_h_t.trans_amount = 0 ")
	@ResultType(HashMap.class)
	List<Map<String, Object>> finfByActivityCode(String activityCode);
   
    @Select("SELECT " + 
    		"	a_d.active_order AS active_order, " + 
    		"	a_d.id AS id, " + 
    		"	a_d.merchant_no AS merchant_no, " + 
    		"	m_i.merchant_name AS merchant_name, " + 
    		"	a_i.agent_name AS agent_name, " + 
    		"	a_d.`status` AS `status`, " + 
    		"	a_d.active_time AS active_time, " + 
    		"	a_h.activiy_name AS activity_name, " + 
    		"	a_h_t.activity_type_name AS activity_type_name  " + 
    		"FROM " + 
    		"	activity_detail a_d " + 
    		"	LEFT JOIN merchant_info m_i ON a_d.merchant_no = m_i.merchant_no " + 
    		"	LEFT JOIN agent_info a_i ON a_d.agent_node = a_i.agent_node " + 
    		"	LEFT JOIN activity_hardware a_h ON a_d.activity_id = a_h.id " + 
    		"	LEFT JOIN activity_hardware_type a_h_t ON a_h.activity_type_no = a_h_t.activity_type_no  " + 
    		"WHERE " + 
    		"	a_d.id = #{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail selectHappyBackDetailById(@Param("id") Integer id);
    
    @Select("SELECT cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark" +
			",ai.agent_name,ai.agent_level,cbd.pre_transfer_status,cbd.pre_transfer_time "+
			"FROM cash_back_detail cbd "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_no = cbd.agent_no "+
			"WHERE cbd.ad_id=#{adId} and cbd.amount_type=#{type} order by ai.agent_level asc")
	@ResultType(CashBackDetail.class)
	List<CashBackDetail> selectAgentReturnCashDetailAll(@Param("adId")Integer adId, @Param("type")int amountType);

    @Select("SELECT cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark" +
			",ai.agent_name,ai.agent_level,cbd.pre_transfer_status,cbd.pre_transfer_time "+
			"FROM cash_back_detail cbd "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_no = cbd.agent_no "+
			"WHERE cbd.amount_type=#{type} and cbd.ad_id=#{id} order by ai.agent_level asc")
	@ResultType(CashBackDetail.class)
	List<CashBackDetail> findAllAgentReturnCashDetail(@Param("id") Integer id,@Param("type") int i);

    
    @Select("SELECT " + 
    		"	full_prize_amount AS full_amount, " + 
    		"	not_full_deduct_amount AS empty_amount  " + 
    		"FROM " + 
    		"	agent_activity  " + 
    		"WHERE " + 
    		"	agent_no = #{agentNo}  " + 
    		"	AND activity_type_no = #{activityTypeNo}")
	ActivityDetail findPrizeDeductAmountByAgentNoAndNo0(@Param("agentNo")String entityId, @Param("activityTypeNo")String activityTypeNo);

    @Select("SELECT " + 
    		"	repeat_full_prize_amount AS full_amount, " + 
    		"	repeat_not_full_deduct_amount AS empty_amount  " + 
    		"FROM " + 
    		"	agent_activity  " + 
    		"WHERE " + 
    		"	agent_no = #{agentNo}  " + 
    		"	AND activity_type_no = #{activityTypeNo}")
	ActivityDetail findPrizeDeductAmountByAgentNoAndNo1(@Param("agentNo")String entityId, @Param("activityTypeNo")String activityTypeNo);

    @Select("SELECT cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark" +
 			",ai.agent_name,ai.agent_level,cbd.pre_transfer_status,cbd.pre_transfer_time "+
 			"FROM cash_back_detail cbd "+
 			"LEFT OUTER JOIN agent_info ai ON ai.agent_no = cbd.agent_no "+
 			"WHERE cbd.amount_type=#{type} and cbd.agent_no=#{agentNo} and cbd.ad_id=#{id} order by ai.agent_level asc")
 	@ResultType(CashBackDetail.class)
	CashBackDetail findAgentReturnCashDetail(@Param("id")Integer adId, @Param("type")int amountType, @Param("agentNo")String curAgentNo);
    
    @Select("SELECT cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark" +
			",ai.agent_name,ai.agent_level,cbd.pre_transfer_status,cbd.pre_transfer_time "+
			"FROM cash_back_detail cbd "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_no = cbd.agent_no "+
			"WHERE cbd.ad_id=#{adId} and cbd.agent_no=#{agentNo} and cbd.amount_type=#{type} order by ai.agent_level asc")
	@ResultType(CashBackDetail.class)
	CashBackDetail selectAgentReturnCashDetail(@Param("adId")Integer adId,  @Param("type")int amountType, @Param("agentNo")String curAgentNo);
}
