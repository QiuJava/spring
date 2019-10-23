package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.bill.SettleTransfer;
import cn.eeepay.framework.model.bill.SettleTransferFile;

public interface SettleTransferService {
		int insertSettleTransfer(SettleTransfer st)  throws Exception;
		int updateSettleTransferById(SettleTransfer st)  throws Exception;
//		int saveSettleTransfer(SettleTransfer st,List<Map<String, String>> list)  throws Exception;
		public Map<String,Object> saveBatchSettleTransfer(SettleTransferFile stf,List<Map<String, String>> list,String fileName,String totalNum) throws Exception;

}
