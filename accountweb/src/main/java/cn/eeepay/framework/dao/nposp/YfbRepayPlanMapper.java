package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.SuperPushShare;
import cn.eeepay.framework.model.nposp.YfbRepayPlan;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 超级推商户分润记录
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 *
 */
public interface YfbRepayPlanMapper {


	@SelectProvider( type=SqlProvider.class,method="findYfbRepayPlanList")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbRepayPlanMapper.BaseResultMap")
	List<SuperPushShare> findYfbRepayPlanList(@Param("yfbRepayPlan") YfbRepayPlan yfbRepayPlan, @Param("sort") Sort sort, Page<YfbRepayPlan> page);

	@SelectProvider( type=YfbRepayPlanMapper.SqlProvider.class,method="findYfbRepayPlanList")
	@ResultMap("cn.eeepay.framework.dao.nposp.YfbRepayPlanMapper.BaseResultMap")
    List<YfbRepayPlan> exportYfbRepayPlanList(@Param("yfbRepayPlan") YfbRepayPlan yfbRepayPlan);

	@SelectProvider(type=YfbRepayPlanMapper.SqlProvider.class,method="cardOrderCollectionDataCount")
	@Results(value = {
			@Result(property = "allEnterAmount", column = "all_enter_amount",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL)
	})
	Map<String,Object> cardOrderCollectionDataCount(@Param("yfbRepayPlan") YfbRepayPlan yfbRepayPlan,@Param("type")Integer type);

	public class SqlProvider{

		public String findYfbRepayPlanList(final Map<String, Object> parameter) {
			final YfbRepayPlan yfbRepayPlan = (YfbRepayPlan) parameter.get("yfbRepayPlan");
			return new SQL(){{
				SELECT(" yrp.*,yrmi.user_name,yrmi.mobile_no,yth.`status` as ru_status,yth.tally_time,yth.create_time as count_time,yth.service_order_no ");
				FROM(" yfb_repay_plan yrp left join yfb_pay_order ypo on ypo.service_order_no = yrp.batch_no and ypo.service = 'ensure' left join yfb_tally_his yth " +
						"on yth.service_order_no = ypo.order_no and yth.service = 'trade' left join yfb_repay_merchant_info yrmi on yrmi.merchant_no = yrp.merchant_no");
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

			}}.toString();
		}

		public String cardOrderCollectionDataCount(final Map<String, Object> parameter) {
			final YfbRepayPlan yfbRepayPlan = (YfbRepayPlan) parameter.get("yfbRepayPlan");
			final Integer type = (Integer) parameter.get("type");
			return new SQL(){{
				SELECT(" count(yrp.ensure_amount) as count_ensure_amount ");
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

	}








	


	



}
