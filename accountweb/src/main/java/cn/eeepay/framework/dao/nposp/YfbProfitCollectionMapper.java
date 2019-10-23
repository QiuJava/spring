package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.YfbProfitCollection;
import cn.eeepay.framework.model.nposp.YfbRepayPlan;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface YfbProfitCollectionMapper {

	@Insert("<script>"
			+" insert into yfb_profit_collection(collectionNo,collection_batch_no,mer_type,mer_no,agent_node,service_cost_rate,service_cost_single_fee,"
			+ " profit_amount,operator,income_status,income_time,create_time,last_update_time)"
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
			+ "( #{item.collectionNo},#{item.collectionBatchNo},#{item.merType},#{item.merNo},"
			+ " #{item.agentNode},#{item.serviceCostRate},#{item.serviceCostSingleFee},"
			+ " #{item.profitAmount},#{item.operator},#{item.incomeStatus},#{item.incomeTime},#{item.createTime},#{item.lastUpdateTime})"
			+ " </foreach > "
            + " </script>")
	int insertServiceShareDaySettleBatch(@Param("list") List<YfbProfitCollection> list);


	@Select("SELECT * FROM yfb_profit_collection WHERE collection_batch_no = #{collectionBatchNo}")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbProfitCollectionMapper.BaseResultMap")
	List<YfbProfitCollection> findServiceShareDaySettleByCollectionBatchNo(@Param("collectionBatchNo")String collectionBatchNo);

	@SelectProvider( type=SqlProvider.class,method="findServiceShareList")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbProfitCollectionMapper.BaseResultMap")
	List<YfbProfitCollection> findServiceShareList(@Param("yfbProfitCollection")YfbProfitCollection yfbProfitCollection, @Param("sort")Sort sort, Page<YfbProfitCollection> page);

	@SelectProvider( type=SqlProvider.class,method="findServiceShareList")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbProfitCollectionMapper.BaseResultMap")
	List<YfbProfitCollection> exportServiceInAccountList(@Param("yfbProfitCollection")YfbProfitCollection yfbProfitCollection);

	@SelectProvider( type=SqlProvider.class,method="serviceInAccountCollectionDataCount")
	@Results(value = {
			@Result(property = "countAmount", column = "count_amount",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL)
	})
	Map<String,Object> serviceInAccountCollectionDataCount(@Param("yfbProfitCollection")YfbProfitCollection yfbProfitCollection);


	public class SqlProvider{

		public String findServiceShareList(final Map<String, Object> parameter) {
			final YfbProfitCollection yfbProfitCollection = (YfbProfitCollection) parameter.get("yfbProfitCollection");
			return new SQL(){{
				SELECT(" ypc.id,ypc.collection_no,ypc.collection_batch_no,ypc.mer_no, ai.agent_name,ai.agent_level,ypc.profit_amount,ypc.service_cost_rate,ypc.collection_time,ypc.create_time,ypc.income_status,ypc.income_time,ypc.profit_type,ypc.allow_income ");
				FROM(" yfb_profit_collection ypc left join agent_info ai on ai.agent_no = ypc.mer_no ");

				//汇总时间
				if(StringUtils.isNotBlank(yfbProfitCollection.getTallyTime1()) ){
					WHERE(" str_to_date(ypc.collection_time,'%Y-%m-%d %H:%i:%s') >= #{yfbProfitCollection.tallyTime1} ");
				}
				if(StringUtils.isNotBlank(yfbProfitCollection.getTallyTime2()) ){
					WHERE(" str_to_date(ypc.collection_time,'%Y-%m-%d %H:%i:%s') <= #{yfbProfitCollection.tallyTime2} ");
				}
				//服务商名称
				if (!StringUtils.isBlank(yfbProfitCollection.getAgentName()))
					WHERE(" ai.agent_name =  #{yfbProfitCollection.agentName} ");

				//服务商编号
				if (!StringUtils.isBlank(yfbProfitCollection.getMerNo()))
					WHERE(" ypc.mer_no = #{yfbProfitCollection.merNo} ");

				//分润金额1
				if (!StringUtils.isBlank(yfbProfitCollection.getFenMoney1()))
					WHERE(" ypc.profit_amount >= #{yfbProfitCollection.fenMoney1} ");

				//分润金额2
				if (!StringUtils.isBlank(yfbProfitCollection.getFenMoney2()))
					WHERE(" ypc.profit_amount <= #{yfbProfitCollection.fenMoney2}  ");

				//入账状态
				if(yfbProfitCollection.getIncomeStatus() != null && yfbProfitCollection.getIncomeStatus() != -1)
					WHERE(" ypc.income_status = #{yfbProfitCollection.incomeStatus} ");

				if(StringUtils.isNotBlank(yfbProfitCollection.getAgentLevel()) && !yfbProfitCollection.getAgentLevel().equals("ALL"))
					WHERE(" ai.agent_level = #{yfbProfitCollection.agentLevel}  ");

				//订单类型
				if(StringUtils.isNotBlank(yfbProfitCollection.getProfitType()) && !yfbProfitCollection.getProfitType().equals("ALL"))
					WHERE(" ypc.profit_type = #{yfbProfitCollection.profitType}  ");

				//是否需要入账
				if(StringUtils.isNotBlank(yfbProfitCollection.getAllowIncome()) && !yfbProfitCollection.getAllowIncome().equals("ALL"))
					WHERE(" ypc.allow_income = #{yfbProfitCollection.allowIncome}  ");


			}}.toString();
		}

		public String cardOrderCollectionDataCount(final Map<String, Object> parameter) {
			final YfbRepayPlan yfbRepayPlan = (YfbRepayPlan) parameter.get("yfbRepayPlan");
			final Integer type = (Integer) parameter.get("type");
			return new SQL(){{
				SELECT(" sum(yrp.ensure_amount) as count_ensure_amount ");
				FROM("yfb_repay_plan yrp left join yfb_pay_order ypo " +
						"on ypo.service_order_no = yrp.batch_no and ypo.service = 'ensure' " +
						"left join yfb_tally_his yth on yth.service_order_no = ypo.order_no and yth.service = 'trade'");
				//分润创建时间
				if(StringUtils.isNotBlank(yfbRepayPlan.getTallyTime1()) ){
					WHERE(" tally_time >= #{yfbRepayPlan.tallyTime1} ");
				}
				if(StringUtils.isNotBlank(yfbRepayPlan.getTallyTime2()) ){
					WHERE(" tally_time <= #{yfbRepayPlan.tallyTime2} ");
				}
				//批次号，订单号
				if (!StringUtils.isBlank(yfbRepayPlan.getBatchNo()))
					WHERE(" batch_no like  \"%\"#{yfbRepayPlan.batchNo}\"%\"  ");

				//还款人，商户号
				if (!StringUtils.isBlank(yfbRepayPlan.getMerchantNo()))
					WHERE(" merchant_no like  \"%\"#{yfbRepayPlan.merchantNo}\"%\"  ");
				//还款人手机号
				if (!StringUtils.isBlank(yfbRepayPlan.getMobileNo()))
					WHERE(" mobile_no like  \"%\"#{yfbRepayPlan.mobileNo}\"%\"  ");

				//任务金额
				if(yfbRepayPlan.getRepayAmount1()!=null)
					WHERE(" repay_amount >= #{yfbRepayPlan.repayAmount1.doubleValue()}");

				if(yfbRepayPlan.getRepayAmount2()!=null)
					WHERE(" repay_amount <= #{yfbRepayPlan.repayAmount2.doubleValue()}");

				//保证金
				if(yfbRepayPlan.getEnsureAmount1()!=null)
					WHERE(" ensure_amount >= #{yfbRepayPlan.ensureAmount1.doubleValue()}");

				if(yfbRepayPlan.getEnsureAmount2()!=null)
					WHERE(" ensure_amount <= #{yfbRepayPlan.ensureAmount2.doubleValue()}");

				//手续费
				if(yfbRepayPlan.getRepayFee1()!=null)
					WHERE(" repay_fee >= #{yfbRepayPlan.repayFee1.doubleValue()}");

				if(yfbRepayPlan.getRepayFee2()!=null)
					WHERE(" repay_fee <= #{yfbRepayPlan.repayFee2.doubleValue()}");

				//入账状态
				if(StringUtils.isNotBlank(yfbRepayPlan.getRuStatus()) && !"-1".equals(yfbRepayPlan.getRuStatus()))
					WHERE(" yth.status = #{yfbRepayPlan.ruStatus} ");

				WHERE("yth.`status` = #{type}");

				GROUP_BY("yrp.batch_no");

			}}.toString();
		}

		public String serviceInAccountCollectionDataCount(final Map<String, Object> parameter) {
			final YfbProfitCollection yfbProfitCollection = (YfbProfitCollection) parameter.get("yfbProfitCollection");
			return new SQL(){{
				SELECT(" sum(ypc.profit_amount) as count_amount");
				FROM(" yfb_profit_collection ypc left join agent_info ai on ai.agent_no = ypc.mer_no ");

				//汇总时间
				if(StringUtils.isNotBlank(yfbProfitCollection.getTallyTime1()) ){
					WHERE(" ypc.collection_time >= #{yfbProfitCollection.tallyTime1} ");
				}
				if(StringUtils.isNotBlank(yfbProfitCollection.getTallyTime2()) ){
					WHERE(" ypc.collection_time <= #{yfbProfitCollection.tallyTime2} ");
				}
				//服务商名称
				if (!StringUtils.isBlank(yfbProfitCollection.getAgentName()))
					WHERE(" ai.agent_name =  #{yfbProfitCollection.agentName} ");

				//服务商编号
				if (!StringUtils.isBlank(yfbProfitCollection.getMerNo()))
					WHERE(" ypc.mer_no = #{yfbProfitCollection.merNo} ");

				//分润金额1
				if (!StringUtils.isBlank(yfbProfitCollection.getFenMoney1()))
					WHERE(" ypc.profit_amount >= #{yfbProfitCollection.fenMoney1} ");

				//分润金额2
				if (!StringUtils.isBlank(yfbProfitCollection.getFenMoney2()))
					WHERE(" ypc.profit_amount <= #{yfbProfitCollection.fenMoney2}  ");

				//入账状态
				if(yfbProfitCollection.getIncomeStatus() != null && yfbProfitCollection.getIncomeStatus() != -1)
					WHERE(" ypc.income_status = #{yfbProfitCollection.incomeStatus} ");

				if(StringUtils.isNotBlank(yfbProfitCollection.getAgentLevel()) && !yfbProfitCollection.getAgentLevel().equals("ALL"))
					WHERE(" ai.agent_level = #{yfbProfitCollection.agentLevel}  ");

				//订单类型
				if(StringUtils.isNotBlank(yfbProfitCollection.getProfitType()) && !yfbProfitCollection.getProfitType().equals("ALL"))
					WHERE(" ypc.profit_type = #{yfbProfitCollection.profitType}  ");

				//是否需要入账
				if(StringUtils.isNotBlank(yfbProfitCollection.getAllowIncome()) && !yfbProfitCollection.getAllowIncome().equals("ALL"))
					WHERE(" ypc.allow_income = #{yfbProfitCollection.allowIncome}  ");

			}}.toString();
		}

	}









}
