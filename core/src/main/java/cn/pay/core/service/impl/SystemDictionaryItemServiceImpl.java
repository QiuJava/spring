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
import org.springframework.util.StringUtils;

import cn.pay.core.entity.sys.SystemDictionary;
import cn.pay.core.entity.sys.SystemDictionaryItem;
import cn.pay.core.pojo.qo.SystemDictionaryItemQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.repository.SystemDictionaryItemRepository;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.SystemDictionaryService;
import cn.pay.core.util.LogicException;

/**
 * 系统字典明细服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class SystemDictionaryItemServiceImpl implements SystemDictionaryItemService {

	@Autowired
	private SystemDictionaryItemRepository repository;

	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@Override
	public List<SystemDictionaryItem> getBySn(String sn) {
		return repository.findBySystemDictionaryId(systemDictionaryService.getSystemDictionaryBySn(sn).getId());
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void deleteById(Long id) {
		repository.delete(id);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void update(SystemDictionaryItem item) {
		SystemDictionaryItem dictionaryItem = repository.saveAndFlush(item);
		if (dictionaryItem == null) {
			throw new LogicException("字典明细更新异常");
		}
	}

	@Override
	public PageResult pageQuery(SystemDictionaryItemQo qo) {
		Page<SystemDictionaryItem> page = repository.findAll(new Specification<SystemDictionaryItem>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionaryItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.hasLength(qo.getTitle())) {
					Predicate likeTitle = cb.like(root.get("title"), qo.getTitle() + "%");
					list.add(likeTitle);
				}
				if (qo.getSystemDictionaryId() != null) {
					Predicate equalSystemDictionaryId = cb.equal(root.get("systemDictionary"),
							new SystemDictionary(qo.getSystemDictionaryId()));
					list.add(equalSystemDictionaryId);
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.ASC, "sequence"));
		if (page == null) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	public SystemDictionaryItem get(Long id) {
		return repository.findOne(id);
	}
}
