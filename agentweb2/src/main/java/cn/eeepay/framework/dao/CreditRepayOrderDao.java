package cn.eeepay.framework.dao;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditRepayOrder;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 信用卡还款订单Dao
 * @author liuks
 * 对应主表 yfb_repay_plan
 * 详情表 yfb_repay_plan_detail
 */
@ReadOnlyDataSource
public interface CreditRepayOrderDao {

    /**
     *根据条件动态查询列表
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="listCreditRepayOrder")
    @ResultType(CreditRepayOrder.class)
    List<CreditRepayOrder> listCreditRepayOrder(@Param("order") CreditRepayOrder order,
                                                @Param("searchAgent") AgentInfo searchAgent, Page<CreditRepayOrder> page);

    /**
     *根据条件动态查询 统计总金额,
     * 返回的实体只含有统计值
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="countCreditRepayOrder")
    @ResultType(CreditRepayOrder.class)
    CreditRepayOrder countCreditRepayOrder(@Param("order") CreditRepayOrder order,
                                           @Param("searchAgent") AgentInfo searchAgent);

    /**
     * 通过id查询
     */
    @Select(
            "select " +
                    " t1.id,t1.repay_type,t1.batch_no,t1.merchant_no,t4.nickname,t2.user_name,t2.mobile_no,t2.id_card_no,t1.repay_num, " +
                    " t1.status,t1.repay_amount,t1.ensure_amount,t1.ensure_amount_rate,t1.repay_fee,t1.repay_fee_rate,t3.account_no, " +
                    " t3.bank_name,t1.create_time,t1.repay_begin_time,t1.repay_end_time,t1.success_repay_amount,t1.success_pay_amount, " +
                    " t1.success_repay_num,t1.actual_pay_fee,t1.actual_withdraw_fee,t1.remark as mission " +
                    " FROM yfb_repay_plan t1 " +
                    " LEFT JOIN yfb_repay_merchant_info t2 ON t2.merchant_no = t1.merchant_no " +
                    " LEFT JOIN yfb_card_manage t3 ON t3.card_no = t1.card_no " +
                    " LEFT OUTER JOIN yfb_wechat_info t4 ON t4.openid = t2.openid " +
                    " where t1.batch_no=#{batchNo} " +
                    " and t2.agent_node like concat(#{loginAgent.agentNode}, '%') "
    )
    @ResultType(CreditRepayOrder.class)
    CreditRepayOrder selectById(@Param("batchNo") String batchNo,@Param("loginAgent") AgentInfo loginAgent);

