package cn.eeepay.framework.dao;

import cn.eeepay.boss.action.MerchantBusinessProductAction;
import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

@ReadOnlyDataSource
public interface MerchantBusinessProductReadOnlyDao {

	@Select("select mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.risk_status,mis.recommended_source,"
			+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time,tis.activity_type,mis.agent_no,mis.one_agent_no "
			+ "from merchant_business_product mbp "
			+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
			//20170118tgh 因是否有机具活动类型而引起的数据重复
			+ "LEFT JOIN terminal_info tis ON mbp.merchant_no = tis.merchant_no AND tis.bp_id = mbp.bp_id"

			+ " where ais.agent_node like (select CONCAT(agent_node,'%') from agent_info where agent_no=#{agentNo} )")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectAllInfo(@Param("page") Page<MerchantBusinessProduct> page, @Param("agentNo") String agentNo);

	@SelectProvider(type= MerchantBusinessProductReadOnlyDao.SqlProvider.class,method="findMbpList")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectByParam(@Param("page") Page<MerchantBusinessProduct> page, @Param("selectParams") MerchantBusinessProductAction.SelectParams selectParams, @Param("agentNo") String agentNo);

	@SelectProvider(type= MerchantBusinessProductReadOnlyDao.SqlProvider.class,method="findMbpList")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> exportMerchantInfo(@Param("selectParams") MerchantBusinessProductAction.SelectParams selectParams, @Param("agentNo") String loginAgentNo);

	@SelectProvider(type= MerchantBusinessProductReadOnlyDao.SqlProvider.class,method="findStatusMbpList")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectByStatusParam(@Param("page") Page<MerchantBusinessProduct> page, @Param("selectParams") MerchantBusinessProductAction.SelectParams selectParams, @Param("agentNo") String agentNo);

	@Select("select mbp.id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,"
			+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time "
			+ "from merchant_business_product mbp "
			+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id where mbp.status=1 and "
			+ "ais.agent_node like (select CONCAT(agent_node,'%') from agent_info where agent_no=#{agentNo})")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectAllByStatusInfo(@Param("page") Page<MerchantBusinessProduct> page, @Param("agentNo") String agentNo);

	//商户汇总统计 309tgh====s
	@SelectProvider(type= MerchantBusinessProductReadOnlyDao.SqlProvider.class,method="selectMechant")
	@ResultType(Integer.class)
	Integer selectMechant(@Param("selectParams") MerchantBusinessProductAction.SelectParams selectParams, @Param("agentNo") String agentNo);

