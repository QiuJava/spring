package cn.pay.core.entity.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典
 * 
 * @author Administrator
 *
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class SystemDictionary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 字典编码 */
	private String sn;
	private String title;
	/** 字典介绍 */
	private String intro;
	private Date gmtCreate;
	private Date gmtModified;
	@OneToMany(mappedBy = "systemDictionary", cascade = { CascadeType.ALL })
	private List<SystemDictionaryItem> systemDictionaryItems;

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>(4);
		json.put("id", this.getId());
		json.put("sn", sn);
		json.put("title", title);
		json.put("intro", intro);
		return JSONObject.toJSONString(json);
	}

	public SystemDictionary(Long id) {
		this.id = id;
	}
}
