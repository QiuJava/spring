package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统字典
 * 
 * @author Qiujian
 * @date 2018/11/01
 */

@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class SystemDictionary implements Serializable {
	private static final long serialVersionUID = 1L;

	public SystemDictionary(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dictName;
	private String dictKey;
	private String dictValue;
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
	@OneToMany(mappedBy = "systemDictionary", cascade = { CascadeType.ALL })
	private List<SystemDictionaryItem> systemDictionaryItems;

	public String getJsonString() {
		return JSONObject.toJSONString(this);
	}

}
