package cn.eeepay.framework.service.bill;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.bill.DuiAccountDetail;

public interface DuiAccountAssemblyOrParsing {

	/**
	 * 快钱对账
	 * @param newFile
	 * @param acqTrans 
	 * @return
	 */
	Map<String,Object> resolveKQZQFileTxt(File newFile, List<DuiAccountDetail> acqTrans)  throws Exception ;


}
