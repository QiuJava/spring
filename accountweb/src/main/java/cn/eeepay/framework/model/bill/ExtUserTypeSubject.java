package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

/**
 * 科目类型实体类
 * @author Administrator
 *bill_ext_user_type_subject
 */
public class ExtUserTypeSubject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userType ;		//用户类型
	private String subjectNo ;		//科目编号
	private Subject subject;
	private String creator;
    private Date createTime;
    private String updator;
    private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "ExtUserTypeSubject [id=" + id + ", userType=" + userType + ", subjectNo=" + subjectNo + ", subject="
				+ subject + "]";
	}
	
	
}
