package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cn.pay.core.domain.base.BaseDomain;
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
// @Table(name="system_account_flow")
public class SystemAccountFlow extends BaseDomain {
	private static final long serialVersionUID = 1L;

	// @Column(name = "create_date")
	private Date createDate;
	/** 系统账户流水类型 */
	// @Column(name = "account_action_type")
	private int accountActionType;
	/** 交易金额 */
	// @Column(name = "amount")
	private BigDecimal amount;
	/** 流水说明 */
	// @Column(name = "note")
	private String note;
	/** 账户余额 */
	// @Column(name = "balance")
	private BigDecimal balance;
	/** 冻结金额 */
	// @Column(name = "freezed_amount")
	private BigDecimal freezedAmount;
	/** 系统账户id */
	// @Column(name = "system_account_id")
	private Long systemAccountId;
	/** 对于用户id */
	// @Column(name = "target_user_id")
	private Long targetUserId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}
