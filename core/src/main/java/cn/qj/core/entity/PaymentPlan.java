package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * 收款计划
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class PaymentPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 这个收款计划对应的投标金额 */
	private BigDecimal bidAmount;
	private Long bidId;
	/** 收款的总金额 */
	private BigDecimal totalAmount;
	/** 收款的本金 */
	private BigDecimal principal;
	/** 收款的利息 */
	private BigDecimal interest;
	/** 第几期 */
	private Integer monthIndex;
	/** 收款结束时间 */
	private Date deadline;
	private Long borrowId;
	/** 实际收款时间 */
	private Date payDate;
	private Integer returnType;
	private Long returnLoginInfoId;
	/** 收款人 */
	private Long collectLoginInfoId;
	/** 对应还款计划的id */
	private RepaymentSchedule repaymentSchedule;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@ManyToOne
	public RepaymentSchedule getRepaymentSchedule() {
		return repaymentSchedule;
	}
}
