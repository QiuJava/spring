package com.example.vo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.Employee;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工视图
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class EmployeeVo implements UserDetails {
	private static final long serialVersionUID = 1221985552224614692L;
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
	private Date newestLoginTime;
	private List<MenuTreeVo> menuTreeVoList;
	private List<PermissionVo> authorities;

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
