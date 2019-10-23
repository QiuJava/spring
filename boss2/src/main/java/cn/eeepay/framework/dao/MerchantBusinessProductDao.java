package cn.eeepay.framework.dao;

import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AutoCheckResult;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.MerchantBusinessProductHistory;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MerchantBusinessProductDao {

	Logger log = LoggerFactory.getLogger(MerchantBusinessProductDao.class);

	@Select("SELECT mbp.*,bpb.bp_name,mi.merchant_type,mi.team_id,mi.merchant_name,mi.mobilephone"
			+ " from merchant_business_product mbp "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
			+ "LEFT JOIN merchant_info mi on mi.merchant_no=mbp.merchant_no "
			//+ "LEFT JOIN live_verify_channel lvc on lvc.channel_code=mbp.auto_mbp_channel "
			+"where mbp.id=#{id}")
	@ResultType(MerchantBusinessProduct.class)
	MerchantBusinessProduct selectByPrimaryKey(@Param("id") Long id);

	
	@Select("SELECT mbp.*,bpb.bp_name,lvc.channel_name"
			+ " from merchant_business_product mbp "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id LEFT JOIN live_verify_channel lvc on lvc.channel_code=mbp.auto_mbp_channel where mbp.merchant_no=#{merchantNo}")
	@ResultType(MerchantBusinessProduct.class)
	MerchantBusinessProduct selectByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("SELECT mbp.*,bpb.bp_name,lvc.channel_name"
			+ " from merchant_business_product mbp "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id LEFT JOIN live_verify_channel lvc on lvc.channel_code=mbp.auto_mbp_channel where mbp.merchant_no=#{merchantNo}")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectByMerchantNoAll(@Param("merchantNo")String merchantNo);
	
	
	
	@Update("update merchant_business_product set `status`=#{status},auditor_id=#{auditor} where id=#{mbpId}")
	int updateBymbpId(@Param("mbpId") Long mbpId, @Param("status") String status, @Param("auditor") String auditor);

    @Update("update merchant_business_product set `reexamine_status`=#{status},reexamine_operator=#{auditor},reexamine_time=now() where id=#{mbpId}")
    int reexamineBymbpId(@Param("mbpId") Long mbpId, @Param("status") String status, @Param("auditor") String auditor);

	@Update("update merchant_business_product set `reexamine_status`=#{status},reexamine_tip_end_time=#{reexamineTipEndTime} where id=#{mbpId}")
	int resetReexamineBymbpId(@Param("mbpId") Long mbpId, @Param("status") String status, @Param("reexamineTipEndTime") Date reexamineTipEndTime);

    @Update("update merchant_business_product set `status`=#{record.status} where id=#{record.id}")
	int updateByItemAndMbpId(@Param("record") MerchantBusinessProduct record);

	@Select("select mbp.id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
			//20170114去掉DISTINCT,查询商户性能优化 tiangh(xy)
//    		@Select("select DISTINCT mbp.id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
			+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time,mis.team_id,tti.team_name,bsu.user_name "
			+ "from merchant_business_product mbp "
			+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
			+ "LEFT JOIN team_info tti on tti.team_id=mis.team_id "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
			+ "LEFT JOIN boss_shiro_user bsu on bsu.id=mbp.auditor_id "
			+ "where mbp.status=2 and mis.merchant_name is not null and mis.mobilephone is not null and mis.team_id is not null "
			+ "order by merCreate_time desc")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectAllByStatusInfo(@Param("page") Page<MerchantBusinessProduct> page);

	@SelectProvider(type = SqlProvider.class, method = "selectMerBpHistoryList")
	@ResultType(MerchantBusinessProductHistory.class)
	List<MerchantBusinessProductHistory> selectMerBpHistoryList(@Param("params") Map<String, Object> params, @Param("page") Page<MerchantBusinessProductHistory> page);

	//20170114去掉DISTINCT,查询商户性能优化 tiangh(xy)
//    @Select("select DISTINCT mbp.id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,mis.risk_status,"
	@Select("select mbp.id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,mis.risk_status,mis.pre_frozen_amount,"
			+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time,mis.team_id,tti.team_name,mis.status as mer_status ,(CASE ad.activity_code WHEN '002' THEN '1' ELSE '0' END ) AS  activity_code "
			+ "from merchant_business_product mbp "
			+ "inner JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
			+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
			+ "LEFT JOIN team_info tti on tti.team_id=mis.team_id "
			+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
			+ "LEFT JOIN  activity_detail ad on ad.merchant_no = mbp.merchant_no "
			+ "where mis.merchant_name is not null and mis.mobilephone is not null and mis.team_id is not null "
			//323tgh去掉order by
//    		+ "order by merCreate_time desc"
	)
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectAllInfo(@Param("page") Page<MerchantBusinessProduct> page);

	@SelectProvider(type = SqlProvider.class, method = "findMbpList")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectByParam(@Param("page") Page<MerchantBusinessProduct> page, @Param("selectParams") SelectParams selectParams);

	@SelectProvider(type = SqlProvider.class, method = "findMbpList")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> exportExamine(@Param("selectParams") SelectParams selectParams);

	@SelectProvider(type = SqlProvider.class, method = "findStatusMbpList")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectByStatusParam(@Param("page") Page<MerchantBusinessProduct> page, @Param("selectParams") SelectParams selectParams);

	@SelectProvider(type = SqlProvider.class, method = "selectAllInfoSale")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectAllInfoSale(@Param("page") Page<MerchantBusinessProduct> page, @Param("str") String str);

	@SelectProvider(type = SqlProvider.class, method = "selectByParamSale")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectByParamSale(@Param("page") Page<MerchantBusinessProduct> page, @Param("selectParams") SelectParams selectParams);

	@Select("select acr.check_info,acr.check_result,acr.check_verdict,acr.create_time,acrs.rule_dis as rule_code from auto_check_result acr "
			+ "LEFT JOIN auto_check_rule acrs on acrs.rule_code=acr.rule_code"
			+ " where acr.merchant_no=#{merchantNo} and acr.bp_id=#{bpId} order by acr.create_time desc")
	@ResultType(AutoCheckResult.class)
	List<AutoCheckResult> selectAutoCheckResult(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);

	@Select("select * from merchant_business_product where merchant_no =#{merchantNo} and bp_id =#{bpId}")
	@ResultType(MerchantBusinessProduct.class)
	MerchantBusinessProduct selectMerBusPro(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);

	//@Select("select mbp.id,mbp.merchant_no,mbp.bp_id,mri.content from merchant_business_product mbp,merchant_require_item mri where mbp.merchant_no=mri.merchant_no and mri.mri_id=3 and (mbp.status=2 or mbp.status=5) and mbp.id in(#{ids})")
	@SelectProvider(type = SqlProvider.class, method = "findMersByBpIds")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> selectMerBusProByIds(@Param("ids") List<String> ids);

	@Update("update merchant_business_product set status = #{status} where id = #{id}")
	int updateStatusById(@Param("status") String status, @Param("id") String id);

	@Update("update app_reg_check_num set check_identity_status='3' where mobile=#{mobile} and teamId=#{teamId}")
	int updateCheckNum(@Param("mobile") String mobile, @Param("teamId") String teamId);

	@Update("update merchant_business_product set trade_type = #{tradeType} where id = #{primaryKey}")
	int updateTradeTypeById(@Param("primaryKey") Long primaryKey, @Param("tradeType") String tradeType);

	@Select(" select t1.*, t3.bp_name from merchant_business_product t1 " +
			" left join merchant_service t2 on (t1.merchant_no = t2.merchant_no and t1.bp_id = t2.bp_id) " +
			" left JOIN business_product_define t3 on t3.bp_id = t1.bp_id where t2.id = #{merServiceId} ")
	@ResultType(MerchantBusinessProduct.class)
	MerchantBusinessProduct selectByServiceId(@Param("merServiceId")Long merServiceId);

	@Update(" UPDATE merchant_business_product t1 SET t1.trade_type = (CASE WHEN EXISTS (SELECT t2.id FROM merchant_service t2 " +
			" WHERE t2.trade_type = '1' AND t2.merchant_no = #{merBusPro.merchantNo} AND t2.bp_id = #{merBusPro.bpId}) THEN '1' ELSE '0' END), t1.zf_picture = '1' " +
			" WHERE t1.id = #{merBusPro.id}")
	int updateTradeTypeByServices(@Param("merBusPro") MerchantBusinessProduct merBusPro);

	@SelectProvider(type = SqlProvider.class, method = "getTeamInfo")
	List<Map<String,Object>> getTeamInfo(@Param("likeTeamId")String likeTeamId);

	@SelectProvider(type = SqlProvider.class, method = "examineTotalByParam")
	@ResultType(Map.class)
	List<Map<String, Object>> examineTotalByParam(@Param("selectParams")SelectParams selectParams);

	@Select(
			"select * from credit_card where merchant_no=#{merNo} LIMIT 1 "
	)
	Map<String, Object> getMerCreditCard(@Param("merNo")String merNo);

    public class SqlProvider {
		public String getTeamInfo(Map<String, Object> param) {
			final String likeTeamId = (String) param.get("likeTeamId");
			String sql="select * from team_info where team_id regexp '^"+likeTeamId+"[0-9]+$'";
			return sql;
		}
		public String examineTotalByParam(Map<String, Object> param) {
			StringBuffer reSql = new StringBuffer();
			reSql.append("select count(0) as total,");
			reSql.append("sum( case when mbp.reexamine_status ='0' then 1 else 0 end )  init,");
			reSql.append("sum( case when mbp.reexamine_status ='1' then 1 else 0 end )  agree,");
			reSql.append("sum( case when mbp.reexamine_status ='2' then 1 else 0 end )  disagree,");
			reSql.append("sum( case when mbp.reexamine_status ='3' then 1 else 0 end )  refund  from ( ");
			String sql = findMbpList(param);
			reSql.append(sql.toString());
			reSql.append(" ) mbp");
			log.info("商户复审统计sql:{}",  reSql.toString());
			return reSql.toString();
		}
		public String findMbpList(Map<String, Object> param) {
			final SelectParams selectParams = (SelectParams) param.get("selectParams");
			SQL sql = new SQL(){{
				SELECT("DISTINCT mbp.id,ais.agent_node,mbp.bp_id,mis.id_card_no,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,mis.pre_frozen_amount,"
						+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time,"
						+ "mbp.item_source,"
						+ "am.acq_merchant_no,"
						+ "mis.merchant_type,"
						+ "mis.team_id,tti.team_name,mis.risk_status,mbp.auto_check_times, (CASE ad.activity_code WHEN '002' THEN '1' ELSE '0' END ) AS  activity_code,mis.status as mer_status,"
						+ "mis.recommended_source,mbp.reexamine_status as reexamine_status,mbp.reexamine_time as reexamine_time,mbp.reexamine_operator as reexamine_operator,"
						+ "IFNULL(ms.trade_type,0) as trade_type,(CASE WHEN ms.trade_type = '1' THEN IFNULL(zmi.sync_status, '0') ELSE '-' END) as sync_status");
				FROM("merchant_business_product mbp");
				LEFT_OUTER_JOIN(" merchant_info mis on mis.merchant_no=mbp.merchant_no ");
				LEFT_OUTER_JOIN("agent_info ais on ais.agent_no=mis.agent_no ");
				LEFT_OUTER_JOIN(" team_info tti on tti.team_id=mis.team_id ");
				LEFT_OUTER_JOIN(" business_product_define bpb on bpb.bp_id=mbp.bp_id ");
				LEFT_OUTER_JOIN(" acq_merchant am on am.merchant_no=mbp.merchant_no");
				LEFT_OUTER_JOIN(" activity_detail ad on ad.merchant_no = mbp.merchant_no ");
				if (StringUtils.isNotBlank(selectParams.getSyncStatus()) && "1".equals(selectParams.getTradeType())) {
					INNER_JOIN(" zq_merchant_info zmi on (zmi.merchant_no = mbp.merchant_no and zmi.mbp_id = mbp.id) ");
					WHERE("zmi.effective_status='1' and zmi.sync_status = #{selectParams.syncStatus}");
				} else {
					LEFT_OUTER_JOIN(" zq_merchant_info zmi on (zmi.merchant_no = mbp.merchant_no and zmi.mbp_id = mbp.id and zmi.effective_status = '1') ");
				}
				if (StringUtils.isNotBlank(selectParams.getTradeType())) {
					LEFT_OUTER_JOIN(" merchant_service ms on (mbp.merchant_no = ms.merchant_no AND mbp.bp_id = ms.bp_id ) ");
					if(!"0".equals(selectParams.getTradeType())){
						WHERE("ms.trade_type=#{selectParams.tradeType} and zmi.channel_code = ms.channel_code");
					} else {
						WHERE("ms.trade_type=#{selectParams.tradeType}");
					}
				}else{
					LEFT_OUTER_JOIN(" merchant_service ms on (mbp.merchant_no = ms.merchant_no AND mbp.bp_id = ms.bp_id and zmi.channel_code = ms.channel_code ) ");
				}

				if (StringUtils.isNotBlank(selectParams.getAcqOrgMerNo())) {
					WHERE("am.acq_merchant_no=#{selectParams.acqOrgMerNo}");
				}

				if (StringUtils.isNotBlank(selectParams.getMerchantNo())) {
					WHERE("(mis.merchant_no=#{selectParams.merchantNo} or mis.merchant_name = #{selectParams.merchantNo})");
				}
				if (StringUtils.isNotBlank(selectParams.getMbpId())) {
					WHERE("mbp.id=#{selectParams.mbpId}");
				}

				if (StringUtils.isNotBlank(selectParams.getMerStatus())) {//商户状态
					WHERE("mis.status=#{selectParams.merStatus}");
				}

				if (StringUtils.isNotBlank(selectParams.getAgentNode())) {
					WHERE("ais.agent_node like #{selectParams.agentNode}");
				}
				if (StringUtils.isNotBlank(selectParams.getAgentName())) {
					WHERE("ais.agent_no=#{selectParams.agentName}");
				}
				if (StringUtils.isNotBlank(selectParams.getRiskStatus())) {
					WHERE("mis.risk_status=#{selectParams.riskStatus}");
				}
				if (StringUtils.isNotBlank(selectParams.getMerAccount())) {
					WHERE("mis.mer_account=#{selectParams.merAccount}");
				}
				if (StringUtils.isNotBlank(selectParams.getCardId())) {
					WHERE("mis.id_card_no=#{selectParams.cardId}");
				}
				if (StringUtils.isNotBlank(selectParams.getMobilephone())) {
					WHERE("mis.mobilephone=#{selectParams.mobilephone}");
				}
				if (StringUtils.isNotBlank(selectParams.getMerchantType())) {
					WHERE("mis.merchant_type=#{selectParams.merchantType}");
				}
				if (StringUtils.isNotBlank(selectParams.getMerchantExamineState()) && !StringUtils.equals("-1", selectParams.getMerchantExamineState())) {
					WHERE("mbp.status=#{selectParams.merchantExamineState}");
				}
				if (StringUtils.isNotBlank(selectParams.getProductType()) && !StringUtils.equals("-1", selectParams.getProductType())) {
					WHERE("mbp.bp_id=#{selectParams.productType}");
				}

				if (selectParams.getsTime() != null) {
					WHERE("mis.create_time>=#{selectParams.sTime}");
				}
				if (selectParams.geteTime() != null) {
					WHERE("mis.create_time<=#{selectParams.eTime}");
				}

				if (StringUtils.isNotBlank(selectParams.getAccountName())) {
					WHERE("mis.lawyer=#{selectParams.accountName}  ");
				}
				//2.2.5 增加预冻结金额查询
				if (StringUtils.isNotBlank(selectParams.getPreFrozenMoney1())) {
					WHERE("mis.pre_frozen_amount>=#{selectParams.preFrozenMoney1}");
				}
				if (StringUtils.isNotBlank(selectParams.getPreFrozenMoney2())) {
					WHERE("mis.pre_frozen_amount<=#{selectParams.preFrozenMoney2}");
				}
				if (StringUtils.isNotBlank(selectParams.getAutoCheckTimes()) && "1".equals(selectParams.getAutoCheckTimes().toString())) {
					if (StringUtils.isNotBlank(selectParams.getOpenStatus())) {
						LEFT_OUTER_JOIN(" examinations_log els ON els.item_no = concat(mbp.id,'') ");
						WHERE(" els.operator = '-1' AND els.open_status = #{selectParams.openStatus} and els.examine_type=1 ");
					}
				}
				if (selectParams.getActivityType() != null && StringUtils.isNotBlank(selectParams.getActivityType())) {
					if ("1".equals(selectParams.getActivityType())) {
						WHERE("ad.activity_code = '002' ");
					} else {
						WHERE("(ad.activity_code != '002' OR ad.activity_code IS NULL) ");
					}
				}
				if (StringUtils.isNotBlank(selectParams.getRecommendedSource())) {
					WHERE("mis.recommended_source=#{selectParams.recommendedSource}");
				}
				if (StringUtils.isNotBlank(selectParams.getItemSource())) {
					WHERE("mbp.item_source=#{selectParams.itemSource}");
				}
				/****风控复审条件***/
				if (StringUtils.isNotBlank(selectParams.getReexamineStatus())) {
					WHERE("mbp.reexamine_status=#{selectParams.reexamineStatus}");
				}
				if(selectParams.getStartReexamineTime() != null){
					WHERE("mbp.reexamine_time>=#{selectParams.startReexamineTime}");
				}
				if(selectParams.getEndReexamineTime() != null){
					WHERE("mbp.reexamine_time<=#{selectParams.endReexamineTime}");
				}
				if (StringUtils.isNotBlank(selectParams.getReexamineOperator())) {
					WHERE("mbp.reexamine_operator=#{selectParams.reexamineOperator}");
				}
				if(StringUtils.isNotBlank(selectParams.getNotMbpId())){
					WHERE("mbp.id<>#{selectParams.notMbpId} ");
				}
				/*******风控复审条件****/
				if(StringUtils.isNotBlank(selectParams.getTeamId())){
					if(selectParams.getTeamId().startsWith("6")||selectParams.getTeamId().startsWith("71")){
						WHERE("mis.team_id regexp '^"+selectParams.getTeamId().substring(0,2)+"[0-9]+$' ");
					}else{
						WHERE("mis.team_id=#{selectParams.teamId}");
					}
				}
				if(StringUtils.isNotBlank(selectParams.getProvince())){
					WHERE("mis.province like '"+selectParams.getProvince()+"%'");
				}
				if(StringUtils.isNotBlank(selectParams.getCity())){
					WHERE("mis.city like '"+selectParams.getCity()+"%'");
				}
//				ORDER_BY("mis.create_time DESC")
			}};
			log.info("商户查询sql:{}",  sql.toString());
			return sql.toString();
		}

		public String findStatusMbpList(Map<String, Object> param) {
			final SelectParams selectParams = (SelectParams) param.get("selectParams");
			return new SQL() {{
				//20170114去掉DISTINCT,查询商户性能优化 tiangh(xy)
//				SELECT_DISTINCT("mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
				SELECT("mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
						+ "mbp.item_source,"
						+ "mis.merchant_type,"
						+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time"
						+ ",mis.team_id,tti.team_name,bsu.user_name,mis.recommended_source ");
				FROM("merchant_business_product mbp "
						+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
						+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
						//20170114,查询商户性能优化 tiangh 注释(xy)
//						+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
						+ "LEFT JOIN team_info tti on tti.team_id=mis.team_id "
						+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
						+ "LEFT JOIN boss_shiro_user bsu on bsu.id=mbp.auditor_id");
//						+ " LEFT JOIN activity_detail ad on ad.merchant_no = mbp.merchant_no "); //20171018注释掉
				if (StringUtils.isNotBlank(selectParams.getMerchantNo())) {
					WHERE("(mis.merchant_no=#{selectParams.merchantNo} or mis.merchant_name = #{selectParams.merchantNo})");
				}
				if (StringUtils.isNotBlank(selectParams.getMbpId())) {
					WHERE("mbp.id=#{selectParams.mbpId}");
				}
				if (StringUtils.isNotBlank(selectParams.getAgentNode())) {
					WHERE("ais.agent_node like #{selectParams.agentNode}");
				}
				if (StringUtils.isNotBlank(selectParams.getAgentName())) {
					WHERE("ais.agent_no=#{selectParams.agentName}");
				}
				if (StringUtils.isNotBlank(selectParams.getCardId())) {
					WHERE("mis.id_card_no=#{selectParams.cardId}");
				}
				if (StringUtils.isNotBlank(selectParams.getAuditorId())) {
					WHERE("mbp.auditor_id=#{selectParams.auditorId}");
				}
				if (StringUtils.isNotBlank(selectParams.getMobilephone())) {
					WHERE("mis.mobilephone=#{selectParams.mobilephone}");
				}
				if (StringUtils.isNotBlank(selectParams.getMerchantType())) {
					WHERE("mis.merchant_type=#{selectParams.merchantType}");
				}

				if(StringUtils.isNotBlank(selectParams.getStatus())){
					if("2".equals(selectParams.getStatus())){
						WHERE("mbp.status=#{selectParams.status}");
					}else if("3".equals(selectParams.getStatus())){
						WHERE("mbp.status=#{selectParams.status} and mbp.auditor_id='-2' ");
					}else if("5".equals(selectParams.getStatus())){
						WHERE("mbp.status=#{selectParams.status}");
					}
					//特殊处理，999表示复审退件，查询复审状态
					if("999".equals(selectParams.getStatus())){
						WHERE("mbp.reexamine_status = '3' and ( mbp.status in('2','5') or (mbp.status='3' and mbp.auditor_id='-2') )  ");
					}else{
						//排除复审退件数据
						WHERE("mbp.reexamine_status in( '0','1','2') ");
					}

				}

				if (StringUtils.isNotBlank(selectParams.getProductType()) && !StringUtils.equals("-1", selectParams.getProductType())) {
					WHERE("bpb.bp_id=#{selectParams.productType}");
				}
				//20170114去掉DISTINCT,查询商户性能优化 tiangh(xy)
//				if(StringUtils.isNotBlank(selectParams.getTermianlType()) && !StringUtils.equals("-1", selectParams.getTermianlType())){
//					WHERE("tis.type=#{selectParams.termianlType}");
//				}
				if (selectParams.getsTime() != null) {
					WHERE("mis.create_time>=#{selectParams.sTime}");
				}
				if (selectParams.geteTime() != null) {
					WHERE("mis.create_time<=#{selectParams.eTime}");
				}

				if (selectParams.getActivityType() != null && StringUtils.isNotBlank(selectParams.getActivityType())) {
//					if("1".equals(selectParams.getActivityType())){
//						WHERE("ad.activity_code = '002' ");
//					}else{
//						WHERE("ad.activity_code != '002' OR ad.activity_code IS NULL ");
//					}
					//WHERE("ad.activity_code = #{selectParams.activityType}");
					WHERE("	EXISTS (SELECT 1 FROM terminal_info ti WHERE 	ti.merchant_no = mbp.merchant_no AND ti.bp_id = mbp.bp_id AND ti.activity_type = #{selectParams.activityType})");
				}
				if (StringUtils.isNotBlank(selectParams.getRecommendedSource())) {
					WHERE("mis.recommended_source=#{selectParams.recommendedSource}");
				}
				if (StringUtils.isNotBlank(selectParams.getItemSource())) {
					WHERE("mbp.item_source=#{selectParams.itemSource}");
				}
				//去掉tgh327
//				ORDER_BY("mbp.create_time DESC");
			}}.toString();
		}

		public String selectAllInfoSale(Map<String, Object> param) {
			final String str = (String) param.get("str");
			return new SQL() {{
				SELECT("mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
						//20170114去掉DISTINCT,查询商户性能优化 tiangh(xy)
//						SELECT_DISTINCT("mbp.id,mbp.bp_id,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
						+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time"
						+ ",mis.team_id,tti.team_name,bsu.user_name");
				FROM("merchant_business_product mbp "
						+ "LEFT JOIN merchant_info mis on mis.merchant_no=mbp.merchant_no "
						+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no "
						//20170114去掉DISTINCT,查询商户性能优化 tiangh(xy)
//						+ "LEFT JOIN terminal_info tis on tis.merchant_no=mis.merchant_no "
						+ "LEFT JOIN acq_merchant am on am.merchant_no= mbp.merchant_no "
						+ "LEFT JOIN team_info tti on tti.team_id=mis.team_id "
						+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id "
						+ "LEFT JOIN boss_shiro_user bsu on bsu.id=mbp.auditor_id");
				WHERE(" mis.merchant_name is not null and mis.mobilephone is not null and mis.team_id is not null");
				if (StringUtils.isNotBlank(str)) {
					WHERE(str);
				}
				ORDER_BY("mbp.create_time DESC");
			}}.toString();
		}

		public String selectByParamSale(Map<String, Object> param) {
			final SelectParams selectParams = (SelectParams) param.get("selectParams");
//			final String saleCondition=(String)param.get("saleCondition");
			String sql = new SQL() {{
				SELECT("mbp.id,mbp.bp_id,mis.id_card_no,mbp.merchant_no,mis.merchant_name,mis.mobilephone,mis.mer_account,"
						+ "bpb.bp_name,ais.agent_name,mbp.`status`,mbp.create_time,mis.create_time as merCreate_time"
						+ ",mis.team_id,tti.team_name,bsu.user_name,ais.agent_node,ais.agent_no,am.acq_merchant_no"
						+ ",mis.risk_status,mbp.auto_check_times");
				FROM("merchant_business_product mbp ");
				LEFT_OUTER_JOIN(" merchant_info mis on mis.merchant_no=mbp.merchant_no ");
				LEFT_OUTER_JOIN(" acq_merchant am on am.merchant_no= mbp.merchant_no ");
				LEFT_OUTER_JOIN(" agent_info ais on ais.agent_no=mis.agent_no ");
				LEFT_OUTER_JOIN(" team_info tti on tti.team_id=mis.team_id ");
				LEFT_OUTER_JOIN(" business_product_define bpb on bpb.bp_id=mbp.bp_id ");
				LEFT_OUTER_JOIN(" boss_shiro_user bsu on bsu.id=mbp.auditor_id ");
				LEFT_OUTER_JOIN(" agent_info info1 on ais.one_level_id = info1.agent_no ");

				if (StringUtils.isNotBlank(selectParams.getMerchantNo())) {
					WHERE("(mis.merchant_no=#{selectParams.merchantNo} or mis.merchant_name = #{selectParams.merchantNo})");
				}
				if (StringUtils.isNotBlank(selectParams.getMbpId())) {
					WHERE("mbp.id=#{selectParams.mbpId}");
				}

				if (StringUtils.isNotBlank(selectParams.getOpenStatus()) || StringUtils.isNotBlank(selectParams.getAutoCheckTimes())) {
					String tempSql = "exists( SELECT 1 FROM examinations_log els WHERE els.item_no = mbp.id   and els.examine_type=1 ";
					if (StringUtils.isNotBlank(selectParams.getOpenStatus())) {
						tempSql = tempSql + " and els.open_status = #{selectParams.openStatus} ";

					}
					tempSql = tempSql + " and els.operator = '-1')  ";
					WHERE(tempSql);
				}

				if (StringUtils.isNotBlank(selectParams.getAgentNode())) {
					WHERE("mis.parent_node like #{selectParams.agentNode}");
				}
				if (StringUtils.isNotBlank(selectParams.getAgentName())) {
					WHERE("mis.agent_no=#{selectParams.agentName}");
				}
				if (StringUtils.isNotBlank(selectParams.getAcqOrgMerNo())) {
					WHERE("am.acq_merchant_no=#{selectParams.acqOrgMerNo}");
				}
				if (StringUtils.isNotBlank(selectParams.getRiskStatus())) {
					WHERE("mis.risk_status=#{selectParams.riskStatus}");
				}
				if (StringUtils.isNotBlank(selectParams.getMerAccount())) {
					WHERE("mis.mer_account=#{selectParams.merAccount}");
				}
				if (StringUtils.isNotBlank(selectParams.getCardId())) {
					WHERE("mis.id_card_no=#{selectParams.cardId}");
				}
				if (StringUtils.isNotBlank(selectParams.getMobilephone())) {
					WHERE("mis.mobilephone=#{selectParams.mobilephone}");
				}
				if (StringUtils.isNotBlank(selectParams.getMerchantExamineState()) && !StringUtils.equals("-1", selectParams.getMerchantExamineState())) {
					WHERE("mis.status=#{selectParams.merchantExamineState}");
				}
				if (StringUtils.isNotBlank(selectParams.getProductType()) && !StringUtils.equals("-1", selectParams.getProductType())) {
					WHERE("mbp.bp_id=#{selectParams.productType}");
				}
				if (selectParams.getsTime() != null) {
					WHERE("mis.create_time>=#{selectParams.sTime}");
				}
				if (selectParams.geteTime() != null) {
					WHERE("mis.create_time<=#{selectParams.eTime}");
				}

				if (selectParams.getActivityType() != null) {
					WHERE("	EXISTS (SELECT 1 FROM terminal_info ti WHERE 	ti.merchant_no = mbp.merchant_no AND ti.bp_id = mbp.bp_id AND ti.activity_type = #{selectParams.activityType})");
				}

//				if(StringUtils.isNotBlank(saleCondition)){
//					WHERE(" ais.one_level_id in  "+saleCondition);
//				}

				if (StringUtils.isNotBlank(selectParams.getSaleName())) {
					WHERE("info1.sale_name=#{selectParams.saleName} and info1.agent_level = '1' ");
				}


			}}.toString();
			return sql;
		}

		public String selectMerBpHistoryList(Map<String, Object> map) {
			@SuppressWarnings("unchecked")
			final Map<String, Object> param = (Map<String, Object>) map.get("params");

			SQL sql = new SQL();
			sql.SELECT("bpd1.bp_name AS bp_name1,bpd2.bp_name AS bp_name2,mbph.create_time,mbph.merchant_no,mbph.operation_type,mbph.operation_person_type,IFNULL(mi.merchant_name,bsu.user_name) AS operationPerson ");
			sql.FROM("merchant_business_product_history mbph");
			sql.LEFT_OUTER_JOIN(" boss_shiro_user bsu on mbph.operation_person_no=bsu.id");
			sql.INNER_JOIN(" merchant_info  mi on mbph.merchant_no=mi.merchant_no");
			sql.LEFT_OUTER_JOIN(" business_product_define  bpd1 on mbph.source_bp_id=bpd1.bp_id ");
			sql.LEFT_OUTER_JOIN(" business_product_define  bpd2 on mbph.new_bp_id = bpd2.bp_id");

			if (param.get("merchantNo") != null && StringUtils.isNotBlank(param.get("merchantNo").toString())) {
				sql.WHERE(" mbph.merchant_no like #{params.merchantNo}");
			}
			if (param.get("mobilephone") != null && StringUtils.isNotBlank(param.get("mobilephone").toString())) {
				sql.WHERE(" mi.mobilephone =  #{params.mobilephone}");
			}

			if (param.get("operationPerson") != null && StringUtils.isNotBlank(param.get("operationPerson").toString())) {
				sql.WHERE(" (mi.merchant_name =  #{params.operationPerson} or bsu.user_name = #{params.operationPerson})");
			}

			if (!"-1".equals(param.get("operationType"))) {
				sql.WHERE("mbph.operation_type =  #{params.operationType}");
			}

			if (!"-1".equals(param.get("operationPersonType"))) {
				sql.WHERE("mbph.operation_person_type =  #{params.operationPersonType}");
			}
			if (!"-1".equals(param.get("sourceProductType"))) {
				sql.WHERE("mbph.source_bp_id =  #{params.sourceProductType}");

			}
			if (!"-1".equals(param.get("newProductType"))) {
				sql.WHERE("mbph.new_bp_id =  #{params.newProductType}");
			}

			if (param.get("sTime") != null) {
				sql.WHERE("mbph.create_time>=#{params.sTime}");
			}
			if (param.get("eTime") != null) {
				sql.WHERE("mbph.create_time<=#{params.eTime}");
			}
			// sql.ORDER_BY("mbph.create_time DESC");

			return sql.toString();
		}

		public String findMersByBpIds(Map<String, Object> param){
			final List<String> ids = (List<String>) param.get("ids");
			StringBuilder sb = new StringBuilder();
			sb.append("select mbp.id,mbp.merchant_no,mbp.bp_id,mri.content from merchant_business_product mbp,merchant_require_item mri where mbp.merchant_no=mri.merchant_no and mri.mri_id=3 and mbp.status in('2','3','5') and mbp.reexamine_status in('0','1','2') and mbp.id in(");
			MessageFormat messageFormat = new MessageFormat("#'{'ids[{0}]},");
			for (int i = 0; i < ids.size(); i++) {
				sb.append(messageFormat.format(new Integer[]{i}));
			}
			sb.setLength(sb.length() - 1);
			sb.append(")");
			System.out.println(sb.toString());
			return sb.toString();
		}
	}

	@Select("select id from merchant_business_product where bp_id= #{bpId} limit 1")
	@ResultType(java.lang.Integer.class)
	Integer findIdByBp(String bpId);

	@Select("SELECT  COUNT(*)  AS products_limit FROM merchant_business_product  WHERE merchant_no= #{merchantNo}")
	@ResultType(java.lang.Integer.class)
	Integer selectMerProLimit(String merchantNo);

	@Select("select mbp.* from merchant_business_product mbp,business_product_define bpd where mbp.bp_id = bpd.bp_id and bpd.rely_hardware='0'  and  mbp.merchant_no= #{merchantNo}")
	@ResultType(MerchantBusinessProduct.class)
	MerchantBusinessProduct findCollectionCodeMbp(@Param("merchantNo") String merchantNo);

	@Select("SELECT DISTINCT source_bp_id ,bpd.* FROM merchant_business_product_history mbph ,business_product_define bpd WHERE mbph.source_bp_id = bpd.bp_id")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectSourceBpInfo();

	@Select("SELECT DISTINCT new_bp_id ,bpd.* FROM merchant_business_product_history mbph ,business_product_define bpd WHERE mbph.new_bp_id = bpd.bp_id")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> selectNewBpInfo();


	@Insert("insert merchant_business_product(merchant_no,bp_id,status,item_source)"
			+ "values(#{merProduct.merchantNo},#{merProduct.bpId},#{merProduct.status},#{merProduct.itemSource})")
	int insertMerProduct(@Param("merProduct") MerchantBusinessProduct merProduct);

	@Select("")
	int selectZqMerStatus(@Param("merNo") String merNo, @Param("bpId") String bpId);

	@Select("select mbp.*,bpd.bp_name from merchant_business_product mbp inner join business_product_define bpd on bpd.bp_id=mbp.bp_id "
			+ " where mbp.merchant_no=#{merchantNo}")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> getByMer(@Param("merchantNo")String merchantNo);

	@Select("SELECT `status` FROM merchant_business_product WHERE bp_id=#{bpId} AND merchant_no=#{merchantNo}")
	String queryExamineStatus(@Param("bpId") String bpId, @Param("merchantNo") String merchantNo);

	@Select("SELECT b.merchant_no, b.id,b.service_id,dt.liquidation_channel,b.channel_code FROM merchant_service b,def_trans_route_group dt "
			+ " WHERE b.service_id = dt.service_id AND b.merchant_no =#{merchantNo} and b.bp_id =#{bpId}"
			+ " AND dt.service_model = 2 AND (dt.liquidation_channel=#{channelCode})")
	@ResultType(Map.class)
	List<Map<String, Object>> findDefRouteGroupAdd(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId, @Param("channelCode") String channelCode);

	
	@Select("SELECT ue.user_id from user_entity_info ue WHERE ue.entity_id=#{merchantNo}")
	@ResultType(String.class)
	String getUserIdByMerchantInfo(@Param("merchantNo")String merchantNo);

	
	@Select("SELECT device_id from merchant_push_device WHERE mobilephone=#{mobilephone}")
	@ResultType(String.class)
	String getDeviceIdByPhone(@Param("mobilephone")String mobilephone);

	@Select("SELECT PARAM_VALUE from sys_config WHERE PARAM_KEY=#{key}")
	@ResultType(String.class)
	String getSysConfigByKey(@Param("key")String string);

	@Select("select * from merchant_business_product where merchant_no=#{merchantNo}")
	@ResultType(MerchantBusinessProduct.class)
	List<MerchantBusinessProduct> getByMerAndBpId(@Param("merchantNo")String merchantNo);

	
	@Select({"SELECT ", 
			"	channel_name ", 
			"FROM ", 
			"	live_verify_channel  ", 
			"WHERE ", 
			"	route_type = #{routeType}  ", 
			"	AND  channel_code= #{autoMbpChannel}"})
	String findByCodeAndType(@Param("autoMbpChannel")String autoMbpChannel,@Param("routeType") int routeType);

}