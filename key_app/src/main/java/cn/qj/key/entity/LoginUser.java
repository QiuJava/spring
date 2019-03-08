package cn.qj.key.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录用户
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Getter
@Setter
@Entity
public class LoginUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -854783099698938267L;

	/**
	 * 1：web 2：手机app 3：微信
	 */
	public static final int WEB_SOURCE = 1;
	public static final int APP_SOURCE = 2;
	public static final int WX_SOURCE = 3;

	/**
	 * 1：普通用户 2：员工 3：管理者
	 */
	public static final int USER_TYPE = 1;
	public static final int EMPLOYEE_TYPE = 2;
	public static final int MANAGE_TYPE = 3;

	/**
	 * 1：正常2：锁定 3：删除
	 */
	public static final int NORMAL_STATUS = 1;
	public static final int LOCK_STATUS = 2;
	public static final int DEL_STATUS = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 注册来源
	 */
	private int registerSource;

	/**
	 * 手机号码
	 */
	private String phoneNum;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 用户类型
	 */
	private int userType;

	/**
	 * 用户状态
	 */
	private int userStatus;

	/**
	 * 锁定时间
	 */
	private Date lockTime;

	/**
	 * 密码错误次数
	 */
	private int passwordErrorCount;
	
	/**
	 * 手机验证码
	 */
	@Transient
	private String authCode;
}
