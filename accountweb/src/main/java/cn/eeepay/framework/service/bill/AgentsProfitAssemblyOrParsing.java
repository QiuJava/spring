package cn.eeepay.framework.service.bill;

import java.io.File;
import java.util.Map;

public interface AgentsProfitAssemblyOrParsing {

	Map<String, Object> resolvebatchPreAdjustFile(File temp, String uname) throws Exception;

	Map<String, Object> resolvebatchPreFreezeFile(File temp, String uname) throws Exception;
	
	Map<String, Object> resolvebatchUnfreezeFile(File temp, String uname) throws Exception;

}
