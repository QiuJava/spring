package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.dto.AllotPermissionDto;
import com.example.entity.Role;
import com.example.mapper.RoleMapper;

/**
 * 角色服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class RoleServiceImpl {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionServiceImpl permissionService;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Role role) {
		return roleMapper.insertSelective(role);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void allotPermission(Long roleId, Long[] permissionIdList) throws LogicException {
		long countRoleById = roleMapper.countRoleById(roleId);
		if (countRoleById != 1) {
			throw new LogicException("角色ID不存在");
		}

		AllotPermissionDto allotPermissionDto = new AllotPermissionDto();
		allotPermissionDto.setRoleId(roleId);
		for (Long permissionId : permissionIdList) {
			boolean hasPermissionById = permissionService.hasPermissionById(permissionId);
			if (!hasPermissionById) {
				continue;
			}
			allotPermissionDto.setPermissionId(permissionId);
			
			// 判断角色权限关系是否存在
			long count = roleMapper.countRolePermissionByRoleIdAndPermissionId(allotPermissionDto);
			if (count > 0) {
				continue;
			}
			
			int insertRolePermission = roleMapper.insertRolePermission(allotPermissionDto);
			if (insertRolePermission < 1) {
				throw new LogicException("分配失败");
			}
		}

	}

}
