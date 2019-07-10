package cn.loan.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.CreditFileVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 信用材料
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@Entity
public class CreditFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 认证分 */
	private Integer score;
	/** 材料文件名 */
	private String fileName;
	@OneToOne
	private SystemDictionaryItem fileType;
	private Integer auditStatus;
	private String remark;
	@OneToOne
	private LoginUser auditor;
	@OneToOne
	private LoginUser submitter;
	private Date auditTime;
	private Date submissionTime;
	@Transient
	private String auditStatusDisplay;

	public String getJsonString() {
		CreditFileVo vo = new CreditFileVo();
		vo.setId(id);
		vo.setFileName(fileName);
		vo.setItemName(fileType.getItemName());
		vo.setUsername(submitter.getUsername());
		return JSONObject.toJSONString(vo);
	}
}
