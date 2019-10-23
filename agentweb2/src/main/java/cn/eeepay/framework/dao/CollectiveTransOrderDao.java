package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.CollectiveTransOrder;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

@WriteReadDataSource
public interface CollectiveTransOrderDao {

//	@SelectProvider(type=SqlProvider.class,method="selectByOrderNo")
    @Select("select profits_${agentNode} as num from collective_trans_order where order_no=#{orderNo}")
    @ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder selectByOrderNo(@Param("agentNode")int agentNode,@Param("orderNo")String orderNo);
    
    
    @SuppressWarnings("rawtypes")
	@Select("select cast(date_format(trans_time, '%Y-%m-%d %H:00:00') as datetime) as trans_time, business_product_id, service_id, agent_node"
    		+ ", count(*) as total_count, ifnull(sum(trans_amount),0) as trans_amount"
    		+ ", ifnull(sum(profits_1),0) as profits_1"
    		+ ", ifnull(sum(profits_2),0) as profits_2"
    		+ ", ifnull(sum(profits_3),0) as profits_3"
    		+ ", ifnull(sum(profits_4),0) as profits_4"
    		+ ", ifnull(sum(profits_5),0) as profits_5"
    		+ ", ifnull(sum(profits_6),0) as profits_6"
    		+ ", ifnull(sum(profits_7),0) as profits_7"
    		+ ", ifnull(sum(profits_8),0) as profits_8"
    		+ ", ifnull(sum(profits_9),0) as profits_9"
    		+ ", ifnull(sum(profits_10),0) as profits_10"
    		+ ", ifnull(sum(profits_11),0) as profits_11"
    		+ ", ifnull(sum(profits_12),0) as profits_12"
    		+ ", ifnull(sum(profits_13),0) as profits_13"
    		+ ", ifnull(sum(profits_14),0) as profits_14"
    		+ ", ifnull(sum(profits_15),0) as profits_15"
    		+ ", ifnull(sum(profits_16),0) as profits_16"
    		+ ", ifnull(sum(profits_17),0) as profits_17"
    		+ ", ifnull(sum(profits_18),0) as profits_18"
    		+ ", ifnull(sum(profits_19),0) as profits_19"
    		+ ", ifnull(sum(profits_20),0) as profits_20"
    		+ ", ifnull(sum(case when profits_1 > 0 then 1 else 0 end),0) as count_1"
    		+ ", ifnull(sum(case when profits_2 > 0 then 1 else 0 end),0) as count_2"
    		+ ", ifnull(sum(case when profits_3 > 0 then 1 else 0 end),0) as count_3"
    		+ ", ifnull(sum(case when profits_4 > 0 then 1 else 0 end),0) as count_4"
    		+ ", ifnull(sum(case when profits_5 > 0 then 1 else 0 end),0) as count_5"
    		+ ", ifnull(sum(case when profits_6 > 0 then 1 else 0 end),0) as count_6"
    		+ ", ifnull(sum(case when profits_7 > 0 then 1 else 0 end),0) as count_7"
    		+ ", ifnull(sum(case when profits_8 > 0 then 1 else 0 end),0) as count_8"
    		+ ", ifnull(sum(case when profits_9 > 0 then 1 else 0 end),0) as count_9"
    		+ ", ifnull(sum(case when profits_10 > 0 then 1 else 0 end),0) as count_10"
    		+ ", ifnull(sum(case when profits_11 > 0 then 1 else 0 end),0) as count_11"
    		+ ", ifnull(sum(case when profits_12 > 0 then 1 else 0 end),0) as count_12"
    		+ ", ifnull(sum(case when profits_13 > 0 then 1 else 0 end),0) as count_13"
    		+ ", ifnull(sum(case when profits_14 > 0 then 1 else 0 end),0) as count_14"
    		+ ", ifnull(sum(case when profits_15 > 0 then 1 else 0 end),0) as count_15"
    		+ ", ifnull(sum(case when profits_16 > 0 then 1 else 0 end),0) as count_16"
    		+ ", ifnull(sum(case when profits_17 > 0 then 1 else 0 end),0) as count_17"
    		+ ", ifnull(sum(case when profits_18 > 0 then 1 else 0 end),0) as count_18"
    		+ ", ifnull(sum(case when profits_19 > 0 then 1 else 0 end),0) as count_19"
    		+ ", ifnull(sum(case when profits_20 > 0 then 1 else 0 end),0) as count_20"
    		+ " from collective_trans_order"
    		+ " where id between  #{id} and #{maxId} and trans_status = 'SUCCESS'"
    		+ " group by cast(date_format(trans_time, '%Y-%m-%d %H:00:00') as datetime), business_product_id, service_id, agent_node"
    		+ " UNION ALL"
    		+ " select cast(date_format(st.create_time, '%Y-%m-%d %H:00:00') as datetime) as trans_time, cto.business_product_id, 0 as service_id, cto.agent_node"
    		+ ", count(1) as total_count, ifnull(sum(st.out_amount),0) as trans_amount"
    		+ ", ifnull(sum(st.profits_1),0) as profits_1"
    		+ ", ifnull(sum(st.profits_2),0) as profits_2"
    		+ ", ifnull(sum(st.profits_3),0) as profits_3"
    		+ ", ifnull(sum(st.profits_4),0) as profits_4"
    		+ ", ifnull(sum(st.profits_5),0) as profits_5"
    		+ ", ifnull(sum(st.profits_6),0) as profits_6"
    		+ ", ifnull(sum(st.profits_7),0) as profits_7"
    		+ ", ifnull(sum(st.profits_8),0) as profits_8"
    		+ ", ifnull(sum(st.profits_9),0) as profits_9"
    		+ ", ifnull(sum(st.profits_10),0) as profits_10"
    		+ ", ifnull(sum(st.profits_11),0) as profits_11"
    		+ ", ifnull(sum(st.profits_12),0) as profits_12"
    		+ ", ifnull(sum(st.profits_13),0) as profits_13"
    		+ ", ifnull(sum(st.profits_14),0) as profits_14"
    		+ ", ifnull(sum(st.profits_15),0) as profits_15"
    		+ ", ifnull(sum(st.profits_16),0) as profits_16"
    		+ ", ifnull(sum(st.profits_17),0) as profits_17"
    		+ ", ifnull(sum(st.profits_18),0) as profits_18"
    		+ ", ifnull(sum(st.profits_19),0) as profits_19"
    		+ ", ifnull(sum(st.profits_20),0) as profits_20"
    		+ ", ifnull(sum(case when st.profits_1 > 0 then 1 else 0 end),0) as count_1"
    		+ ", ifnull(sum(case when st.profits_2 > 0 then 1 else 0 end),0) as count_2"
    		+ ", ifnull(sum(case when st.profits_3 > 0 then 1 else 0 end),0) as count_3"
    		+ ", ifnull(sum(case when st.profits_4 > 0 then 1 else 0 end),0) as count_4"
    		+ ", ifnull(sum(case when st.profits_5 > 0 then 1 else 0 end),0) as count_5"
    		+ ", ifnull(sum(case when st.profits_6 > 0 then 1 else 0 end),0) as count_6"
    		+ ", ifnull(sum(case when st.profits_7 > 0 then 1 else 0 end),0) as count_7"
    		+ ", ifnull(sum(case when st.profits_8 > 0 then 1 else 0 end),0) as count_8"
    		+ ", ifnull(sum(case when st.profits_9 > 0 then 1 else 0 end),0) as count_9"
    		+ ", ifnull(sum(case when st.profits_10 > 0 then 1 else 0 end),0) as count_10"
    		+ ", ifnull(sum(case when st.profits_11 > 0 then 1 else 0 end),0) as count_11"
    		+ ", ifnull(sum(case when st.profits_12 > 0 then 1 else 0 end),0) as count_12"
    		+ ", ifnull(sum(case when st.profits_13 > 0 then 1 else 0 end),0) as count_13"
    		+ ", ifnull(sum(case when st.profits_14 > 0 then 1 else 0 end),0) as count_14"
    		+ ", ifnull(sum(case when st.profits_15 > 0 then 1 else 0 end),0) as count_15"
    		+ ", ifnull(sum(case when st.profits_16 > 0 then 1 else 0 end),0) as count_16"
    		+ ", ifnull(sum(case when st.profits_17 > 0 then 1 else 0 end),0) as count_17"
    		+ ", ifnull(sum(case when st.profits_18 > 0 then 1 else 0 end),0) as count_18"
    		+ ", ifnull(sum(case when st.profits_19 > 0 then 1 else 0 end),0) as count_19"
    		+ ", ifnull(sum(case when st.profits_20 > 0 then 1 else 0 end),0) as count_20"
    		+ " from collective_trans_order cto join settle_transfer st on cto.order_no = st.order_no and st.status='4'"
    		+ " where cto.id between  #{id} and #{maxId} and cto.trans_status = 'SUCCESS'"
    		+ " group by cast(date_format(st.create_time, '%Y-%m-%d %H:00:00') as datetime), cto.business_product_id, cto.agent_node")
    @ResultType(Map.class)
	List<Map> selectGroupAfterId(@Param("id") int id,@Param("maxId") int maxId);

