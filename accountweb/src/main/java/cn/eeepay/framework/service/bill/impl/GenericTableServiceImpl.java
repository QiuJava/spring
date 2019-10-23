package cn.eeepay.framework.service.bill.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import cn.eeepay.framework.dao.bill.GenericTableMapper;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.util.DateUtil;

@Service("genericTableService")
//@Transactional
public class GenericTableServiceImpl  implements GenericTableService{
	private static final Logger log = LoggerFactory.getLogger(GenericTableServiceImpl.class);
	@Resource
	private GenericTableMapper genericTableMapper;
	@Resource
	private DataSourceTransactionManager transactionManager;
	// 内部账户账号产生
	@Override
	public String createKey() throws Exception {
		String num = updateKey("bill_ins_account");
		return String.format("%012d", new BigInteger(num));
	}

	// 调账ID自动生成10位
	@Override
	public String adjustAccountID() throws Exception {
		String num = updateKey("adjust_accountID");
		return String.format("%010d", new BigInteger(num));
	}
	@Override
	public synchronized String updateKey(String table) throws Exception{
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	    //事务的传播属性
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	    TransactionStatus status = transactionManager.getTransaction(def);
	    String num = null;
	    try{
	    	num = genericTableMapper.getCurrKey(table);
			BigInteger big=new BigInteger(num).add(new BigInteger("1"));
			num = big.toString();
			genericTableMapper.updataKey(table,num);
			log.info("产生序列:" + num);
			return num;
	    }catch(Exception e){
	    	transactionManager.rollback(status);
	        return num;
	    }finally {
	    	transactionManager.commit(status);
	    }
	}
	@Override
	public String createTest() throws Exception{
		String num = updateKey("test");
		return String.format("%012d", new BigInteger(num));
	}
	/**
	 * 外部账号账号产生
	 * 
	 * @return
	 */
	@Override
	public String createExtKey() throws Exception{
		String num = updateKey("bill_ext_account");
		return String.format("%012d", new BigInteger(num));
	}
	@Override
	public String createSettleTransferBatchId() throws Exception{
		String num = updateKey("settle_transfer_batch_id");
		return String.format("%012d", new BigInteger(num));
	}

	@Override
	public String outBillDetailId() throws Exception {
		String num = updateKey("out_bill_detail");
		return String.format("%08d", new BigInteger(num));
	}
	
	@Override
	public String subOutBillDetailNo() throws Exception {
		String num = updateKey("sub_out_bill_detail");
		return String.format("%07d", new BigInteger(num));
	}
	
	/**
	 * 代理商汇总批次号
	 * @return
	 * @throws Exception
	 */
	@Override
	public String createAgentProfitCollectionBatchNo() throws Exception{
		String num = updateKey("agent_profit_collection_batch_no");
		String collectionBatchNo = DateUtil.getCurrentYYMMDDDate() + StringUtils.leftPad(num, 4, "0");
		return collectionBatchNo;
	}

	@Override
	public String createSuperPushShareCollectionBatchNo() throws Exception {
		String num = updateKey("super_push_share_collection_batch_no");
		String collectionBatchNo = DateUtil.getCurrentYYMMDDDate() + StringUtils.leftPad(num, 4, "0");
		return collectionBatchNo;
	}

	@Override
	public String createServiceShareCollectionBatchNo() throws Exception {
		String num = updateKey("super_push_share_collection_batch_no");
		String collectionBatchNo = DateUtil.getCurrentYYMMDDDate() + StringUtils.leftPad(num, 4, "0");
		return collectionBatchNo;
	}

}
