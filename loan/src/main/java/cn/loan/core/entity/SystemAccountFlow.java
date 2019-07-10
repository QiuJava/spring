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
 * 系统账户流水
 * 
 * @author Qiujian
 * 
 */
@Getter
@Setter
@Entity
public class SystemAccountFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date createDate;
	/** 系统账户流水类型 */
	private Integer actionType;
	/** 交易金额 */
	private BigDecimal actionAmount;
	/** 账户余额 */
	private BigDecimal usableBalance;
	/** 冻结金额 */
	private BigDecimal freezedAmount;
	/** 系统账户id */
	private Long systemAccountId;
	/** 对于用户id */
	private Long targetUserId;

}
