
package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ip日志
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class IpLog implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_FAIL = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private Integer loginState;
	private String ip;
	@CreatedDate
	private Date loginTime;
	private Integer userType;

	public String getDisplayState() {
		return loginState == LOGIN_FAIL ? "登陆失败" : "登陆成功";
	}
}
