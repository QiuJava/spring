package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.PageResult;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.qo.PermissionQo;
import com.example.util.DataSourceUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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

	@Transactional(rollbackFor = RuntimeException.class)
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

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Permission permission) {
		return permissionMapper.updateByPrimaryKeySelective(permission);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public Permission getById(Long id) {
		return permissionMapper.selectByPrimaryKey(id);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) {
		permissionMapper.deleteRolePermissionByPermissionId(id);
		return permissionMapper.deleteByPrimaryKey(id);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public PageResult<Permission> listByQo(PermissionQo qo) {
		Page<Permission> page = PageHelper.startPage(qo.getPageNum(), qo.getPageSize(), qo.getCount());
		permissionMapper.selectByQo(qo);
		return new PageResult<Permission>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteByMenuId(Long menuId) {
		return permissionMapper.deleteByMenuId(menuId);
	}

	public boolean hasPermissionById(Long permissionId) {
		return permissionMapper.countByPermissionId(permissionId) == 1;
	}

}
