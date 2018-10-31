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
 * 订单
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Data
@Entity
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int IN_TRADING = 0;
	public static final int SUCCESS_TRADING = 1;
	public static final int Fail_TRADING = 2;

	public static final int TRANS_TYPE_WECHAT = 0;
	public static final int TRANS_TYPE_ALIPAY = 1;
	public static final int TRANS_TYPE_FASTPAY = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private BigDecimal amount;
	private String transChannel;
	private int transStatus;
	private Date createTime;

}
