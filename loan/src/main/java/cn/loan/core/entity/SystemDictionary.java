package cn.loan.core.entity;

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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.SystemDictionaryVo;
import cn.loan.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典
 * 
 * @author Qiujian
 * @date 2018/11/01
 */

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SystemDictionary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dictName;
	private String dictKey;
	private String dictValue;
	private Integer sequence;
	@CreatedDate
	private Date createTime;
	@LastModifiedDate
	private Date updateTime;
	@CreatedBy
	private Long createUser;
	@LastModifiedBy
	private Long updateUser;
	@OneToMany(mappedBy = StringUtil.SYSTEM_DICTIONARY, cascade = { CascadeType.REMOVE })
	private List<SystemDictionaryItem> systemDictionaryItems;

	public String getJsonString() {
		SystemDictionaryVo vo = new SystemDictionaryVo();
		vo.setId(id);
		vo.setDictName(dictName);
		vo.setDictKey(dictKey);
		vo.setDictValue(dictValue);
		vo.setSequence(sequence);
		return JSONObject.toJSONString(vo);
	}

}
