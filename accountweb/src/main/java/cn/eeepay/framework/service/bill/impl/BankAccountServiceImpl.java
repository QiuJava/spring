package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.BankAccountMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BankAccount;
import cn.eeepay.framework.service.bill.BankAccountService;



@Service("bankAccountService")
@Transactional
public class BankAccountServiceImpl implements BankAccountService{
	
	@Resource
	public BankAccountMapper bankAccountMapper;

	@Override
	public int insertBankAccount(BankAccount bankAccount) throws Exception {
		return bankAccountMapper.insertBankAccount(bankAccount);
	}

	@Override
	public BankAccount existBankAccount(String accountNo) {
		return bankAccountMapper.exsitBankAccount(accountNo);
	}

	@Override
	public List<BankAccount> findBankAccountList(BankAccount bankAccount, Sort sort, Page<BankAccount> page)
			throws Exception {
		return bankAccountMapper.findBankAccountList(bankAccount,sort,page);
	}

	@Override
	public BankAccount findBankAccountById(String id) {
		return bankAccountMapper.findBankAccountById(id);
	}

	@Override
	public int updateBankAccount(BankAccount bankAccount) throws Exception {
		return bankAccountMapper.updateBankAccount(bankAccount);
	}

	
	
}
