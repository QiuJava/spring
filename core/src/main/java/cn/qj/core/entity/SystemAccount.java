package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import cn.qj.core.consts.BidConst;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统账户
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
@ToString
@Entity
public class SystemAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Version
	private int version;
	private Date beginDate;
	private Date endDate;
	private Date createDate;
	/** 系统账户可用余额 */
	private BigDecimal totalBalance = BidConst.ZERO;
	/** 系统账户冻结金额 */
	private BigDecimal freezedAmount = BidConst.ZERO;

}
