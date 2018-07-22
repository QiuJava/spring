package cn.pay.core.domain.business;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import cn.pay.core.consts.BidConst;
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
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 乐观锁版本号 */
	private Integer version;
	/** 交易密码 */
	private String tradePassword;
	/** 可用余额 */
	private BigDecimal usableAmount = BidConst.ZERO;
	/** 冻结金额 */
	private BigDecimal freezedAmount = BidConst.ZERO;
	/** 待收本金 */
	private BigDecimal unReceivePrincipal = BidConst.ZERO;
	/** 待收利息 */
	private BigDecimal unReceiveInterest = BidConst.ZERO;
	/** 待还本息 */
	private BigDecimal unReturnAmount = BidConst.ZERO;
	/** 剩余授信额度 */
	// @Column(name = "remain_borrow_limit")
	private BigDecimal remainBorrowLimit = BidConst.ZERO;
	/** 账户授信额度 */
	private BigDecimal borrowLimit = BidConst.INIT_BORROW_LIMIT;
	/** 数据库列校验key */
	private String verifyKey;

	@Id
	public Long getId() {
		return id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

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
