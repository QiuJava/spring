package com.example.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.config.listener.ContextStartListener;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.qo.PermissionQo;
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

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Permission permission) throws LogicException {
		Date date = new Date();
		permission.setCreateTime(date);
		permission.setUpdateTime(date);

		return permissionMapper.insertSelective(permission);
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
		return permissionMapper.updateByPrimaryKeySelective(permission);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		permissionMapper.deleteRolePermissionByPermissionId(id);
		return permissionMapper.deleteById(id);
	}

	public List<Permission> listByQo(PermissionQo qo) {
		return permissionMapper.listByQo(qo);
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

	public String getAuthorityByUrl(String requestUrl) {
		return permissionMapper.selectAuthorityByUrl(requestUrl);
	}

	public void settingPermissionMap() {
		List<Permission> listByQo = this.listByQo(new PermissionQo());
		Map<String, String> map = new HashMap<>(listByQo.size());
		listByQo.forEach(permission -> map.put(permission.getMappingAddress(), permission.getAuthority()));
		valueOperations.set(ContextStartListener.PEMISSION_MAP, map);
	}

}
