package cn.pay.core.domain.sys;

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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 登陆信息
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@ToString
@Entity
public class LoginInfo implements UserDetails {
	private static final long serialVersionUID = 1L;
	public static final int NORMAL = 0;
	public static final int DEL = -1;
	public static final int LOCK = 1;
	public static final int LOSER_MAX_COUNT = 5;
	public static final Integer USER = 0;
	public static final Integer MANAGER = 1;

	private Long id;
	private String username;
	private String password;
	private Integer userType = LoginInfo.USER;
	private boolean admin;
	private Integer status = LoginInfo.NORMAL;
	private Integer loserCount = 0;
	private Date lockTime;
	private List<Role> roles;

	private Collection<? extends GrantedAuthority> authorities;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@ManyToMany
	public List<Role> getRoles() {
		return roles;
	}

	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * 判断账户是不过期
	 */
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 判断账户是不锁定
	 */
	@Transient
	public boolean isAccountNonLocked() {
		return this.status != LoginInfo.LOCK;
	}

	/**
	 * 判断用户密码是不过期
	 */
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 判断用户是可用
	 */
	@Transient
	public boolean isEnabled() {
		return this.status != LoginInfo.DEL;
	}

}
