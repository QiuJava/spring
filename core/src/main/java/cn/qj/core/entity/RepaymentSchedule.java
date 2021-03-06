package cn.qj.core.entity;

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

import lombok.Data;

/**
 * 还款计划
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class RepaymentSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 正常待还 */
	public static final int NORMAL = 0;
	/** 已还 */
	public static final int PAYBACK = 1;
	/** 逾期 */
	public static final int OVERDUE = 2;

	private long id;

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
	private int monthIndex;
	private int state;
	/** 借款类型 */
	private int borrowType;
	private int returnType;
	private long borrowId;
	private long borrowUserId;
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
