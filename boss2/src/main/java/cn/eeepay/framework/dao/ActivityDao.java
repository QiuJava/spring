package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
	@Select("SELECT *  FROM activity_config WHERE activity_code=#{activityCode}")
	@ResultType(ActivityConfig.class)
	ActivityConfig selectActivityMainCofig(@Param("activityCode")String activityCode);
	
	// @Select("SELECT * FROM activity_config where activity_code=#{activityCode}")
	@Select("SELECT ah.*,hp.type_name,hp.hp_id FROM activity_hardware ah ,hardware_product hp WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{activityCode} ")
	@ResultType(ActivityHardware.class)
	List<ActivityHardware> selectActivityHardware(@Param("page")Page<ActivityHardware> page,@Param("activityCode")String activityCode);

	@Select("SELECT ah.*,hp.type_name,hp.hp_id FROM activity_hardware ah ,hardware_product hp WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{activityCode} ")
	@ResultType(ActivityHardware.class)
	List<ActivityHardware> selectActivityConfig(@Param("activityCode")String activityCode);

	@Insert({
		"insert into activity_hardware (activity_code,activiy_name,hard_id,price,target_amout,create_time)"
			+ "values " +
			"(#{n.activityCode},#{n.activiyName},#{n.hardId},#{n.price},#{n.targetAmout},now());"
	})
	int insertActivityHardware(@Param("n")ActivityHardware n);
	  
	@Update("update activity_config set start_time=#{n.startTime},end_time=#{n.endTime},wait_day=#{n.waitDay},cash_service_id=#{n.cashServiceId},agent_service_id=#{n.agentServiceId},"
			+ "cumulate_trans_day=#{n.cumulateTransDay},cumulate_amount_minus=#{n.cumulateAmountMinus},cumulate_amount_add=#{n.cumulateAmountAdd},cumulate_trans_minus_day=#{n.cumulateTransMinusDay}"
			+ " where activity_code=#{n.activityCode}")
	int updateActivityConfig(@Param("n")ActivityConfig n);

	@Insert("insert into activity_config (activity_code,start_time,end_time,wait_day,cash_service_id,agent_service_id,"
			+ "cumulate_trans_day,cumulate_amount_minus,cumulate_amount_add,cumulate_trans_minus_day) values (#{n.activityCode},#{n.startTime},#{n.endTime},#{n.waitDay},"
			+ "#{n.cashServiceId},#{n.agentServiceId},#{n.cumulateTransDay},#{n.cumulateAmountMinus},#{n.cumulateAmountAdd},#{n.cumulateTransMinusDay})")
	int insetActivityConfig(@Param("n")ActivityConfig n);

	@Update("update activity_hardware set price=#{activityHardware.price},target_amout=#{activityHardware.targetAmout}"
				+ " where hard_id=#{activityHardware.hardId}")
	int updateActivityHardware(@Param("activityHardware")ActivityHardware activityHardware);

	// -------------------------- 以下是欢乐返活动 !!! ----------------------------

	// SELECT hh.*,hp.type_name FROM hlf_hardware hh, hardware_product hp WHERE hh.hard_id=hp.hp_id AND hh.activity_code='008'
	@Select("SELECT ah.activity_code,ah.hard_id,hp.type_name,ah.activity_type_no,aht.activity_type_name,aht.trans_amount," +
			"aht.cash_back_amount,aht.repeat_register_amount,aht.empty_amount,aht.full_amount,aht.repeat_empty_amount,aht.repeat_full_amount," +
			"ah.cash_last_ally_amount,ah.default_status,ah.cumulate_trans_day,ah.cumulate_amount_minus,ah.cumulate_amount_add,ah.cumulate_trans_minus_day " +
            "FROM activity_hardware ah " +
			"LEFT JOIN hardware_product hp ON ah.hard_id=hp.hp_id " +
			"LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{activityCode} " +
			" order by ah.create_time desc")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHlfHardware(String activityCode);

	@SelectProvider(type =SqlProvider.class, method = "selectHlfActivityHardware")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHlfActivityHardware(@Param("h")HlfHardware h);

	// SELECT hh.*,hp.type_name FROM hlf_hardware hh, hardware_product hp WHERE hh.hard_id=hp.hp_id AND hh.activity_code='008'
	@Select("SELECT ah.id,ah.activity_code,ah.hard_id,hp.type_name,ah.activity_type_no,aht.rule_id activity_merchant_id,aht.activity_type_name,aht.trans_amount," +
			"aht.cash_back_amount,aht.empty_amount,aht.full_amount,ah.cash_last_ally_amount," +
			"ah.default_status,ah.cumulate_trans_day,ah.cumulate_amount_minus,ah.cumulate_amount_add,ah.cumulate_trans_minus_day,hp.org_id team_id," +
			"aht.repeat_empty_amount,aht.repeat_full_amount,ah.repeat_cumulate_trans_day,ah.repeat_cumulate_amount_minus,ah.repeat_cumulate_amount_add," +
			"ah.repeat_cumulate_trans_minus_day,ah.activity_reward_config_id " +
			"FROM activity_hardware ah " +
			"LEFT JOIN hardware_product hp ON ah.hard_id=hp.hp_id " +
			"LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"WHERE ah.hard_id=hp.hp_id AND ah.activity_code=#{a.activityCode} and hard_id=#{a.hardId} and ah.activity_type_no=#{a.activityTypeNo}")
	@ResultType(HlfHardware.class)
	HlfHardware selectHlfHardwareByHardId(@Param("a")HlfHardware hlfHardware);

	@Select("SELECT * FROM activity_hardware where id=#{id}")
	@ResultType(HlfHardware.class)
	HlfHardware selectHlfHardwareById(@Param("id")Integer id);

	@Select("SELECT ah.*,aht.activity_type_name FROM activity_hardware ah " +
			"LEFT JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"where ah.activity_code=#{a.activityCode} and ah.hard_id=#{a.hardId} and ah.activity_type_no=#{a.activityTypeNo}")
	@ResultType(HlfHardware.class)
	HlfHardware selectHlfHardwareInfo(@Param("a")HlfHardware hlfHardware);

	@Update("update activity_hardware set trans_amount=#{h.transAmount},cash_back_amount=#{h.cashBackAmount}," +
			"activity_type_no=#{h.activityTypeNo}," +
			"operator=#{h.operator},cash_last_ally_amount=#{h.cashLastAllyAmount},default_status=#{h.defaultStatus}," +
			"cumulate_trans_day=#{h.cumulateTransDay},cumulate_amount_minus=#{h.cumulateAmountMinus},cumulate_amount_add=#{h.cumulateAmountAdd}," +
			"cumulate_trans_minus_day=#{h.cumulateTransMinusDay}," +
			"repeat_cumulate_trans_day=#{h.repeatCumulateTransDay},repeat_cumulate_amount_minus=#{h.repeatCumulateAmountMinus},repeat_cumulate_amount_add=#{h.repeatCumulateAmountAdd}," +
			"repeat_cumulate_trans_minus_day=#{h.repeatCumulateTransMinusDay},activity_reward_config_id=#{h.activityRewardConfigId}"
			+ " where id=#{h.id}")
	int updateHlfHardware(@Param("h")HlfHardware h);

	@Select("SELECT id FROM activity_hardware WHERE activity_type_no=#{h.activityTypeNo} and hard_id=#{h.hardId}")
	@ResultType(Integer.class)
	Integer isExistHardware(@Param("h")HlfHardware h);

	@Select("SELECT id FROM activity_hardware WHERE activity_type_no=#{h.activityTypeNo} and hard_id=#{h.hardId} and id!=#{h.id}")
	@ResultType(Integer.class)
	Integer isExistHardwareByNoId(@Param("h")HlfHardware h);

	@Insert({
		"insert into activity_hardware (activity_code,activiy_name,hard_id,trans_amount,cash_back_amount," +
				"create_time,operator,activity_type_no,cash_last_ally_amount," +
				"default_status,cumulate_trans_day,cumulate_amount_minus,cumulate_amount_add,cumulate_trans_minus_day," +
				"repeat_cumulate_trans_day,repeat_cumulate_amount_minus," +
				"repeat_cumulate_amount_add,repeat_cumulate_trans_minus_day,activity_reward_config_id)"
			+ "values " +
			"(#{h.activityCode},#{h.activiyName},#{h.hardId},#{h.transAmount},#{h.cashBackAmount}," +
				"now(),#{h.operator},#{h.activityTypeNo},#{h.cashLastAllyAmount}," +
				"#{h.defaultStatus},#{h.cumulateTransDay},#{h.cumulateAmountMinus},#{h.cumulateAmountAdd},#{h.cumulateTransMinusDay}," +
				"#{h.repeatCumulateTransDay},#{h.repeatCumulateAmountMinus}," +
				"#{h.repeatCumulateAmountAdd},#{h.repeatCumulateTransMinusDay},#{h.activityRewardConfigId})"
	})
	int insertHlfHardware(@Param("h")HlfHardware h);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnType")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> selectHappyReturnType(@Param("a") ActivityHardwareType activityHardwareType, @Param("page") Page<ActivityHardwareType> page);

	@Select("SELECT count(activity_type_name) FROM activity_hardware_type WHERE activity_type_name=#{a.activityTypeName} and activity_type_no !=#{a.activityTypeNo}")
	@ResultType(Integer.class)
	int queryByActivityTypeName(@Param("a")ActivityHardwareType activityHardwareType);

	@Insert({"insert into activity_hardware_type (activity_type_no,activity_type_name,activity_code,trans_amount," +
			"cash_back_amount,remark,create_time,repeat_register_amount,empty_amount,full_amount," +
			"repeat_empty_amount,repeat_full_amount,rule_id)"
			+ "values (#{a.activityTypeNo},#{a.activityTypeName},#{a.activityCode},#{a.transAmount}," +
			"#{a.cashBackAmount},#{a.remark},now(),#{a.repeatRegisterAmount},#{a.emptyAmount},#{a.fullAmount}," +
			"#{a.repeatEmptyAmount},#{a.repeatFullAmount},#{a.ruleId} )"})
	int insertHappyReturnType(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update activity_hardware_type set activity_type_name=#{a.activityTypeName},trans_amount=#{a.transAmount}," +
			"cash_back_amount=#{a.cashBackAmount},remark=#{a.remark},update_time=now(),repeat_register_amount=#{a.repeatRegisterAmount}," +
			"empty_amount=#{a.emptyAmount},full_amount=#{a.fullAmount},repeat_empty_amount=#{a.repeatEmptyAmount}," +
			"repeat_full_amount=#{a.repeatFullAmount},rule_id=#{a.ruleId} " +
			"where activity_type_no=#{a.activityTypeNo}")
	int updateHappyReturnType(@Param("a")ActivityHardwareType activityHardwareType);

	@Update("update activity_hardware set trans_amount=#{a.transAmount},cash_back_amount=#{a.cashBackAmount}"
			+ " where activity_type_no=#{a.activityTypeNo} ")
	int updateActivityHardwareByActivityTypeNo(@Param("a")ActivityHardwareType activityHardwareType);


	@Update("update agent_activity a LEFT JOIN agent_info b on a.agent_no=b.agent_no set a.cash_back_amount=#{a.cashBackAmount},a.repeat_register_amount=#{a.repeatRegisterAmount}," +
			"a.full_prize_amount=#{a.fullAmount},a.repeat_full_prize_amount=#{a.repeatFullAmount},a.not_full_deduct_amount=#{a.emptyAmount},a.repeat_not_full_deduct_amount=#{a.repeatEmptyAmount}" +
			" where a.activity_type_no=#{a.activityTypeNo} and b.agent_level=1")
	int updateAgentSctivityCashBackAmount(@Param("a")ActivityHardwareType activityHardwareType);

	@Delete("delete from activity_hardware_type where activity_type_no=#{activityTypeNo}")
	int delHappyReturnType(@Param("activityTypeNo")String activityTypeNo);

	@Update("update activity_hardware_type set update_agent_status=#{a.updateAgentStatus}" +
			" where activity_type_no=#{a.activityTypeNo}")
	int updateAgentStatusSwitch(@Param("a")ActivityHardwareType activityHardwareType);


	@Update("update activity_hardware_type set count_trade_scope=#{countTradeScope}" +
			" where id=#{id}")
	int updateCountTradeScope(@Param("id")String id,@Param("countTradeScope")String countTradeScope);

	@Select("SELECT count(activity_type_no) FROM activity_hardware WHERE activity_type_no=#{activityTypeNo}")
	@ResultType(Integer.class)
	int queryActivityCount(@Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT * FROM activity_hardware_type WHERE activity_code=#{activityCode}")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> queryByactivityTypeNoList(@Param("activityCode")String activityCode);

	@Select("SELECT * FROM activity_hardware_type WHERE activity_type_no=#{activityTypeNo}")
	@ResultType(ActivityHardwareType.class)
	ActivityHardwareType queryByActivityHardwareType(@Param("activityTypeNo")String activityTypeNo);

	@Select("SELECT * FROM activity_hardware_type")
	@ResultType(ActivityHardwareType.class)
	List<ActivityHardwareType> getActivityTypeNoList();

	// -------------------------- 以上是欢乐返活动 !!! ----------------------------

	@Insert("insert into activity_reward_config (activity_name,start_time,end_time,cumulate_trans_minus_day," +
			"cumulate_trans_day,cumulate_amount_minus,cumulate_amount_add,repeat_cumulate_trans_minus_day," +
			"repeat_cumulate_trans_day,repeat_cumulate_amount_minus,repeat_cumulate_amount_add) " +
			"values (#{n.activityName},#{n.startTime},#{n.endTime},#{n.cumulateTransMinusDay}," +
			"#{n.cumulateTransDay},#{n.cumulateAmountMinus},#{n.cumulateAmountAdd},#{n.repeatCumulateTransMinusDay}," +
			"#{n.repeatCumulateTransDay},#{n.repeatCumulateAmountMinus},#{n.repeatCumulateAmountAdd})")
	int insertHappyReturnRewardActivity(@Param("n") ActivityRewardConfig activityRewardConfig);

	@Update("update activity_reward_config set activity_name=#{n.activityName},start_time=#{n.startTime},end_time=#{n.endTime},cumulate_trans_minus_day=#{n.cumulateTransMinusDay}," +
			"cumulate_trans_day=#{n.cumulateTransDay},cumulate_amount_minus=#{n.cumulateAmountMinus},cumulate_amount_add=#{n.cumulateAmountAdd}," +
			"repeat_cumulate_trans_minus_day=#{n.repeatCumulateTransMinusDay},repeat_cumulate_trans_day=#{n.repeatCumulateTransDay}," +
			"repeat_cumulate_amount_minus=#{n.repeatCumulateAmountMinus},repeat_cumulate_amount_add=#{n.repeatCumulateAmountAdd}" +
			" where id=#{n.id}")
	int updateHappyReturnRewardActivity(@Param("n") ActivityRewardConfig activityRewardConfig);

	@Delete("delete from activity_reward_config where id=#{id}")
	int deleteHappyReturnRewardActivity(@Param("id") String id);

	@Select("SELECT count(*) FROM activity_hardware WHERE activity_reward_config_id=#{id}")
	int queryHappyReturnAgentActivityCount(@Param("id") String id);

	@Select("SELECT * FROM agent_activity_reward_config WHERE agent_no=#{agentNo}")
	@ResultType(AgentActivityRewardConfig.class)
	AgentActivityRewardConfig queryHappyReturnAgentActivityByAgentNo(@Param("agentNo") String agentNo);

	@Insert("Insert into agent_activity_reward_config(agent_no,agent_name,create_time,create_user,activity_id) "
			+ "values(#{info.agentNo},#{info.agentName},"
			+ "#{info.createTime},#{info.createUser},#{info.activityId})")
	int insertHappyReturnAgentActivity(@Param("info") AgentActivityRewardConfig info);

	@Delete("delete from agent_activity_reward_config where id=#{id}")
	int deleteHappyReturnAgentActivity(@Param("id") String id);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnRewardActivity")
	@ResultType(ActivityRewardConfig.class)
	List<ActivityRewardConfig> selectHappyReturnRewardActivity(@Param("a") ActivityRewardConfig activityRewardConfig, @Param("page") Page<ActivityRewardConfig> page);

	@Select("SELECT * FROM activity_reward_config WHERE id=#{id}")
	@ResultType(ActivityRewardConfig.class)
	ActivityRewardConfig selectHappyReturnRewardActivityById(@Param("id") String id);

	@SelectProvider(type =SqlProvider.class, method = "selectHappyReturnAgentActivity")
	@ResultType(AgentActivityRewardConfig.class)
	List<AgentActivityRewardConfig> selectHappyReturnAgentActivity(@Param("a") AgentActivityRewardConfig agentActivityRewardConfig, @Param("page") Page<AgentActivityRewardConfig> page);

	@SelectProvider(type =SqlProvider.class, method = "queryActivityVipList")
	@ResultType(Map.class)
	List<Map> queryActivityVipList(@Param("map") Map map,@Param("page") Page<Map> page);

	@Select("SELECT * from merchant_subscribe_vip where to_days(date_add(now(), interval ${time} day)) = to_days(validity_end)")
	@ResultType(Map.class)
	List<Map> queryActivityVipPush(@Param("time") String time);

	@Select("SELECT * FROM activity_reward_config")
	@ResultType(ActivityRewardConfig.class)
	List<ActivityRewardConfig> queryByActivityRewardConfigList();

	@Select("SELECT ah.activity_type_no,aht.activity_type_name " +
			"FROM activity_hardware ah " +
			"LEFT OUTER JOIN activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no " +
			"WHERE ah.hard_id=#{hardId} AND ah.activity_code=#{activityCode}")
	@ResultType(HlfHardware.class)
	List<HlfHardware> selectHBActivityHardwareList(@Param("hardId") String hardId, @Param("activityCode")String activityCode);

	@Insert("insert into hlf_activity_merchant_rule (rule_name,start_time,end_time,first_reward_type," +
			"first_reward_month,first_reward_total_amount,first_reward_amount,first_deduct_type," +
			"first_deduct_month,first_deduct_total_amount,first_deduct_amount,first_repeat_status," +
			"repeat_reward_type,repeat_reward_month,repeat_reward_total_amount,repeat_reward_amount," +
			"repeat_deduct_type,repeat_deduct_month,repeat_deduct_total_amount,repeat_deduct_amount," +
			"create_time,operator) " +
			"values (#{n.ruleName},#{n.startTime},#{n.endTime},#{n.firstRewardType}," +
			"#{n.firstRewardMonth},#{n.firstRewardTotalAmount},#{n.firstRewardAmount},#{n.firstDeductType}," +
			"#{n.firstDeductMonth},#{n.firstDeductTotalAmount},#{n.firstDeductAmount},#{n.firstRepeatStatus}," +
			"#{n.repeatRewardType},#{n.repeatRewardMonth},#{n.repeatRewardTotalAmount},#{n.repeatRewardAmount}," +
			"#{n.repeatDeductType},#{n.repeatDeductMonth},#{n.repeatDeductTotalAmount},#{n.repeatDeductAmount}," +
			"now(),#{n.operator})")
	int insertHlfActivityMerchantRule(@Param("n") HlfActivityMerchantRule info);

	@Update("update hlf_activity_merchant_rule set rule_name=#{n.ruleName},start_time=#{n.startTime},end_time=#{n.endTime},first_reward_type=#{n.firstRewardType}," +
			"first_reward_month=#{n.firstRewardMonth},first_reward_total_amount=#{n.firstRewardTotalAmount},first_reward_amount=#{n.firstRewardAmount}," +
			"first_deduct_type=#{n.firstDeductType},first_deduct_month=#{n.firstDeductMonth},first_deduct_total_amount=#{n.firstDeductTotalAmount}," +
			"first_deduct_amount=#{n.firstDeductAmount},first_repeat_status=#{n.firstRepeatStatus},repeat_reward_type=#{n.repeatRewardType}," +
			"repeat_reward_month=#{n.repeatRewardMonth},repeat_reward_total_amount=#{n.repeatRewardTotalAmount},repeat_reward_amount=#{n.repeatRewardAmount}," +
			"repeat_deduct_type=#{n.repeatDeductType},repeat_deduct_month=#{n.repeatDeductMonth},repeat_deduct_total_amount=#{n.repeatDeductTotalAmount}," +
			"repeat_deduct_amount=#{n.repeatDeductAmount} " +
			" where rule_id=#{n.ruleId}")
	int updateHlfActivityMerchantRule(@Param("n") HlfActivityMerchantRule info);

	@Delete("delete from hlf_activity_merchant_rule where rule_id=#{ruleId}")
	int deleteHlfActivityMerchantRule(@Param("ruleId") String ruleId);

	@SelectProvider(type =SqlProvider.class, method = "selectHlfActivityMerchantRule")
	@ResultType(HlfActivityMerchantRule.class)
	List<HlfActivityMerchantRule> selectHlfActivityMerchantRule(@Param("a") HlfActivityMerchantRule info, @Param("page") Page<HlfActivityMerchantRule> page);

	@Select("SELECT * FROM hlf_activity_merchant_rule WHERE rule_id=#{ruleId}")
	@ResultType(HlfActivityMerchantRule.class)
	HlfActivityMerchantRule selectHlfActivityMerchantRuleById(@Param("ruleId") String ruleId);

	@Select("select * from hlf_activity_merchant_rule")
	@ResultType(HlfActivityMerchantRule.class)
	List<HlfActivityMerchantRule> selectHlfActivityMerchantRuleAllInfo();

	@Select("select * from hlf_activity_merchant_rule where rule_id like concat('%',#{item},'%') or rule_name like concat('%',#{item},'%')")
	@ResultType(HlfActivityMerchantRule.class)
	List<HlfActivityMerchantRule> selectHlfActivityMerchantRuleAllInfo2(@Param("item") String item);

	@Select("select count(*) from activity_hardware_type where rule_id=#{ruleId}")
	int findActivityHardwareTypeByRuleIdCount(@Param("ruleId") String ruleId);

	public class SqlProvider {

		public String selectHappyReturnType(Map<String, Object> param) {
			final ActivityHardwareType a = (ActivityHardwareType) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" aht.*,hamr.rule_name"
					);
					FROM("activity_hardware_type aht ");
					LEFT_OUTER_JOIN("hlf_activity_merchant_rule hamr on hamr.rule_id=aht.rule_id");
					WHERE("1=1 ");
					if (StringUtils.isNotBlank(a.getActivityTypeNo())) {
						WHERE(" aht.activity_type_no=#{a.activityTypeNo}");
					}
					if (StringUtils.isNotBlank(a.getActivityTypeName())) {
						WHERE(" aht.activity_type_name=#{a.activityTypeName}");
					}
					if (StringUtils.isNotBlank(a.getActivityCode())) {
						WHERE(" aht.activity_code=#{a.activityCode}");
					}
					if (StringUtil.isNotBlank(a.getRuleId())) {
						WHERE(" aht.rule_id=#{a.ruleId}");
					}
					ORDER_BY("aht.id");
				}
			}.toString();
			return sql;
		}

		public String selectHappyReturnRewardActivity(Map<String, Object> param) {
			final ActivityRewardConfig a = (ActivityRewardConfig) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" arc.*"
					);
					FROM("activity_reward_config arc ");
					WHERE("1=1 ");
					if (StringUtils.isNotBlank(a.getActivityName())) {
						WHERE(" arc.activity_name=#{a.activityName}");
					}
				}
			}.toString();
			return sql;
		}

		public String selectHappyReturnAgentActivity(Map<String, Object> param) {
			final AgentActivityRewardConfig a = (AgentActivityRewardConfig) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" aarc.*"
					);
					FROM("agent_activity_reward_config aarc ");
					WHERE("1=1 ");
					WHERE(" aarc.activity_id=#{a.activityId}");
					if (StringUtils.isNotBlank(a.getAgentNo())) {
						WHERE(" aarc.agent_no=#{a.agentNo}");
					}
					if (StringUtils.isNotBlank(a.getAgentName())) {
						WHERE(" aarc.agent_name=#{a.agentName}");
					}
				}
			}.toString();
			return sql;
		}

		public String queryActivityVipList(Map<String, Object> param) {
			final Map<String, Object> map = (Map<String, Object>) param.get("map");
			String sql = new SQL() {
				{
					SELECT("sv.*,mi.merchant_name,mi.merchant_no,mi.mobilephone,ai.agent_name,ai2.agent_name one_agent_name,av.name,av.team_id "
					);
					FROM("subscribe_vip sv ");
					LEFT_OUTER_JOIN("activity_vip av on av.id=sv.vip_type");
					LEFT_OUTER_JOIN("merchant_info mi on mi.merchant_no=sv.user_id");
					LEFT_OUTER_JOIN("agent_info ai on ai.agent_no=mi.agent_no");
					LEFT_OUTER_JOIN("agent_info ai2 on ai2.agent_no=mi.one_agent_no");
					WHERE("1=1 ");
					if (StringUtil.isNotBlank(map.get("team_id"))) {
						WHERE(" av.team_id=#{map.team_id}");
					}
					
					if (StringUtil.isNotBlank(map.get("order_no"))) {
						WHERE(" sv.order_no=#{map.order_no}");
					}
					if (StringUtil.isNotBlank(map.get("payment_order_no"))) {
						WHERE(" sv.payment_order_no=#{map.payment_order_no}");
					}
					if (StringUtil.isNotBlank(map.get("merchantN"))) {
						WHERE(" (mi.merchant_no=#{map.merchantN} or mi.merchant_name like concat('%',#{map.merchantN},'%'))");
					}
					if (StringUtil.isNotBlank(map.get("mobilephone"))) {
						WHERE(" mi.mobilephone=#{map.mobilephone}");
					}
					if (StringUtil.isNotBlank(map.get("oneAgentNo"))) {
						WHERE(" mi.one_agent_no=#{map.oneAgentNo}");
					}
					if (StringUtil.isNotBlank(map.get("agentN"))) {
						WHERE(" mi.agent_no=#{map.agentN}");
					}
					if (StringUtil.isNotBlank(map.get("subscribe_status"))) {
						WHERE(" sv.subscribe_status=#{map.subscribe_status}");
					}
					if (StringUtil.isNotBlank(map.get("payment_type"))) {
						WHERE(" sv.payment_type=#{map.payment_type}");
					}
					if (StringUtil.isNotBlank(map.get("startTime"))) {
						WHERE(" sv.create_time>=#{map.startTime}");
					}
					if (StringUtil.isNotBlank(map.get("endTime"))) {
						WHERE(" sv.create_time<=#{map.endTime}");
					}
					
					if (StringUtil.isNotBlank(map.get("startValidityEnd"))) {
						WHERE(" sv.validity_end>=#{map.startValidityEnd}");
					}
					if (StringUtil.isNotBlank(map.get("endValidityEnd"))) {
						WHERE(" sv.validity_end<=#{map.endValidityEnd}");
					}
					
					if (StringUtil.isNotBlank(map.get("startTransTime"))) {
						WHERE(" sv.trans_time>=#{map.startTransTime}");
					}
					if (StringUtil.isNotBlank(map.get("endTransTime"))) {
						WHERE(" sv.trans_time<=#{map.endTransTime}");
					}
					
					ORDER_BY("sv.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String selectHlfActivityHardware(Map<String, Object> param) {
			final HlfHardware h = (HlfHardware) param.get("h");
			String sql = new SQL() {
				{
					SELECT("ah.id,ah.activity_reward_config_id,ah.activity_code,ah.hard_id,ah.activity_type_no,aht.empty_amount,aht.full_amount,ah.cash_last_ally_amount," +
							"ah.default_status,ah.cumulate_trans_day,ah.cumulate_amount_minus,ah.cumulate_amount_add,ah.cumulate_trans_minus_day," +
							"ah.cumulate_trans_day,ah.cumulate_trans_minus_day,aht.repeat_empty_amount,aht.repeat_full_amount,ah.repeat_cumulate_trans_day,ah.repeat_cumulate_trans_minus_day," +
							"hp.type_name,aht.activity_type_name,aht.trans_amount,aht.cash_back_amount,aht.repeat_register_amount,ti.team_name,aht.rule_id activityMerchantId"
					);
					FROM("activity_hardware ah ");
					LEFT_OUTER_JOIN("hardware_product hp ON ah.hard_id=hp.hp_id ");
					LEFT_OUTER_JOIN("activity_hardware_type aht ON aht.activity_type_no=ah.activity_type_no ");
					LEFT_OUTER_JOIN("team_info ti ON ti.team_id=hp.org_id ");
					WHERE("ah.hard_id=hp.hp_id AND ah.activity_code=#{h.activityCode} ");

					if (StringUtil.isNotBlank(h.getHardId())) {
						WHERE(" ah.hard_id=#{h.hardId}");
					}
					if (StringUtil.isNotBlank(h.getTeamId())) {
						WHERE(" ti.team_id=#{h.teamId}");
					}
					if (StringUtil.isNotBlank(h.getDefaultStatus())) {
						WHERE(" ah.default_status=#{h.defaultStatus}");
					}
					if (StringUtil.isNotBlank(h.getIsMerchant())) {
						if(h.getIsMerchant()==0){
							WHERE(" aht.rule_id is null");
						}else{
							WHERE(" aht.rule_id is not null");
						}
					}
					ORDER_BY("ah.create_time desc");
				}
			}.toString();
			return sql;
		}

		public String selectHlfActivityMerchantRule(Map<String, Object> param) {
			final HlfActivityMerchantRule a = (HlfActivityMerchantRule) param.get("a");
			String sql = new SQL() {
				{
					SELECT(" ham.*");
					FROM("hlf_activity_merchant_rule ham ");
					WHERE("1=1 ");
					if (StringUtil.isNotBlank(a.getRuleId())) {
						WHERE(" ham.rule_id=#{a.ruleId}");
					}
					if (StringUtil.isNotBlank(a.getRuleName())) {
						WHERE(" ham.rule_name like concat('%',#{a.ruleName},'%')");
					}
				}
			}.toString();
			return sql;
		}
	}
}
