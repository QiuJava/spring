package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.pay.core.domain.base.BaseDomain;
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
@Table(name = "account_flow")
public class AccountFlow extends BaseDomain {
	private static final long serialVersionUID = 1L;

	@Column(name = "account_id")
	private Long accountId;
	@Column(name = "amount")
	private BigDecimal amount;
	/** 发生变化的时间 */
	@Column(name = "account_time")
	private Date actionTime;
	@Column(name = "remark")
	private String remark;
	@Column(name = "freezed")
	private BigDecimal freezed;
	/** 当前可用余额 */
	@Column(name = "balance")
	private BigDecimal balance;
	@Column(name = "account_action_type")
	private Integer accountActionType;
	/** 操作信息 */
	@Column(name = "note")
	private String note;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}
