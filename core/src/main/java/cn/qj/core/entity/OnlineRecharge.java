package cn.qj.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 线上充值
 * 
 * @author Qiujian
 * @date 2018/11/05
 */
@Data
@Entity
public class OnlineRecharge implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String WECHAT_PAY_CHANNEL = "wx";
	public static final String ALIPAY_CHANNEL = "zfb";
	public static final String UNIONPAY_FAST_CHANNEL = "kj";

	public static final int TRANS_IN = 0;
	public static final int TRANS_SUCCESS = 1;
	public static final int TRANS_FAILURE = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 渠道 */
	private String channel;
	/** 交易状态 */
	private Integer transStatus;
	private Date createTime;
	private BigDecimal amount;

}
