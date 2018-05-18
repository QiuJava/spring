package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.SystemDictionaryItemRepository;
import cn.pay.core.domain.sys.SystemDictionaryItem;
import cn.pay.core.obj.qo.SystemDictionaryQo;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.SystemDictionaryService;
import cn.pay.core.util.LogicException;

@Service
public class SystemDictionaryItemServiceImpl implements SystemDictionaryItemService {

	@Autowired
	private SystemDictionaryItemRepository repository;

	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@Override
	public List<SystemDictionaryItem> getBySn(String sn) {
		return repository.findBySystemDictionaryId(systemDictionaryService.listBySn(sn).getId());
	}

	@Override
	@Transactional
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Long getSystemDictionaryId(Long itemId) {
		SystemDictionaryItem item = repository.findOne(itemId);
		if (item == null) {
			throw new LogicException("没有找到字典明细");
		}
		return item.getSystemDictionaryId();
	}

	@Override
	@Transactional
	public void update(SystemDictionaryItem item) {
		SystemDictionaryItem dictionaryItem = repository.saveAndFlush(item);
		if (dictionaryItem == null) {
			throw new LogicException("字典明细更新异常");
		}
	}

	@Override
	public Page<SystemDictionaryItem> page(SystemDictionaryQo qo) {
		Page<SystemDictionaryItem> page = repository.findAll(new Specification<SystemDictionaryItem>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionaryItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (qo.getKeyword() != null) {
					list.add(cb.like(root.get("title"), "%" + qo.getKeyword() + "%"));
				}
				if (qo.getSystemDictionaryId() != -1) {
					list.add(cb.equal(root.get("systemDictionaryId"), qo.getSystemDictionaryId()));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.ASC, "sequence"));
		return page;
	}

	@Override
	public SystemDictionaryItem get(Long id) {
		return repository.findOne(id);
	}
}
