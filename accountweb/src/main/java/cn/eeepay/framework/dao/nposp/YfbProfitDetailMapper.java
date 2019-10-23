package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.*;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 超级还服务商分润记录
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 *
 */
public interface YfbProfitDetailMapper {


	@SelectProvider( type=SqlProvider.class,method="findYfbProfitDetailList")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbProfitDetailMapper.BaseResultMap")
	List<YfbProfitDetail> findYfbProfitDetailList(@Param("yfbProfitDetail") YfbProfitDetail yfbProfitDetail, @Param("sort") Sort sort, Page<YfbProfitDetail> page);

	@SelectProvider( type=SqlProvider.class,method="findYfbProfitDetailList")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbProfitDetailMapper.BaseResultMap")
    List<YfbProfitDetail> exportServiceShareList(@Param("yfbProfitDetail") YfbProfitDetail yfbProfitDetail);

	@SelectProvider(type=SqlProvider.class,method="serviceCollectionDataCount")
	@Results(value = {
			@Result(property = "countAmount", column = "count_amount",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL)
	})
	Map<String,Object> serviceCollectionDataCount(@Param("yfbProfitDetail") YfbProfitDetail yfbProfitDetail);

	@SelectProvider( type=SqlProvider.class,method="findCollectionGropByDate")
	@Results(value = {
			@Result(property = "mer_type", column = "mer_type",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property = "mer_no", column = "mer_no",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property = "agent_node", column = "agent_node",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property = "service_cost_rate", column = "service_cost_rate",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "service_cost_single_fee", column = "service_cost_single_fee",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "profit_amount", column = "profit_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
	})
	List<Map<String, Object>> findCollectionGropByDate(@Param("params") Map<String, String> params);

	@Select("select * from yfb_pay_channel where channel_code = #{acqCode}")
	@ResultType(java.util.Map.class)
	Map<String,Object> findYfbPayChannelByAcqCode(@Param("acqCode") String acqCode);

	@Select("SELECT * FROM yfb_profit_detail WHERE profit_mer_no = #{yfbProfitCollection.mer_no} "
			+ " AND DATE_FORMAT(trans_time, '%Y-%m-%d') = DATE_FORMAT(#{yfbProfitCollection.createTime}, '%Y-%m-%d') and collection_batch_no is null")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbProfitDetailMapper.BaseResultMap")
    List<YfbProfitDetail> findYfbProfitDetailListByModel(@Param("yfbProfitCollection")YfbProfitCollection yfbProfitCollection);

	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+"  update yfb_profit_detail set collection_batch_no=#{item.collectionBatchNo},"
			+ " profit_no=#{item.profitNo},"
			+ " collection_time=#{item.collectionTime}"
			+"   where id=#{item.id}"
			+ "     </foreach> "
			+ " </script>")
	int updateServiceShareBatch(@Param("list")List<YfbProfitDetail> asdList);

	@Select("select * from yfb_pay_order where order_no = #{orderNo}")
	@Results(value = {
			@Result(property = "transFeeRate", column = "trans_fee_rate",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "acqFeeRate", column = "acq_fee_rate",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property = "acqFee", column = "acq_fee",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "acqCode", column = "acq_code",javaType=String.class,jdbcType=JdbcType.VARCHAR)
	})
	YfbPayOrder findPayOrderByOrderNo(String orderNo);

	public class SqlProvider{

		public String findYfbProfitDetailList(final Map<String, Object> parameter) {
			final YfbProfitDetail yfbProfitDetail = (YfbProfitDetail) parameter.get("yfbProfitDetail");
			return new SQL(){{
				SELECT(" ypd.id,ypd.order_no,ypd.trans_time,ypd.profit_mer_no,ai.agent_name,ypd.share_amount,ypd.collection_batch_no,yrp.repay_amount,yrp.ensure_amount,yrp.repay_fee,yrp.actual_pay_fee,yrp.actual_withdraw_fee,yrp.repay_fee_rate,yrmi.merchant_no,yrmi.user_name,ai.agent_level,ai.parent_id,ai.one_level_id,yrp.success_pay_amount,yrp.success_repay_amount,yrp.acq_code,ypd.profit_type,ypd.to_profit_amount");
				FROM(" yfb_profit_detail ypd left join agent_info ai on ypd.profit_mer_no = ai.agent_no left join yfb_repay_plan yrp on yrp.batch_no = ypd.order_no left join yfb_repay_merchant_info yrmi on yrmi.merchant_no = ypd.merchant_no " );
				//服务商名称
				if (!StringUtils.isBlank(yfbProfitDetail.getAgentName()))
					WHERE(" ai.agent_name = #{yfbProfitDetail.agentName} ");

				//服务商编号
				if (!StringUtils.isBlank(yfbProfitDetail.getProfitMerNo()))
					WHERE(" ypd.profit_mer_no = #{yfbProfitDetail.profitMerNo}  ");

				//还款订单
				if (!StringUtils.isBlank(yfbProfitDetail.getOrderNo()))
					WHERE(" ypd.order_no = #{yfbProfitDetail.orderNo} ");

				//用户编号
				if (!StringUtils.isBlank(yfbProfitDetail.getMerchantNo()))
					WHERE(" yrmi.merchant_no = #{yfbProfitDetail.merchantNo}  ");

				//用户名称
				if (!StringUtils.isBlank(yfbProfitDetail.getUserName()))
					WHERE(" yrmi.user_name = #{yfbProfitDetail.userName} ");

				//分润金额1
				if (!StringUtils.isBlank(yfbProfitDetail.getFenMoney1()))
					WHERE(" ypd.share_amount >= #{yfbProfitDetail.fenMoney1} ");

				//分润金额2
				if (!StringUtils.isBlank(yfbProfitDetail.getFenMoney2()))
					WHERE(" ypd.share_amount <= #{yfbProfitDetail.fenMoney2}  ");

				//订单时间1
				if (!StringUtils.isBlank(yfbProfitDetail.getOrderTime1()))
					WHERE(" ypd.trans_time >= #{yfbProfitDetail.orderTime1} ");

				//订单时间2
				if (!StringUtils.isBlank(yfbProfitDetail.getOrderTime2()))
					WHERE(" ypd.trans_time <= #{yfbProfitDetail.orderTime2}  ");

				//代理商级别
				if(StringUtils.isNotBlank(yfbProfitDetail.getAgentLevel()) && !yfbProfitDetail.getAgentLevel().equals("ALL"))
					WHERE(" ai.agent_level = #{yfbProfitDetail.agentLevel}  ");

				//订单类型
				if(StringUtils.isNotBlank(yfbProfitDetail.getProfitType()) && !yfbProfitDetail.getProfitType().equals("ALL"))
					WHERE(" ypd.profit_type = #{yfbProfitDetail.profitType}  ");


			}}.toString();
		}

