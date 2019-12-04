package com.example.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限复选框
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class PermissionCheckboxVo {
	private Long permissionId;
	private boolean checked;
	private String permissionName;
}
