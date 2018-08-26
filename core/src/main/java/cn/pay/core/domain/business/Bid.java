package cn.pay.core.domain.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import cn.pay.core.domain.sys.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 投标类
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int SUCCEED = 1;
	public static final int FAILD = -1;

	private Long id;
	/** 实际年利率 */
	private BigDecimal actualRate;
	private BigDecimal amount;
	private Borrow borrow;
	/** 借款标题 */
	private String borrowTitle;
	/** 投标人 投资人 */
	private LoginInfo createUser;
	private Date createTime;
	private Integer state = Bid.SUCCEED;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@ManyToOne
	public Borrow getBorrow() {
		return borrow;
	}

	@OneToOne
	public LoginInfo getCreateUser() {
		return createUser;
	}
}
