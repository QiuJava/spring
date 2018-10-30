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

import cn.qj.core.entity.SystemTimedTask;
import cn.qj.core.pojo.qo.SystemTimedTaskQo;
import cn.qj.core.pojo.vo.PageResult;
import cn.qj.core.repository.SystemTimedTaskRepository;
import cn.qj.core.service.SystemTimedTaskService;

/**
 * 系统定时任务服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class SystemTimedTaskServiceImpl implements SystemTimedTaskService {

	@Autowired
	private SystemTimedTaskRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public SystemTimedTask saveSystemTimedTask(SystemTimedTask systemTimedTask) {
		return repository.saveAndFlush(systemTimedTask);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public SystemTimedTask updateSystemTimedTask(SystemTimedTask systemTimedTask) {
		return repository.saveAndFlush(systemTimedTask);
	}

	@Override
	public PageResult pageQuerySystemTimedTask(SystemTimedTaskQo qo) {
		Page<SystemTimedTask> page = repository.findAll(new Specification<SystemTimedTask>() {
			@Override
			public Predicate toPredicate(Root<SystemTimedTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				if (StringUtils.hasLength(qo.getGroupName())) {
					Predicate groupNameLike = cb.like(root.get("groupName"), qo.getGroupName() + "%");
					predicateList.add(groupNameLike);
				}
				Predicate[] ps = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize()));
		if (page.getTotalElements() < 1) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	public SystemTimedTask getSystemTimedTaskById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void deleteById(Long id) {
		repository.delete(id);
	}

}
