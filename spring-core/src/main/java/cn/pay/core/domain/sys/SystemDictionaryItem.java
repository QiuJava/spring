package cn.pay.core.domain.sys;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典明细
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
@Entity
// @Table(name = "system_dictionary_item")
public class SystemDictionaryItem extends BaseDomain {
	private static final long serialVersionUID = 1L;

	// @Column(name = "system_dictionary_id")
	private Long systemDictionaryId;
	// @Column(name = "title")
	private String title;
	// @Column(name = "intro")
	private String intro;
	// @Column(name = "sequence")
	private Integer sequence;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Transient
	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("systemDictionaryId", systemDictionaryId);
		json.put("title", title);
		json.put("sequence", sequence);
		json.put("intro", intro);
		return JSONObject.toJSONString(json);
	}
}
