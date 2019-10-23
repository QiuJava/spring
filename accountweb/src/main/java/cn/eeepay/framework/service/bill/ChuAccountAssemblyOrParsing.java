package cn.eeepay.framework.service.bill;

import java.io.File;
import java.util.Map;

public interface ChuAccountAssemblyOrParsing {

	Map<String, Object> resolveOutBillFile(File temp);
	
}

