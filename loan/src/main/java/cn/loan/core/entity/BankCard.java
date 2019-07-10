package cn.loan.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行卡
 * 
 * @author Qiujian
 * 
 */
@Getter
@Setter
@Entity
public class BankCard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String bankForkName;
	private String bankCode;
	private String cardNumber;
	private Long loginUserId;
	private String accountName;

}
