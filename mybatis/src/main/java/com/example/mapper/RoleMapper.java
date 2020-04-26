package com.example.mapper;

import java.util.List;

import com.example.dto.AllotPermissionDto;
import com.example.dto.InitPermissionDto;
import com.example.entity.Role;
import com.example.qo.RoleQo;

/**
 * 角色数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface RoleMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(Role record);

	int insertSelective(Role record);

	Role selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Role record);

	int updateByPrimaryKey(Role record);

	int insertRolePermission(AllotPermissionDto allotPermissionDto);

	int deleteRolePermissionByRoleId(Integer roleId);

	int deleteRolePermissionByRoleIdAndMenuId(InitPermissionDto initPermissionDto);
	
	List<Role> listByQo(RoleQo roleQo);
}