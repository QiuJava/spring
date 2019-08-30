package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import cn.loan.core.util.EncryptUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 账户
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	/** 乐观锁版本号 */
	@Version
	private Integer version;
	/** 可用余额 */
	private BigDecimal usableBalance;
	/** 冻结金额 */
	private BigDecimal freezedAmount;
	/** 待收本金 */
	private BigDecimal unReceivePrincipal;
	/** 待收利息 */
	private BigDecimal unReceiveInterest;
	/** 待还本息 */
	private BigDecimal unReturnAmount;
	/** 剩余授信额度 */
	private BigDecimal remainBorrowLimit;
	/** 账户授信额度 */
	private BigDecimal borrowLimit;
	/** 数据库列校验key */
	private String verifyKey;

	public String getVerifyKey() {
		return EncryptUtil.md5Encrypt(id.toString(), String.valueOf(usableBalance.hashCode()),
				String.valueOf(freezedAmount.hashCode()));
	}

	public boolean checkVerifyKey() {
		return EncryptUtil.md5Encrypt(id.toString(), String.valueOf(usableBalance.hashCode()),
				String.valueOf(freezedAmount.hashCode())).equals(this.verifyKey);
	}

	public BigDecimal getTotalAmount() {
		return usableBalance.add(freezedAmount);
	}

}
