package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import cn.qj.core.consts.StatusConst;
import lombok.Data;

/**
 * 投标
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Data
@Entity
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	/** 实际年利率 */
	private BigDecimal actualRate;
	private BigDecimal amount;
	private Borrow borrow;
	/** 借款标题 */
	private String borrowTitle;
	/** 投标人 投资人 */
	private LoginInfo createUser;
	private Date createTime;
	private int state = StatusConst.SUCCEED;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
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
