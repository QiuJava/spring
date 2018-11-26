package cn.qj.core.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.qj.core.consts.StatusConst;
import lombok.Data;

/**
 * 登录信息
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class LoginInfo implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private Integer userType;
	private Boolean isAdmin;
	private Integer status =StatusConst.NORMAL;
	private Integer loserCount = 0;
	private Date lockTime;
	/** 创建时间 */
	private Date gmtCreate;
	/** 修改时间 */
	private Date gmtModified;
	@ManyToMany
	private List<Role> roles;

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * 账户不过期
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 判断账户是不锁定
	 */
	@Override
	public boolean isAccountNonLocked() {
		return status != StatusConst.LOCK;
	}

	/**
	 * 判断用户密码是不过期
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 判断用户是可用
	 */
	@Override
	public boolean isEnabled() {
		return status == StatusConst.DEL;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