		public String serviceCollectionDataCount(final Map<String, Object> parameter) {
			final YfbProfitDetail yfbProfitDetail = (YfbProfitDetail) parameter.get("yfbProfitDetail");
			return new SQL(){{
				SELECT(" sum(ypd.share_amount) as count_amount ");
				FROM(" yfb_profit_detail ypd left join agent_info ai on ypd.profit_mer_no = ai.agent_no left join yfb_repay_plan yrp on yrp.batch_no = ypd.order_no left join yfb_repay_merchant_info yrmi on yrmi.merchant_no = ypd.merchant_no" );
				//服务商名称
				if (!StringUtils.isBlank(yfbProfitDetail.getAgentName()))
					WHERE(" ai.agent_name = #{yfbProfitDetail.agentName} ");

				//服务商编号
				if (!StringUtils.isBlank(yfbProfitDetail.getProfitMerNo()))
					WHERE(" ypd.profit_mer_no = #{yfbProfitDetail.profitMerNo}  ");

				//还款订单
				if (!StringUtils.isBlank(yfbProfitDetail.getOrderNo()))
					WHERE(" ypd.order_no = #{yfbProfitDetail.orderNo} ");

				//用户编号
				if (!StringUtils.isBlank(yfbProfitDetail.getMerchantNo()))
					WHERE(" yrmi.merchant_no = #{yfbProfitDetail.merchantNo}  ");

				//用户名称
				if (!StringUtils.isBlank(yfbProfitDetail.getUserName()))
					WHERE(" yrmi.user_name = #{yfbProfitDetail.userName} ");

				//分润金额1
				if (!StringUtils.isBlank(yfbProfitDetail.getFenMoney1()))
					WHERE(" ypd.share_amount >= #{yfbProfitDetail.fenMoney1} ");

				//分润金额2
				if (!StringUtils.isBlank(yfbProfitDetail.getFenMoney2()))
					WHERE(" ypd.share_amount <= #{yfbProfitDetail.fenMoney2}  ");

				//订单时间1
				if (!StringUtils.isBlank(yfbProfitDetail.getOrderTime1()))
					WHERE(" ypd.trans_time >= #{yfbProfitDetail.orderTime1} ");

				//订单时间2
				if (!StringUtils.isBlank(yfbProfitDetail.getOrderTime2()))
					WHERE(" ypd.trans_time <= #{yfbProfitDetail.orderTime2}  ");

				//代理商级别
				if(StringUtils.isNotBlank(yfbProfitDetail.getAgentLevel()) && !yfbProfitDetail.getAgentLevel().equals("ALL"))
					WHERE(" ai.agent_level = #{yfbProfitDetail.agentLevel}  ");

				//订单类型
				if(StringUtils.isNotBlank(yfbProfitDetail.getProfitType()) && !yfbProfitDetail.getProfitType().equals("ALL"))
					WHERE(" ypd.profit_type = #{yfbProfitDetail.profitType}  ");

			}}.toString();
		}

		public String findCollectionGropByDate(final Map<String, Object> parameter){
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String createTime1 = params.get("createTime1");
			return new SQL(){{
				SELECT(" ypd.profit_mer_type as mer_type,ypd.profit_mer_no as mer_no,count(ypd.share_amount) as profit_amount,ai.agent_node  " +
						",jsc.rate as service_cost_rate ,jsc.single_amount as service_cost_single_fee");
				FROM(" yfb_profit_detail ypd left join agent_info ai on ai.agent_no = ypd.profit_mer_no" +
						" left join yfb_service_cost jsc on ypd.profit_mer_no = jsc.agent_no");
				WHERE(" collection_batch_no is null and DATE_FORMAT(ypd.trans_time, '%Y-%m-%d') = DATE_FORMAT(#{params.createTime1}, '%Y-%m-%d') ");
				GROUP_BY(" ypd.profit_mer_no,DATE_FORMAT(ypd.trans_time, '%Y-%m-%d') ");
				ORDER_BY(" ypd.trans_time ");
			}}.toString();
		}


	}







}
