package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.LogicException;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.qo.PermissionQo;
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

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Permission permission) {
		return permissionMapper.insert(permission);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasByPermissionName(String permissionName) {
		return permissionMapper.countByPermissionName(permissionName) == 1;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasByAuthority(String authority) {
		return permissionMapper.countByAuthority(authority) == 1;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasByUrl(String url) {
		return permissionMapper.countByUrl(url) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Permission permission) {
		return permissionMapper.updateById(permission);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public Permission getById(Long id) {
		return permissionMapper.selectById(id);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		long countRolePermissionByPermissionId = permissionMapper.countRolePermissionByPermissionId(id);
		if (countRolePermissionByPermissionId > 0) {
			int deleteRolePermissionByPermissionId = permissionMapper.deleteRolePermissionByPermissionId(id);
			if (countRolePermissionByPermissionId != deleteRolePermissionByPermissionId) {
				throw new LogicException("删除失败");
			}
		}
		return permissionMapper.deleteById(id);
	}

	public List<Permission> listByQo(PermissionQo qo) {
		return permissionMapper.selectByQo(qo);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteByMenuId(Long menuId) {
		return permissionMapper.deleteByMenuId(menuId);
	}

	public boolean hasById(Long permissionId) {
		return permissionMapper.countById(permissionId) == 1;
	}

	public long countByMenuId(Long menuId) {
		return permissionMapper.countByMenuId(menuId);
	}

}
