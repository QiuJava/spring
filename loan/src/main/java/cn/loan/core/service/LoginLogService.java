package cn.loan.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.LoginLog;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.qo.LoginLogQo;
import cn.loan.core.repository.LoginLogDao;
import cn.loan.core.repository.specification.LoginLogSpecification;
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
		Page<LoginLog> page = loginLogDao.findAll(
				Specifications.where(LoginLogSpecification.equalLoginStatus(qo.getLoginStatus()))
						.and(LoginLogSpecification.greaterThanOrEqualToLoginTime(qo.getBeginTime()))
						.and(LoginLogSpecification.lessThanOrEqualToLoginTime(qo.getEndTime()))
						.and(LoginLogSpecification.likeUsername(qo.getUsername())),
				new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.LOGIN_TIME));
		// 获取登录状态字典
		List<SystemDictionaryItem> items = SystemDictionaryUtil.getItems(SystemDictionaryUtil.LOGIN_STATUS,
				systemDictionaryHashService);
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
		return loginLogDao.findByUsernameOrderByLoginTimeDesc(username, new PageRequest(0, 1)).get(0);
	}

	public List<LoginLog> getAll() {
		return loginLogDao.findAll();
	}

}
