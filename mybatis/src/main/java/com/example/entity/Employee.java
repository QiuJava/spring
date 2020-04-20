package com.example.entity;

import java.util.Date;
import java.util.List;

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
	private Integer passwordErrors;
	private String status;
	private Date lockTime;
	private Date createTime;
	private Date updateTime;
	private String employeeType;

	private List<Permission> authorities;

	private List<MenuTree> menuTreeList;

	private Date newestLoginTime;

	public static final String NORMAL_STATUS = "NORMAL_STATUS";
	public static final String LOCK_STATUS = "LOCK_STATUS";
	public static final String INVALID_STATUS = "INVALID_STATUS";
	
	public static final String SUPER_ADMIN_TYPE = "SUPER_ADMIN_TYPE";

	public static final String ADMIN = "ADMIN";
	
	public static final int MAX_PASSWORD_ERRORS = 5;
	public static final int PASSWORD_ERRORS_INIT = 0;
	
	

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public List<Permission> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !Employee.INVALID_STATUS.equals(status);
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