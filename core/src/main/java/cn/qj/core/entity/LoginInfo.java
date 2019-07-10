package cn.qj.core.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 登录信息
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoginInfo implements UserDetails {
	private static final long serialVersionUID = 1L;

	public static final int NORMAL = 0;
	public static final int LOCK = 1;
	public static final int DEL = 2;
	public static final int USER_PLATFORM = 0;
	public static final int MANAGER = 1;
	public static final int ADMIN = 2;

	public static final int LOSER_MAX_COUNT = 5;
	public static final long LOCK_TIME = 1000 * 60;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private int userType;
	private int status;
	private int loserCount;
	private Date lockTime;
	@CreatedDate
	private Date createTime;
	@LastModifiedDate
	private Date updateTime;
	@CreatedBy
	private String createUser;
	@LastModifiedDate
	private String updateUser;
	@ManyToMany
	private List<Role> roles;

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles == null ? authorities : roles;
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
		return status != LOCK;
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
		return status == DEL;
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
