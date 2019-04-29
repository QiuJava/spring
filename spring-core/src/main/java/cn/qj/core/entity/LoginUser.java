package cn.qj.core.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录用户
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Entity
@Setter
@Getter
public class LoginUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	public static final Integer NORMAL = 1;
	public static final Integer LOCK = 2;
	public static final Integer NOT_ENABLED = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private Integer userStatus;
	/**
	 * 密码过期时间为半年
	 */
	private Date passwordExpiration;
	/**
	 * 账户有效期为1年
	 */
	private Date accountExpiration;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Authority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * 账户未过期
	 */
	@Override
	public boolean isAccountNonExpired() {
		return accountExpiration.getTime() > System.currentTimeMillis();
	}

	/**
	 * 账户未锁定
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !LOCK.equals(userStatus);
	}

	/**
	 * 密码未过期
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return passwordExpiration.getTime() > System.currentTimeMillis();
	}

	@Override
	public boolean isEnabled() {
		return !NOT_ENABLED.equals(userStatus);
	}

}
