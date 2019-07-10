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
 * 用户材料
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
@ToString
@Entity
public class UserFile extends AuthComponent {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 认证分 */
	private Integer score = 0;
	/** 材料文件名 */
	private String file;
	private SystemDictionaryItem fileType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@OneToOne
	public SystemDictionaryItem getFileType() {
		return fileType;
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
		Map<String, Object> json = new HashMap<String, Object>(5);
		json.put("id", getId());
		json.put("file", file);
		json.put("fileType", fileType.getItemName());
		json.put("name", applier.getUsername());
		return JSONObject.toJSONString(json);
	}
}
