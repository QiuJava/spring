
package cn.pay.core.domain.sys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private Integer loginState;
	private String ip;
	private Date loginTime;
	private Integer userType;
	/** 创建时间 */
	private Date gmtCreate;
	/** 修改时间 */
	private Date gmtModified;

	public String getDisplayState() {
		return loginState.equals(IpLog.LOGIN_FAIL) ? "登陆失败" : "登陆成功";
	}
}
