package cn.pay.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.BorrowAuditHistroyRepository;
import cn.pay.core.domain.business.BorrowAuditHistroy;
import cn.pay.core.service.BorrowAuditHistroyService;

@Service
public class BorrowAuditHistroyServiceImpl implements BorrowAuditHistroyService {

	@Autowired
	private BorrowAuditHistroyRepository repository;

	@Override
	@Transactional
	public void saveAndUpdate(BorrowAuditHistroy borrowAuditHistroy) {
		repository.saveAndFlush(borrowAuditHistroy);
	}

	@Override
	public List<BorrowAuditHistroy> getByBorrowId(Long borrowId) {
		return repository.findByBorrowId(borrowId);
	}

}
