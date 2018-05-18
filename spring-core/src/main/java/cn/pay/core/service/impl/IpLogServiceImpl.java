package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.IpLogRepository;
import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.obj.qo.IpLogQo;
import cn.pay.core.redis.service.IpLogRedisService;
import cn.pay.core.service.IpLogService;

@Service
public class IpLogServiceImpl implements IpLogService {

	@Autowired
	private IpLogRepository repository;

	@Autowired
	private IpLogRedisService redisService;

	@Cacheable("ipLogCache")
	@Override
	public Page<IpLog> page(IpLogQo qo) {
		Page<IpLog> page = repository.findAll(new Specification<IpLog>() {
			@Override
			public Predicate toPredicate(Root<IpLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (qo.getState() != -1) {
					list.add(cb.equal(root.get("loginState").as(Integer.class), qo.getState()));
				}
				if (qo.getBeginDate() != null) {
					list.add(cb.greaterThanOrEqualTo(root.get("loginTime").as(Date.class), qo.getBeginDate()));
				}
				if (qo.getEndDate() != null) {
					list.add(cb.lessThanOrEqualTo(root.get("loginTime").as(Date.class), qo.getEndDate()));
				}
				if (qo.getUsername() != null) {
					if (qo.isLike()) {
						list.add(cb.like(root.get("username"), qo.getUsername() + "%"));
					} else {
						list.add(cb.equal(root.get("username"), qo.getUsername()));
					}
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.DESC, "loginTime"));
		return page;
	}

	@Override
	public IpLog getNewestIpLog(String username) {
		List<IpLog> list = repository.findByUsernameOrderByLoginTimeDesc(username, new PageRequest(0, 1));
		return list.get(0);
	}

	@CacheEvict(value = "ipLogCache", allEntries = true)
	@Override
	@Transactional
	public void saveAndUpdate(IpLog ipLog) {
		IpLog log = repository.saveAndFlush(ipLog);
		redisService.put(log.getId().toString(), log, -1);
	}

}
