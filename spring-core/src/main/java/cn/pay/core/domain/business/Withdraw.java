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

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.AuthComponent;
import cn.pay.core.domain.sys.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 提现
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class Withdraw extends AuthComponent {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 提现申请的银行卡账号 */
	private String accountNumber;
	/** 支行名称 */
	private String bankForkName;
	private String bankName;
	private String realName;
	private BigDecimal moneyAmount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public Integer getState() {
		return state;
	}

	public String getRemark() {
		return remark;
	}

	@OneToOne
	public LoginInfo getAuditor() {
		return auditor;
	}

	@OneToOne
	public LoginInfo getApplier() {
		return applier;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<String, Object>(8);
		json.put("id", getId());
		json.put("name", applier.getUsername());
		json.put("realName", realName);
		json.put("accountNumber", accountNumber);
		json.put("bankForkName", bankForkName);
		json.put("moneyAmount", moneyAmount);
		json.put("bankName", bankName);
		return JSONObject.toJSONString(json);
	}
}