	public class SqlProvider {
		public String selectMechant(Map<String,Object> param){
			final MerchantBusinessProductAction.SelectParams selectParams=(MerchantBusinessProductAction.SelectParams)param.get("selectParams");
			final String agentNo=(String)param.get("agentNo");
			return new SQL(){{
				SELECT(" COUNT(DISTINCT mis.merchant_no) ");
				FROM("merchant_info mis "
						+ "left JOIN merchant_business_product mbp on mis.merchant_no=mbp.merchant_no "
						+ "JOIN agent_info ais on ais.agent_no=mis.agent_no "
						+ " join terminal_info tis on tis.merchant_no=mbp.merchant_no and tis.bp_id=mbp.bp_id "
						+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
						+ "LEFT JOIN activity_detail ad on ad.merchant_no = mis.merchant_no ");
				//xm tgh是否包含下级无效20170209
				if(StringUtils.isNotBlank(agentNo)){
					WHERE("ais.agent_node like (select CONCAT(agent_node,'%') from agent_info where agent_no=#{agentNo})");
				}
				if(StringUtils.isNotBlank(selectParams.getMerchantNo())){
					WHERE("(mis.merchant_no=#{selectParams.merchantNo} or mis.merchant_name = #{selectParams.merchantNo})");
				}
				
				if(StringUtils.isNotBlank(selectParams.getTeamEntryId())){
					WHERE(" mis.team_entry_id=#{selectParams.teamEntryId}");
				}
				if (StringUtils.isNotBlank(selectParams.getQuickPayment())){
					if (StringUtils.equals("1", selectParams.getQuickPayment())){
						WHERE(" EXISTS ( " +
//								" SELECT 1 FROM credit_card cc WHERE cc.merchant_no = mis.merchant_no )" );
								" SELECT 1 FROM add_creaditcard_log cc WHERE cc.merchant_no = mis.merchant_no )" );

					}else{
						WHERE(" NOT EXISTS ( " +
//								" SELECT 1 FROM credit_card cc WHERE cc.merchant_no = mis.merchant_no " +
								" SELECT 1 FROM add_creaditcard_log cc WHERE cc.merchant_no = mis.merchant_no " +
								")");
					}
				}
//				if(StringUtils.isNotBlank(selectParams.getMbpId())){
//					WHERE("mbp.id=#{selectParams.mbpId}");
//				}
				//xm tgh是否包含下级无效20170207
				if(StringUtils.isNotBlank(selectParams.getAgentNode())){
					WHERE("mis.parent_node like #{selectParams.agentNode}");
				}
				if(StringUtils.isNotBlank(selectParams.getAgentName())){
					WHERE("ais.agent_no=#{selectParams.agentName}");
				}
				if(StringUtils.isNotBlank(selectParams.getRiskStatus())){
					WHERE("mis.risk_status=#{selectParams.riskStatus}");
				}
				if(StringUtils.isNotBlank(selectParams.getMobilephone())){
					WHERE("mis.mobilephone=#{selectParams.mobilephone}");
				}
				//310tgh
				if(StringUtils.isNotBlank(selectParams.getMerchantExamineState())){
					WHERE("mbp.status=#{selectParams.merchantExamineState}");
				}
				if(StringUtils.isNotBlank(selectParams.getProductType()) && !StringUtils.equals("-1", selectParams.getProductType())){
					WHERE("bpb.bp_id=#{selectParams.productType}");
				}
//				if(StringUtils.isNotBlank(selectParams.getTermianlType()) && !StringUtils.equals("-1", selectParams.getTermianlType())){
//					WHERE("tis.type=#{selectParams.termianlType}");
//				}
				
				if (StringUtils.isNotBlank(selectParams.getTerminalNo())) {
    				WHERE(" tis.sn=#{selectParams.terminalNo}");
				}else {
					if(StringUtils.isNotBlank(selectParams.getsTime())){
						WHERE("mis.create_time>=#{selectParams.sTime}");
					}
					if(StringUtils.isNotBlank(selectParams.geteTime())){
						WHERE("mis.create_time<=#{selectParams.eTime}");
					}
				}
    			
    			if (!"-1".equals(selectParams.getMerTeamId())) {
					 WHERE(" mis.team_id = #{selectParams.merTeamId} ");
				} 
				//20170120tgh
				if("1".equals(selectParams.getActivityType())){
					WHERE("ad.activity_code = '002' ");
				}else if("0".equals(selectParams.getActivityType())){
					WHERE(" (ad.activity_code != '002' OR ad.activity_code IS NULL) ");
				}
				//是否为微创业用户
				if (StringUtils.isNotBlank(selectParams.getRecommendedSource())) {
					WHERE(" mis.recommended_source = #{selectParams.recommendedSource}");
//					if ("1".equals(selectParams.getRecommendedSource())) {
//						WHERE(" mis.recommended_source = #{selectParams.recommendedSource}");
//					}else{
//						WHERE(" (mis.recommended_source = #{selectParams.recommendedSource} OR mis.recommended_source is null OR mis.recommended_source = '') ");
//					}
				}
			}}.toString();
		}
		
		
		public String findStatusMbpList(Map<String,Object> param){
			final MerchantBusinessProductAction.SelectParams selectParams=(MerchantBusinessProductAction.SelectParams)param.get("selectParams");
			return new SQL(){{
				SELECT("mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,"
						+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time"
						+ ",mis.team_id,tti.team_name,tis.activity_type");
				FROM("merchant_business_product mbp "
						+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
						+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
						+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
						+ "LEFT JOIN team_info tti on tti.team_id=mis.team_id "
						+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id");
				WHERE("mbp.status='1'");
				WHERE("ais.agent_node like (select CONCAT(agent_node,'%') from agent_info where agent_no=#{agentNo})");
				if(StringUtils.isNotBlank(selectParams.getMerchantNo())){
					WHERE("(mis.merchant_no=#{selectParams.merchantNo} or mis.merchant_name = #{selectParams.merchantNo})");
				}
				if(StringUtils.isNotBlank(selectParams.getMbpId())){
					WHERE("mbp.id=#{selectParams.mbpId}");
				}
				if(StringUtils.isNotBlank(selectParams.getAgentNode())){
					WHERE("ais.agent_node like #{selectParams.agentNode}");
				}
				if(StringUtils.isNotBlank(selectParams.getAgentName())){
					WHERE("ais.agent_no=#{selectParams.agentName}");
				}
//				if(StringUtils.isNotBlank(selectParams.getCardId())){
//					WHERE("mis.id_card_no=#{selectParams.cardId}");
//				}
				if(StringUtils.isNotBlank(selectParams.getMobilephone())){
					WHERE("mis.mobilephone=#{selectParams.mobilephone}");
				}
				if(StringUtils.isNotBlank(selectParams.getProductType()) && !StringUtils.equals("-1", selectParams.getProductType())){
					WHERE("bpb.bp_id=#{selectParams.productType}");
				}
				if(StringUtils.isNotBlank(selectParams.getTermianlType()) && !StringUtils.equals("-1", selectParams.getTermianlType())){
					WHERE("tis.type=#{selectParams.termianlType}");
				}
				if(StringUtils.isNotBlank(selectParams.getsTime())){
					WHERE("mis.create_time>=#{selectParams.sTime}");
				}
				if(StringUtils.isNotBlank(selectParams.geteTime())){
					WHERE("mis.create_time<=#{selectParams.eTime}");
				}
				//20170215tgh
				if(StringUtils.isNotBlank(selectParams.getActivityType())){
					WHERE("tis.activity_type=#{selectParams.activityType}");
				}
			}}.toString();
		}

		public String findMbpList(Map<String,Object> param){
			final MerchantBusinessProductAction.SelectParams selectParams=(MerchantBusinessProductAction.SelectParams)param.get("selectParams");
			final String agentNo=(String)param.get("agentNo");
			return new SQL(){{
				String select = "mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.risk_status, mis.agent_no,mis.recommended_source,"
						+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time,"
						+ " (CASE ad.activity_code WHEN '002' THEN '1' ELSE '0' END ) AS  activity_code,mis.one_agent_no,mis.team_id as merTeamId,tis.sn as sn," +
						"(SELECT COUNT(1) FROM add_creaditcard_log cc WHERE cc.merchant_no = mis.merchant_no) AS quickPayment,"+
						"(SELECT DISTINCT a_i.team_name FROM	app_info a_i WHERE	a_i.team_id = mis.team_id) as merGroup ";
				SELECT(select);
				FROM("merchant_business_product mbp "
						+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
						+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
						+ "join terminal_info tis on tis.merchant_no=mbp.merchant_no and tis.bp_id=mbp.bp_id  "
						+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
						+ "LEFT JOIN activity_detail ad on ad.merchant_no = mis.merchant_no ");
				//xm tgh是否包含下级无效20170209
				if(StringUtils.isNotBlank(agentNo)){
					WHERE("ais.agent_node like (select CONCAT(agent_node,'%') from agent_info where agent_no=#{agentNo})");
				}
				if(StringUtils.isNotBlank(selectParams.getMerchantNo())){
					WHERE("(mis.merchant_no=#{selectParams.merchantNo} or mis.merchant_name = #{selectParams.merchantNo})");
				}
				if(StringUtils.isNotBlank(selectParams.getTeamEntryId())){
					WHERE(" mis.team_entry_id=#{selectParams.teamEntryId} ");
				}
				if (StringUtils.isNotBlank(selectParams.getQuickPayment())){
					if (StringUtils.equals("1", selectParams.getQuickPayment())){
						WHERE(" EXISTS ( " +
//								" SELECT 1 FROM credit_card cc WHERE cc.merchant_no = mis.merchant_no " +
								" SELECT 1 FROM add_creaditcard_log cc WHERE cc.merchant_no = mis.merchant_no " +
								")");
					}else{
						WHERE(" NOT EXISTS ( " +
//								" SELECT 1 FROM credit_card cc WHERE cc.merchant_no = mis.merchant_no " +
								" SELECT 1 FROM add_creaditcard_log cc WHERE cc.merchant_no = mis.merchant_no " +
								")");
					}
				}
//				if(StringUtils.isNotBlank(selectParams.getMbpId())){
//					WHERE("mbp.id=#{selectParams.mbpId}");
//				}
				//xm tgh是否包含下级无效20170207
				if(StringUtils.isNotBlank(selectParams.getAgentNode())){
					WHERE("mis.parent_node like #{selectParams.agentNode}");
				}
				if(StringUtils.isNotBlank(selectParams.getAgentName())){
					WHERE("ais.agent_no=#{selectParams.agentName}");
				}
				if(StringUtils.isNotBlank(selectParams.getRiskStatus())){
					WHERE("mis.risk_status=#{selectParams.riskStatus}");
				}
				if(StringUtils.isNotBlank(selectParams.getMobilephone())){
					WHERE("mis.mobilephone=#{selectParams.mobilephone}");
				}
				//310tgh
				if(StringUtils.isNotBlank(selectParams.getMerchantExamineState())){
					WHERE("mbp.status=#{selectParams.merchantExamineState}");
				}
//				if(StringUtils.isNotBlank(selectParams.getMerchantExamineState()) && !StringUtils.equals("0", selectParams.getMerchantExamineState())){
//					WHERE("mbp.status=#{selectParams.merchantExamineState}");
//				}
				if(StringUtils.isNotBlank(selectParams.getProductType()) && !StringUtils.equals("-1", selectParams.getProductType())){
					WHERE("bpb.bp_id=#{selectParams.productType}");
				}
//				if(StringUtils.isNotBlank(selectParams.getTermianlType()) && !StringUtils.equals("-1", selectParams.getTermianlType())){
//					WHERE("tis.type=#{selectParams.termianlType}");
//				}
				
				if (StringUtils.isNotBlank(selectParams.getTerminalNo())) {
    				WHERE(" tis.sn=#{selectParams.terminalNo}");
				}else {
					if(StringUtils.isNotBlank(selectParams.getsTime())){
						WHERE("mis.create_time>=#{selectParams.sTime}");
					}
					if(StringUtils.isNotBlank(selectParams.geteTime())){
						WHERE("mis.create_time<=#{selectParams.eTime}");
					}
				}
    			
    			if (!"-1".equals(selectParams.getMerTeamId())) {
					 WHERE(" mis.team_id = #{selectParams.merTeamId} ");
				} 
				
				//20170120tgh
				if("1".equals(selectParams.getActivityType())){
					WHERE("ad.activity_code = '002' ");
				}else if("0".equals(selectParams.getActivityType())){
					WHERE(" (ad.activity_code != '002' OR ad.activity_code IS NULL) ");
				}
				//是否为微创业用户
				if (StringUtils.isNotBlank(selectParams.getRecommendedSource())) {
					WHERE(" mis.recommended_source = #{selectParams.recommendedSource}");
				}
				ORDER_BY("mis.create_time DESC");
			}}.toString();
		}
		
	}
	
}