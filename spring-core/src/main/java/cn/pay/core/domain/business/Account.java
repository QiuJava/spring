package cn.pay.core.domain.business;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import cn.pay.core.consts.BidConst;
import cn.pay.core.domain.base.BaseDomain;
import cn.pay.core.util.Md5;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 账户
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "account")
public class Account extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/** 乐观锁版本号 */
	@Version
	private Integer version;
	/** 交易密码 */
	@Column(name = "trade_password")
	private String tradePassword;
	/** 可用余额 */
	@Column(name = "usable_amount")
	private BigDecimal usableAmount = BidConst.ZERO;
	/** 冻结金额 */
	@Column(name = "freezed_amount")
	private BigDecimal freezedAmount = BidConst.ZERO;
	/** 待收本金 */
	@Column(name = "un_receive_principal")
	private BigDecimal unReceivePrincipal = BidConst.ZERO;
	/** 待收利息 */
	@Column(name = "un_receive_interest")
	private BigDecimal unReceiveInterest = BidConst.ZERO;
	/** 待还本息 */
	@Column(name = "un_return_amount")
	private BigDecimal unReturnAmount = BidConst.ZERO;
	/** 剩余授信额度 */
	@Column(name = "remain_borrow_limit")
	private BigDecimal remainBorrowLimit = BidConst.ZERO;
	/** 账户授信额度 */
	@Column(name = "borrow_limit")
	private BigDecimal borrowLimit = BidConst.INIT_BORROW_LIMIT;
	/** 数据库列校验key */
	private String verifyKey;

	@Id
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	@Column(name = "verify_key")
	public String getVerifyKey() {
		return Md5.encode(this.usableAmount.hashCode() + "" + this.freezedAmount.hashCode());
	}

	public boolean checkVerifyKey() {
		return Md5.encode(this.usableAmount.hashCode() + "" + this.freezedAmount.hashCode()).equals(this.verifyKey);
	}

	@Transient
	public BigDecimal getTotalAmount() {
		return usableAmount.add(freezedAmount).add(unReceivePrincipal);
	}

}
