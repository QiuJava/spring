package cn.eeepay.framework.dao.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AcqOutBill;
import cn.eeepay.framework.model.bill.BeforeAdjustApply;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public interface BeforeAdjustApplyMapper {

	@Insert("insert into before_adjust_apply(agent_no,agent_name,freeze_amount,activity_available_amount,activity_freeze_amount,generate_amount,apply_date,remark,applicant,is_apply)"
			+" values(#{beforeAdjustApply.agentNo},#{beforeAdjustApply.agentName},#{beforeAdjustApply.freezeAmount},#{beforeAdjustApply.activityAvailableAmount},#{beforeAdjustApply.activityFreezeAmount},#{beforeAdjustApply.generateAmount},#{beforeAdjustApply.applyDate},#{beforeAdjustApply.remark},#{beforeAdjustApply.applicant},#{beforeAdjustApply.isApply})"
			)
	int insertBeforeAdjustApply(@Param("beforeAdjustApply") BeforeAdjustApply beforeAdjustApply);


	@SelectProvider( type=SqlProvider.class,method="findBeforeAdjustApplyList")
	@ResultMap("cn.eeepay.framework.dao.bill.BeforeAdjustApplyMapper.BaseResultMap")
	List<BeforeAdjustApply> findBeforeAdjustApplyList(@Param("beforeAdjustApply") BeforeAdjustApply beforeAdjustApply, @Param("params") Map<String, String> params, @Param("sort") Sort sort, Page<BeforeAdjustApply> page);

	@SelectProvider( type=SqlProvider.class,method="findBeforeAdjustApplyList")
	@ResultMap("cn.eeepay.framework.dao.bill.BeforeAdjustApplyMapper.BaseResultMap")
	List<BeforeAdjustApply> exportBeforeAdjustApplyList(@Param("beforeAdjustApply") BeforeAdjustApply beforeAdjustApply);

	@SelectProvider(type=SqlProvider.class,method="findBeforeAdjustApplyListCollection")
	@Results(value = {
			@Result(property = "allFreezeAmount", column = "all_freeze_amount",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL),
			@Result(property = "allActivityAvailableAmount", column = "all_activity_available_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allActivityFreezeAmount", column = "all_activity_freeze_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allGenerateAmount", column = "all_generate_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL)
	})
	Map<String,BigDecimal> findBeforeAdjustApplyListCollection(@Param("beforeAdjustApply")BeforeAdjustApply beforeAdjustApply);


	public class SqlProvider{
		public String findBeforeAdjustApplyList(final Map<String, Object> parameter) {
			final BeforeAdjustApply beforeAdjustApply = (BeforeAdjustApply) parameter.get("beforeAdjustApply");
			return new SQL(){{
				SELECT("*");
				FROM(" before_adjust_apply ");
				if(StringUtils.isNotBlank(beforeAdjustApply.getAgentNo())){
					WHERE(" agent_no  = #{beforeAdjustApply.agentNo} ");
				}
				if(StringUtils.isNotBlank(beforeAdjustApply.getDate1())){
					WHERE("apply_date >= #{beforeAdjustApply.date1}");
				}
				if(StringUtils.isNotBlank(beforeAdjustApply.getDate2())){
					WHERE("apply_date <= #{beforeAdjustApply.date2}");
				}
				if(beforeAdjustApply.getIsDetail() == null){
					WHERE("(is_apply is null or is_apply = 1)");
				}else{
					WHERE("is_apply is not null");
				}
				if(beforeAdjustApply.getIsDetail() != null){
					WHERE("(activity_available_amount != 0 or activity_freeze_amount != 0)");
				}
				ORDER_BY("apply_date desc");
			}}.toString();
		}


		public String findBeforeAdjustApplyListCollection(final Map<String, Object> parameter) {
			final BeforeAdjustApply beforeAdjustApply = (BeforeAdjustApply) parameter.get("beforeAdjustApply");
			return new SQL(){{
				SELECT("sum(freeze_amount) as all_freeze_amount,sum(activity_available_amount) as all_activity_available_amount,sum(activity_freeze_amount) as all_activity_freeze_amount,sum(generate_amount) as all_generate_amount");
				FROM(" before_adjust_apply ");
				if(StringUtils.isNotBlank(beforeAdjustApply.getAgentNo())){
					WHERE(" agent_no  = #{beforeAdjustApply.agentNo} ");
				}
				if(StringUtils.isNotBlank(beforeAdjustApply.getDate1())){
					WHERE("apply_date >= #{beforeAdjustApply.date1}");
				}
				if(StringUtils.isNotBlank(beforeAdjustApply.getDate2())){
					WHERE("apply_date <= #{beforeAdjustApply.date2}");
				}
				if(beforeAdjustApply.getIsDetail() != null){
					WHERE("(activity_available_amount != 0 or activity_freeze_amount != 0)");
				}
				if(beforeAdjustApply.getIsDetail() == null){		//如果是预调账的则不显示定时任务的
					WHERE("(is_apply is null or is_apply = 1)");
				}else{
					WHERE("is_apply is not null");
				}
				ORDER_BY("id");
			}}.toString();
		}

	}

	
}
