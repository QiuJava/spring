package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.HlfAgentDebtRecord;


/**
 * 
 *
 */
public interface HlfAgentDebtRecordMapper {
	@SelectProvider( type=SqlProvider.class,method="findHlfAgentDebtRecordList")
	@ResultMap("cn.eeepay.framework.dao.bill.HlfAgentDebtRecordMapper.BaseResultMap")
	List<HlfAgentDebtRecord> findHlfAgentDebtRecordList(@Param("hlfAgentDebtRecord")HlfAgentDebtRecord hlfAgentDebtRecord, Sort sort, Page<HlfAgentDebtRecord> page);
	
	@SelectProvider( type=SqlProvider.class,method="exportHlfAgentDebtRecordList")
	@ResultMap("cn.eeepay.framework.dao.bill.HlfAgentDebtRecordMapper.BaseResultMap")
	List<HlfAgentDebtRecord> exportHlfAgentDebtRecordList(@Param("hlfAgentDebtRecord")HlfAgentDebtRecord hlfAgentDebtRecord);
	
	@SelectProvider( type=SqlProvider.class,method="findHlfAgentDebtRecordListCollection")
	@Results(value = {  
            @Result(property = "debtAmount", column = "all_debt_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
        })
	Map<String, BigDecimal> findHlfAgentDebtRecordListCollection(@Param("hlfAgentDebtRecord")HlfAgentDebtRecord hlfAgentDebtRecord);
	
	@SelectProvider( type=SqlProvider.class,method="findHlfAgentDebtRecordShouldDebtAmountCollection")
	@Results(value = {  
            @Result(property = "shouldDebtAmount", column = "all_should_debt_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	Map<String, BigDecimal> findHlfAgentDebtRecordShouldDebtAmountCollection(@Param("hlfAgentDebtRecord")HlfAgentDebtRecord hlfAgentDebtRecord);
	
	@SelectProvider( type=SqlProvider.class,method="findHlfAgentDebtRecordAgentNo")
	@ResultMap("cn.eeepay.framework.dao.bill.HlfAgentDebtRecordMapper.BaseResultMap")
	List<HlfAgentDebtRecord> findHlfAgentDebtRecordAgentNo(@Param("hlfAgentDebtRecord")HlfAgentDebtRecord hlfAgentDebtRecord);
	
	@SelectProvider( type=SqlProvider.class,method="findHlfAgentDebtRecordListCollectionByList")
	@Results(value = {  
            @Result(property = "totalDebtAmount", column = "all_total_debt_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	Map<String, BigDecimal> findHlfAgentDebtRecordListCollectionByList(@Param("agentInfoList")String agentInfoList);
	
	 public class SqlProvider{

			public String findHlfAgentDebtRecordList(final Map<String, Object> parameter) {
				final HlfAgentDebtRecord hlfAgentDebtRecord = (HlfAgentDebtRecord) parameter.get("hlfAgentDebtRecord");
				return new SQL(){{
					SELECT(" a.*  ");
					FROM(" hlf_agent_debt_record a " );
					WHERE(" 1 = 1");
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getAgentNo())){
						WHERE(" a.agent_no  = #{hlfAgentDebtRecord.agentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getParentAgentNo())){
						WHERE(" a.parent_agent_no  = #{hlfAgentDebtRecord.parentAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getOneAgentNo())){
						WHERE(" a.one_agent_no  = #{hlfAgentDebtRecord.oneAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
						WHERE(" a.debt_time  >= #{hlfAgentDebtRecord.date1} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
						WHERE(" a.debt_time  <= #{hlfAgentDebtRecord.date2} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getOrderNo())){
						WHERE(" a.order_no  = #{hlfAgentDebtRecord.orderNo} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getMerchantNo())){
						WHERE(" a.merchant_no  = #{hlfAgentDebtRecord.merchantNo} ");
					}
					ORDER_BY(" a.debt_time desc");
				}}.toString();
			}
			
			public String exportHlfAgentDebtRecordList(final Map<String, Object> parameter) {
				final HlfAgentDebtRecord hlfAgentDebtRecord = (HlfAgentDebtRecord) parameter.get("hlfAgentDebtRecord");
				return new SQL(){{
					SELECT(" a.* ");
					FROM(" hlf_agent_debt_record a " );
					WHERE(" 1 = 1");
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getAgentNo())){
						WHERE(" a.agent_no  = #{hlfAgentDebtRecord.agentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getParentAgentNo())){
						WHERE(" a.parent_agent_no  = #{hlfAgentDebtRecord.parentAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getOneAgentNo())){
						WHERE(" a.one_agent_no  = #{hlfAgentDebtRecord.oneAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
						WHERE(" a.debt_time  >= #{hlfAgentDebtRecord.date1} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
						WHERE(" a.debt_time  <= #{hlfAgentDebtRecord.date2} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getOrderNo())){
						WHERE(" a.order_no  = #{hlfAgentDebtRecord.orderNo} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getMerchantNo())){
						WHERE(" a.merchant_no  = #{hlfAgentDebtRecord.merchantNo} ");
					}
					ORDER_BY(" a.debt_time desc");
				}}.toString();
			}

			public String findHlfAgentDebtRecordShouldDebtAmountCollection(final Map<String, Object> parameter){
				final HlfAgentDebtRecord hlfAgentDebtRecord  =(HlfAgentDebtRecord)parameter.get("hlfAgentDebtRecord");
				StringBuffer StringBuffer = new StringBuffer();
			    String sql1 = "select sum(should_debt_amount) as all_should_debt_amount from hlf_agent_debt_record where id in (select MAX(id) from hlf_agent_debt_record where 1=1 ";
				StringBuffer.append(sql1);
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getAgentNo())){
						StringBuffer.append(" and agent_no  = #{hlfAgentDebtRecord.agentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getParentAgentNo())){
						StringBuffer.append(" and parent_agent_no  = #{hlfAgentDebtRecord.parentAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getOneAgentNo())){
						StringBuffer.append(" and one_agent_no  = #{hlfAgentDebtRecord.oneAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
						StringBuffer.append(" and debt_time  >= #{hlfAgentDebtRecord.date1} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
						StringBuffer.append(" and debt_time  <= #{hlfAgentDebtRecord.date2} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getOrderNo())){
						StringBuffer.append(" and order_no  = #{hlfAgentDebtRecord.orderNo} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getMerchantNo())){
						StringBuffer.append(" and merchant_no  = #{hlfAgentDebtRecord.merchantNo} ");
					}
			    String sqlAppend2 = " GROUP BY agent_no )";
				String sql = StringBuffer.append(sqlAppend2).toString();
				return sql;
			}	
			
			public String findHlfAgentDebtRecordListCollection(final Map<String, Object> parameter){
				final HlfAgentDebtRecord hlfAgentDebtRecord  =(HlfAgentDebtRecord)parameter.get("hlfAgentDebtRecord");
				return new SQL(){{
					SELECT(" sum(a.debt_amount) all_debt_amount");
					FROM(" hlf_agent_debt_record a " );
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getAgentNo())){
						WHERE(" a.agent_no  = #{hlfAgentDebtRecord.agentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getParentAgentNo())){
						WHERE(" a.parent_agent_no  = #{hlfAgentDebtRecord.parentAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getOneAgentNo())){
						WHERE(" a.one_agent_no  = #{hlfAgentDebtRecord.oneAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
						WHERE(" a.debt_time  >= #{hlfAgentDebtRecord.date1} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
						WHERE(" a.debt_time  <= #{hlfAgentDebtRecord.date2} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getOrderNo())){
						WHERE(" a.order_no  = #{hlfAgentDebtRecord.orderNo} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getMerchantNo())){
						WHERE(" a.merchant_no  = #{hlfAgentDebtRecord.merchantNo} ");
					}
				}}.toString();
			}	
			
			public String findHlfAgentDebtRecordAgentNo(final Map<String, Object> parameter){
				final HlfAgentDebtRecord hlfAgentDebtRecord  =(HlfAgentDebtRecord)parameter.get("hlfAgentDebtRecord");
				return new SQL(){{
					SELECT(" DISTINCT agent_no ");
					FROM(" hlf_agent_debt_record a " );
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getAgentNo())){
						WHERE(" a.agent_no  = #{hlfAgentDebtRecord.agentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getParentAgentNo())){
						WHERE(" a.parent_agent_no  = #{hlfAgentDebtRecord.parentAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getOneAgentNo())){
						WHERE(" a.one_agent_no  = #{hlfAgentDebtRecord.oneAgentNo} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
						WHERE(" a.debt_time  >= #{hlfAgentDebtRecord.date1} ");
					}
					if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
						WHERE(" a.debt_time  <= #{hlfAgentDebtRecord.date2} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getOrderNo())){
						WHERE(" a.order_no  = #{hlfAgentDebtRecord.orderNo} ");
					}
					if(!StringUtils.isBlank(hlfAgentDebtRecord.getMerchantNo())){
						WHERE(" a.merchant_no  = #{hlfAgentDebtRecord.merchantNo} ");
					}
				}}.toString();
			}	
			
			public String findHlfAgentDebtRecordListCollectionByList(final Map<String, Object> parameter){
				final String agentInfoList  =(String)parameter.get("agentInfoList");
				return new SQL(){{
					SELECT(" sum(a.adjust_amount) as all_total_debt_amount ");
					FROM(" agent_acc_pre_adjust a " );
					if(StringUtils.isNotBlank(agentInfoList)){
						WHERE(" a.agent_no  in ("+agentInfoList+")");
					}
				}}.toString();
			}
		}



}
