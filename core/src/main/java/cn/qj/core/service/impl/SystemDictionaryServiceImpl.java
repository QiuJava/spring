package cn.qj.core.service.impl;

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
import org.springframework.util.StringUtils;

import cn.qj.core.common.PageResult;
import cn.qj.core.entity.SystemDictionary;
import cn.qj.core.pojo.qo.SystemDictionaryQo;
import cn.qj.core.repository.SystemDictionaryRepository;
import cn.qj.core.service.SystemDictionaryService;
import cn.qj.core.util.ResultUtil;

/**
 * 系统洗点服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service
public class SystemDictionaryServiceImpl implements SystemDictionaryService {

	@Autowired
	private SystemDictionaryRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void deleteById(Long id) {
		repository.delete(id);
	}

	@Override
	public PageResult pageQuerySystemDictionary(SystemDictionaryQo qo) {
		Page<SystemDictionary> page = repository.findAll(new Specification<SystemDictionary>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				if (StringUtils.hasLength(qo.getKeyword())) {
					Predicate snLike = cb.like(root.get("sn"), qo.getKeyword() + "%");
					Predicate titleLike = cb.like(root.get("title"), qo.getKeyword() + "%");
					Predicate snOrTitleLike = cb.or(snLike, titleLike);
					predicateList.add(snOrTitleLike);
				}
				Predicate[] ps = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize()));
		if (page.getTotalElements() < 1) {
			return ResultUtil.empty((qo.getPageSize()));
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	public List<SystemDictionary> listAll() {
		return repository.findAll();
	}

	@Override
	public SystemDictionary getSystemDictionaryBySn(String sn) {
		return repository.findBySn(sn);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public SystemDictionary updateSystemDictionary(SystemDictionary systemDictionary) {
		SystemDictionary formerSysDict = repository.findOne(systemDictionary.getId());
		formerSysDict.setIntro(systemDictionary.getIntro());
		return repository.saveAndFlush(formerSysDict);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public SystemDictionary saveSystemDictionary(SystemDictionary systemDictionary) {
		return repository.saveAndFlush(systemDictionary);
	}

}
