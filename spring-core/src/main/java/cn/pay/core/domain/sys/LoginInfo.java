package cn.pay.core.domain.sys;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.pay.core.domain.base.BaseDomain;
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
@Table(name = "login_info")
public class LoginInfo extends BaseDomain implements UserDetails {
	private static final long serialVersionUID = 1L;
	public static final int NORMAL = 0;
	public static final int DEL = -1;
	public static final int LOCK = 1;
	public static final int LOSER_MAX_COUNT = 5;
	public static final Integer USER = 0;
	public static final Integer MANAGER = 1;

	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "user_type")
	private Integer userType = LoginInfo.USER;
	@Column(name = "admin")
	private boolean admin;
	@Column(name = "status")
	private Integer status = LoginInfo.NORMAL;
	@Column(name = "loser_count")
	private Integer loserCount = 0;
	@Column(name = "lock_time")
	private Date lockTime;
	@ManyToMany
	private List<Role> roleList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}

	@Transient
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 判断账户是否过期
	 */
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 判断账户哦是否锁定
	 */
	@Transient
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 判断用户密码是否过期
	 */
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 判断用户是否禁用
	 */
	@Override
	@Transient
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
