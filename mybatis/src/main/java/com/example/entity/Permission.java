package com.example.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class Permission implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long menuId;
	private String permissionName;
	private String authority;
	private String url;
	private String intro;
	private Date createTime;
	private Date updateTime;

}