    /**
     *导出数据查询
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="listCreditRepayOrder")
    @ResultType(CreditRepayOrder.class)
    List<CreditRepayOrder> exportSelectAllList(@Param("order") CreditRepayOrder order, @Param("searchAgent") AgentInfo searchAgent);


    class SqlProvider {

        public String listCreditRepayOrder(final Map<String, Object> param){
            return countOrList(param, true);
        }

        public String countCreditRepayOrder(final Map<String, Object> param){
            return countOrList(param, false);
        }

        private String countOrList(Map<String, Object> param, final boolean isList) {
            final CreditRepayOrder order = (CreditRepayOrder) param.get("order");
            SQL sql = new SQL(){{
                if (isList){
                    SELECT("ai.agent_name,t1.complete_time," +
                            "t1.id,t1.batch_no,t1.merchant_no,t4.nickname,t2.user_name,t2.mobile_no,\n" +
                            "t1.status,t1.repay_amount,t1.ensure_amount,t1.repay_fee,t3.account_no,t1.repay_num,\n" +
                            "t3.bank_name,t1.create_time,t1.repay_begin_time,t1.repay_end_time,\n" +
                            "t1.remark as mission,t1.success_repay_amount,t1.success_pay_amount,t1.success_repay_num,\n" +
                            "t1.actual_pay_fee,t1.actual_withdraw_fee,\n" +
                            "t6.order_no as tally_order_no,t6.status as billing_status,t1.repay_type");
                    ORDER_BY("t1.create_time DESC");
                }else{
                    SELECT("sum(t1.repay_amount) as repay_amount_all, " +
                            "sum(t1.ensure_amount) as ensure_amount_all," +
                            "sum(t1.repay_fee) as repay_fee_all," +
                            "sum(if((t1.status=0 or t1.status=3),0,t1.ensure_amount)) as ensure_amount_freezing_all");
                }
                FROM("yfb_repay_plan t1");
                LEFT_OUTER_JOIN("yfb_repay_merchant_info t2 ON t2.merchant_no=t1.merchant_no");
                LEFT_OUTER_JOIN("agent_info ai ON t2.agent_node=ai.agent_node");
                LEFT_OUTER_JOIN("yfb_card_manage t3 ON t3.card_no=t1.card_no");
                LEFT_OUTER_JOIN("yfb_wechat_info t4 ON t4.openid=t2.openid");
                LEFT_OUTER_JOIN("yfb_pay_order t5 ON (t5.service_order_no=t1.batch_no and t5.service='ensure') ");
                LEFT_OUTER_JOIN("yfb_tally_his t6 ON (t6.service_order_no=t5.order_no and t6.service='trade' )");
                if (StringUtils.equals("1", order.getBool())){
                    WHERE("ai.agent_node LIKE  concat(#{searchAgent.agentNode}, '%')");
                }else{
                    WHERE("ai.agent_node = #{searchAgent.agentNode}");
                }
                if(StringUtils.isNotBlank(order.getBatchNo())) {
                    WHERE("t1.batch_no=#{order.batchNo}");
                }
                if(StringUtils.isNotBlank(order.getMerchantNo())){
                    WHERE("t1.merchant_no=#{order.merchantNo}");
                }
                if (StringUtils.isNotBlank(order.getMobileNo())) {
                    WHERE("t2.mobile_no=#{order.mobileNo}");
                }
                if (StringUtils.isNotBlank(order.getStatus()) && !StringUtils.equals("0", order.getStatus())) {
                    WHERE("t1.status=#{order.status}");
                }else{
                    WHERE("t1.status <> '0'");
                }

                if (StringUtils.isNotBlank(order.getRepayType())){
                    WHERE("t1.repay_type=#{order.repayType}");
                }
                if(order.getMinRepayAmount()!=null){
                    WHERE("t1.repay_amount>=#{order.minRepayAmount}");
                }
                if(order.getMaxRepayAmount()!=null){
                    WHERE("t1.repay_amount<=#{order.maxRepayAmount}");
                }

                if(order.getMinEnsureAmount()!=null){
                    WHERE("t1.ensure_amount>=#{order.minEnsureAmount}");
                }
                if(order.getMaxEnsureAmount()!=null){
                    WHERE("t1.ensure_amount<=#{order.maxEnsureAmount}");
                }

                if(order.getMinRepayFee()!=null){
                    WHERE("t1.repay_fee>=#{order.minRepayFee}");
                }
                if(order.getMaxRepayFee()!=null){
                    WHERE("t1.repay_fee<=#{order.maxRepayFee}");
                }
                if(StringUtils.isNotBlank(order.getCreateTimeBegin())){
                    WHERE("t1.create_time >= #{order.createTimeBegin}");
                }
                if(StringUtils.isNotBlank(order.getCreateTimeEnd())){
                    WHERE("t1.create_time <= #{order.createTimeEnd}");
                }
                if(StringUtils.isNotBlank(order.getCompleteTimeBegin())){
                    WHERE("t1.complete_time >= #{order.completeTimeBegin}");
                }
                if(StringUtils.isNotBlank(order.getCompleteTimeEnd())){
                    WHERE("t1.complete_time <= #{order.completeTimeEnd}");
                }
                if (StringUtils.isNotBlank(order.getBillingStatus())) {
                    WHERE("t6.status=#{order.billingStatus}");
                }
            }};

            return sql.toString();
        }

    }
}
