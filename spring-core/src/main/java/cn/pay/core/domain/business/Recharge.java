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
// @Table(name = "recharge")
public class Recharge extends AuthComponent {
	private static final long serialVersionUID = 1L;

	/** 系统银行账户信息 */
	private CompanyBankInfo bankInfo;
	/** 交易号 */
	// @Column(name = "trade_code")
	private String tradeCode;
	// @Column(name = "trade_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tradeTime;
	// @Column(name = "amount")
	private BigDecimal amount;
	/** 操作记录 */
	// @Column(name = "note")
	private String note;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	// @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	// @JoinColumn(name = "bank_info_id")
	@OneToOne
	public CompanyBankInfo getBankInfo() {
		return bankInfo;
	}

	// @Column(name = "state")
	public Integer getState() {
		return state;
	}

	// @Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	// @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	// @JoinColumn(name = "auditor_id")
	@OneToOne
	public LoginInfo getAuditor() {
		return auditor;
	}

	// @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	// @JoinColumn(name = "applier_id")
	@OneToOne
	public LoginInfo getApplier() {
		return applier;
	}

	// @Column(name = "apply_time")
	public Date getApplyTime() {
		return applyTime;
	}

	// @Column(name = "audit_time")
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
