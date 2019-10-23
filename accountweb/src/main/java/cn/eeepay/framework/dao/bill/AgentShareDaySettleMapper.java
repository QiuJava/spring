package cn.eeepay.framework.dao.bill;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentShareDaySettle;
/**
 * 代理商预调账表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年4月15日13:40:44
 *
 */
public interface AgentShareDaySettleMapper {
	@Insert("insert into agent_share_day_settle(collection_batch_no,group_time,trans_date,one_agent_no,one_agent_name,agent_no,agent_name,agent_node,agent_level,parent_agent_no,sale_name,trans_total_amount,trans_total_num,dui_succ_trans_total_amount,dui_succ_trans_total_num,cash_total_num,"
			+ "mer_fee,mer_cash_fee,acq_out_cost,acq_out_profit,dai_cost,dian_cost,pre_trans_share_amount,pre_trans_cash_amount,open_back_amount,rate_diff_amount,tui_cost_amount,risk_sub_amount,bail_sub_amount,"
			+ "mer_mg_amount,other_amount,adjust_trans_share_amount,adjust_trans_cash_amount,adjust_total_share_amount,terminal_freeze_amount,other_freeze_amount,enter_account_status,real_enter_share_amount,"
			+ "trans_deduction_fee,actual_fee,merchant_price,deduction_mer_fee,actual_optional_fee)"
			+ " values(#{agentShareDaySettle.collectionBatchNo},#{agentShareDaySettle.groupTime},#{agentShareDaySettle.transDate},#{agentShareDaySettle.oneAgentNo},#{agentShareDaySettle.oneAgentName},#{agentShareDaySettle.agentNo},#{agentShareDaySettle.agentName},#{agentShareDaySettle.agentNode},#{agentShareDaySettle.agentLevel},#{agentShareDaySettle.parentAgentNo},#{agentShareDaySettle.saleName},#{agentShareDaySettle.transTotalAmount},#{agentShareDaySettle.transTotalNum},#{agentShareDaySettle.duiSuccTransTotalAmount},#{agentShareDaySettle.duiSuccTransTotalNum},#{agentShareDaySettle.cashTotalNum},"
			+ "#{agentShareDaySettle.merFee},#{agentShareDaySettle.merCashFee},#{agentShareDaySettle.acqOutCost},#{agentShareDaySettle.acqOutProfit},#{agentShareDaySettle.daiCost},#{agentShareDaySettle.dianCost},#{agentShareDaySettle.preTransShareAmount},#{agentShareDaySettle.preTransCashAmount},#{agentShareDaySettle.openBackAmount},#{agentShareDaySettle.rateDiffAmount},#{agentShareDaySettle.tuiCostAmount},#{agentShareDaySettle.riskSubAmount},#{agentShareDaySettle.bailSubAmount},"
			+ "#{agentShareDaySettle.merMgAmount},#{agentShareDaySettle.otherAmount},#{agentShareDaySettle.adjustTransShareAmount},#{agentShareDaySettle.adjustTransCashAmount},#{agentShareDaySettle.adjustTotalShareAmount},#{agentShareDaySettle.terminalFreezeAmount},#{agentShareDaySettle.otherFreezeAmount},#{agentShareDaySettle.enterAccountStatus},#{agentShareDaySettle.realEnterShareAmount},"
			+ "#{agentShareDaySettle.transDeductionFee},#{agentShareDaySettle.actualFee},#{agentShareDaySettle.merchantPrice},#{agentShareDaySettle.deductionMerFee},#{agentShareDaySettle.actualOptionalFee})")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	int insertAgentShareDaySettle(@Param("agentShareDaySettle")AgentShareDaySettle agentShareDaySettle);
	
