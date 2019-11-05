package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.LoginLog;
import com.example.mapper.LoginLogMapper;

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

}
