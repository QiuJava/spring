package cn.qj.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
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

import cn.qj.core.common.DataSourceKey;
import cn.qj.core.common.PageResult;
import cn.qj.core.entity.IpLog;
import cn.qj.core.pojo.qo.IpLogQo;
import cn.qj.core.pojo.vo.IpLogCountVo;
import cn.qj.core.pojo.vo.IpLogVo;
import cn.qj.core.repository.IpLogRepository;
import cn.qj.core.service.IpLogService;
import cn.qj.core.util.DataSourceUtil;
import cn.qj.core.util.QueryUtil;
import cn.qj.core.util.ResultUtil;

/**
 * 登录日志服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service
public class IpLogServiceImpl implements IpLogService {

	@Autowired
	private IpLogRepository repository;

	@Autowired
	private EntityManager entityManager;

	@Override
	@Cacheable("pageQueryIpLog")
	@DataSourceKey(DataSourceUtil.READ_ONE_KEY)
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
			return ResultUtil.empty(qo.getPageSize());
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

	@Override
	public List<IpLogVo> listAllVo() {
		String sql = "SELECT i_l.ip AS ip,i_l.username AS username FROM ip_log i_l WHERE i_l.username=?1 ";
		sql = QueryUtil.handelPageSql(sql, 1, 5);
		List<Object> params = new ArrayList<>();
		params.add("独孤求败");
		return QueryUtil.findListResult(entityManager, sql, IpLogVo.class, params);
	}

	@Override
	public Page<IpLog> page() {
		return repository.findAll(new PageRequest(1, 10, Direction.DESC, "loginTime"));
	}

	@Override
	public IpLogCountVo count() {
		String sql = "SELECT count(i_l.id) AS count,i_l.username AS username FROM ip_log i_l WHERE i_l.username = ?1 GROUP BY username";
		List<Object> params = new ArrayList<>();
		params.add("独孤求败");
		return QueryUtil.findOneResult(entityManager, sql, IpLogCountVo.class, params);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> listAllMap() {
		String sql = "SELECT i_l.ip AS ip,i_l.username AS username FROM ip_log i_l WHERE i_l.username=?1 ";
		List<Object> params = new ArrayList<>();
		params.add("独孤求败");
		return QueryUtil.findListResult(entityManager, sql, HashMap.class, params);
	}
}
