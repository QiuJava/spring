package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统账户
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class SystemAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	private Integer version;
	/** 系统账户可用余额 */
	private BigDecimal usableBalance;
	/** 系统账户冻结金额 */
	private BigDecimal freezedAmount;

}
