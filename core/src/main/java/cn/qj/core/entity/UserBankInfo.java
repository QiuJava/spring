package cn.qj.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 用户银行卡信息
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class UserBankInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String bankForkName;
	private String bankName;
	private String accountNumber;
	private Long loginInfoId;
	private String accountName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}
