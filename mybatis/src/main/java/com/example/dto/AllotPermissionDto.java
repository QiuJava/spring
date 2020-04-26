package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分配权限数据
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class AllotPermissionDto {
	private Integer roleId;
	private Integer permissionId;

}
