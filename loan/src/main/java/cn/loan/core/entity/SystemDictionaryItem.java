package cn.loan.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.SystemDictionaryItemVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典明细
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
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
	private Integer sequence;
	@CreatedDate
	private Date createTime;
	@LastModifiedDate
	private Date updateTime;
	@CreatedBy
	private Long createUser;
	@LastModifiedBy
	private Long updateUser;
	@ManyToOne(fetch = FetchType.EAGER)
	private SystemDictionary systemDictionary;

	public String getJsonString() {
		SystemDictionaryItemVo vo = new SystemDictionaryItemVo();
		vo.setId(id);
		vo.setItemName(itemName);
		vo.setItemKey(itemKey);
		vo.setItemValue(itemValue);
		vo.setSequence(sequence);
		vo.setSystemDictionaryId(systemDictionary.getId());
		return JSONObject.toJSONString(vo);
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
