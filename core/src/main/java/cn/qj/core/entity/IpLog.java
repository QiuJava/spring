
package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cn.qj.core.consts.StatusConst;
import lombok.Data;

/**
 * Ip日志
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class IpLog implements Serializable {
	private static final long serialVersionUID = 1L;

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
		return loginState == StatusConst.LOGIN_FAIL ? "登陆失败" : "登陆成功";
	}
}
