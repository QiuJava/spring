package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cn.qj.core.util.HttpSessionUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮箱验证
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@NoArgsConstructor
@Entity
public class EmailVerify implements Serializable {
	private static final long serialVersionUID = 1L;

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
		this.userId = HttpSessionUtil.getCurrentLoginInfo().getId();
		this.verify = UUID.randomUUID().toString();
		this.verifyDate = new Date();
	}

}
