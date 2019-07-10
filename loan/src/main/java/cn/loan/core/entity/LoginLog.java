package cn.loan.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoginLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String ipAddress;
	@CreatedDate
	private Date loginTime;
	private Integer loginStatus;
	
	@Transient
	private String displayStatus;
}
