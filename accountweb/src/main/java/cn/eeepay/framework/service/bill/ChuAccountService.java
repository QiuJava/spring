package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SettleTransfer;
import cn.eeepay.framework.model.bill.SettleTransferFile;
import cn.eeepay.framework.model.bill.UserInfo;

public interface ChuAccountService {
	
	List<SettleTransferFile> findSettleTransferFileList(SettleTransferFile settleTransferFile, Sort sort, Page<SettleTransferFile> page) throws Exception;
	
	/**
	 * 通过 id 查询  结算转账  的详情
	 * @param id
	 * @return
	 */
	SettleTransferFile findSettleTransferFileById(Integer id) throws Exception;
	
	/**
	 * 生成出账单
	 * @param outAccountTaskId
	 * @return
	 * @throws Exception
	 */
	int createOutBill(Integer outAccountTaskId, UserInfo userInfo) throws Exception;
	
	
	List<SettleTransfer> findSettleTransferList(SettleTransfer settleTransfer, Sort sort, Page<SettleTransfer> page) throws Exception;
	
	
	List<SettleTransferFile> findSubmitChuKuanChannelList(SettleTransferFile settleTransferFile,String createDate1,String createDate2, Sort sort, Page<SettleTransferFile> page) throws Exception;

	int judgeCreateOutBill(Integer outAccountTaskId) throws Exception;

}