	/**
	 * 取当前时间向前推移10分钟的最大交易自增id,
	 * 防止出现临界值前创建订单,但是临界值后完成交易的订单未被统计
	 * @return
	 */
	@Select(" SELECT id FROM collective_trans_order \n" +
			" WHERE create_time <= DATE_ADD(NOW(),INTERVAL -10 MINUTE)\n" +
			" ORDER BY id DESC \n" +
			" LIMIT 1")
    String selectMaxId();

	@SelectProvider(type = CollectiveTransOrderDao.SqlProvider.class, method = "batchSelectPayMethod")
	@ResultType(Map.class)
    List<Map<String, String>> batchSelectPayMethod(@Param("orderNos") List<String> orderNos);

	public class SqlProvider {
		public String batchSelectPayMethod(Map<String, Object> param){
			final List<String> list = (List<String>) param.get("orderNos");
			SQL sql = new SQL(){
				{
					SELECT("order_no,pay_method");
					FROM("collective_trans_order");
					StringBuffer sb = new StringBuffer();
					sb.append("(");
					for (int i = 0; i < list.size(); i++) {
						sb.append("'" + list.get(i) + "'");
						sb.append(i == list.size() - 1 ? "": ",");
					}
					sb.append(")");
					WHERE("order_no in " + sb.toString());
					//ORDER_BY("Field (merchant_no," + sb.toString().substring(1));
				}
			};
			return sql.toString();
		}
	}
}