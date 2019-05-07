package cn.qj.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志
 * 
 * @author Qiujian
 * @date 2019年4月12日
 *
 */
@Entity
@Getter
@Setter
public class LoginLog implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int SUCCESS = 1;
	public static final int BAD_CREDENTIALS = 2;
	public static final int DISABLED = 3;
	public static final int LOCKED = 4;
	public static final int CREDENTIALS_EXPIRED = 5;
	public static final int ACCOUNT_EXPIRED = 6;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private Integer loginStatus;
	private String loginIp;
	private String loginMsg;

}
