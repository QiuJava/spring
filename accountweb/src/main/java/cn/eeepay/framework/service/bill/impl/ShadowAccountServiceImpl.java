package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ExtAccountMapper;
import cn.eeepay.framework.dao.bill.ShadowAccountMapper;
import cn.eeepay.framework.model.bill.ShadowAccount;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectService;
@Service("shadowAccountService")
@Transactional
public class ShadowAccountServiceImpl  implements ShadowAccountService{
	@Resource
	public ExtAccountMapper outAccountMapper;
	
	private static final Logger log = LoggerFactory.getLogger(ShadowAccountServiceImpl.class);
	@Resource
	public ShadowAccountMapper shadowAccountMapper;
	@Resource
	public CurrencyService  currencyService;
	
	@Resource
	public SubjectService  subjectService;
	
	@Resource
	public OrgInfoService orgInfoService;
	
	@Resource
	public  GenericTableService  genericTableService;

	@Override
	public ShadowAccount getShadowAccount(String accountNo, String accountFlag, String transDate) throws Exception {
		return shadowAccountMapper.getShadowAccount(accountNo, accountFlag, transDate);
	}

	@Override
	public int updateShadowAccount(ShadowAccount shadowAccount) throws Exception {
		return shadowAccountMapper.updateShadowAccount(shadowAccount);
	}

	@Override
	public int insertShadowAccount(ShadowAccount shadowAccount) throws Exception {
		return shadowAccountMapper.insertShadowAccount(shadowAccount);
	}

	@Override
	public List<ShadowAccount> findShadowAccountByAccountFlag(String accountFlag) throws Exception {
		return shadowAccountMapper.findShadowAccountByAccountFlag(accountFlag);
	}
	
	
}
