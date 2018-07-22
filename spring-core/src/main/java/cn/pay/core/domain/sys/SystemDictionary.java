package cn.pay.core.domain.sys;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.IdComponent;
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
// @Table(name = "system_dictionary")
public class SystemDictionary extends IdComponent {
	private static final long serialVersionUID = 1L;

	/** 字典编码 */
	// @Column(name = "sn", nullable = false)
	private String sn;
	// @Column(name = "title", nullable = false)
	private String title;
	/** 字典介绍 */
	// @Column(name = "intro", nullable = false)
	private String intro;
	// @Column(name = "sequence", nullable = false)
	private Integer sequence;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
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
