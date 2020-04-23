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

	private Integer id;
	private String permissionName;
	private String authority;
	private String mappingAddress;
	private Integer menuId;
	private Date createTime;
	private Date updateTime;

	private static final long serialVersionUID = -8093648188829987622L;

	@Override
	public String getAuthority() {
		return authority;
	}

}
