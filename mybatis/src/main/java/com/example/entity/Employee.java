package com.example.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 员工
 *
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class Employee implements UserDetails {
	private Long id;
	private String username;
	private String password;
	private String email;
	private String nickname;
	private Integer passwordErrors;
	private Integer status;
	private Integer superAdmin;
	private Integer employeeType;
	private String employeeNumber;
	private String intro;
	private Date lockTime;
	private Date createTime;
	private Date updateTime;
	private List<Role> roleList;
	private List<? extends GrantedAuthority> authorities;

	public static final int NORMAL_STATUS = 0;
	public static final int LOCK_STATUS = 1;
	public static final int INVALID_STATUS = 2;

	public static final int IS_ADMIN = 1;
	public static final int IS_NOT_ADMIN = 0;

	public static final int ADMIN_TYPE = 0;

	public static final int MAX_PASSWORD_ERRORS = 5;
	
	public static final int PASSWORD_ERRORS_INIT = 0;

	private static final long serialVersionUID = 1221985552224614692L;

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return status != INVALID_STATUS;
	}

	@Override
	public boolean isAccountNonLocked() {
		return status != LOCK_STATUS;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}