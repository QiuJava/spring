package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.alibaba.fastjson.JSONObject;

import cn.qj.core.consts.BidConst;
import lombok.Data;

/**
 * 借款
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class Borrow implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private int version;
	/** 借款类型 */
	private int type;
	private int state;
	private BigDecimal amount;
	/** 借款利率 */
	private BigDecimal rate;
	/** 还款期限(月数) */
	private int monthReturn;
	/** 投标总数 */
	private int bidCount = 0;
	/** 产生总利息 */
	private BigDecimal totalInterestAmount;
	/** 当前投标总金额 */
	private BigDecimal currentSum = BidConst.ZERO;
	private String title;
	/** 借款描述 */
	private String description;
	/** 借款注释 */
	private String note;
	/** 投标截止日期 */
	private Date disableDate;
	/** 招标天数 */
	private int disableDays;
	/** 最小投标金额 */
	private BigDecimal minBidAmount;
	private Date applyTime;
	/** 发布时间 */
	private Date publishTime;
	/** 还款方式 */
	private int returnType;
	/** 借款人 */
	private LoginInfo createUser;
	private List<Bid> bidList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	@Version
	public int getVersion() {
		return version;
	}

	@OneToOne
	public LoginInfo getCreateUser() {
		return createUser;
	}

	@OneToMany(mappedBy = "borrow")
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
		return BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL.equals(returnType) ? "按月分期" : "到月分期";
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
