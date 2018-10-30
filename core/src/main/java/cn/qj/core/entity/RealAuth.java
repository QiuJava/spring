package cn.qj.core.entity;

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
public class RealAuth extends AuthComponent {
	private static final long serialVersionUID = 1L;
	public static final int MAN = 0;
	public static final int WOMAN = 1;

	private Long id;
	private String realname;
	private int sex;
	private String birthDate;
	/** 身份证号码 */
	private String idNumber;
	private String address;
	/** 身份证正面 */
	private String image1;
	/** 身份证反面 */
	private String image2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Override
	public int getState() {
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
		return RealAuth.MAN == sex ? "男" : "女";
	}

}
