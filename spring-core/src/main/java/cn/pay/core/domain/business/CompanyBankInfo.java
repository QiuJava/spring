package cn.pay.core.domain.business;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 公司银行账号信息
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "company_bank_info")
public class CompanyBankInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	@Column(name = "bank_name")
	private String bankName;
	/** 账户名称 */
	@Column(name = "account_name")
	private String accountName;
	/** 银行卡号 */
	@Column(name = "bank_number")
	private String bankNumber;
	/** 开户支行名称 */
	@Column(name = "bank_fork_name")
	private String bankForkName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<String, Object>(6);
		json.put("id", getId());
		json.put("bankName", bankName);
		json.put("accountName", accountName);
		json.put("bankNumber", bankNumber);
		json.put("bankForkName", bankForkName);
		return JSONObject.toJSONString(json);
	}
}
