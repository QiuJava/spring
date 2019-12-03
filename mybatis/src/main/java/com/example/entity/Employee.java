package com.example.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
	private static final long serialVersionUID = -8429721708182193581L;
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

	private List<Permission> authorities;

	private List<MenuTree> menuTreeList;

	public static final int NORMAL_STATUS = 0;
	public static final int LOCK_STATUS = 1;
	public static final int INVALID_STATUS = 2;

	public static final int IS_ADMIN = 1;
	public static final int IS_NOT_ADMIN = 0;

	public static final int ADMIN_TYPE = 0;

	public static final int MAX_PASSWORD_ERRORS = 5;

	public static final int PASSWORD_ERRORS_INIT = 0;

	public static final String INIT_PASSWORD_SUFFIX = "a123";
	public static final String INIT_EMPLOYEE_NUMBER = "000";

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
		return status != Employee.INVALID_STATUS;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
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