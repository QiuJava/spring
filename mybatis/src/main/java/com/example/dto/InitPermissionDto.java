package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 初始权限数据
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class InitPermissionDto {
	private Integer roleId;
	private Integer menuId;
}
