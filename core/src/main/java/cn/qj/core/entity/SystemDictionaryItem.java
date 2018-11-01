package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
 * 系统字典明细
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@Entity
public class SystemDictionaryItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String intro;
	private Integer sequence;
	private Date gmtCreate;
	private Date gmtModified;
	@ManyToOne
	private SystemDictionary systemDictionary;

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>(5);
		json.put("id", id);
		json.put("title", title);
		json.put("sequence", sequence);
		json.put("intro", intro);
		json.put("systemDictionaryId", systemDictionary.getId());
		return JSONObject.toJSONString(json);
	}

	public String getIdAndSysDictIdJsonStr() {
		Map<String, Object> json = new HashMap<>(2);
		json.put("id", id);
		json.put("sysDictId", systemDictionary.getId());
		return JSONObject.toJSONString(json);
	}
}
