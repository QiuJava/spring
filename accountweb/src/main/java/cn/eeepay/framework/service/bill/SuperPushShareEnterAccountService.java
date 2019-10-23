package cn.eeepay.framework.service.bill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.model.bill.AgentShareDaySettle;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;

public interface SuperPushShareEnterAccountService {
	
	Map<String, Object> agentProfitEnterAccount(SuperPushShareDaySettle superPushShareDaySettle) throws JsonMappingException, IOException, Exception;
	
	Map<String, Object> mchProfitRecordAccount(SuperPushShareDaySettle superPushShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitRecordAccount(SuperPushShareDaySettle superPushShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	
	
	
//	Map<String, Object> agentProfitTerminalFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
//	
//	Map<String, Object> agentProfitTerminalUnFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
//	
//	Map<String, Object> agentProfitOtherFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
//	
//	Map<String, Object> agentProfitOtherUnFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	
	Map<String, Object> chongZheng(AgentShareDaySettle agentShareDaySettle) throws Exception;
	
//	Map<String, Object> agentProfitFreezeRecord(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException;
//	
//	Map<String, Object> agentProfitUnFreezeRecord(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException;
	
}
