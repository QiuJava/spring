package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/20
 */
@ReadOnlyDataSource
public interface HappyBackActivityMerchantDao {

    /**
     * 欢乐返活跃商户活动查询列表
     * @param page
     * @param happyBackActivityMerchant
     */
    @SelectProvider(type = SqlProvider.class, method = "selectHappyBackActivityMerchant")
    @ResultType(HappyBackActivityMerchant.class)
    List<HappyBackActivityMerchant> selectHappyBackActivityMerchant(@Param("page") Page<HappyBackActivityMerchant> page,
                                                                    @Param("info")HappyBackActivityMerchant happyBackActivityMerchant);

    @SelectProvider(type = SqlProvider.class, method = "selectHappyBackActivityMerchant")
    @ResultType(HappyBackActivityMerchant.class)
    List<HappyBackActivityMerchant> exportExcel(@Param("info")HappyBackActivityMerchant happyBackActivityMerchant);

    @SelectProvider(type = SqlProvider.class, method = "countRewardAmount")
    @ResultType(BigDecimal.class)
    BigDecimal countRewardAmount(@Param("info")HappyBackActivityMerchant happyBackActivityMerchant, @Param("status")String status);

    @SelectProvider(type = SqlProvider.class, method = "countDeductAmount")
    @ResultType(BigDecimal.class)
    BigDecimal countDeductAmount(@Param("info")HappyBackActivityMerchant happyBackActivityMerchant,@Param("status")String status);

    class SqlProvider{
        public String selectHappyBackActivityMerchant(Map<String, Object> param) {
            SQL sql = new SQL() {{
                SELECT(" * ");
                FROM("hlf_activity_merchant_order");
            }};
            whereSql(param,sql);
            sql.ORDER_BY(" active_time desc ");
            return sql.toString();
        }
        public String countRewardAmount(Map<String, Object> param) {
            SQL sql = new SQL() {{
                SELECT(" sum(reward_amount) ");
                FROM(" hlf_activity_merchant_order ");
                WHERE(" reward_account_status = #{status}");
            }};
            whereSql(param,sql);
            return sql.toString();
        }
        public String countDeductAmount(Map<String, Object> param) {
            SQL sql = new SQL() {{
                SELECT(" sum(deduct_amount) ");
                FROM(" hlf_activity_merchant_order ");
                WHERE(" deduct_status = #{status}");
            }};
            whereSql(param,sql);
            return sql.toString();
        }

        private void whereSql(Map<String, Object> param,SQL sql) {
            final HappyBackActivityMerchant info = (HappyBackActivityMerchant) param.get("info");
            sql.WHERE(" agent_node like concat(#{info.currentAgentNode},'%')");
            if (StringUtils.isNotBlank(info.getActiveOrder())) {
                sql.WHERE(" FIND_IN_SET(active_order,#{info.activeOrder})");
            }
            if (StringUtils.isNotBlank(info.getTargetStatus())) {
                sql.WHERE(" target_status = #{info.targetStatus}");
            }
            if (StringUtils.isNotBlank(info.getRewardAccountStatus())) {
                sql.WHERE(" reward_account_status = #{info.rewardAccountStatus}");
            }
            if (StringUtils.isNotBlank(info.getDeductStatus())) {
                sql.WHERE(" deduct_status = #{info.deductStatus}");
            }
            if (info.getRewardAmountMin() != null) {
                sql.WHERE(" reward_amount >= #{info.rewardAmountMin}");
            }
            if (info.getRewardAmountMax() != null) {
                sql.WHERE(" reward_amount <= #{info.rewardAmountMax}");
            }
            if (info.getDeductAmountMin() != null) {
                sql.WHERE(" deduct_amount >= #{info.deductAmountMin}");
            }
            if (info.getDeductAmountMax() != null) {
                sql.WHERE(" deduct_amount <= #{info.deductAmountMax}");
            }
            if (StringUtils.isNotBlank(info.getMerchantNo())) {
                sql.WHERE(" merchant_no = #{info.merchantNo}");
            }
            if (StringUtils.isNotBlank(info.getAgentNode())) {
                if ("1".equals(info.getBool())){
                    sql.WHERE(" agent_node like concat(#{info.agentNode},'%')");
                }else {
                    sql.WHERE(" agent_node = #{info.agentNode}");
                }
            }
            if (StringUtils.isNotBlank(info.getActiveTimeStart())) {
                sql.WHERE(" active_time >= #{info.activeTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getActiveTimeEnd())) {
                sql.WHERE(" active_time <= #{info.activeTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getTargetTimeStart())) {
                sql.WHERE(" target_time >= #{info.targetTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getTargetTimeEnd())) {
                sql.WHERE(" target_time <= #{info.targetTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getRewardAccountTimeStart())) {
                sql.WHERE(" reward_account_time >= #{info.rewardAccountTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getRewardAccountTimeEnd())) {
                sql.WHERE(" reward_account_time <= #{info.rewardAccountTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getDeductTimeStart())) {
                sql.WHERE(" deduct_time >= #{info.deductTimeStart}");
            }
            if (StringUtils.isNotBlank(info.getDeductTimeEnd())) {
                sql.WHERE(" deduct_time <= #{info.deductTimeEnd}");
            }
        }
    }
}
