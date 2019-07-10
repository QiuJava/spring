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

import cn.loan.core.entity.vo.RealAuthVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 实名认证
 * 
 * @author Qiujian
 * 
 */
@Getter
@Setter
@Entity
public class RealAuth implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String realName;
	private Integer gender;
	private String birthday;
	/** 身份证号码 */
	private String idNumber;
	private String address;
	/** 身份证正面 */
	private String frontImage;
	/** 身份证反面 */
	private String reverseImage;
	@OneToOne
	private LoginUser auditor;
	private Integer auditStatus;
	private String remark;
	@OneToOne
	private LoginUser submitter;
	private Date auditTime;
	private Date submissionTime;
	@Transient
	private String displayGender;
	@Transient
	private String auditStatusDisplay;

	public String getJsonString() {
		RealAuthVo vo = new RealAuthVo();
		vo.setId(id);
		vo.setUsername(submitter.getUsername());
		vo.setRealName(realName);
		vo.setIdNumber(idNumber);
		vo.setDisplayGender(displayGender);
		vo.setBirthday(birthday);
		vo.setAddress(address);
		vo.setFrontImage(frontImage);
		vo.setReverseImage(reverseImage);
		return JSONObject.toJSONString(vo);
	}

}
