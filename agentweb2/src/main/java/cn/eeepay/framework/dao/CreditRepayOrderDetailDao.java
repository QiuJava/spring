package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditRepayOrderDetail;
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
 * 还款订单处理流水dao
 * @author liuks
 */
@ReadOnlyDataSource
public interface  CreditRepayOrderDetailDao {
    /**
     * 查询订单详情子列表信息
     */
    @Select("select " +
            " t1.plan_no,t1.plan_amount,t2.account_no,t1.plan_type,t1.plan_status,t1.res_msg,t1.plan_time,t1.create_time, " +
            " t1.bak1,t1.bak2 " +
            " from yfb_repay_plan_detail t1 " +
            " LEFT JOIN yfb_card_manage t2 on t2.card_no=t1.card_no " +
            " where t1.batch_no=#{batchNo}"
    )
    @ResultType(CreditRepayOrderDetail.class)
    List<CreditRepayOrderDetail> selectDetailList(@Param("batchNo") String batchNo);

    /**
     *根据条件动态查询订单流水列表
     */
    @SelectProvider(type=CreditRepayOrderDetailDao.SqlProvider.class,method="selectDetailAllList")
    @ResultType(CreditRepayOrderDetail.class)
    List<CreditRepayOrderDetail> selectDetailAllList(@Param("queryaram") CreditRepayOrderDetail orderDetail,
                                                     @Param("loginAgent") AgentInfo loginAgent, Page<CreditRepayOrderDetail> page);

    /**
     *导出数据查询
     */
    @SelectProvider(type=CreditRepayOrderDetailDao.SqlProvider.class,method="selectDetailAllList")
    @ResultType(CreditRepayOrderDetail.class)
    List<CreditRepayOrderDetail> exportDetailAllList(@Param("queryaram") CreditRepayOrderDetail orderDetail,
                                                     @Param("loginAgent") AgentInfo loginAgent);


    class SqlProvider {
        public String selectDetailAllList(final Map<String, Object> param) {
            final CreditRepayOrderDetail orderDetail = (CreditRepayOrderDetail) param.get("queryaram");
            return new SQL(){{
                SELECT( " t1.plan_no,t1.plan_amount,t2.account_no,t1.plan_type,t1.plan_status,t1.res_msg," +
                        " t1.create_time,t1.plan_time,t1.bak1,t1.bak2,t1.merchant_no,t1.batch_no " );
                FROM("yfb_repay_plan_detail t1");
                LEFT_OUTER_JOIN("yfb_card_manage t2 on t2.card_no=t1.card_no");
                LEFT_OUTER_JOIN("yfb_repay_merchant_info t3 on t3.merchant_no = t1.merchant_no");
                WHERE("t3.agent_node like concat(#{loginAgent.agentNode}, '%')");
                if(StringUtils.isNotBlank(orderDetail.getPlanNo())){
                    WHERE("t1.plan_no=#{queryaram.planNo}");
                }
                if(StringUtils.isNotBlank(orderDetail.getPlanStatus())){
                    WHERE("t1.plan_status=#{queryaram.planStatus}");
                }
                if(StringUtils.isNotBlank(orderDetail.getBatchNo())){
                    WHERE("t1.batch_no=#{queryaram.batchNo}");
                }
                if(StringUtils.isNotBlank(orderDetail.getMerchantNo())){
                    WHERE("t1.merchant_no=#{queryaram.merchantNo}");
                }
                if(orderDetail.getMinPlanAmount()!=null){
                    WHERE("t1.plan_amount>=#{queryaram.minPlanAmount}");
                }
                if(orderDetail.getMaxPlanAmount()!=null){
                    WHERE("t1.plan_amount<=#{queryaram.maxPlanAmount}");
                }
                if(StringUtils.isNotBlank(orderDetail.getAccountNo())){
                    WHERE("t2.account_no=#{queryaram.accountNo}");
                }
                if(orderDetail.getPlanTimeBegin()!= null){
                    WHERE("t1.plan_time>=#{queryaram.planTimeBegin}");
                }
                if(orderDetail.getPlanTimeEnd()!= null){
                    WHERE("t1.plan_time<=#{queryaram.planTimeEnd}");
                }
                ORDER_BY("t1.plan_time DESC");
                ORDER_BY("t1.plan_no DESC");
            }}.toString();
        }
    }
}
