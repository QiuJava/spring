package cn.qj.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.qj.entity.vo.MenuVo;
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
	private Date createTime;
	private Date updateTime;
	/**
	 * 密码过期时间为半年
	 */
	private Date passwordExpiration;
	/**
	 * 账户有效期为1年
	 */
	private Date accountExpiration;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Role> roles;

	@Transient
	private List<Authority> authorities = new ArrayList<>();

	@Transient
	private List<MenuVo> menus;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		for (Role role : roles) {
			authorities.addAll(role.getAuthorities());
		}
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
