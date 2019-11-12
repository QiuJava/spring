package com.example.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限查询
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class PermissionQo extends BaseQo {
	private String permissionName;
	private String authority;
}
