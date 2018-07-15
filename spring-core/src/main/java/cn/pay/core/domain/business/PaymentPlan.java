package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 收款计划
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
// @Table(name = "payment_plan")
public class PaymentPlan extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/** 这个收款计划对应的投标金额 */
	// @Column(name = "bid_amount")
	private BigDecimal bidAmount;
	// @Column(name = "bid_id")
	private Long bidId;
	/** 收款的总金额 */
	// @Column(name = "total_amount")
	private BigDecimal totalAmount;
	/** 收款的本金 */
	// @Column(name = "principal")
	private BigDecimal principal;
	/** 收款的利息 */
	// @Column(name = "interest")
	private BigDecimal interest;
	/** 第几期 */
	// @Column(name = "month_index")
	private Integer monthIndex;
	/** 收款结束时间 */
	// @Column(name = "deadline")
	private Date deadline;
	// @Column(name = "borrow_id")
	private Long borrowId;
	/** 实际收款时间 */
	// @Column(name = "pay_date")
	private Date payDate;
	// @Column(name = "return_type")
	private Integer returnType;
	// @Column(name = "return_login_info_id")
	private Long returnLoginInfoId;
	/** 收款人 */
	@Column(name = "collect_login_info_id")
	private Long collectLoginInfoId;
	/** 对应还款计划的id */
	private RepaymentSchedule repaymentSchedule;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@JoinColumn(name = "repayment_schedule_id")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	public RepaymentSchedule getRepaymentSchedule() {
		return repaymentSchedule;
	}
}
