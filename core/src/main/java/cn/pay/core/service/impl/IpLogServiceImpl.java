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
import org.springframework.util.StringUtils;

import cn.pay.core.dao.IpLogRepository;
import cn.pay.core.entity.sys.IpLog;
import cn.pay.core.pojo.qo.IpLogQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.IpLogService;

/**
 * 登录日志服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class IpLogServiceImpl implements IpLogService {

	@Autowired
	private IpLogRepository repository;

	@Override
	@Cacheable("pageQueryIpLog")
	public PageResult pageQueryIpLog(IpLogQo qo) {
		Page<IpLog> page = repository.findAll(new Specification<IpLog>() {
			@Override
			public Predicate toPredicate(Root<IpLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				if (qo.getState() != -1) {
					Predicate loginState = cb.equal(root.get("loginState").as(Integer.class), qo.getState());
					predicateList.add(loginState);
				}
				if (qo.getBeginDate() != null) {
					Predicate beginLoginTime = cb.greaterThanOrEqualTo(root.get("loginTime").as(Date.class),
							qo.getBeginDate());
					predicateList.add(beginLoginTime);
				}
				if (qo.getEndDate() != null) {
					Predicate endLoginTime = cb.lessThanOrEqualTo(root.get("loginTime").as(Date.class),
							qo.getEndDate());
					predicateList.add(endLoginTime);
				}
				if (StringUtils.hasLength(qo.getUsername())) {
					if (qo.getIsLike()) {
						Predicate likeUsername = cb.like(root.get("username"), qo.getUsername() + "%");
						predicateList.add(likeUsername);
					} else {
						Predicate equalUsername = cb.equal(root.get("username"), qo.getUsername());
						predicateList.add(equalUsername);
					}
				}
				Predicate[] predicateArray = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(predicateArray));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.DESC, "loginTime"));
		if (page.getTotalElements() < 1) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	@Cacheable("getNewestIpLogByUsername")
	public IpLog getNewestIpLogByUsername(String username) {
		return repository.findByUsernameOrderByLoginTimeDesc(username, new PageRequest(0, 1)).get(0);
	}

	@CacheEvict(value = { "pageQueryIpLog", "getNewestIpLogByUsername" }, allEntries = true)
	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public IpLog saveIpLog(IpLog ipLog) {
		return repository.saveAndFlush(ipLog);
	}

	@CacheEvict(value = { "pageQueryIpLog", "getNewestIpLogByUsername" }, allEntries = true)
	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public IpLog updateIpLog(IpLog ipLog) {
		return repository.saveAndFlush(ipLog);
	}

}
