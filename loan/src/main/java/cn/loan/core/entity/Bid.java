package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 投标
 * 
 * @author Qiujian
 * 
 */
@Entity
@Getter
@Setter
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal returnRate;
	private BigDecimal bidAmount;
	@ManyToOne
	private Borrow borrow;
	private String borrowTitle;
	/** 投标人 投资人 */
	@OneToOne
	private LoginUser investor;
	private Date bidTime;
	private Integer bidStatus;
}
