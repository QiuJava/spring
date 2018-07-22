package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import cn.pay.core.domain.base.IdComponent;
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
// @Table(name = "system_account")
public class SystemAccount extends IdComponent {
	private static final long serialVersionUID = 1L;

	// @Version
	private Integer version;
	// @Column(name = "begin_date")
	private Date beginDate;
	// @Column(name = "end_date")
	private Date endDate;
	// @Column(name = "create_date")
	private Date createDate;
	/** 系统账户可用余额 */
	// @Column(name = "total_balance")
	private BigDecimal totalBalance;
	/** 系统账户冻结金额 */
	// @Column(name = "freezed_amount")
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
