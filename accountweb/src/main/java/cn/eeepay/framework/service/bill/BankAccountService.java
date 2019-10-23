package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BankAccount;

public interface BankAccountService {
	
	/**
	 * 新增银行账户
	 * @param bankAccount
	 * @return
	 * @throws Exception
	 */
	int insertBankAccount(BankAccount bankAccount)  throws Exception;
	
	/**
	 * 判断当前银行账号是否存在
	 * @param accountNo
	 * @return
	 */
	BankAccount existBankAccount(String accountNo);
	
	/**
	 * 查询银行账户信息
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<BankAccount> findBankAccountList(BankAccount bankAccount,Sort sort,Page<BankAccount> page) throws Exception;
	
	/**
	 * 通过  id 查询银行账户
	 * @param id
	 * @return
	 */
	BankAccount findBankAccountById(String id) ;
	
	/**
	 * 修改  银行账户  信息
	 * @param bankAccount
	 * @return
	 * @throws Exception
	 */
	int updateBankAccount(BankAccount bankAccount) throws Exception ;
	
}
