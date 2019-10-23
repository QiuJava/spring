package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SupertuiRule;

/**
 * 超级推dao
 * 
 * @author junhu
 *
 */
@WriteReadDataSource
public interface PushDao {

	@Insert("insert into supertui_rule (bp_id, recommended_amount, reward_valid_node,"
			+ "order_agent_fee, fee_valid_node, efficient_date," + "disabled_date, order_threshold, grab_order_time,"
			+ "feedback_time)"
			+ "values (#{bpId}, #{recommendedAmount}, #{rewardValidNode},"
			+ " #{orderAgentFee}, #{feeValidNode}, #{efficientDate},"
			+ "#{disabledDate}, #{orderThreshold}, #{grabOrderTime}," + "#{feedbackTime})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
	int insertSupertuiRule(SupertuiRule supertuiRule);

	@SelectProvider(type = SqlProvider.class, method = "getSupertuiRule")
	@ResultType(SupertuiRule.class)
	List<SupertuiRule> getSupertuiRule(Map<String, Object> param, Page<SupertuiRule> page);

	@SelectProvider(type = SqlProvider.class, method = "getSupertuiRule")
	@ResultType(SupertuiRule.class)
	SupertuiRule getSupertuiRuleInfo(Map<String, Object> param);

	@UpdateProvider(type = SqlProvider.class, method = "updateSupertuiRule")
	int updateSupertuiRule(SupertuiRule supertuiRule);

	@SuppressWarnings("rawtypes")
	@SelectProvider(type = SqlProvider.class, method = "getSupertuiOrder")
	@ResultType(Map.class)
	List<Map> getSupertuiOrder(Map<String, Object> param, Page<Map> page);

	@SuppressWarnings("rawtypes")
	@SelectProvider(type = SqlProvider.class, method = "getSupertuiOrder")
	@ResultType(Map.class)
	Map getSupertuiOrderInfo(Map<String, Object> param);

	public class SqlProvider {
		public String getSupertuiRule(final Map<String, Object> param) {
			return new SQL() {
				{
					SELECT("sr.id,bp.bp_name as bp_id,sr.recommended_amount,sr.reward_valid_node,sr.order_agent_fee,sr.fee_valid_node,sr.efficient_date,sr.disabled_date,sr.order_threshold,sr.grab_order_time,sr.feedback_time");
					FROM("supertui_rule sr left join business_product_define bp on sr.bp_id=bp.bp_id");
					if (StringUtils.isNotEmpty(String.valueOf(param.get("bpId") == null ? "" : param.get("bpId")))) {
						WHERE("bp.bp_name like CONCAT('%',#{bpId},'%')");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("id") == null ? "" : param.get("id")))) {
						WHERE("sr.id=#{id}");
					}
				}
			}.toString();
		}

		public String updateSupertuiRule(final SupertuiRule supertuiRule) {
			StringBuilder sb = new StringBuilder();
			sb.append("update supertui_rule" + " set recommended_amount = #{recommendedAmount},"
					+ " reward_valid_node = #{rewardValidNode},order_agent_fee = #{orderAgentFee},"
					+ " fee_valid_node = #{feeValidNode},order_threshold = #{orderThreshold},"
					+ " grab_order_time = #{grabOrderTime},feedback_time = #{feedbackTime}");
			if (supertuiRule.getEfficientDate() != null) {
				sb.append(",efficient_date = #{efficientDate}");
			}
			if (supertuiRule.getDisabledDate() != null) {
				sb.append(",disabled_date = #{disabledDate}");
			}
			sb.append(" where id = #{id} ");
			return sb.toString();
		}

		public String getSupertuiOrder(final Map<String, Object> param) {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT so.*, mi.merchant_name,mi.lawyer AS t_lawyer,"
					+ "mi.mobilephone AS t_mobilephone,mi.agent_no,ai.agent_name AS t_agent_name,"
					+ "ui.user_name,ai2.agent_name AS j_agent_name,mbp.id AS mbp_id,"
					+ "mbp.create_time AS j_create_time,bpd.bp_name FROM supertui_order so"
					+ " LEFT JOIN merchant_info mi ON so.merchant_no = mi.merchant_no"
					+ " LEFT JOIN agent_info ai ON mi.agent_no = ai.agent_no"
					+ " LEFT JOIN user_info ui ON so.order_user_id = ui.user_id"
					+ " LEFT JOIN agent_info ai2 ON so.order_agent_id = ai2.agent_no"
					+ " LEFT JOIN merchant_business_product mbp ON (so.merchant_id = mbp.merchant_no AND so.bp_id = mbp.bp_id)"
					+ " LEFT JOIN business_product_define bpd ON so.bp_id = bpd.bp_id");
			sb.append(" where 1 = 1");
			if (StringUtils.isNotEmpty(String.valueOf(param.get("order_id") == null ? "" : param.get("order_id")))) {
				sb.append(" and so.order_id like CONCAT('%',#{order_id},'%')");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("order_status") == null ? "" : param.get("order_status")))) {
				if (!(param.get("order_status").toString()).equals("-1")) {
					sb.append(" and so.order_status=#{order_status}");
				}
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("lawyer") == null ? "" : param.get("lawyer")))) {
				sb.append(" and so.lawyer like CONCAT('%',#{lawyer},'%')");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("mobilephone") == null ? "" : param.get("mobilephone")))) {
				sb.append(" and so.mobilephone like CONCAT('%',#{mobilephone},'%')");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("merchant_no") == null ? "" : param.get("merchant_no")))) {
				sb.append(" and (so.merchant_no like CONCAT('%',#{merchant_no},'%') or mi.merchant_name like CONCAT('%',#{merchant_no},'%'))");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("agent_no") == null ? "" : param.get("agent_no")))) {
				sb.append(" and (mi.agent_no like CONCAT('%',#{agent_no},'%') or ai.agent_name like CONCAT('%',#{agent_no},'%'))");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("order_user_type") == null ? "" : param.get("order_user_type")))) {
				if (!(param.get("order_user_type").toString()).equals("-1")) {
					sb.append(" and so.order_user_type=#{order_user_type}");
				}
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("j_agent_name") == null ? "" : param.get("j_agent_name")))) {
				sb.append(" and ai2.agent_name like CONCAT('%',#{j_agent_name},'%')");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("s_create_time") == null ? "" : param.get("s_create_time")))) {
				sb.append(" and so.create_time>=#{s_create_time}");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("e_create_time") == null ? "" : param.get("e_create_time")))) {
				sb.append(" and so.create_time<=#{e_create_time}");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("s_order_time") == null ? "" : param.get("s_order_time")))) {
				sb.append(" and so.order_time>=#{s_order_time}");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("e_order_time") == null ? "" : param.get("e_order_time")))) {
				sb.append(" and so.create_time<=#{e_order_time}");
			}
			if (StringUtils.isNotEmpty(String.valueOf(param.get("bp_name") == null ? "" : param.get("bp_name")))) {
				sb.append(" and bpd.bp_name like CONCAT('%',#{bp_name},'%')");
			}

			if (StringUtils.isNotEmpty(String.valueOf(param.get("id") == null ? "" : param.get("id")))) {
				sb.append(" and so.id=#{id}");
			}

			return sb.toString();
		}
	}
}
