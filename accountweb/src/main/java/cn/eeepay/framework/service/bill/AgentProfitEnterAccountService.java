package cn.eeepay.framework.service.bill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.model.bill.AgentShareDaySettle;

public interface AgentProfitEnterAccountService {
	
	Map<String, Object> agentProfitEnterAccount(AgentShareDaySettle agentShareDaySettle) throws JsonMappingException, IOException, Exception;
	
	Map<String, Object> agentProfitEnterAccountRecordWithLevel1(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitEnterAccountRecordWithLevel2(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitTerminalFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitTerminalUnFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitOtherFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitOtherUnFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException;
	
	
	Map<String, Object> chongZheng(AgentShareDaySettle agentShareDaySettle) throws Exception;
	
	Map<String, Object> agentProfitFreezeRecord(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException;
	
	Map<String, Object> agentProfitUnFreezeRecord(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException;
	
}
