package cn.pay.core.domain.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.BaseAuthDomain;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.domain.sys.SystemDictionaryItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户风控材料
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@ToString
@Entity
// @Table(name = "user_file")
public class UserFile extends BaseAuthDomain {
	private static final long serialVersionUID = 1L;

	/** 认证分 */
	// @Column(name = "score")
	private Integer score = 0;
	/** 材料文件名 */
	// @Column(name = "file")
	private String file;
	private SystemDictionaryItem fileType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "file_type_id")
	public SystemDictionaryItem getFileType() {
		return fileType;
	}

	// @Column(name = "state")
	public Integer getState() {
		return state;
	}

	// @Column(name = "remark")
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
		Map<String, Object> json = new HashMap<String, Object>(5);
		json.put("id", getId());
		json.put("file", file);
		json.put("fileType", fileType.getTitle());
		json.put("name", applier.getUsername());
		return JSONObject.toJSONString(json);
	}
}
