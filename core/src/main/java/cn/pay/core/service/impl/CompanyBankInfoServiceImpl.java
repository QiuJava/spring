package cn.pay.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.entity.business.CompanyBankInfo;
import cn.pay.core.pojo.qo.CompanyBankInfoQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.repository.CompanyBankInfoRepository;
import cn.pay.core.service.CompanyBankInfoService;

/**
 * 公司银行账号信息服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class CompanyBankInfoServiceImpl implements CompanyBankInfoService {

	@Autowired
	private CompanyBankInfoRepository repositpry;

	@Override
	public List<CompanyBankInfo> list() {
		return repositpry.findAll();
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void update(CompanyBankInfo info) {
		repositpry.saveAndFlush(info);
	}

	@Override
	public PageResult page(CompanyBankInfoQo qo) {
		Page<CompanyBankInfo> page = repositpry.findAll(new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize()));
		if (page.getContent().isEmpty()) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	public CompanyBankInfo get(Long bankInfoId) {
		return repositpry.findOne(bankInfoId);
	}

}
