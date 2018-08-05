package cn.pay.core.domain.sys;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统定时任务管理
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class SystemTimedTask implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int PAUSE = 1;
	public static final int NORMAL = 0;

	private Long id;
	private String jobName;
	/** 定时任务的组 */
	private String groupName;
	/** 计划任务表达式 */
	private String cronExpression;
	private String description;// 描述
	private Integer status;// 状态

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Transient
	public String getStatusDisplay() {
		switch (status) {
		case PAUSE:
			return "暂停";
		case NORMAL:
			return "开启";
		default:
			return "开启";
		}
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("jobName", jobName);
		json.put("groupName", groupName);
		json.put("cronExpression", cronExpression);
		json.put("description", description);
		return JSONObject.toJSONString(json);
	}
}