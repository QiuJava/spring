package cn.pay.core.domain.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.BaseAuthDomain;
import cn.pay.core.domain.sys.LoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 实名认证
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "real_auth")
public class RealAuth extends BaseAuthDomain {
	private static final long serialVersionUID = 1L;
	public static final Integer MAN = 0;
	public static final Integer WOMAN = 1;
	
	@Column(name = "real_name")
	private String realname;
	@Column(name = "sex")
	private Integer sex;
	@Column(name = "brith_date")
	private String birthDate;
	/** 身份证号码 */
	@Column(name = "id_number")
	private String idNumber;
	@Column(name = "address")
	private String address;
	/** 身份证正面 */
	@Column(name = "image1")
	private String image1;
	/** 身份证反面 */
	@Column(name = "image2")
	private String image2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "auditor_id")
	public LoginInfo getAuditor() {
		return auditor;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "applier_id")
	public LoginInfo getApplier() {
		return applier;
	}

	@Column(name = "apply_time")
	public Date getApplyTime() {
		return applyTime;
	}

	@Column(name = "audit_time")
	public Date getAuditTime() {
		return auditTime;
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> map = new HashMap<String, Object>(10);
		map.put("id", getId());
		map.put("username", applier.getUsername());
		map.put("realname", realname);
		map.put("idNumber", idNumber);
		map.put("sex", getSexDisplay());
		map.put("birthDate", birthDate);
		map.put("address", address);
		map.put("image1", image1);
		map.put("image2", image2);
		return JSONObject.toJSONString(map);
	}

	@Transient
	public String getSexDisplay() {
		return sex == RealAuth.MAN ? "男" : "女";
	}

}
