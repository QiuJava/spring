package com.example.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.util.DateTimeUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

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
	private Integer id;
	private String employeeName;
	private String phoneNumber;
	private Integer age;
	@DateTimeFormat(pattern = DateTimeUtil.DATATIME_PATTERN)
	private Date entryTime;
	private Date resignationTime;
	private String gender;
	private String emailAddress;
	private Integer positionId;
	private String employeeStatus;
	private String profilePhotoLinkAddress;
	private Date createTime;
	private Date updateTime;
	private String idCardNo;
	private String bankCard;
	private Float remainingAnnualLeaveDay;
	private Float remainingLieuLeaveDay;
	private String password;
	private Integer passwordErrors;
	private String employeeType;
	private BigDecimal socialSecurityFundRatio;
	private String employeeDynamic;
	private Date lockingTime;
	
	private List<Permission> authorities;

	private List<MenuTree> menuTreeList;
	
	private String employeeStatusName;
	private String employeeTypeName;
	private String employeeDynamicName;
	private String genderName;
	private String departmentName;
	private String positionName;
	private String departmentId; 

	public static final String NORMAL_STATUS = "NORMAL_STATUS";
	public static final String LOCK_STATUS = "LOCK_STATUS";
	public static final String INVALID_STATUS = "INVALID_STATUS";
	
	public static final String SUPER_ADMIN_TYPE = "SUPER_ADMIN_TYPE";
	public static final String ADMIN_TYPE = "ADMIN_TYPE";
	
	public static final String ON_DUTY_DYNAMIC = "ON_DUTY_DYNAMIC";
	public static final String EGRESS_DYNAMIC = "EGRESS_DYNAMIC";
	public static final String MEETING_DYNAMIC = "MEETING_DYNAMIC";
	public static final String VACATION_DYNAMIC = "VACATION_DYNAMIC";

	public static final String MAN = "MAN";
	public static final String WOMAN = "WOMAN";
	
	public static final int MAX_PASSWORD_ERRORS = 5;
	public static final int PASSWORD_ERRORS_INIT = 0;
	public static final float ZERO_DAY_FLOAT = 0.0F;
	
	

	@Override
	public String getUsername() {
		return employeeName;
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
		return !Employee.INVALID_STATUS.equals(employeeStatus);
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