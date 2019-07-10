package cn.loan.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.PageResult;
import cn.loan.core.entity.CompanyBankCard;
import cn.loan.core.entity.qo.CompanyBankCardQo;
import cn.loan.core.repository.CompanyBankCardDao;

/**
 * 公司银行卡服务
 * 
 * @author qiujian
 *
 */
@Service
public class CompanyBankCardService {

	@Autowired
	private CompanyBankCardDao companyBankCardDao;

	public List<CompanyBankCard> listAll() {
		return companyBankCardDao.findAll();
	}

	public PageResult pageQuery(CompanyBankCardQo qo) {
		Page<CompanyBankCard> page = companyBankCardDao.findAll(new PageRequest(qo.getPage(), qo.getSize()));
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(CompanyBankCard bankCard) {
		companyBankCardDao.save(bankCard);
	}

}
