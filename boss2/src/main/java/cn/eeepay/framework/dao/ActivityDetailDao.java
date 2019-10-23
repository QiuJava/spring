package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.CashBackDetail;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface ActivityDetailDao {

	@Update("update activity_detail set billing_status=#{billingStatus},billing_msg=#{billingMsg},"
			+ "billing_time=now() where id =#{id}")
	int updateBillStatus(@Param("id")Integer id, @Param("billingStatus")int billingStatus,
						   @Param("billingMsg")String billingMsg);

	@Update("update activity_detail set billing_status=#{billingStatus},billing_msg=#{billingMsg},"
			+ "billing_time=now(), status='6' where id =#{id}")
	int updateBillStatusOk(@Param("id")Integer id, @Param("billingStatus")int billingStatus,
						 @Param("billingMsg")String billingMsg);


	@Select("SELECT IFNULL(aht.cash_back_amount,0) FROM activity_detail ad LEFT JOIN activity_hardware_type aht ON ad.activity_type_no = aht.activity_type_no where ad.id =#{id}")
	BigDecimal getMaxAmount(@Param("id")Integer id);

	@SelectProvider(type = SqlProvider.class, method = "selectAllInfo")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectAllInfo(@Param("page") Page<ActivityDetail> page,@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectAllInfo")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectAllInfoAll(@Param("activityDetail")ActivityDetail activityDetail);

	@Update("update activity_detail set discount_status=#{discountStatus},discount_operator=#{discountOperator},"
			+ "discount_time=now() where merchant_no=#{merchantNo} and active_order=#{activeOrder}")
	int updateDiscount(ActivityDetail info);
	
	@Delete("delete from activity_detail where status=#{status} and activity_code=#{activityCode}"
			+ " and merchant_no=#{merchantNo}")
	int deleteByCodeAndMer(ActivityDetail activityDetail);
	

	@SelectProvider(type=SqlProvider.class, method="selectListByParam")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectListByParam(@Param("merchantNo")String merchantNo,@Param("activityCodeList") String[] activityCodeList);
	
	/**
	 * by Ivan
	 * @param id
	 * @return ActivityDetail
	 */
	@Select("SELECT ad.merchant_no,ad.id,ad.activity_code,ad.active_time,ad.enter_time,ad.trans_total,ad.frozen_amout,ad. STATUS,ad.cash_time,ad.active_order,"+
		"ad.cash_order,ad.check_status,ad.discount_status,ad.liquidation_status,ad.account_check_status,ad.cash_back_amount,"
		+ "ai.agent_no,ai.agent_name,"+
		"ai.one_level_id oneAgentNo,oneAgent.agent_name oneAgentName,mi.merchant_name,ah.target_amout,"+
		"cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee "+
		"FROM activity_detail ad "+
		"LEFT OUTER JOIN agent_info ai ON ai.agent_node = ad.agent_node "+
		"LEFT OUTER JOIN agent_info oneAgent ON (oneAgent.agent_no = ai.one_level_id "+
		"AND ai.agent_node = ad.agent_node) "+
		"LEFT OUTER JOIN merchant_info mi ON mi.merchant_no = ad.merchant_no "+
		"LEFT OUTER JOIN activity_hardware ah ON ah.id = ad.activity_id "+
		"LEFT OUTER JOIN collective_trans_order cto ON cto.order_no = ad.active_order " +
		"WHERE ad.id=#{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail selectActivityDetailById(@Param("id")int id);


	//定时任务查询需要的参数
	@Select(
			"select ad.*,ai.agent_no,ai.agent_name,oneAgent.agent_no oneAgentNo,oneAgent.agent_name oneAgentName, " +
					" cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee,mi.recommended_source " +
					" from activity_detail ad " +
					"  LEFT JOIN merchant_info mi ON mi.merchant_no = ad.merchant_no " +
					"  LEFT JOIN agent_info ai ON ai.agent_no = mi.agent_no " +
					"  LEFT JOIN agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no " +
					"  LEFT JOIN collective_trans_order cto ON cto.order_no = ad.active_order " +
					" where ad.id=#{id} "
	)
	@ResultType(ActivityDetail.class)
	ActivityDetail getActivityDetail(@Param("id")int id);

	@Select("select ai.agent_name ,ai.agent_level,ai.parent_id,ai.agent_node," +
			"ai.cash_back_switch agent_cash_back_switch,ai.full_prize_switch," +
			"ai.not_full_deduct_switch,cbd.* " +
			"from cash_back_detail cbd " +
			"LEFT JOIN agent_info ai ON ai.agent_no = cbd.agent_no " +
			"where cbd.ad_id=#{id} and cbd.amount_type=#{amountType} " +
			"ORDER BY ABS(ai.agent_level) ")
	@ResultType(CashBackDetail.class)
	List<CashBackDetail> getCashBackDetailById(@Param("id")Integer id ,@Param("amountType") int amountType);

	@Update("update cash_back_detail set cash_back_switch=#{a.agentCashBackSwitch},entry_status=#{a.entryStatus},"
			+ "entry_time=now(),remark=#{a.remark},pre_transfer_status=#{a.preTransferStatus},pre_transfer_time=#{a.preTransferTime} where id =#{a.id}")
	int updateCashBackDetail(@Param("a") CashBackDetail cashBackDetail);

	/**
	 * by Ivan
	 * @param id
	 * @param status
	 * @param userId
	 * @return
	 */
	@Update("update activity_detail set check_status=#{status},check_operator=#{userId},"
			+ "check_time=now() where id =#{id}")
	int updateAdjustStatus(@Param("id")Integer id, @Param("status")String status,
			@Param("userId")String userId);
	
	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackDetail")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectHappyBackDetail(@Param("page") Page<ActivityDetail> page, @Param("activityDetail")ActivityDetail activityDetail);


	@Select("SELECT ad.id,ad.active_order,ad.activity_code,ad.trans_total,ad.status,mi.merchant_name,mi.merchant_no,ai.agent_name,ai.agent_no,ad.cumulate_trans_amount,ah.activiy_name,aht.activity_type_name "+
			"FROM activity_detail ad "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_node = ad.agent_node "+
			"LEFT OUTER JOIN merchant_info mi ON mi.merchant_no = ad.merchant_no "+
			"LEFT OUTER JOIN activity_hardware ah ON ah.id = ad.activity_id "+
			"LEFT OUTER JOIN activity_hardware_type aht ON aht.activity_type_no = ad.activity_type_no "+
			"WHERE ad.id=#{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail selectHappyBackDetailById(@Param("id") Integer id);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackDetail")
	@ResultType(ActivityDetail.class)
	List<ActivityDetail> selectHappyBackDetailAll( @Param("activityDetail")ActivityDetail activityDetail);

	@Select("SELECT cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark,cbd.amount_type,cbd.pre_transfer_status,cbd.pre_transfer_time" +
			",ai.agent_name,ai.agent_level "+
			"FROM cash_back_detail cbd "+
			"LEFT OUTER JOIN agent_info ai ON ai.agent_no = cbd.agent_no "+
			"WHERE cbd.ad_id=#{id} and cbd.amount_type=#{amountType} order by ai.agent_level asc")
	@ResultType(CashBackDetail.class)
	List<CashBackDetail> selectAgentReturnCashDetailAll(@Param("id")Integer id,@Param("amountType") int amountType);


	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackTotalAmount")
	@ResultType(Map.class)
	Map<String, Object> selectHappyBackTotalAmount(@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackTotalTransTotal")
	@ResultType(Map.class)
	Map<String, Object> selectHappyBackTotalTransTotal(@Param("activityDetail")ActivityDetail activityDetail);

	@SelectProvider(type = SqlProvider.class, method = "selectHappyBackCashBackAmount")
	@ResultType(Map.class)
	Map<String, Object> selectHappyBackCashBackAmount(@Param("activityDetail")ActivityDetail activityDetail);
	
	/**
	 * 修改清算核算同意,且已返代理商
	 * @author tans
	 * @date 2017年6月27日 上午10:41:50
	 * @param id
	 * @param liquidationStatus
	 * @param userId 
	 * @return
	 */
	@Update("update activity_detail set liquidation_status=#{liquidationStatus},liquidation_time=now(),liquidation_operator=#{operator} where status='2' and id=#{id}")
	int updateAgreeLiquidationStatus(@Param("id")Integer id, @Param("liquidationStatus")String liquidationStatus, @Param("operator")Integer userId);

	@Update("update activity_detail set status='6' where status='2' and id=#{id}")
	int updateAgreeLiquidationStatusById(@Param("id")Integer id);
	
	/**
	 * 清算核算
	 * @author tans
	 * @date 2017年7月4日 上午11:55:23
	 * @param id
	 * @param liquidationStatus
	 * @param userId
	 * @return
	 */
	@Update("update activity_detail set liquidation_status=#{liquidationStatus},liquidation_time=now(),liquidation_operator=#{operator} where status='2' and id=#{id}")
	int updatLiquidationStatus(@Param("id")Integer id, @Param("liquidationStatus")String liquidationStatus, @Param("operator")Integer userId);
	
	/**
	 * 财务核算同意，且已返代理商
	 * @author tans
	 * @date 2017年6月27日 上午10:41:58
	 * @param id
	 * @param accountCheckStatus
	 * @return
	 */
	@Update("update activity_detail set account_check_status=#{accountCheckStatus},account_check_time=now(),account_check_operator=#{operator} "
			+ " where status=2 and liquidation_status=1 and id=#{id}")
	int updateAgreeAccountCheckStatus(@Param("id")Integer id, @Param("accountCheckStatus")String accountCheckStatus, @Param("operator")Integer userId);

	@Update("update activity_detail set status='6' "
			+ " where status=2 and liquidation_status=1 and id=#{id}")
	int updateAgreeAccountCheckStatusById(@Param("id")Integer id);

	/**
	 * 财务核算
	 * @author tans
	 * @date 2017年7月4日 下午12:06:45
	 * @param id
	 * @param accountCheckStatus
	 * @param userId
	 * @return
	 */
	@Update("update activity_detail set account_check_status=#{accountCheckStatus},account_check_time=now(),account_check_operator=#{operator} "
			+ " where status=2 and liquidation_status=1 and id=#{id}")
	int updateAccountCheckStatus(@Param("id")Integer id, @Param("accountCheckStatus")String accountCheckStatus, @Param("operator")Integer userId);


	/**
	 * 奖励入账更新  liuks
	 */
	@Update("update activity_detail set status=#{h.status},add_amount_time=now()"
			+ " where id=#{h.id}")
	int updateRewardIsBooked(@Param("h")ActivityDetail activityDetail);

	/**
	 * 欢乐返扣减更新  liuks
	 */
	@Update("update activity_detail set status=#{h.status},minus_amount_time=now()"
			+ " where id=#{h.id}")
	int updateRewardIsBookedMinus(@Param("h")ActivityDetail activityDetail);

	/**
	 * 查询活动商户表
	 */
	@Select("SELECT ad.*,mi.one_agent_no one_agent_no,ai.agent_name one_agent_name,ai.agent_type "+
				" FROM activity_detail ad "+
				"  LEFT JOIN  merchant_info mi ON mi.merchant_no = ad.merchant_no"+
			    "  LEFT JOIN agent_info ai ON mi.one_agent_no = ai.agent_no "+
				"  WHERE ad.id=#{id}")
	@ResultType(ActivityDetail.class)
	ActivityDetail getActivityDetailById(@Param("id")int id);


	/**
	 * 更新欢乐返商户的累计值，并标注已达标  liuks
	 */
	@Update("update activity_detail set "+
			"   cumulate_trans_amount=#{info.cumulateTransAmount}," +
			"   end_cumulate_time=NOW()," +
			"   is_standard=#{info.isStandard},"+
			"   standard_time=#{info.standardTime}"+
			" where id=#{info.id}")
	int updateActivityDetailForStandard(@Param("info")ActivityDetail ad);

	/**
	 * 更新欢乐返商户的累计值  liuks
	 */
	@Update("update activity_detail set "+
			"  cumulate_trans_amount=#{info.cumulateTransAmount}," +
			"  end_cumulate_time=NOW() " +
			" where id=#{info.id}")
	int updateActivityDetailForSum(@Param("info")ActivityDetail ad);

	/**
	 * 更新扣减商户统计值
	 */
	@Update("update activity_detail set"+
			"  cumulate_trans_min_amount=#{info.cumulateTransMinAmount}," +
			"  end_cumulate_min_time=NOW() " +
			" where id=#{info.id}")
	int updateAdDeduction(@Param("info")ActivityDetail ad);


	public class SqlProvider {

		public String selectAllInfo(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT(" ad.id,ad.active_time,ad.enter_time,ad.trans_total,ad.frozen_amout,ad.status,ad.cash_time,"
							+ "ad.active_order,ad.cash_order,ad.check_status,ad.discount_status,ad.check_operator,"
							+ "ad.check_time,ad.discount_operator,ad.discount_time,"
							+ "sd1.sys_name checkStatusStr,"
							+ "sd2.sys_name statusStr,"
							+ "ai.agent_no,ai.agent_name,oneAgent.agent_no oneAgentNo,oneAgent.agent_name oneAgentName,"
							+ "mi.merchant_name,mi.merchant_no,"
							+ "ah.target_amout,ah.activiy_name,"
							+ "aht.activity_type_name,"
							+ "cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee,"
							+ "st.id settleTransferId,st.out_amount merchantOutAmount,st.fee_amount merchantFeeAmount,"
							+ "bsu1.real_name checkOperatorName,bsu2.real_name discountOperatorName");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");
					LEFT_OUTER_JOIN("activity_hardware ah ON ah.id = ad.activity_id ");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no = ad.activity_type_no ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					LEFT_OUTER_JOIN("settle_transfer st on st.status=4 and st.correction = '0' and st.trans_id=ad.cash_order and st.settle_type = '2'");
					LEFT_OUTER_JOIN("boss_shiro_user bsu1 on bsu1.id=ad.check_operator");
					LEFT_OUTER_JOIN("boss_shiro_user bsu2 on bsu2.id=ad.discount_operator");
					//导出的时候，需要显示这两个状态的对应值，如果后面数据量大影响性能
					//那就在导出的时候再去查
					LEFT_OUTER_JOIN("sys_dict sd1 on sd1.sys_value=ad.check_status and sd1.sys_key='CHECK_STATUS'");
					LEFT_OUTER_JOIN("sys_dict sd2 on sd2.sys_value=ad.status and sd2.sys_key='ACTIVITY_STATUS'");
					WHERE("ad.activity_code='002'");
					
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" ad.active_order in ("+activityDetail.getActiveOrder()+")");
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if(activityDetail.getAcqOrgId()!=null){
						WHERE(" cto.acq_org_id=#{activityDetail.acqOrgId}");
					}
					if(StringUtils.isNotBlank(activityDetail.getOneAgentNo())){
						WHERE(" oneAgent.agent_no=#{activityDetail.oneAgentNo}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						WHERE("(mi.merchant_name like concat(#{activityDetail.merchantN},'%') or mi.merchant_no = #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
						if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
							WHERE(" (ai.agent_node = #{activityDetail.agentN} )");
						}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') and ai.agent_node <> #{activityDetail.agentN})");
						} else {    //所有商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') )");
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
					if (activityDetail.getActiveTimeStart()!=null) {
					WHERE(" ad.active_time>=#{activityDetail.activeTimeStart}");
				}
					if (activityDetail.getActiveTimeEnd()!=null) {
						WHERE(" ad.active_time<=#{activityDetail.activeTimeEnd}");
					}

					if (activityDetail.getEnterTimeStart()!=null) {
						WHERE(" ad.enter_time>=#{activityDetail.enterTimeStart}");
					}
					if (activityDetail.getEnterTimeEnd()!=null) {
						WHERE(" ad.enter_time<=#{activityDetail.enterTimeEnd}");
					}

					if (activityDetail.getCashTimeStart()!=null) {
						WHERE(" ad.cash_time>=#{activityDetail.cashTimeStart}");
					}
					if (activityDetail.getCashTimeEnd()!=null) {
						WHERE(" ad.cash_time<=#{activityDetail.cashTimeEnd}");
					}
					if(StringUtils.isNotBlank(activityDetail.getCheckIds())){
						WHERE(" ad.id in ("+activityDetail.getCheckIds()+") ");
					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
		}
		
		public String selectListByParam(Map<String, Object> param){
			final String[] activityCodeList = (String[]) param.get("activityCodeList");
			String sql = new SQL(){{
				SELECT("id,activity_code");
				FROM("activity_detail");
				if(activityCodeList!=null && activityCodeList.length>0){
					MessageFormat message = new MessageFormat("#'{'activityCodeList[{0}]}");
					StringBuilder sb = new StringBuilder();
					for(int i=0; i<activityCodeList.length; i++){
						sb.append(message.format(new Integer[]{i}));
						sb.append(",");
					}
					sb.setLength(sb.length()-1);
					WHERE("activity_code in (" + sb + ")");
				}
				WHERE("status <> 1");
				WHERE("merchant_no = #{merchantNo}");
			}}.toString();
			System.out.println(sql);
			return sql;
		}

		public String selectHappyBackTotalAmount(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT("SUM(IF(ad.status = 7,IFNULL(ad.empty_amount,0), 0)) totalEmptyAmount," +
							"SUM(IF(ad.status = 8,IFNULL(ad.empty_amount,0),0)) totalAdjustmentAmount," +
							"SUM(IF(ad.status = 9,IFNULL(ad.full_amount ,0),0)) totalFullAmount");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}

		public String selectHappyBackTotalTransTotal(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT("SUM(IFNULL(ad.trans_total,0)) totalTransTotal");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}
		
		public String selectHappyBackCashBackAmount(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT("SUM(IF(ad.billing_status = 0,IFNULL(cbd.cash_back_amount,0), 0)) cashBackAmountNotPay," +
							"SUM(IF(ad.billing_status = 1,IFNULL(cbd.cash_back_amount,0), 0)) cashBackAmountHavePay");
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}
		
		public String selectHappyBackDetail(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			SQL sql = new SQL() {
				{
					SELECT(" ad.id,ad.active_order,ad.active_time,ad.activity_code,ad.enter_time,ad.overdue_time,"
							+ "ad.trans_total,ad.status,ad.liquidation_status,ad.liquidation_time,cbd.cash_back_amount,"
							+ "bsu1.real_name as liquidation_operator,bsu2.real_name as account_check_operator,"
							+ "ad.account_check_status,ad.account_check_time,ad.min_overdue_time,"
							+ "mi.merchant_name,mi.merchant_no,"
							+ "aht.activity_type_name,aht.activity_type_no,"
							+" cto.acq_enname,cto.acq_org_id,cto.trans_amount,cto.merchant_fee,cto.trans_time,cto.acq_merchant_fee, "
							+ "ai.agent_no,ai.agent_name,oneAgent.agent_name oneAgentName,"
							+ "oneAgent.agent_no oneAgentNo,"
							+ "ad.cumulate_trans_amount, ad.end_cumulate_time, ad.cumulate_amount_minus, ad.cumulate_amount_add, ad.empty_amount, ad.full_amount, ad.is_standard,"
							+ "ad.standard_time, ad.minus_amount_time, ad.add_amount_time,mi.recommended_source,ad.repeat_register,ad.billing_time,ad.billing_msg,ad.billing_status"

							);
					FROM("activity_detail ad ");
					LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = ad.merchant_no ");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no = ad.activity_type_no ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = mi.agent_no ");
					LEFT_OUTER_JOIN("agent_info oneAgent ON oneAgent.agent_no = mi.one_agent_no ");
					LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=ad.active_order");
					LEFT_OUTER_JOIN("boss_shiro_user bsu1 on bsu1.id=ad.liquidation_operator ");
					LEFT_OUTER_JOIN("boss_shiro_user bsu2 on bsu2.id=ad.account_check_operator ");
					LEFT_OUTER_JOIN("cash_back_detail cbd on cbd.ad_id=ad.id and cbd.agent_no=mi.one_agent_no and cbd.amount_type=1");
					WHERE(" ad.activity_code IN ('008','009','021') ");
				}
			};
			where(sql,activityDetail);
			sql.ORDER_BY("ad.create_time desc");
			return sql.toString();
		}

		public String selectAgentReturnCashDetail(Map<String, Object> param) {
			final ActivityDetail activityDetail = (ActivityDetail) param.get("activityDetail");
			String sql = new SQL() {
				{
					SELECT("cbd.agent_no,cbd.cash_back_amount,cbd.cash_back_switch,cbd.entry_status,cbd.entry_time,cbd.remark"
									+ "ai.agent_name,ai.agent_level "
							);
					FROM("cash_back_detail cbd ");
					LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = cbd.agent_no ");
					WHERE(" aa.activity_type_no=? IN ('008','009','021') ");
					WHERE(" ad.cbd.active_order=#{activityDetail.activityCode}");
					if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
						WHERE(" aa..active_order in ("+activityDetail.getActiveOrder()+")");
					}
					if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
						WHERE(" ad.activity_code=#{activityDetail.activityCode}");
					}
					if(activityDetail.getAcqOrgId()!=null){
						WHERE(" cto.acq_org_id=#{activityDetail.acqOrgId}");
					}
					if(StringUtils.isNotBlank(activityDetail.getOneAgentNo())){
						WHERE(" oneAgent.agent_no=#{activityDetail.oneAgentNo}");
					}
					if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
						activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
						WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
					}
					if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
						if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
							WHERE(" (ai.agent_node = #{activityDetail.agentN} )");
						}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') and ai.agent_node <> #{activityDetail.agentN})");
						} else {    //所有商户
							WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') )");
						}
					}
					if (activityDetail.getStatus()!=null) {
						WHERE(" ad.status=#{activityDetail.status}");
					}
					if (activityDetail.getActiveTimeStart()!=null) {
						WHERE(" ad.active_time>=#{activityDetail.activeTimeStart}");
					}
					if (activityDetail.getActiveTimeEnd()!=null) {
						WHERE(" ad.active_time<=#{activityDetail.activeTimeEnd}");
					}
					if (activityDetail.getEnterTimeStart()!=null) {
						WHERE(" ad.enter_time>=#{activityDetail.enterTimeStart}");
					}
					if (activityDetail.getEnterTimeEnd()!=null) {
						WHERE(" ad.enter_time<=#{activityDetail.enterTimeEnd}");
					}
					if (activityDetail.getTransTotal()!=null) {
						WHERE(" ad.trans_total=#{activityDetail.transTotal}");
					}
					if (activityDetail.getCashBackAmount()!=null) {
						WHERE(" ad.cash_back_amount=#{activityDetail.cashBackAmount}");
					}
					if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
						WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
					}
					if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
						WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
					}
					if (activityDetail.getLiquidationTimeStart()!=null) {
						WHERE(" ad.liquidation_time>=#{activityDetail.liquidationTimeStart}");
					}
					if (activityDetail.getLiquidationTimeEnd()!=null) {
						WHERE(" ad.liquidation_time<=#{activityDetail.liquidationTimeEnd}");
					}
					if(activityDetail.getMinCumulateTransAmount()!=null){
						WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
					}
					if (activityDetail.getMaxCumulateTransAmount()!=null){
						WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
					}
					if (activityDetail.getMinStandardTime()!=null){
						WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
					}
					if (activityDetail.getMaxStandardTime()!=null){
						WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
					}
					if (activityDetail.getMinMinusAmountTime()!=null){
						WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
					}
					if (activityDetail.getMaxMinusAmountTime()!=null){
						WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
					}
					if (activityDetail.getMinAddAmountTime()!=null){
						WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
					}
					if (activityDetail.getMaxAddAmountTime()!=null){
						WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
					}
					if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
						WHERE(" ad.is_standard = #{activityDetail.isStandard}");
					}
					if(StringUtils.isNotBlank(activityDetail.getCheckIds())){
						WHERE(" ad.id in ("+activityDetail.getCheckIds()+") ");
					}
					ORDER_BY("ad.create_time desc");
				}
			}.toString();
			return sql;
		}

		public void where(SQL sql, ActivityDetail activityDetail) {
			if (StringUtils.isNotBlank(activityDetail.getActiveOrder())) {
				sql.WHERE(" ad.active_order in ("+activityDetail.getActiveOrder()+")");
			}
			if (StringUtils.isNotBlank(activityDetail.getActivityCode())) {
				sql.WHERE(" ad.activity_code=#{activityDetail.activityCode}");
			}
			/*if(activityDetail.getAcqOrgId()!=null){
				sql.WHERE(" cto.acq_org_id=#{activityDetail.acqOrgId}");
			}*/
			if(StringUtils.isNotBlank(activityDetail.getOneAgentNo())){
				sql.WHERE(" mi.one_agent_no=#{activityDetail.oneAgentNo}");
			}
			if (StringUtils.isNotBlank(activityDetail.getMerchantN())) {
				activityDetail.setMerchantN(activityDetail.getMerchantN() + "%");
				sql.WHERE(" (mi.merchant_name like #{activityDetail.merchantN} or mi.merchant_no like #{activityDetail.merchantN})");
			}
			if (StringUtils.isNotBlank(activityDetail.getAgentN())) {
				if(StringUtils.equals("1", activityDetail.getMerchantType())){  // 直营商户
					sql.WHERE(" (ai.agent_node = #{activityDetail.agentN} )");
				}else if(StringUtils.equals("2", activityDetail.getMerchantType())){ // 所有代理商商户
					sql.WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') and ai.agent_node <> #{activityDetail.agentN})");
				} else {    //所有商户
					sql.WHERE(" (ai.agent_node like CONCAT(#{activityDetail.agentN}, '%') )");
				}
			}
			if (activityDetail.getStatus()!=null) {
				sql.WHERE(" ad.status=#{activityDetail.status}");
			}
			if (activityDetail.getActiveTimeStart()!=null) {
				sql.WHERE(" ad.active_time>=#{activityDetail.activeTimeStart}");
			}
			if (activityDetail.getActiveTimeEnd()!=null) {
				sql.WHERE(" ad.active_time<=#{activityDetail.activeTimeEnd}");
			}
			if (activityDetail.getEnterTimeStart()!=null) {
				sql.WHERE(" ad.enter_time>=#{activityDetail.enterTimeStart}");
			}
			if (activityDetail.getEnterTimeEnd()!=null) {
				sql.WHERE(" ad.enter_time<=#{activityDetail.enterTimeEnd}");
			}
			if (activityDetail.getCashBackAmount()!=null) {
				sql.WHERE(" cbd.cash_back_amount=#{activityDetail.cashBackAmount}");
			}
			if (StringUtils.isNotBlank(activityDetail.getLiquidationStatus())) {
				sql.WHERE(" ad.liquidation_status=#{activityDetail.liquidationStatus}");
			}
			if (StringUtils.isNotBlank(activityDetail.getAccountCheckStatus())) {
				sql.WHERE(" ad.account_check_status=#{activityDetail.accountCheckStatus}");
			}
			if (activityDetail.getLiquidationTimeStart()!=null) {
				sql.WHERE(" ad.liquidation_time>=#{activityDetail.liquidationTimeStart}");
			}
			if (activityDetail.getLiquidationTimeEnd()!=null) {
				sql.WHERE(" ad.liquidation_time<=#{activityDetail.liquidationTimeEnd}");
			}
			if(activityDetail.getMinCumulateTransAmount()!=null){
				sql.WHERE(" ad.cumulate_trans_amount >= #{activityDetail.minCumulateTransAmount}");
			}
			if (activityDetail.getMaxCumulateTransAmount()!=null){
				sql.WHERE(" ad.cumulate_trans_amount <= #{activityDetail.maxCumulateTransAmount}");
			}
			if(activityDetail.getMinTransTotal()!=null){
				sql.WHERE(" ad.trans_total >= #{activityDetail.minTransTotal}");
			}
			if (activityDetail.getMaxTransTotal()!=null){
				sql.WHERE(" ad.trans_total <= #{activityDetail.maxTransTotal}");
			}
			if(activityDetail.getMinFullAmount()!=null){
				sql.WHERE(" ad.full_amount >= #{activityDetail.minFullAmount}");
			}
			if (activityDetail.getMaxFullAmount()!=null){
				sql.WHERE(" ad.full_amount <= #{activityDetail.maxFullAmount}");
			}
			if(activityDetail.getMinEmptyAmount()!=null){
				sql.WHERE(" ad.empty_amount >= #{activityDetail.minEmptyAmount}");
			}
			if (activityDetail.getMaxEmptyAmount()!=null){
				sql.WHERE(" ad.empty_amount <= #{activityDetail.maxEmptyAmount}");
			}
			if (activityDetail.getMinStandardTime()!=null){
				sql.WHERE(" ad.standard_time >= #{activityDetail.minStandardTime}");
			}
			if (activityDetail.getMaxStandardTime()!=null){
				sql.WHERE(" ad.standard_time <= #{activityDetail.maxStandardTime}");
			}
			if (activityDetail.getMinMinusAmountTime()!=null){
				sql.WHERE(" ad.minus_amount_time >= #{activityDetail.minMinusAmountTime}");
			}
			if (activityDetail.getMaxMinusAmountTime()!=null){
				sql.WHERE(" ad.minus_amount_time <= #{activityDetail.maxMinusAmountTime}");
			}
			if (activityDetail.getMinAddAmountTime()!=null){
				sql.WHERE(" ad.add_amount_time >= #{activityDetail.minAddAmountTime}");
			}
			if (activityDetail.getMaxAddAmountTime()!=null){
				sql.WHERE(" ad.add_amount_time <= #{activityDetail.maxAddAmountTime}");
			}
			if (StringUtils.isNotBlank(activityDetail.getIsStandard())){
				sql.WHERE(" ad.is_standard = #{activityDetail.isStandard}");
			}
			if(StringUtils.isNotBlank(activityDetail.getCheckIds())){
				sql.WHERE(" ad.id in ("+activityDetail.getCheckIds()+") ");
			}
			if(StringUtils.isNotBlank(activityDetail.getRecommendedSource())){
				sql.WHERE(" mi.recommended_source = #{activityDetail.recommendedSource}");
			}
			if (activityDetail.getRepeatRegister()!=null){
				sql.WHERE(" ad.repeat_register = #{activityDetail.repeatRegister}");
			}
			if (StringUtils.isNotBlank(activityDetail.getActivityTypeNo())){
				sql.WHERE(" ad.activity_type_no = #{activityDetail.activityTypeNo}");
			}
			if (activityDetail.getBillingTimeStart() != null) {
				sql.WHERE(" ad.billing_time>=#{activityDetail.billingTimeStart}");
			}
			if (activityDetail.getBillingTimeEnd() != null) {
				sql.WHERE(" ad.billing_time<=#{activityDetail.billingTimeEnd}");
			}
			if(activityDetail.getBillingStatus() != null ){
				sql.WHERE(" ad.billing_status = #{activityDetail.billingStatus}");
			}
		}
	}



}

