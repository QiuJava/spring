package cn.pay.core.domain.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.AuthComponent;
import cn.pay.core.domain.sys.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 充值
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class Recharge extends AuthComponent {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 系统银行账户信息 */
	private CompanyBankInfo bankInfo;
	/** 交易号 */
	private String tradeCode;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tradeTime;
	private BigDecimal amount;
	/** 操作记录 */
	private String note;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@OneToOne
	public CompanyBankInfo getBankInfo() {
		return bankInfo;
	}
	
	@Override
	public Integer getState() {
		return state;
	}
	
	@Override
	public String getRemark() {
		return remark;
	}
	
	@Override
	@OneToOne
	public LoginInfo getAuditor() {
		return auditor;
	}
	
	@Override
	@OneToOne
	public LoginInfo getApplier() {
		return applier;
	}
	
	@Override
	public Date getApplyTime() {
		return applyTime;
	}
	
	@Override
	public Date getAuditTime() {
		return auditTime;
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<String, Object>(6);
		json.put("id", getId());
		json.put("name", getApplier().getUsername());
		json.put("tradeCode", tradeCode);
		json.put("amount", amount);
		json.put("tradeTime", tradeTime);
		return JSONObject.toJSONString(json);
	}
}
