package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskEventRecord;

public interface RiskEventRecordDao {

	@SelectProvider(type = SqlProvider.class, method = "queryEventRecordList")
	@ResultType(RiskEventRecord.class)
	List<RiskEventRecord> queryEventRecordList(@Param("page")Page<RiskEventRecord> page, @Param("riskEventRecord")RiskEventRecord riskEventRecord);
	
//	@SelectProvider(type = SqlProvider.class, method = "exportEventRecordList") //20180327 mays修改
	@SelectProvider(type = SqlProvider.class, method = "queryEventRecordList")
	@ResultType(RiskEventRecord.class)
	List<RiskEventRecord> riskEventRecordExport(@Param("riskEventRecord")RiskEventRecord riskEventRecord);
	
	@Select("select rer.*,ai.agent_name from risk_event_record rer,agent_info ai where rer.agent_no = ai.agent_no and rer.id=#{id}")
	@ResultType(RiskEventRecord.class)
	RiskEventRecord findRiskEventRecordById(@Param("id")int id);
	
	@Update(" update risk_event_record set "
			+ " handle_status='1',"
			+ " handle_results=#{riskEventRecord.handleResults},"
			+ " handle_time=#{riskEventRecord.handleTime},"
			+ " handle_remark=#{riskEventRecord.handleRemark},"
			+ " handle_person=#{riskEventRecord.handlePerson}"
			+ " where id=#{riskEventRecord.id}")
	int updateHandleStatus(@Param("riskEventRecord")RiskEventRecord riskEventRecord);
	
