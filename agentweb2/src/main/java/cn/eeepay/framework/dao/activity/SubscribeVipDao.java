package cn.eeepay.framework.dao.activity;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.activity.SubscribeVip;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
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
 * @author MXG
 * create 2019/03/25
 */
@ReadOnlyDataSource
public interface SubscribeVipDao {

    @SelectProvider(type = SubscribeVipDao.SqlProvider.class, method = "selectByParam")
    @ResultType(SubscribeVip.class)
    List<SubscribeVip> selectByParamWithPage(@Param("order") SubscribeVip order, @Param("page") Page<SubscribeVip> page);

    @SelectProvider(type = SubscribeVipDao.SqlProvider.class, method = "selectByParam")
    @ResultType(SubscribeVip.class)
    List<SubscribeVip> selectByParam(@Param("order") SubscribeVip order);

    public class SqlProvider {

        public String selectByParam(Map<String, Object> param){
            final SubscribeVip order = (SubscribeVip)param.get("order");
            SQL sql = new SQL(){
                {
                    SELECT("sv.*," +
                            "mi.merchant_name,mi.merchant_no,mi.mobilephone," +
                            "ai.agent_name,av.name,av.time," +
                            "date_format(sv.create_time,'%Y-%m-%d %H:%i:%s') as createTime");
                    FROM("subscribe_vip sv");
                    LEFT_OUTER_JOIN("merchant_info mi on sv.user_id=mi.merchant_no");
                    LEFT_OUTER_JOIN("agent_info ai on ai.agent_no=mi.agent_no");
                    LEFT_OUTER_JOIN("activity_vip av on av.id=sv.vip_type");
                    //LEFT_OUTER_JOIN("collective_trans_order cto on cto.order_no=sv.payment_order_no");
                }
            };
            sqlWhere(sql, order);
            sql.ORDER_BY("sv.create_time DESC");
            return sql.toString();
        }

        private void sqlWhere(SQL sql, SubscribeVip order) {
            sql.WHERE("mi.parent_node like concat(#{order.currentAgentNode},'%')");
            //sql.WHERE("cto.trans_status='SUCCESS'");
            sql.WHERE("sv.subscribe_status='SUCCESS'");
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sql.WHERE("sv.order_no=#{order.orderNo}");
            }
            if(StringUtils.isNotBlank(order.getUserId())){
                sql.WHERE("mi.merchant_no=#{order.userId} or mi.merchant_name like concat(concat('%',#{order.userId}), '%')");
            }
            if(StringUtils.isNotBlank(order.getMobilephone())){
                sql.WHERE("mi.mobilephone=#{order.mobilephone}");
            }
            if(StringUtils.isNotBlank(order.getAgentNo())){
                sql.WHERE("mi.agent_no=#{order.agentNo}");
            }
            if(StringUtils.isNotBlank(order.getPaymentType())){
                sql.WHERE("sv.payment_type=#{order.paymentType}");
            }
            if(StringUtils.isNotBlank(order.getPaymentOrderNo())){
                sql.WHERE("sv.payment_order_no=#{order.paymentOrderNo}");
            }
            if(StringUtils.isNotBlank(order.getCreateTimeStart())){
                sql.WHERE("sv.create_time>=#{order.createTimeStart}");
            }
            if(StringUtils.isNotBlank(order.getCreateTimeEnd())){
                sql.WHERE("sv.create_time<=#{order.createTimeEnd}");
            }
        }
    }
}
