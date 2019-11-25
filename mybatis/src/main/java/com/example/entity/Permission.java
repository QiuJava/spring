package com.example.entity;

import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

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
public class Permission implements GrantedAuthority {

	private static final long serialVersionUID = -8093648188829987622L;
	private Long id;
	private Long menuId;
	private String permissionName;
	private String authority;
	private String url;
	private String intro;
	private Date createTime;
	private Date updateTime;

}
