package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.annotation.DataSourceKey;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.util.DataSourceUtil;

/**
 * 权限服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class PermissionServiceImpl {

	@Autowired
	private PermissionMapper permissionMapper;

	public int save(Permission permission) {
		return permissionMapper.insertSelective(permission);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasPermissionName(String permissionName) {
		return permissionMapper.countByPermissionName(permissionName) > 0;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasAuthority(String authority) {
		return permissionMapper.countByAuthority(authority) > 0;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasUrl(String url) {
		return permissionMapper.countByUrl(url) > 0;
	}

}