	@Insert("<script>"
			+" insert into agent_share_day_settle(collection_batch_no,group_time,trans_date,one_agent_no,one_agent_name,agent_no,agent_name,agent_node,agent_level,parent_agent_no,sale_name,trans_total_amount,trans_total_num,dui_succ_trans_total_amount,dui_succ_trans_total_num,cash_total_num,"
			+ "mer_fee,mer_cash_fee,deduction_fee,acq_out_cost,acq_out_profit,dai_cost,dian_cost,pre_trans_share_amount,pre_trans_cash_amount,open_back_amount,rate_diff_amount,tui_cost_amount,risk_sub_amount,bail_sub_amount,"
			+ "mer_mg_amount,other_amount,adjust_trans_share_amount,adjust_trans_cash_amount,adjust_total_share_amount,terminal_freeze_amount,other_freeze_amount,enter_account_status,real_enter_share_amount,trans_deduction_fee,actual_fee,merchant_price,deduction_mer_fee,actual_optional_fee)"
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
			+ " (#{item.collectionBatchNo},#{item.groupTime},#{item.transDate},#{item.oneAgentNo},#{item.oneAgentName},#{item.agentNo},#{item.agentName},#{item.agentNode},#{item.agentLevel},#{item.parentAgentNo},#{item.saleName},#{item.transTotalAmount},#{item.transTotalNum},#{item.duiSuccTransTotalAmount},#{item.duiSuccTransTotalNum},#{item.cashTotalNum},"
			+ "#{item.merFee},#{item.merCashFee},#{item.deductionFee},#{item.acqOutCost},#{item.acqOutProfit},#{item.daiCost},#{item.dianCost},#{item.preTransShareAmount},#{item.preTransCashAmount},#{item.openBackAmount},#{item.rateDiffAmount},#{item.tuiCostAmount},#{item.riskSubAmount},#{item.bailSubAmount},"
			+ "#{item.merMgAmount},#{item.otherAmount},#{item.adjustTransShareAmount},#{item.adjustTransCashAmount},#{item.adjustTotalShareAmount},#{item.terminalFreezeAmount},#{item.otherFreezeAmount},#{item.enterAccountStatus},#{item.realEnterShareAmount},#{item.transDeductionFee},#{item.actualFee},"
			+ "#{item.merchantPrice},#{item.deductionMerFee},#{item.actualOptionalFee})"
            + " </foreach > "
            + " </script>")
	int insertAgentShareDaySettleBatch(@Param("list")List<AgentShareDaySettle> list);
	
	
	@Update("update agent_share_day_settle set "
			+ " collection_batch_no=#{agentShareDaySettle.collectionBatchNo},"
			+ " group_time=#{agentShareDaySettle.groupTime},"
			+ " trans_date=#{agentShareDaySettle.transDate},"
			+ " one_agent_no=#{agentShareDaySettle.oneAgentNo},"
			+ " one_agent_name=#{agentShareDaySettle.oneAgentName},"
			+ " agent_no=#{agentShareDaySettle.agentNo},"
			+ " agent_name=#{agentShareDaySettle.agentName},"
			+ " agent_node=#{agentShareDaySettle.agentNode},"
			+ " agent_level=#{agentShareDaySettle.agentLevel},"
			+ " parent_agent_no=#{agentShareDaySettle.parentAgentNo},"
			+ " sale_name=#{agentShareDaySettle.saleName},"
			+ " trans_total_amount=#{agentShareDaySettle.transTotalAmount},"
			+ " trans_total_num=#{agentShareDaySettle.transTotalNum},"
			+ " dui_succ_trans_total_amount=#{agentShareDaySettle.duiSuccTransTotalAmount},"
			+ " dui_succ_trans_total_num=#{agentShareDaySettle.duiSuccTransTotalNum},"
			+ " cash_total_num=#{agentShareDaySettle.cashTotalNum},"
			+ " mer_fee=#{agentShareDaySettle.merFee},"
			+ " mer_cash_fee=#{agentShareDaySettle.merCashFee},"
			+ " acq_out_cost=#{agentShareDaySettle.acqOutCost},"
			+ " acq_out_profit=#{agentShareDaySettle.acqOutProfit},"
			+ " dai_cost=#{agentShareDaySettle.daiCost},"
			+ " dian_cost=#{agentShareDaySettle.dianCost},"
			+ " pre_trans_share_amount=#{agentShareDaySettle.preTransShareAmount},"
			+ " pre_trans_cash_amount=#{agentShareDaySettle.preTransCashAmount},"
			+ " open_back_amount=#{agentShareDaySettle.openBackAmount},"
			+ " rate_diff_amount=#{agentShareDaySettle.rateDiffAmount},"
			+ " tui_cost_amount=#{agentShareDaySettle.tuiCostAmount},"
			+ " risk_sub_amount=#{agentShareDaySettle.riskSubAmount},"
			+ " bail_sub_amount=#{agentShareDaySettle.bailSubAmount},"
			+ " mer_mg_amount=#{agentShareDaySettle.merMgAmount},"
			+ " other_amount=#{agentShareDaySettle.otherAmount},"
			+ " adjust_trans_share_amount=#{agentShareDaySettle.adjustTransShareAmount},"
			+ " adjust_trans_cash_amount=#{agentShareDaySettle.adjustTransCashAmount},"
			+ " adjust_total_share_amount=#{agentShareDaySettle.adjustTotalShareAmount},"
			+ " terminal_freeze_amount=#{agentShareDaySettle.terminalFreezeAmount},"
			+ " other_freeze_amount=#{agentShareDaySettle.otherFreezeAmount},"
			+ " enter_account_status=#{agentShareDaySettle.enterAccountStatus},"
			+ " enter_account_message=#{agentShareDaySettle.enterAccountMessage},"
			+ " operator=#{agentShareDaySettle.operator},"
			+ " enter_account_time=#{agentShareDaySettle.enterAccountTime},"
			+ " real_enter_share_amount=#{agentShareDaySettle.realEnterShareAmount},"
			+ " trans_deduction_fee=#{agentShareDaySettle.transDeductionFee},"
			+ " actual_fee=#{agentShareDaySettle.actualFee},"
			+ " merchant_price=#{agentShareDaySettle.merchantPrice},"
			+ " deduction_mer_fee=#{agentShareDaySettle.deductionMerFee},"
			+ " actual_optional_fee=#{agentShareDaySettle.actualOptionalFee}"
			+"   where id=#{agentShareDaySettle.id}")
	int updateAgentShareDaySettle(@Param("agentShareDaySettle")AgentShareDaySettle agentShareDaySettle);
	
	
	
	
	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+ " update agent_share_day_settle  "
			+ " set collection_batch_no=#{item.collectionBatchNo},"
			+ " group_time=#{item.groupTime},"
			+ " trans_date=#{item.transDate},"
			+ " one_agent_no=#{item.oneAgentNo},"
			+ " one_agent_name=#{item.oneAgentName},"
			+ " agent_no=#{item.agentNo},"
			+ " agent_name=#{item.agentName},"
			+ " agent_node=#{item.agentNode},"
			+ " agent_level=#{item.agentLevel},"
			+ " parent_agent_no=#{item.parentAgentNo},"
			+ " sale_name=#{item.saleName},"
			+ " trans_total_amount=#{item.transTotalAmount},"
			+ " trans_total_num=#{item.transTotalNum},"
			+ " dui_succ_trans_total_amount=#{item.duiSuccTransTotalAmount},"
			+ " dui_succ_trans_total_num=#{item.duiSuccTransTotalNum},"
			+ " cash_total_num=#{item.cashTotalNum},"
			+ " mer_fee=#{item.merFee},"
			+ " mer_cash_fee=#{item.merCashFee},"
			+ " acq_out_cost=#{item.acqOutCost},"
			+ " acq_out_profit=#{item.acqOutProfit},"
			+ " dai_cost=#{item.daiCost},"
			+ " dian_cost=#{item.dianCost},"
			+ " pre_trans_share_amount=#{item.preTransShareAmount},"
			+ " pre_trans_cash_amount=#{item.preTransCashAmount},"
			+ " open_back_amount=#{item.openBackAmount},"
			+ " rate_diff_amount=#{item.rateDiffAmount},"
			+ " tui_cost_amount=#{item.tuiCostAmount},"
			+ " risk_sub_amount=#{item.riskSubAmount},"
			+ " bail_sub_amount=#{item.bailSubAmount},"
			+ " mer_mg_amount=#{item.merMgAmount},"
			+ " other_amount=#{item.otherAmount},"
			+ " adjust_trans_share_amount=#{item.adjustTransShareAmount},"
			+ " adjust_trans_cash_amount=#{item.adjustTransCashAmount},"
			+ " adjust_total_share_amount=#{item.adjustTotalShareAmount},"
			+ " terminal_freeze_amount=#{item.terminalFreezeAmount},"
			+ " other_freeze_amount=#{item.otherFreezeAmount},"
			+ " enter_account_status=#{item.enterAccountStatus},"
			+ " enter_account_message=#{item.enterAccountMessage},"
			+ " real_enter_share_amount=#{item.realEnterShareAmount},"
			+ " trans_deduction_fee=#{item.transDeductionFee},"
			+ " actual_fee=#{item.actualFee},"
			+ " merchant_price=#{item.merchantPrice},"
			+ " deduction_mer_fee=#{item.deductionMerFee},"
			+ " actual_optional_fee=#{item.actualOptionalFee}"
			+"   where id=#{item.id}"
			+ "     </foreach> "
            + " </script>")
	int updateAgentShareDaySettleBatch(@Param("list")List<AgentShareDaySettle> list);
	
	@Delete("delete from agent_share_day_settle where id = #{id}")
	int deleteAgentShareDaySettle(@Param("id")Integer id);
	
	@Select("select * from agent_share_day_settle ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findAllAgentShareDaySettle();
	
	@Select("select * from agent_share_day_settle where id=#{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	AgentShareDaySettle findEntityById(@Param("id")Integer id);
	
	
	/**
	 * 查询出所有代理商未入账的
	 * @param enterAccountStatus
	 * @param transDate
	 * @return
	 */
	@Select("select * from agent_share_day_settle where enter_account_status = #{enterAccountStatus} and trans_date= #{transDate}")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findAllAgentShareDaySettleByEnterAccountStatusAndTransDate(@Param("enterAccountStatus")String enterAccountStatus,@Param("transDate")String transDate);
	
	/**
	 * 查询出一级代理商未入账的
	 * @param enterAccountStatus
	 * @param transDate
	 * @return
	 */
	@Select("select * from agent_share_day_settle where enter_account_status = 'NOENTERACCOUNT' and trans_date= #{transDate} and agent_level = #{agentLevel} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findNoEnterOneEntityByTransDateAndLevel(@Param("transDate")String transDate,@Param("agentLevel")int agentLevel);
	
	
	@Select("select * from agent_share_day_settle where enter_account_status = 'NOENTERACCOUNT' and trans_date= #{transDate} and agent_no = #{agentNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findNoEnterOneEntityByTransDateAndAgentNo(@Param("transDate")String transDate,@Param("agentNo")String agentNo);
	
	/**
	 * 查询出一级代理商未入账的
	 * @param enterAccountStatus
	 * @param transDate
	 * @return
	 */
	@Select("select * from agent_share_day_settle "
			+ " where enter_account_status = 'NOENTERACCOUNT' "
			+ " and agent_node like  \"\"#{agentNode}\"%\" "
			+ " and collection_batch_no = #{batchNo} "
			+ " and agent_level = #{agentLevel} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findNoEnterOneEntityByLikeAgentNodeAndLevel(@Param("batchNo")String batchNo,@Param("agentNode")String agentNode,@Param("agentLevel")int agentLevel);
	
	
	@Select("select * from agent_share_day_settle where enter_account_status = 'NOENTERACCOUNT'  and parent_agent_no = #{agentNo}  ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findNoEnterDirectChilrenEntityByAgentNo(@Param("agentNo")String agentNo);
	
	
	@Select("select * from agent_share_day_settle where enter_account_status = #{enterAccountStatus} "
			+ " and collection_batch_no= #{collectionBatchNo} "
			+ " and parent_agent_no  = #{agentNo}  ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findAgentDirectChildrenShareDaySettleList(
			@Param("enterAccountStatus")String enterAccountStatus,
			@Param("collectionBatchNo")String collectionBatchNo,
			@Param("agentNo")String agentNo);
	
	
	@SelectProvider(type=SqlProvider.class,method="findAgentDirectChildrenShareDaySettleListCollection")
	@Results(value = {  
            @Result(property = "allAdjustTotalShareAmount", column = "all_adjust_total_share_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	Map<String, Object> findAgentDirectChildrenShareDaySettleListCollection(@Param("batchNo")String batchNo,@Param("agentNo")String agentNo);
	
	
	@Select("select * from agent_share_day_settle where enter_account_status = 'NOENTERACCOUNT' and id=#{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	AgentShareDaySettle findSingleNoEnterEntityById(@Param("id")Integer id);
	
	@Select("select * from agent_share_day_settle where collection_batch_no = #{batchNo} and agent_no=#{agentNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	AgentShareDaySettle findEntityByBatchNoAndAgentNo(@Param("batchNo")String batchNo,@Param("agentNo")String agentNo);
	
	@SelectProvider(type=SqlProvider.class,method="findAgentShareDaySettleList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findAgentShareDaySettleList(@Param("agentShareDaySettle")AgentShareDaySettle agentShareDaySettle,@Param("sort")Sort sort,Page<AgentShareDaySettle> page);
	
	
	@SelectProvider(type=SqlProvider.class,method="findAgentShareDaySettleListCollection")
	@Results(value = {  
            @Result(property = "allTransTotalAmount", column = "all_trans_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "allCashTotalNum", column = "all_cash_total_num",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
            @Result(property = "allAdjustTotalShareAmount", column = "all_adjust_total_share_amount",javaType=String.class,jdbcType=JdbcType.VARCHAR),
            @Result(property = "allPreTransShareAmount", column = "all_pre_trans_share_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
            @Result(property = "allPreTransCashAmount", column = "all_pre_trans_cash_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
            @Result(property = "allAdjustAmount", column = "all_adjust_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	Map<String, Object> findAgentShareDaySettleListCollection(@Param("agentShareDaySettle")AgentShareDaySettle agentShareDaySettle);
	
	
	@SelectProvider(type=SqlProvider.class,method="exportAgentShareDaySettleList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> exportAgentShareDaySettleList(@Param("agentShareDaySettle")AgentShareDaySettle agentShareDaySettle);
	
	
	@SelectProvider( type=SqlProvider.class,method="findCollectionGropByAgent")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	List<AgentShareDaySettle> findCollectionGropByAgent(@Param("params") Map<String, String> params);
	
	
	@SelectProvider( type=SqlProvider.class,method="findCollectionGropByNoCollectAgent")
	@Results(value = {  
            @Result(property = "transTotalAmount", column = "trans_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "transTotalNum", column = "trans_total_num",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
            @Result(property = "agentNo", column = "agent_no",javaType=String.class,jdbcType=JdbcType.VARCHAR),
        })
	Map<String,Object> findCollectionGropByNoCollectAgent(@Param("params") Map<String, String> params);
	
	
	@Select("select * from agent_share_day_settle where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	AgentShareDaySettle findAgentShareDaySettleById(@Param("id")Integer id);
	
	
	@SelectProvider(type=SqlProvider.class,method="findNoCollectTransShortInfoByAgentNodeAndLevel")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper.BaseResultMap")
	AgentShareDaySettle findNoCollectTransShortInfoByAgentNodeAndLevel(@Param("params") Map<String, String> params);
	
	
	@SelectProvider( type=SqlProvider.class,method="findNoCollectTransShortInfoGroupByAgentNodeAndLevel")
	@Results(value = {  
            @Result(property = "transTotalNum", column = "trans_total_num",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
        })
	Map<String,Object> findNoCollectTransShortInfoGroupByAgentNodeAndLevel(@Param("params") Map<String, String> params);

	@Update("update agent_share_day_settle set terminal_freeze_amount = 0,other_freeze_amount = 0 where agent_no = #{agentNo}")
	void clearUnfreezeAmount(String agentNo);

	public class SqlProvider{
		public String findAgentShareDaySettleList(final Map<String, Object> parameter){
			final AgentShareDaySettle agentShareDaySettle=(AgentShareDaySettle)parameter.get("agentShareDaySettle");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM("agent_share_day_settle");
				if(StringUtils.isNotBlank(agentShareDaySettle.getAgentNo()) && !"ALL".equals(agentShareDaySettle.getAgentNo())){
					WHERE(" agent_no=#{agentShareDaySettle.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getEnterAccountStatus()) && !"ALL".equals(agentShareDaySettle.getEnterAccountStatus())){
					WHERE(" enter_account_status=#{agentShareDaySettle.enterAccountStatus} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getTransDate1()) ){
					WHERE(" trans_date >= #{agentShareDaySettle.transDate1} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getTransDate2()) ){
					WHERE(" trans_date <= #{agentShareDaySettle.transDate2} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getGroupTime1()) ){
					WHERE(" group_time >= #{agentShareDaySettle.groupTime1} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getGroupTime2()) ){
					WHERE(" group_time <= #{agentShareDaySettle.groupTime2} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getAgentLevel())  && !"ALL".equals(agentShareDaySettle.getAgentLevel())){
					WHERE(" agent_level = #{agentShareDaySettle.agentLevel} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getCollectionBatchNo())){
					WHERE(" collection_batch_no like  \"%\"#{agentShareDaySettle.collectionBatchNo}\"%\" ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("group_time desc ");
				}
			}}.toString();
		}
		
		
		public String findAgentDirectChildrenShareDaySettleListCollection(final Map<String, Object> parameter){
			final String agentNo=(String)parameter.get("agentNo");
			final String batchNo=(String)parameter.get("batchNo");
			return new SQL(){{
				SELECT("  sum(adjust_total_share_amount) as all_adjust_total_share_amount");
				FROM(" agent_share_day_settle");
				WHERE(" parent_agent_no = #{agentNo} ");
				WHERE(" collection_batch_no = #{batchNo} ");
			}}.toString();
		}
		
		public String findAgentShareDaySettleListCollection(final Map<String, Object> parameter){
			final AgentShareDaySettle agentShareDaySettle=(AgentShareDaySettle)parameter.get("agentShareDaySettle");
			return new SQL(){{
				SELECT(" sum(trans_total_amount) as all_trans_total_amount,"
						+ " sum(cash_total_num) as all_cash_total_num,"
						+ " sum(pre_trans_share_amount) as all_pre_trans_share_amount,"
						+ " sum(pre_trans_cash_amount) as all_pre_trans_cash_amount,"
						+ " sum( open_back_amount + tui_cost_amount + rate_diff_amount + mer_mg_amount + risk_sub_amount + bail_sub_amount + other_amount )  as all_adjust_amount,"
						+ " sum(adjust_total_share_amount) as all_adjust_total_share_amount");
				FROM("agent_share_day_settle");
				if(StringUtils.isNotBlank(agentShareDaySettle.getAgentNo()) && !"ALL".equals(agentShareDaySettle.getAgentNo())){
					WHERE(" agent_no=#{agentShareDaySettle.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getEnterAccountStatus()) && !"ALL".equals(agentShareDaySettle.getEnterAccountStatus())){
					WHERE(" enter_account_status=#{agentShareDaySettle.enterAccountStatus} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getTransDate1()) ){
					WHERE(" trans_date >= #{agentShareDaySettle.transDate1} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getTransDate2()) ){
					WHERE(" trans_date <= #{agentShareDaySettle.transDate2} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getGroupTime1()) ){
					WHERE(" group_time >= #{agentShareDaySettle.groupTime1} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getGroupTime2()) ){
					WHERE(" group_time <= #{agentShareDaySettle.groupTime2} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getAgentLevel()) && !"ALL".equals(agentShareDaySettle.getAgentLevel())){
					WHERE(" agent_level = #{agentShareDaySettle.agentLevel} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getCollectionBatchNo()) ){
					WHERE(" collection_batch_no = #{agentShareDaySettle.collectionBatchNo} ");
				}
				ORDER_BY(" group_time desc ");
			}}.toString();
		}
		
		
		public String findNoCollectTransShortInfoGroupByAgentNodeAndLevel(final Map<String, Object> parameter){
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String transDate1 = params.get("transDate1");
			final String agentNode = params.get("agentNode");
			return new SQL(){{
				SELECT(" count(1) as trans_total_num ");
				FROM(" check_account_detail as a ,trans_short_info b ");
				WHERE(" a.plate_order_no = b.plate_order_no  ");
				WHERE(" b.agent_profit_collection_status='NOCOLLECT'  ");
				if(StringUtils.isNotBlank(transDate1) ){
					WHERE("b.trans_time between #{params.date1} and #{params.date2}");
				}
				if(StringUtils.isNotBlank(agentNode) ){
					WHERE(" b.agent_node like  \"\"#{params.agentNode}\"%\" ");
				}
			}}.toString();
		}
		
		
		public String exportAgentShareDaySettleList(final Map<String, Object> parameter){
			final AgentShareDaySettle agentShareDaySettle=(AgentShareDaySettle)parameter.get("agentShareDaySettle");
			return new SQL(){{
				SELECT("*");
				FROM("agent_share_day_settle");
				if(StringUtils.isNotBlank(agentShareDaySettle.getAgentNo()) && !"ALL".equals(agentShareDaySettle.getAgentNo())){
					WHERE(" agent_no=#{agentShareDaySettle.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getEnterAccountStatus()) && !"ALL".equals(agentShareDaySettle.getEnterAccountStatus())){
					WHERE(" enter_account_status=#{agentShareDaySettle.enterAccountStatus} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getTransDate1()) ){
					WHERE(" trans_date >= #{agentShareDaySettle.transDate1} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getTransDate2()) ){
					WHERE(" trans_date <= #{agentShareDaySettle.transDate2} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getGroupTime1()) ){
					WHERE(" group_time >= #{agentShareDaySettle.groupTime1} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getGroupTime2()) ){
					WHERE(" group_time <= #{agentShareDaySettle.groupTime2} ");
				}
				if(StringUtils.isNotBlank(agentShareDaySettle.getAgentLevel())  && !"ALL".equals(agentShareDaySettle.getAgentLevel())){
					WHERE(" agent_level = #{agentShareDaySettle.agentLevel} ");
				}
				ORDER_BY(" group_time desc ");
			}}.toString();
		}
		
		public String findCollectionGropByAgent(final Map<String, Object> parameter){
//			final AgentShareDaySettle agentShareDaySettle=(AgentShareDaySettle)parameter.get("agentShareDaySettle");
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
//			final Sort sord=(Sort)parameter.get("sort");
			final String transDate1 = params.get("transDate1");
//			final String transDate2 = params.get("transDate2");
//			final String groupTime1 = params.get("groupTime1");
//			final String groupTime2 = params.get("groupTime2");
			return new SQL(){{
				SELECT(" collection_batch_no,"
						+ " b.trans_time as trans_date,"
						+ " b.one_agent_no,"
						+ " b.one_agent_name,"
						+ " b.agent_no,"
						+ " b.agent_name,"
						+ " b.agent_node,"
						+ " b.agent_level,"
						+ " b.parent_agent_no,"
						+ " b.sale_name,"
						+ " sum(b.trans_amount) as trans_total_amount, "
						+ " count(1) as trans_total_num, "
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.trans_amount else 0 end) as dui_succ_trans_total_amount,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then 1 else 0 end) as dui_succ_trans_total_num,"
						+ " sum(case when (b.mer_cash_fee >=0 and a.check_account_status = 'SUCCESS') then 1 else 0 end) as cash_total_num,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.merchant_fee else 0 end) as mer_fee,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.mer_cash_fee else 0 end) as mer_cash_fee,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.acq_out_cost else 0 end) as acq_out_cost,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.acq_out_profit else 0 end) as acq_out_profit,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.dai_cost else 0 end) as dai_cost,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.dian_cost else 0 end) as dian_cost,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.agent_share_amount else 0 end) as pre_trans_share_amount,"
						+ " sum(case when a.check_account_status = 'SUCCESS' then b.cash_agent_share_amount else 0 end) as pre_trans_cash_amount");
				FROM(" check_account_detail as a ,trans_short_info b");
				WHERE(" a.plate_order_no = b.plate_order_no ");
				if(StringUtils.isNotBlank(transDate1) ){
					WHERE(" DATE_FORMAT(b.trans_time, '%Y-%m-%d') = DATE_FORMAT(#{params.transDate1}, '%Y-%m-%d') ");
				}
				WHERE(" b.agent_profit_collection_status='NOCOLLECT' ");
				GROUP_BY(" b.one_agent_no");
				ORDER_BY(" b.one_agent_no ");
			}}.toString();
		}
		
		public String findNoCollectTransShortInfoByAgentNodeAndLevel(final Map<String, Object> parameter){
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String transDate1 = params.get("transDate1");
			final String agentNode = params.get("agentNode");
			final String agentLevel = params.get("agentLevel");
			
			String sql = "";
			 sql = new SQL(){{
				SELECT(" sum(ifnull(b.profits_" + agentLevel + ",0)) as pre_trans_share_amount, "
					 + " sum(ifnull(b.settle_profits_" + agentLevel + ",0)) as pre_trans_cash_amount, "
					 + " b.agent_no,"
					 + " b.agent_node,"
					 + " b.agent_name,"
					 + " b.trans_time as trans_date , "
					 + " b.one_agent_no,"
					 + " b.one_agent_name,"
					 + " b.agent_level,"
					 + " b.parent_agent_no, "
					 + " b.sale_name,"
					 + " sum(ifnull(b.trans_amount,0)) as trans_total_amount, "
					 //+ " count(1) as trans_total_num,"
					 + " sum(case when a.check_account_status = 'SUCCESS' then ifnull(b.trans_amount,0) else 0 end) as dui_succ_trans_total_amount,"
					 + " sum(case when a.check_account_status = 'SUCCESS' then 1 else 0 end) as dui_succ_trans_total_num,"
					 + " sum(case when (ifnull(b.mer_cash_fee,0) >=0) then 1 else 0 end) as cash_total_num,"
					 + " sum(ifnull(b.merchant_fee,0)) as mer_fee,"
					 + " sum(ifnull(b.trans_deduction_fee,0)) as trans_deduction_fee,"
					 + " sum(ifnull(b.actual_fee,0)) as actual_fee,"
					 + " sum(ifnull(b.merchant_price,0)) as merchant_price,"
					 + " sum(ifnull(b.deduction_mer_fee,0)) as deduction_mer_fee,"
					 + " sum(ifnull(b.actual_optional_fee,0)) as actual_optional_fee,"
					 + " sum(ifnull(b.mer_cash_fee,0)) as mer_cash_fee,"
						+ " sum(ifnull(b.deduction_fee,0)) as deduction_fee,"
					 + " sum(ifnull(b.acq_out_cost,0)) as acq_out_cost,"
					 + " sum(ifnull(b.acq_out_profit,0)) as acq_out_profit,"
					 + " sum(ifnull(b.dai_cost,0)) as dai_cost,"
					 + " sum(ifnull(b.dian_cost,0)) as dian_cost");
				FROM(" trans_short_info b left join check_account_detail as a on a.plate_order_no = b.plate_order_no");
				WHERE(" b.agent_profit_collection_status='NOCOLLECT'  ");
				WHERE(" a.plate_trans_status = 'SUCCESS'  ");
				WHERE(" a.check_account_status = 'SUCCESS'  ");
				if(StringUtils.isNotBlank(transDate1) ){
					WHERE("b.trans_time between #{params.date1} and #{params.date2}");
				}
				if(StringUtils.isNotBlank(agentNode) ){
					WHERE(" b.agent_node like  \"\"#{params.agentNode}\"%\" ");
				}
				//GROUP_BY(" b.agent_node ");
			}}.toString();
			return sql;
		}
		
		public String findCollectionGropByNoCollectAgent(final Map<String, Object> parameter){
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String transDate1 = params.get("transDate1");
			final String agentNo = params.get("agentNo");
			return new SQL(){{
				SELECT("  b.agent_no,"
						+ " sum(b.trans_amount) as trans_total_amount, "
						+ " count(1) as trans_total_num ");
				FROM(" check_account_detail as a ,trans_short_info b");
				WHERE(" a.plate_order_no = b.plate_order_no ");
				if(StringUtils.isNotBlank(transDate1) ){
					WHERE(" DATE_FORMAT(b.trans_time, '%Y-%m-%d') = DATE_FORMAT(#{params.transDate1}, '%Y-%m-%d') ");
				}
				if(StringUtils.isNotBlank(agentNo) ){
					WHERE(" b.agent_no = #{params.agentNo} ");
				}
				WHERE(" b.agent_profit_collection_status='NOCOLLECT' ");
				GROUP_BY(" b.agent_no");
				ORDER_BY(" b.agent_no ");
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","collection_batch_no","group_time","trans_date","agent_no","agent_name","sale_name","trans_total_amount"};
		    final String[] columns={"id","collectionBatchNo","groupTime","transDate","agentNo","agentName","saleName","transTotalAmount"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
	}

	
}
