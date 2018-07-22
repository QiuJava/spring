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
 * 账户流水
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
@Entity
public class AccountFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long accountId;
	private BigDecimal amount;
	/** 发生变化的时间 */
	private Date actionTime;
	private String remark;
	private BigDecimal freezed;
	/** 当前可用余额 */
	private BigDecimal balance;
	private Integer accountActionType;
	/** 操作信息 */
	private String note;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}
