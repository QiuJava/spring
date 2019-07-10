package cn.loan.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.RechargeVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 充值
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class Recharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private CompanyBankCard companyBankCard;
	/** 交易流水号 */
	private String serialNumber;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tradeTime;
	private BigDecimal amount;
	@OneToOne
	private LoginUser auditor;
	private Integer auditStatus;
	private String remark;
	@OneToOne
	private LoginUser submitter;
	private Date auditTime;
	private Date submissionTime;
	@Transient
	private String auditStatusDisplay;

	public String getJsonString() {
		RechargeVo vo = new RechargeVo();
		vo.setId(id);
		vo.setAmount(amount);
		vo.setSerialNumber(serialNumber);
		vo.setTradeTime(tradeTime);
		vo.setUsername(submitter.getUsername());
		return JSONObject.toJSONString(vo);
	}
}
