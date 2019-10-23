package cn.eeepay.framework.daoPerAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.PaOrder;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/07/15
 */
@WriteReadDataSource
public interface PaOrderDao {


    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "selectPaOrderByParam")
    @ResultType(PaOrder.class)
    List<PaOrder> selectPaOrderByParam(@Param("page")Page<PaOrder> page, @Param("order") PaOrder order);

    // 机具款项已入账,未入账
    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "selectGoodsTotal")
    @ResultType(BigDecimal.class)
    BigDecimal selectGoodsTotal(@Param("order")PaOrder orderInfo,@Param("entryStatus")String entryStatus);

 	//机具分润已入账,未入账
    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "selectShareAmountTotal")
    @ResultType(BigDecimal.class)
    BigDecimal selectShareAmountTotal(@Param("order")PaOrder orderInfo,@Param("accStatus")String accStatus);

 	//已发货机具总数
    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "selectTerminalCount")
    @ResultType(Integer.class)
 	Integer selectTerminalCount(@Param("order")PaOrder orderInfo);

    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "selectPaOrderByParam")
    @ResultType(PaOrder.class)
    List<PaOrder> selectAllList(@Param("order") PaOrder order);

    @Select("select user_code from pa_agent_user where agent_no=#{agentNo}")
    @ResultType(String.class)
    String selectUserCodeByAgentNo(@Param("agentNo") String agentNo);

    @Select("SELECT o.*,u.user_type " +
            "FROM pa_order o " +
            "LEFT JOIN pa_user_info u ON o.user_code=u.user_code " +
            "WHERE o.order_no=#{orderNo}")
    @ResultType(PaOrder.class)
    PaOrder selectOrderByOrderNo(@Param("orderNo") String orderNo);

    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "countReceived")
    @ResultType(Map.class)
    Map<String,Object> countReceived(@Param("order") PaOrder orderInfo);

    @SelectProvider(type = PaOrderDao.SqlProvider.class, method = "countWaitSend")
    @ResultType(Map.class)
    Map<String,Object> countWaitSend(@Param("order") PaOrder orderInfo);
    
    @Select("select * from pa_goods where goods_code = (select good_code from pa_order where order_no = #{orderNo})")
    @ResultType(Map.class)
    Map<String,Object> selectPaOrderByOrderNo(@Param("orderNo")String orderNo);

    public class SqlProvider{

        public String selectGoodsTotal(Map<String, Object> param){
            final PaOrder order = (PaOrder) param.get("order");
            SQL sql = new SQL(){
                {
                    SELECT("sum(o.goods_total) goodsTotal");
                    FROM("pa_order o");
                    LEFT_OUTER_JOIN("pa_user_info u on o.user_code = u.user_code");
                    LEFT_OUTER_JOIN("pa_goods pg on o.good_code = pg.goods_code");
                    LEFT_OUTER_JOIN("pa_share_detail psd on o.order_no = psd.trans_no and share_type = '7'");
                    WHERE("o.entry_status = #{entryStatus}");
                }
            };
            where(sql, order);
            return sql.toString();
        }
        public String selectShareAmountTotal(Map<String, Object> param){
        	final PaOrder order = (PaOrder) param.get("order");
        	SQL sql = new SQL(){
        		{
        			SELECT("sum(psd.share_amount) shareAmountTotal");
        			FROM("pa_order o");
        			LEFT_OUTER_JOIN("pa_user_info u on o.user_code = u.user_code");
        			LEFT_OUTER_JOIN("pa_goods pg on o.good_code = pg.goods_code");
        			LEFT_OUTER_JOIN("pa_share_detail psd on o.order_no = psd.trans_no and share_type = '7'");
        			WHERE("psd.acc_status = #{accStatus}");
        		}
        	};
        	where(sql, order);
        	return sql.toString();
        }
        public String selectTerminalCount(Map<String, Object> param){
        	final PaOrder order = (PaOrder) param.get("order");
        	SQL sql = new SQL(){
        		{
        			SELECT("sum(o.num) num");
        			FROM("pa_order o");
        			LEFT_OUTER_JOIN("pa_user_info u on o.user_code = u.user_code");
        			LEFT_OUTER_JOIN("pa_goods pg on o.good_code = pg.goods_code");
        			LEFT_OUTER_JOIN("pa_share_detail psd on o.order_no = psd.trans_no and share_type = '7'");
        			WHERE("o.order_status = '2'");
        		}
        	};
        	where(sql, order);
        	return sql.toString();
        }
        public String countReceived(Map<String, Object> param){
        	final PaOrder order = (PaOrder) param.get("order");
//        	order.setOrderStatus("3");
        	SQL sql = new SQL(){
        		{
//                    SELECT("count(o.id) as receivedTotal, sum(o.total_amount) as totalReceivedAmount");
//                    FROM("pa_order o");
//                    LEFT_OUTER_JOIN("pa_user_info u on o.user_code=u.user_code");
        			SELECT("count(o.id) as receivedTotal, sum(o.total_amount) as totalReceivedAmount");
        			FROM("pa_order o");
        			LEFT_OUTER_JOIN("pa_user_info u on o.user_code=u.user_code");
        			LEFT_OUTER_JOIN("pa_goods pg on o.good_code=pg.goods_code");
        			LEFT_OUTER_JOIN("pa_share_detail psd on o.order_no=psd.trans_no and share_type='7'");
        			WHERE("o.order_status = '2'");
        		}
        	};
        	where(sql, order);
        	return sql.toString();
        }

        public String countWaitSend(Map<String, Object> param){
            final PaOrder order = (PaOrder) param.get("order");
//            order.setOrderStatus("0");
            SQL sql = new SQL(){
                {
//                    SELECT("count(o.id) as waitSendTotal, sum(o.total_amount) as totalWaitSendAmount");
//                    FROM("pa_order o");
//                    LEFT_OUTER_JOIN("pa_user_info u on o.user_code=u.user_code");
                    SELECT("count(o.id) as waitSendTotal, sum(o.total_amount) as totalWaitSendAmount");
                    FROM("pa_order o");
                    LEFT_OUTER_JOIN("pa_user_info u on o.user_code=u.user_code");
                    LEFT_OUTER_JOIN("pa_goods pg on o.good_code=pg.goods_code");
                    LEFT_OUTER_JOIN("pa_share_detail psd on o.order_no=psd.trans_no and share_type='7'");
                    WHERE("o.order_status = '0'");
                }
            };
            where(sql, order);
            return sql.toString();
        }

        public String selectPaOrderByParam(Map<String, Object> param){
            final PaOrder order = (PaOrder) param.get("order");
            SQL sql = new SQL(){
                {
                    SELECT("u.real_name as userName,u.user_type,u.agent_no,u.agent_node," +
                            "o.order_no,o.post_no,o.transport_company,o.g_name,o.num,o.total_amount,o.user_code,pg.img,o.remark," +
                            "o.send_type,o.is_platform,o.trans_time,o.receipt_date,o.goods_total,o.entry_status,o.entry_time,o.size,o.color,o.price," +
                            "o.trans_channel,o.order_status,o.receiver,o.receiver_mobile,o.receiver_address,o.send_time,o.create_time," +
                            "psd.share_amount,psd.acc_status,psd.acc_time " +
                            "");
                    FROM("pa_order o");
                    LEFT_OUTER_JOIN("pa_user_info u on o.user_code=u.user_code");
                    LEFT_OUTER_JOIN("pa_goods pg on o.good_code=pg.goods_code");
                    LEFT_OUTER_JOIN("pa_share_detail psd on o.order_no=psd.trans_no and share_type='7'");
                    ORDER_BY("create_time DESC");
                }
            };
            where(sql, order);
            return sql.toString();
        }

        public void where(SQL sql, PaOrder order){
            sql.WHERE("u.agent_node like concat(#{order.agentNode}, '%')");
            if(StringUtils.isNotBlank(order.getUserName())){
            	sql.WHERE("u.real_name like concat(concat(#{order.userName},'%'), '%')");
            }
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sql.WHERE("o.order_no=#{order.orderNo}");
            }
            if(StringUtils.isNotBlank(order.getUserCode())){
                sql.WHERE("o.user_code=#{order.userCode}");
            }
            if(StringUtils.isNotBlank(order.getTransChannel())){
                sql.WHERE("o.trans_channel=#{order.transChannel}");
            }
            if(StringUtils.isNotBlank(order.getOrderStatus())){
                sql.WHERE("o.order_status=#{order.orderStatus}");
            }
            if(StringUtils.isNotBlank(order.getSendType())){
                sql.WHERE("o.send_type=#{order.sendType}");
            }
            if(StringUtils.isNotBlank(order.getIsPlatform())){
                sql.WHERE("o.is_platform=#{order.isPlatform}");
            }
            if(StringUtils.isNotBlank(order.getCreateTimeBegin())){
                sql.WHERE("o.create_time>=#{order.createTimeBegin}");
            }
            if(StringUtils.isNotBlank(order.getCreateTimeEnd())){
                sql.WHERE("o.create_time<=#{order.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getTransTimeBegin())){
                sql.WHERE("o.trans_time>=#{order.transTimeBegin}");
            }
            if(StringUtils.isNotBlank(order.getTransTimeEnd())){
                sql.WHERE("o.trans_time<=#{order.transTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getSendTimeBegin())){
                sql.WHERE("o.send_time>=#{order.sendTimeBegin}");
            }
            if(StringUtils.isNotBlank(order.getSendTimeEnd())){
                sql.WHERE("o.send_time<=#{order.sendTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getReceiptDateBegin())){
                sql.WHERE("o.receipt_date>=#{order.receiptDateBegin}");
            }
            if(StringUtils.isNotBlank(order.getReceiptDateEnd())){
                sql.WHERE("o.receipt_date<=#{order.receiptDateEnd}");
            }
            if(StringUtils.isNotBlank(order.getEntryTimeBegin())){
            	sql.WHERE("o.entry_time >= #{order.entryTimeBegin}");
            }
            if(StringUtils.isNotBlank(order.getEntryTimeEnd())){
            	sql.WHERE("o.entry_time <= #{order.entryTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getAccTimeBegin())){
            	sql.WHERE("psd.acc_time >= #{order.accTimeBegin}");
            }
            if(StringUtils.isNotBlank(order.getAccTimeEnd())){
            	sql.WHERE("psd.acc_time <= #{order.accTimeEnd}");
            }
        }

    }
    
}
