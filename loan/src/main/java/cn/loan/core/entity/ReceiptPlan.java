package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 收款计划
 * 
 * @author Qiujian
 * 
 */
@Getter
@Setter
@Entity
public class ReceiptPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private Date receiptTime;
	private Long borrowId;
	/** 实际收款时间 */
	private Date actualReceiptTime;
	private Integer repaymentMethod;
	private Long returnUserId;
	/** 收款人 */
	private Long receiverId;
	@ManyToOne
	private RepaymentPlan repaymentPlan;
}
