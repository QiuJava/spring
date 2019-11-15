package com.example.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.LogicException;
import com.example.dto.AllotPermissionDto;
import com.example.entity.Role;
import com.example.mapper.RoleMapper;
import com.example.util.DataSourceUtil;

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

		List<Long> oldPermissionIdList = roleMapper.selectEmployeeIdByRoleId(roleId);
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
			int deleteByPermissionIdList = roleMapper.deleteByPermissionIdList(oldPermissionIdList);
			if (deleteByPermissionIdList != oldPermissionIdList.size()) {
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
		return roleMapper.updateByPrimaryKeySelective(role);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasRoleByRoleName(String roleName) {
		return roleMapper.countByRoleName(roleName) == 1;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public String getRoleNameById(Long id) {
		return roleMapper.selectRoleNameById(id);
	}

}
