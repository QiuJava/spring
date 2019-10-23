package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String userId;

	private String userName;

	private String mobilephone;

	private String email;

	private String status;

	private Date updatePwdTime;

	private String password;

	private String teamId;

	private String secondUserNode;// 二级代理节点

	private Date createTime;
	private String hasChildren;
	private List<UserEntityInfo> userEntityInfos;
	private Date lockTime;
	private int wrongPasswordCount;
	
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSecondUserNode() {
		return secondUserNode;
	}
	
	public void setSecondUserNode(String secondUserNode) {
		this.secondUserNode = secondUserNode;
	}
	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone == null ? null : mobilephone.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public Date getUpdatePwdTime() {
		return updatePwdTime;
	}

	public void setUpdatePwdTime(Date updatePwdTime) {
		this.updatePwdTime = updatePwdTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<UserEntityInfo> getUserEntityInfos() {
		return userEntityInfos;
	}

	public void setUserEntityInfos(List<UserEntityInfo> userEntityInfos) {
		this.userEntityInfos = userEntityInfos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public int getWrongPasswordCount() {
		return wrongPasswordCount;
	}

	public void setWrongPasswordCount(int wrongPasswordCount) {
		this.wrongPasswordCount = wrongPasswordCount;
	}
}