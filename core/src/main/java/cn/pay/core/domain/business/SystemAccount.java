package cn.pay.core.domain.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统账户
 * 
 * @author QIujian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class SystemAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer version;
	private Date beginDate;
	private Date endDate;
	private Date createDate;
	/** 系统账户可用余额 */
	private BigDecimal totalBalance;
	/** 系统账户冻结金额 */
	private BigDecimal freezedAmount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

}
