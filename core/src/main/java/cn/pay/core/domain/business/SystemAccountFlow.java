package cn.pay.core.domain.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统账户流水
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class SystemAccountFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date createDate;
	/** 系统账户流水类型 */
	private int accountActionType;
	/** 交易金额 */
	private BigDecimal amount;
	/** 流水说明 */
	private String note;
	/** 账户余额 */
	private BigDecimal balance;
	/** 冻结金额 */
	private BigDecimal freezedAmount;
	/** 系统账户id */
	private Long systemAccountId;
	/** 对于用户id */
	private Long targetUserId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}
