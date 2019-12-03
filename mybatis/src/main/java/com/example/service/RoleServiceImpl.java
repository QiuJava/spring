package com.example.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.dto.AllotPermissionDto;
import com.example.entity.Role;
import com.example.mapper.RoleMapper;
import com.example.qo.RoleQo;
import com.example.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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
	public int save(Role role) throws LogicException {
		Date date = new Date();
		role.setCreateTime(date);
		role.setUpdateTime(date);
		boolean hasByRoleName = this.hasByRoleName(role.getRoleName());
		if (hasByRoleName) {
			throw new LogicException("角色名已存在");
		}
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
		String roleName = role.getRoleName();

		role.setUpdateTime(new Date());
		String oldRoleName = this.getRoleNameById(role.getId());
		if (StrUtil.noText(oldRoleName)) {
			throw new LogicException("角色ID不正确");
		}

		if (!oldRoleName.equals(roleName)) {
			boolean hasByRoleName = this.hasByRoleName(roleName);
			if (hasByRoleName) {
				throw new LogicException("角色名已存在");
			}
		}

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
		boolean hasById = this.hasById(id);
		if (!hasById) {
			throw new LogicException("角色ID不存在");
		}
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

	public Page<Role> listByQo(RoleQo roleQo) {
		Page<Role> page = PageHelper.startPage(roleQo.getPage(), roleQo.getRows(), roleQo.getCount());
		roleMapper.selectByQo(roleQo);
		return page;
	}

}
