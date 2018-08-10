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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.SystemDictionaryRepository;
import cn.pay.core.domain.sys.SystemDictionary;
import cn.pay.core.pojo.qo.SystemDictionaryQo;
import cn.pay.core.service.SystemDictionaryService;
import cn.pay.core.util.LogicException;

/**
 * 系统洗点服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class SystemDictionaryServiceImpl implements SystemDictionaryService {

	@Autowired
	private SystemDictionaryRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	@Transactional(rollbackFor = { LogicException.class })
	public SystemDictionary save(SystemDictionary systemDictionary) {
		SystemDictionary dictionary = repository.saveAndFlush(systemDictionary);
		if (dictionary == null) {
			throw new LogicException("系统字典更新异常");
		}

		return dictionary;
	}

	@Override
	public Page<SystemDictionary> page(SystemDictionaryQo qo) {
		Page<SystemDictionary> page = repository.findAll(new Specification<SystemDictionary>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (qo.getKeyword() != null) {
					list.add(cb.or(cb.like(root.get("sn"), "%" + qo.getKeyword() + "%"),
							cb.like(root.get("title"), "%" + qo.getKeyword() + "%")));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize()));
		return page;
	}

	@Override
	public List<SystemDictionary> list() {
		return repository.findAll();
	}

	@Override
	public SystemDictionary listBySn(String sn) {
		return repository.findBySn(sn);
	}

}
