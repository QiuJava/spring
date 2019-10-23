package cn.eeepay.framework.service.bill;

public interface GenericTableService {
	// 内部账户账号产生
	String createKey() throws Exception;
	// 调账Id自动生成10位
	String adjustAccountID() throws Exception;
	String updateKey(String table) throws Exception;
	String createTest()  throws Exception;
	String createExtKey()  throws Exception;
	String createSettleTransferBatchId()  throws Exception;
	/**
	 * 出账单明细Id自动生成8位
	 * @return
	 * @throws Exception
	 */
	String outBillDetailId() throws Exception;
	String subOutBillDetailNo() throws Exception;
	
	/**
	 * 代理商汇总批次号
	 * @return
	 * @throws Exception
	 */
	String createAgentProfitCollectionBatchNo() throws Exception;
	
	String createSuperPushShareCollectionBatchNo() throws Exception;

	/**
	 * 服务商分润汇总批次号
	 * @return
	 * @throws Exception
	 */
	String createServiceShareCollectionBatchNo() throws Exception;
}
