package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 还款计划
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
// @Table(name = "repayment_schedule")
public class RepaymentSchedule extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/** 正常待还 */
	public static final int NORMAL = 0;
	/** 已还 */
	public static final int PAYBACK = 1;
	/** 逾期 */
	public static final int OVERDUE = 2;

	/** 应还时间 */
	// @Column(name = "deadline")
	private Date deadline;
	/** 实际还款时间 */
	// @Column(name = "pay_date")
	private Date payDate;
	/** 还款总金额 */
	// @Column(name = "total_amount")
	private BigDecimal totalAmount;
	/** 还款本金 */
	// @Column(name = "principal")
	private BigDecimal principal;
	/** 还款利息 */
	// @Column(name = "interest")
	private BigDecimal interest;
	/** 第几期 */
	// @Column(name = "month_index")
	private Integer monthIndex;
	// @Column(name = "state")
	private Integer state = RepaymentSchedule.NORMAL;
	/** 借款类型 */
	// @Column(name = "borrow_type")
	private Integer borrowType;
	// @Column(name = "return_type")
	private Integer returnType;
	// @Column(name = "borrow_id")
	private Long borrowId;
	// @Column(name = "borrow_user_id")
	private Long borrowUserId;
	// @Column(name = "borrow_title")
	private String borrowTitle;
	/** 对应的收款的计划 */
	private List<PaymentPlan> paymentPlanList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "repayment_schedule_id")
	public List<PaymentPlan> getPaymentPlanList() {
		return paymentPlanList;
	}

	@Transient
	public String getStateDisplay() {
		switch (state) {
		case NORMAL:
			return "正常待还";
		case PAYBACK:
			return "已还";
		case OVERDUE:
			return "逾期";
		default:
			return "异常状态";
		}
	}
}
