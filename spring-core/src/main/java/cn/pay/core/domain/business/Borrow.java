package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.consts.BidConst;
import cn.pay.core.domain.base.BaseDomain;
import cn.pay.core.domain.sys.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 借款
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "borrow")
public class Borrow extends BaseDomain {
	private static final long serialVersionUID = 1L;

	@Version
	private Integer version;
	/** 借款类型 */
	@Column(name = "type")
	private Integer type;
	@Column(name = "state")
	private Integer state;
	@Column(name = "amount")
	private BigDecimal amount;
	/** 借款利率 */
	@Column(name = "rate")
	private BigDecimal rate;
	/** 还款期限(月数) */
	@Column(name = "month_return")
	private Integer monthReturn;
	/** 投标总数 */
	@Column(name = "bid_count")
	private Integer bidCount = 0;
	/** 产生总利息 */
	@Column(name = "total_interest_amount")
	private BigDecimal totalInterestAmount;
	/** 当前投标总金额 */
	@Column(name = "current_sum")
	private BigDecimal currentSum = BidConst.ZERO;
	@Column(name = "title")
	private String title;
	/** 借款描述 */
	@Column(name = "description")
	private String description;
	/** 借款注释 */
	@Column(name = "note")
	private String note;
	/** 投标截止日期 */
	@Column(name = "disable_date")
	private Date disableDate;
	/** 招标天数 */
	@Column(name = "disable_days")
	private Integer disableDays;
	/** 最小投标金额 */
	@Column(name = "min_bid_amount")
	private BigDecimal minBidAmount;
	@Column(name = "apply_time")
	private Date applyTime;
	/** 发布时间 */
	@Column(name = "publish_time")
	private Date publishTime;
	/** 还款方式 */
	@Column(name = "return_type")
	private Integer returnType;
	/** 借款人 */
	private LoginInfo createUser;
	private List<Bid> bidList = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "create_user_id")
	public LoginInfo getCreateUser() {
		return createUser;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "borrow_id")
	public List<Bid> getBidList() {
		return bidList;
	}

	/** 已投金额占借款金额的百分比 */
	@Transient
	public BigDecimal getPersent() {
		return currentSum.divide(amount, BidConst.DISP_SCALE).multiply(new BigDecimal("100"));
	}

	/** 剩余可投金额 */
	@Transient
	public BigDecimal getRemainAmount() {
		return amount.subtract(currentSum);
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<String, Object>(10);
		json.put("id", id);
		json.put("username", createUser.getUsername());
		json.put("title", title);
		json.put("amount", amount);
		json.put("rate", rate);
		json.put("monthReturn", monthReturn);
		json.put("returnType", returnType);
		json.put("monthReturn", monthReturn);
		json.put("totalInterestAmount", totalInterestAmount);
		return JSONObject.toJSONString(json);
	}

	/** 还款方式 */
	@Transient
	public String getReturnTypeDisplay() {
		return returnType == BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL ? "按月分期" : "到月分期";
	}

	@Transient
	public String getBorrowStateDisplay() {
		switch (state) {
		case BidConst.BORROW_STATE_PUBLISH_PENDING:
			return "待发布";
		case BidConst.BORROW_STATE_BIDDING:
			return "招标中";
		case BidConst.BORROW_STATE_UNDO:
			return "已撤销";
		case BidConst.BORROW_STATE_BIDDING_OVERDUE:
			return "流标";
		case BidConst.BORROW_STATE_APPROVE_PENDING_1:
			return "满标1审";
		case BidConst.BORROW_STATE_APPROVE_PENDING_2:
			return "满标2审";
		case BidConst.BORROW_STATE_REJECTED:
			return "满标审核被拒绝";
		case BidConst.BORROW_STATE_PAYING_BACK:
			return "还款中";
		case BidConst.BORROW_STATE_COMPLETE_PAY_BACK:
			return "已还清";
		case BidConst.BORROW_STATE_PAY_BACK_OVERDUE:
			return "逾期";
		case BidConst.BORROW_STATE_PUBLISH_REFUSE:
			return "初审拒绝状态";
		default:
			return "异常状态";
		}
	}
}
