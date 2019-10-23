package cn.eeepay.framework.model.bill;

/**
 * 用户类型科目关联查询实体类
 * @author Administrator
 *
 */
public class SubjectExtQuery {
	
	private String userType ;
	private String subjectName ;
	private String subjectNo ;
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	
	
}
