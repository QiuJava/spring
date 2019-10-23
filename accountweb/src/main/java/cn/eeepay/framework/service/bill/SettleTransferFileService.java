package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.model.bill.SettleTransferFile;

public interface SettleTransferFileService {
		int insertSettleTransferFile(SettleTransferFile stf)  throws Exception;
		int updateSettleTransferFileById(SettleTransferFile stf)  throws Exception;
		int updateSettleTransferFileByIdAndStatus(SettleTransferFile stf)  throws Exception;
		List<SettleTransferFile> findFileByFileName(String fileName) throws Exception;
		List<SettleTransferFile> findFileByFileMD5(String fileMd5) throws Exception;

}
