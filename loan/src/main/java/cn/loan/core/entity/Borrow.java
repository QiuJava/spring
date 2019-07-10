package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.BorrowVo;
import cn.loan.core.util.BigDecimalUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 借款
 * 
 * @author Qiujian
 * 
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Borrow implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	private Integer version;
	private Integer borrowStatus;
	private BigDecimal borrowAmount;
	/** 借款利率 */
	private BigDecimal rate;
	/** 还款期限(月数) */
	private Integer repaymentMonths;
	/** 投标总数 */
	private Integer bidNum;
	/** 产生总利息 */
	private BigDecimal grossInterest;
	/** 当前投标总金额 */
	private BigDecimal bidTotal;
	private String title;
	/** 借款描述 */
	private String description;
	/** 投标截止日期 */
	private Date deadline;
	/** 招标天数 */
	private Integer bidDays;
	/** 最小投标金额 */
	private BigDecimal minBidAmount;
	@CreatedDate
	private Date applyTime;
	/** 发布时间 */
	private Date publishTime;
	/** 还款方式 */
	private Integer repaymentMethod;
	/** 借款人 */
	@OneToOne
	private LoginUser borrower;
	@OneToMany(mappedBy = "borrow")
	private List<Bid> bidList;

	@Transient
	private String borrowStatusDisplay;
	@Transient
	private String repaymentMethodDisplay;

	/** 已投金额占借款金额的百分比 */
	public BigDecimal getPersent() {
		BigDecimal multiply = bidTotal.divide(borrowAmount, BigDecimalUtil.CALC_SCALE, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimalUtil.HUNDRED);
		return multiply.setScale(BigDecimalUtil.SAVE_SCALE, BigDecimal.ROUND_HALF_UP);
	}

	/** 剩余可投金额 */
	public BigDecimal getRemainAmount() {
		return borrowAmount.subtract(bidTotal);
	}

	public String getJsonString() {
		BorrowVo vo = new BorrowVo();
		vo.setId(id);
		vo.setUsername(borrower.getUsername());
		vo.setTitle(title);
		vo.setBorrowAmount(borrowAmount);
		vo.setRate(rate);
		vo.setRepaymentMethodDisplay(repaymentMethodDisplay);
		vo.setRepaymentMonths(repaymentMonths);
		vo.setGrossInterest(grossInterest);
		return JSONObject.toJSONString(vo);
	}

}
