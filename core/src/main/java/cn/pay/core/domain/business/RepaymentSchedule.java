package cn.pay.core.domain.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
public class RepaymentSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 正常待还 */
	public static final int NORMAL = 0;
	/** 已还 */
	public static final int PAYBACK = 1;
	/** 逾期 */
	public static final int OVERDUE = 2;

	/** 应还时间 */
	private Date deadline;
	/** 实际还款时间 */
	private Date payDate;
	/** 还款总金额 */
	private BigDecimal totalAmount;
	/** 还款本金 */
	private BigDecimal principal;
	/** 还款利息 */
	private BigDecimal interest;
	/** 第几期 */
	private Integer monthIndex;
	private Integer state = RepaymentSchedule.NORMAL;
	/** 借款类型 */
	private Integer borrowType;
	private Integer returnType;
	private Long borrowId;
	private Long borrowUserId;
	private String borrowTitle;
	/** 对应的收款的计划 */
	private List<PaymentPlan> paymentPlanList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@OneToMany(mappedBy = "repaymentSchedule")
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
