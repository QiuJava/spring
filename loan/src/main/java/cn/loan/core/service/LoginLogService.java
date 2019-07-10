package cn.loan.core.service;

import java.util.ArrayList;
import java.util.Date;
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

import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.LoginLog;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.qo.LoginLogQo;
import cn.loan.core.repository.LoginLogDao;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 登录日志服务
 * 
 * @author qiujian
 *
 */
@Service
public class LoginLogService {

	@Autowired
	private LoginLogDao loginLogDao;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	
	@Transactional(rollbackFor = RuntimeException.class)
	public void save(LoginLog loginLog) {
		loginLogDao.save(loginLog);
	}

	public PageResult pageQuery(LoginLogQo qo) {
		Page<LoginLog> page = loginLogDao.findAll(new Specification<LoginLog>() {
			@Override
			public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				Integer loginStatus = qo.getLoginStatus();
				if (loginStatus !=null && loginStatus != -1) {
					Predicate loginStatusPredicate = cb.equal(root.get(StringUtil.LOGIN_STATUS), loginStatus);
					predicateList.add(loginStatusPredicate);
				}
				Date beginTime = qo.getBeginTime();
				if (beginTime != null) {
					Predicate beginTimePredicate = cb.greaterThanOrEqualTo(root.get(StringUtil.LOGIN_TIME), beginTime);
					predicateList.add(beginTimePredicate);
				}
				Date endTime = qo.getEndTime();
				if (endTime != null) {
					Predicate endTimePredicate = cb.lessThanOrEqualTo(root.get(StringUtil.LOGIN_TIME), endTime);
					predicateList.add(endTimePredicate);
				}
				String username = qo.getUsername();
				if (StringUtil.hasLength(username)) {
					Predicate likeUsername = cb.like(root.get(StringUtil.USERNAME), qo.getUsername() + StringUtil.PER_CENT);
					predicateList.add(likeUsername);
				}
				Predicate[] predicateArray = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(predicateArray));
			}
		}, new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC,StringUtil.LOGIN_TIME));
		// 获取登录状态字典
		List<SystemDictionaryItem> items = SystemDictionaryUtil.getItems(SystemDictionaryUtil.LOGIN_STATUS, systemDictionaryHashService);
		// 填充状态显示名称
		List<LoginLog> content = page.getContent();
		for (LoginLog loginLog : content) {
			for (SystemDictionaryItem item : items) {
				if (loginLog.getLoginStatus().equals(Integer.valueOf(item.getItemValue()))) {
					loginLog.setDisplayStatus(item.getItemName());
				}
			}
		}
		return new PageResult(content, page.getTotalPages(), qo.getCurrentPage());
	}

	public LoginLog getNewest(String username) {
		return loginLogDao.findByUsernameOrderByLoginTimeDesc(username,new PageRequest(0, 1)).get(0);
	}

}
