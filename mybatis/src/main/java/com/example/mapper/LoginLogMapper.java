package com.example.mapper;

import java.util.Date;

import com.example.entity.LoginLog;

/**
 * 登录日志数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface LoginLogMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(LoginLog record);

	int insertSelective(LoginLog record);

	LoginLog selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LoginLog record);

	int updateByPrimaryKey(LoginLog record);

	Date getNewestCreateTimeByEmployeeId(Integer employeeId);

}