package cn.pay.core.domain.business;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cn.pay.core.util.HttpSessionContext;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 绑定邮箱的验证相关内容
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
public class EmailVerify implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 邮箱验证码有效时间 */
	public final static int VALIDITY_DAY = 5;

	private Long id;
	private String verify;
	private String email;
	private Long userId;
	/** 校验时间 */
	private Date verifyDate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public EmailVerify(String email) {
		this.email = email;
		this.userId = HttpSessionContext.getCurrentLoginInfo().getId();
		this.verify = UUID.randomUUID().toString();
		this.verifyDate = new Date();
	}

}
