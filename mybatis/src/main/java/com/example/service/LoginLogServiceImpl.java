package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.entity.LoginLog;
import com.example.mapper.LoginLogMapper;
import com.example.util.DataSourceUtil;

/**
 * 登录日志服务实现
 * 
 * @author Qiu Jian
 *
 */
@Service
public class LoginLogServiceImpl {

	@Autowired
	private LoginLogMapper loginLogMapper;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(LoginLog loginLog) {
		return loginLogMapper.insertSelective(loginLog);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public Date getNewestLoginTimeByUsername(String username) {
		return loginLogMapper.selectNewestLoginTimeByUsername(username);
	}

}
