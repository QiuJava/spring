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
import org.springframework.util.StringUtils;

import cn.pay.core.dao.SystemTimedTaskRepository;
import cn.pay.core.domain.sys.SystemTimedTask;
import cn.pay.core.pojo.qo.SystemTimedTaskQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.SystemTimedTaskService;

@Service
public class SystemTimedTaskServiceImpl implements SystemTimedTaskService {

	@Autowired
	private SystemTimedTaskRepository repository;

	@Override
	@Transactional
	public void saveAndUpdate(SystemTimedTask systemTimedTask) {
		repository.saveAndFlush(systemTimedTask);
	}

	@Override
	public PageResult listQuery(SystemTimedTaskQo qo) {
		Page<SystemTimedTask> page = repository.findAll(new Specification<SystemTimedTask>() {
			@Override
			public Predicate toPredicate(Root<SystemTimedTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.hasLength(qo.getGroupName())) {
					Predicate predicate = cb.like(root.get("groupName"), "%" + qo.getGroupName() + "%");
					list.add(predicate);
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize()));
		if (page.getTotalElements() < 1) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	public SystemTimedTask get(Long id) {
		return repository.findById(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
