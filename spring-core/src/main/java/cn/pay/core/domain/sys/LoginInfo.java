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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private Integer userType;
	private boolean admin;
	private Integer status;
	private Integer loserCount;
	private Date lockTime;
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
		return !status.equals(LoginInfo.LOCK);
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
		return !status.equals(LoginInfo.DEL);
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
