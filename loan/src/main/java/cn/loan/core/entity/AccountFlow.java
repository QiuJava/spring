package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 账户流水
 * 
 * @author Qiujian
 * 
 */
@Getter
@Setter
@Entity
public class AccountFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long accountId;
	private BigDecimal actionAmount;
	private Date actionTime;
	private String remark;
	private BigDecimal freezedAmount;
	private BigDecimal usableBalance;
	private Integer actionType;

}
