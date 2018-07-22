
package cn.pay.core.domain.sys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用戶Ip日志
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@Entity
public class IpLog implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAIL = 0;

	private Long id;
	private String username;
	private Integer loginState = IpLog.LOGIN_FAIL;
	private String ip;
	private Date loginTime;
	private Integer userType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Transient
	public String getDisplayState() {
		return loginState == IpLog.LOGIN_FAIL ? "登陆失败" : "登陆成功";
	}
}
