package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import cn.pay.core.domain.base.IdComponent;
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
// @Table(name = "bid")
public class Bid extends IdComponent {
	private static final long serialVersionUID = 1L;

	public static final int SUCCEED = 1;
	public static final int FAILD = -1;

	/** 实际年利率 */
	// @Column(name = "actual_rate")
	private BigDecimal actualRate;
	// @Column(name = "amount")
	private BigDecimal amount;
	private Borrow borrow;
	/** 借款标题 */
	// @Column(name = "borrow_title")
	private String borrowTitle;
	/** 投标人 投资人 */
	private LoginInfo createUser;
	// @Column(name = "create_time")
	private Date createTime;
	// @Column(name = "state")
	private Integer state = Bid.SUCCEED;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	// @JoinColumn(name = "borrow_id")
	// @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@ManyToOne
	public Borrow getBorrow() {
		return borrow;
	}

	// @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	// @JoinColumn(name = "create_user_id")
	@OneToOne
	public LoginInfo getCreateUser() {
		return createUser;
	}
}
