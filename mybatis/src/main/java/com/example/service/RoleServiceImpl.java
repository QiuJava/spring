package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.dto.AllotPermissionDto;
import com.example.dto.InitPermissionDto;
import com.example.entity.Role;
import com.example.mapper.RoleMapper;
import com.example.qo.RoleQo;
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
	private EmployeeServiceImpl employeeService;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Role role) throws LogicException {
		Date date = new Date();
		role.setCreateTime(date);
		role.setUpdateTime(date);
		return roleMapper.insert(role);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void allotPermission(Long roleId, Long[] permissionIdList, Long menuId) throws LogicException {
		InitPermissionDto initPermissionDto = new InitPermissionDto();
		initPermissionDto.setRoleId(roleId);
		initPermissionDto.setMenuId(menuId);
		// 删除这个角色这个菜单下的所有权限
		roleMapper.deleteRolePermissionByRoleIdAndMenuId(initPermissionDto);

		AllotPermissionDto allotPermissionDto = new AllotPermissionDto();
		allotPermissionDto.setRoleId(roleId);
		for (Long permissionId : permissionIdList) {
			allotPermissionDto.setPermissionId(permissionId);
			roleMapper.insertRolePermission(allotPermissionDto);
		}

	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updateById(Role role) {
		role.setUpdateTime(new Date());
		return roleMapper.updateById(role);
	}

	public boolean hasByRoleName(String roleName) {
		return roleMapper.countByRoleName(roleName) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		// 删除角色对应的权限关系和员工关系
		roleMapper.deleteRolePermissionByRoleId(id);
		employeeService.deleteEmployeeRoleByRoleId(id);
		return roleMapper.deleteById(id);
	}

	public Page<Role> listByQo(RoleQo roleQo) {
		Page<Role> page = PageHelper.startPage(roleQo.getPage(), roleQo.getRows(), roleQo.getCount());
		roleMapper.selectByQo(roleQo);
		return page;
	}

	public String getRoleNameById(Long id) {
		return roleMapper.selectRoleNameById(id);
	}

}
