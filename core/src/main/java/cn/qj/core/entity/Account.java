package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import cn.qj.core.consts.BidConst;
import cn.qj.core.consts.SysConst;
import lombok.Data;

/**
 * 账户
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Data
@Entity
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	/** 乐观锁版本号 */
	private int version;
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
	private BigDecimal remainBorrowLimit = BidConst.ZERO;
	/** 账户授信额度 */
	private BigDecimal borrowLimit = BidConst.INIT_BORROW_LIMIT;
	/** 数据库列校验key */
	private String verifyKey;

	@Id
	public long getId() {
		return id;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public String getVerifyKey() {
		String rawPass = this.usableAmount.hashCode() + "" + this.freezedAmount.hashCode();
		String encodePassword = SysConst.MD5.encodePassword(rawPass, null);
		return encodePassword;
	}

	@Transient
	public boolean checkVerifyKey() {
		String rawPass = this.usableAmount.hashCode() + "" + this.freezedAmount.hashCode();
		String encodePassword = SysConst.MD5.encodePassword(rawPass, null);
		return encodePassword.equals(this.verifyKey);
	}

	@Transient
	public BigDecimal getTotalAmount() {
		return usableAmount.add(freezedAmount).add(unReceivePrincipal);
	}

}
