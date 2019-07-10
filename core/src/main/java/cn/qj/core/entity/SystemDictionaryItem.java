package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统字典明细
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SystemDictionaryItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String itemName;
	private String itemKey;
	private String itemValue;
	private String intro;
	private Integer sequence;
	@CreatedDate
	private Date createTime;
	@LastModifiedDate
	private Date updateTime;
	@CreatedBy
	private String createUser;
	@LastModifiedDate
	private String updateUser;
	@ManyToOne
	private SystemDictionary systemDictionary;

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>(5);
		return JSONObject.toJSONString(json);
	}

	public String getIdAndSysDictIdJsonStr() {
		Map<String, Object> json = new HashMap<>(2);
		return JSONObject.toJSONString(json);
	}
}
