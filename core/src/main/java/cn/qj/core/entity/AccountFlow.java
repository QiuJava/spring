package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 账户流水
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Setter
@Getter
@ToString
@Entity
public class AccountFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long accountId;
	private BigDecimal amount;
	/** 发生变化的时间 */
	private Date actionTime;
	private String remark;
	private BigDecimal freezed;
	/** 当前可用余额 */
	private BigDecimal balance;
	private Integer accountActionType;
	/** 操作信息 */
	private String note;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
}
