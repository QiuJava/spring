package com.example.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限列表视图
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class PermissionListVo {
	private Long id;
	private String permissionName;
	private String authority;
	private String url;
	private String intro;
	private String menuName;
	private Date createTime;
	private Date updateTime;

}
