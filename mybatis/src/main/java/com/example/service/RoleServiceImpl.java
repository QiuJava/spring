package com.example.service;

import java.util.Iterator;
import java.util.List;

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
	@Autowired
	private EmployeeServiceImpl employeeService;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Role role) {
		return roleMapper.insert(role);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void allotPermission(Long roleId, Long[] permissionIdList) throws LogicException {
		long countById = roleMapper.countById(roleId);
		if (countById != 1) {
			throw new LogicException("角色ID不存在");
		}

		List<Long> oldPermissionIdList = roleMapper.selectPermissionIdByRoleId(roleId);
		for (Iterator<Long> iterator = oldPermissionIdList.iterator(); iterator.hasNext();) {
			Long oldId = (Long) iterator.next();
			for (Long id : permissionIdList) {
				if (oldId.equals(id)) {
					iterator.remove();
				}
			}
		}

		if (oldPermissionIdList.size() > 0) {
			// 批量删除
			int deleteRolePermissionByPermissionIdList = roleMapper
					.deleteRolePermissionByPermissionIdList(oldPermissionIdList);
			if (deleteRolePermissionByPermissionIdList != oldPermissionIdList.size()) {
				throw new LogicException("分配失败");
			}
		}

		AllotPermissionDto allotPermissionDto = new AllotPermissionDto();
		allotPermissionDto.setRoleId(roleId);
		for (Long permissionId : permissionIdList) {
			boolean hasById = permissionService.hasById(permissionId);
			if (!hasById) {
				continue;
			}
			allotPermissionDto.setPermissionId(permissionId);

			// 判断角色权限关系是否存在
			long count = roleMapper.countRolePermissionByRoleIdAndPermissionId(allotPermissionDto);
			if (count == 1) {
				continue;
			}

			int insertRolePermission = roleMapper.insertRolePermission(allotPermissionDto);
			if (insertRolePermission != 1) {
				throw new LogicException("分配失败");
			}
		}

	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updateById(Role role) {
		return roleMapper.updateById(role);
	}

	public boolean hasByRoleName(String roleName) {
		return roleMapper.countByRoleName(roleName) == 1;
	}

	public String getRoleNameById(Long id) {
		return roleMapper.selectRoleNameById(id);
	}

	public boolean hasById(Long id) {
		return roleMapper.countById(id) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		// 删除角色对应的权限关系和员工关系
		int countRolePermissionByRoleId = roleMapper.countRolePermissionByRoleId(id);
		if (countRolePermissionByRoleId > 0) {
			int deleteRolePermissionByRoleId = roleMapper.deleteRolePermissionByRoleId(id);
			if (countRolePermissionByRoleId != deleteRolePermissionByRoleId) {
				throw new LogicException("删除失败");
			}
		}

		int countEmployeeRoleByRoleId = employeeService.countEmployeeRoleByRoleId(id);
		if (countEmployeeRoleByRoleId > 0) {
			int deleteEmployeeRoleByRoleId = employeeService.deleteEmployeeRoleByRoleId(id);
			if (deleteEmployeeRoleByRoleId != countEmployeeRoleByRoleId) {
				throw new LogicException("删除失败");
			}
		}
		return roleMapper.deleteById(id);
	}

}
