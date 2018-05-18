package cn.pay.core.domain.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
public class LoginInfo extends BaseDomain {
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
	@Column(name = "status")
	private Integer loserCount = 0;
	@Column(name = "status")
	private Date lockTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}
}
