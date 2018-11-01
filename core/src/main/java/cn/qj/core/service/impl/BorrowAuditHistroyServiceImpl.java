package cn.qj.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.BorrowAuditHistroy;
import cn.qj.core.repository.BorrowAuditHistroyRepository;
import cn.qj.core.service.BorrowAuditHistroyService;

/**
 * 借款审核历史服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service
public class BorrowAuditHistroyServiceImpl implements BorrowAuditHistroyService {

	@Autowired
	private BorrowAuditHistroyRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void saveAndUpdate(BorrowAuditHistroy borrowAuditHistroy) {
		repository.saveAndFlush(borrowAuditHistroy);
	}

	@Override
	public List<BorrowAuditHistroy> getByBorrowId(Long borrowId) {
		return repository.findByBorrowId(borrowId);
	}

}