	public class SqlProvider {
		public String queryEventRecordList(Map<String, Object> param) {
			final RiskEventRecord riskEventRecord = (RiskEventRecord) param.get("riskEventRecord");
			return new SQL() {
				{
					SELECT(" rer.*, rr.roll_status ");
					FROM(" risk_event_record rer "
							+ "left join risk_roll rr on rer.roll_no = rr.roll_no and rer.rules_instruction = rr.roll_type and rr.roll_belong = 2 ");
					if (StringUtils.isNotBlank(riskEventRecord.getMobilephone())) {
	    				LEFT_OUTER_JOIN(" merchant_info mi ON mi.merchant_no = rer.merchant_no ");
	    			}
	    			if(StringUtils.isNotBlank(riskEventRecord.getRollNo())){//商户编号/身份证号/银行卡号
	    				riskEventRecord.setRollNo(riskEventRecord.getRollNo()+"%");
	    				WHERE(" rer.roll_no like #{riskEventRecord.rollNo}");
	    			}
	    			/*if(StringUtils.isNotBlank(riskEventRecord.getMerchantNo())){//商户编号
	    				riskEventRecord.setMerchantNo(riskEventRecord.getMerchantNo()+"%");
	    				WHERE(" rer.merchant_no like #{riskEventRecord.merchantNo}");
	    			}*/
	    			if(StringUtils.isNotBlank(riskEventRecord.getMerchantName())){//商户名称
	    				riskEventRecord.setMerchantName(riskEventRecord.getMerchantName()+"%");
	    				WHERE(" rer.merchant_name like #{riskEventRecord.merchantName}");
	    			}
	    			if(StringUtils.isNotBlank(riskEventRecord.getAgentNo())){//代理商编号
	    				riskEventRecord.setAgentNo(riskEventRecord.getAgentNo()+"%");
	    				WHERE(" rer.agent_no like #{riskEventRecord.agentNo}");
	    			}
	    			if(riskEventRecord.getRulesInstruction()!= null && !riskEventRecord.getRulesInstruction().equals(-1)){//规则指令
	    				WHERE(" rer.rules_instruction = #{riskEventRecord.rulesInstruction}");
	    			}
	    			if(riskEventRecord.getRulesNo()!=null){//规则编号
	    				WHERE(" rer.rules_no = #{riskEventRecord.rulesNo}");
	    			}
	    			if(riskEventRecord.getHandleStatus()!= null && !riskEventRecord.getHandleStatus().equals(-1)){//处理状态
	    				WHERE(" rer.handle_status = #{riskEventRecord.handleStatus}");
	    			}
	    			if(riskEventRecord.getHandleResults()!= null  && !riskEventRecord.getHandleResults().equals(-1)){//处理结果
	    				WHERE(" rer.handle_results = #{riskEventRecord.handleResults}");
	    			}
	    			if(StringUtils.isNotBlank(riskEventRecord.getHandlePerson())){//处理人
	    				WHERE(" rer.handle_person = #{riskEventRecord.handlePerson}");
	    			}
	    			if (riskEventRecord.getScreateTime() != null) {//创建时间
	    				WHERE(" rer.create_time>=#{riskEventRecord.screateTime}");
	    			}
	    			if (riskEventRecord.getEcreateTime() != null) {
	    				WHERE(" rer.create_time<=#{riskEventRecord.ecreateTime}");
	    			}
	    			if (riskEventRecord.getShandleTime() != null) {//操作时间
	    				WHERE(" rer.handle_time>=#{riskEventRecord.shandleTime}");
	    			}
	    			if (riskEventRecord.getEhandleTime() != null) {
	    				WHERE(" rer.handle_time<=#{riskEventRecord.ehandleTime}");
	    			}
	    			if(riskEventRecord.getOrderNo() != null){
						riskEventRecord.setOrderNo("%"+riskEventRecord.getOrderNo()+"%");
						WHERE(" rer.order_no like #{riskEventRecord.orderNo}");
					}
	    			if (StringUtils.isNotBlank(riskEventRecord.getMobilephone())) {
	    				WHERE(" mi.mobilephone = #{riskEventRecord.mobilephone}");
	    			}
					ORDER_BY(" rer.create_time desc ");
				}
			}.toString();

		}
		
		
		public String exportEventRecordList(Map<String, Object> param) {
			final RiskEventRecord riskEventRecord = (RiskEventRecord) param.get("riskEventRecord");
			return new SQL() {
				{
					SELECT(" rer.*, rr.roll_status ");
					FROM(" risk_event_record rer left join risk_roll rr "
							+ " on rer.roll_no = rr.roll_no "
							+ " and rer.rules_instruction = rr.roll_type");
	    			if(StringUtils.isNotBlank(riskEventRecord.getRollNo())){//商户编号/身份证号/银行卡号
	    				riskEventRecord.setRollNo(riskEventRecord.getRollNo()+"%");
	    				WHERE(" rer.roll_no like #{riskEventRecord.rollNo}");
	    			}
	    			if(StringUtils.isNotBlank(riskEventRecord.getMerchantName())){//商户名称
	    				riskEventRecord.setMerchantName(riskEventRecord.getMerchantName()+"%");
	    				WHERE(" rer.merchant_name like #{riskEventRecord.merchantName}");
	    			}
	    			if(StringUtils.isNotBlank(riskEventRecord.getAgentNo())){//代理商编号
	    				riskEventRecord.setAgentNo(riskEventRecord.getAgentNo()+"%");
	    				WHERE(" rer.agent_no like #{riskEventRecord.agentNo}");
	    			}
	    			if(riskEventRecord.getRulesInstruction()!= null && !riskEventRecord.getRulesInstruction().equals(-1)){//规则指令
	    				WHERE(" rer.rules_instruction = #{riskEventRecord.rulesInstruction}");
	    			}
	    			if(riskEventRecord.getRulesNo()!=null){//规则编号
	    				WHERE(" rer.rules_no = #{riskEventRecord.rulesNo}");
	    			}
	    			if(riskEventRecord.getHandleStatus()!= null && !riskEventRecord.getHandleStatus().equals(-1)){//处理状态
	    				WHERE(" rer.handle_status = #{riskEventRecord.handleStatus}");
	    			}
	    			if(riskEventRecord.getHandleResults()!= null  && !riskEventRecord.getHandleResults().equals(-1)){//处理结果
	    				WHERE(" rer.handle_results = #{riskEventRecord.handleResults}");
	    			}
	    			if(StringUtils.isNotBlank(riskEventRecord.getHandlePerson())){//处理人
	    				WHERE(" rer.handle_person = #{riskEventRecord.handlePerson}");
	    			}
	    			if (riskEventRecord.getScreateTime() != null) {//创建时间
	    				WHERE(" rer.create_time>=#{riskEventRecord.screateTime}");
	    			}
	    			if (riskEventRecord.getEcreateTime() != null) {
	    				WHERE(" rer.create_time<=#{riskEventRecord.ecreateTime}");
	    			}
	    			if (riskEventRecord.getShandleTime() != null) {//操作时间
	    				WHERE(" rer.handle_time>=#{riskEventRecord.shandleTime}");
	    			}
	    			if (riskEventRecord.getEhandleTime() != null) {
	    				WHERE(" rer.handle_time<=#{riskEventRecord.ehandleTime}");
	    			}
					ORDER_BY(" rer.create_time desc ");
				}
			}.toString();

		}
	}

	

	
}
