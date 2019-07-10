package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * 还款计划
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class RepaymentPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 应还时间 */
	private Date returnTime;
	/** 实际还款时间 */
	private Date actualReturnTime;
	/** 还款总金额 */
	private BigDecimal totalAmount;
	/** 还款本金 */
	private BigDecimal principal;
	/** 还款利息 */
	private BigDecimal interest;
	/** 第几期 */
	private Integer monthIndex;
	private Integer status;
	private Integer repaymentMethod;
	private Long borrowId;
	private Long borrowerId;
	private String title;
	/** 对应的收款的计划 */
	@OneToMany(mappedBy = "repaymentPlan")
	private List<ReceiptPlan> receiptPlans;

}
