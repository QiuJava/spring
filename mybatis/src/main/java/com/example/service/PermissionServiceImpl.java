package com.example.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.qo.PermissionQo;
import com.example.util.StrUtil;
import com.example.vo.PermissionCheckboxVo;

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
	public int save(Permission permission) throws LogicException {
		Date date = new Date();
		permission.setCreateTime(date);
		permission.setUpdateTime(date);

		boolean hasByPermissionName = this.hasByPermissionName(permission.getPermissionName());
		if (hasByPermissionName) {
			throw new LogicException("权限名称已存在");
		}

		boolean hasByAuthority = this.hasByAuthority(permission.getAuthority());
		if (hasByAuthority) {
			throw new LogicException("权限编码已存在");
		}

		String url = permission.getUrl();
		if (StrUtil.hasText(url)) {
			boolean hasByUrl = this.hasByUrl(url);
			if (hasByUrl) {
				throw new LogicException("权限路径已存在");
			}
		}
		return permissionMapper.insert(permission);
	}

	public boolean hasByPermissionName(String permissionName) {
		return permissionMapper.countByPermissionName(permissionName) == 1;
	}

	public boolean hasByAuthority(String authority) {
		return permissionMapper.countByAuthority(authority) == 1;
	}

	public boolean hasByUrl(String url) {
		return permissionMapper.countByUrl(url) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Permission permission) throws LogicException {
		permission.setUpdateTime(new Date());

		Permission oldPermission = permissionMapper.selectById(permission.getId());

		String permissionName = permission.getPermissionName();
		if (!permissionName.equals(oldPermission.getPermissionName())) {
			boolean hasByPermissionName = this.hasByPermissionName(permissionName);
			if (hasByPermissionName) {
				throw new LogicException("权限名称已存在");
			}
		} else {
			// 不更新权限名称
			permission.setPermissionName(null);
		}

		String authority = permission.getAuthority();
		if (!authority.equals(oldPermission.getAuthority())) {
			boolean hasByAuthority = this.hasByAuthority(authority);
			if (hasByAuthority) {
				throw new LogicException("权限编码已存在");
			}
		} else {
			// 不更新权限编码
			permission.setAuthority(null);
		}

		String url = permission.getUrl();

		if (StrUtil.hasText(url)) {
			if (!url.equals(oldPermission.getUrl())) {
				boolean hasByUrl = this.hasByUrl(url);
				if (hasByUrl) {
					throw new LogicException("权限路径已存在");
				}
			}
		}

		return permissionMapper.updateById(permission);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		permissionMapper.deleteRolePermissionByPermissionId(id);
		return permissionMapper.deleteById(id);
	}

	public List<Permission> listByQo(PermissionQo qo) {
		return permissionMapper.selectByQo(qo);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteByMenuId(Long menuId) {
		return permissionMapper.deleteByMenuId(menuId);
	}

	public List<PermissionCheckboxVo> listPermissionCheckboxVoByMenuId(Long menuId, Long roleId) {

		List<PermissionCheckboxVo> selectPermissionCheckboxVoByMenuId = permissionMapper
				.selectPermissionCheckboxVoByMenuId(menuId);
		List<Long> currentRolePermissionIdList = permissionMapper.selectIdByRoleId(roleId);
		for (PermissionCheckboxVo permissionCheckboxVo : selectPermissionCheckboxVoByMenuId) {
			Long permissionId = permissionCheckboxVo.getPermissionId();
			if (currentRolePermissionIdList.contains(permissionId)) {
				permissionCheckboxVo.setChecked(true);
			}
		}

		return selectPermissionCheckboxVoByMenuId;
	}

}
