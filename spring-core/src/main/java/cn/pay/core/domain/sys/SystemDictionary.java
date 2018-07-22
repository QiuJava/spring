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
 * 字典
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@ToString
@Entity
public class SystemDictionary implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 字典编码 */
	private String sn;
	private String title;
	/** 字典介绍 */
	private String intro;
	private Integer sequence;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", this.getId());
		json.put("sn", sn);
		json.put("title", title);
		json.put("intro", intro);
		return JSONObject.toJSONString(json);
	}
}
