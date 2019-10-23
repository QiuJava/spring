package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import cn.eeepay.framework.util.OemTypeEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;   
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class UserLoginInfo extends User  implements Serializable{   
    private static final long serialVersionUID = 1L;

    private String userInfoId;
    private Integer userId;   
    private String realName;
    private String telNo;
    private String email;
    private String status;
    private String theme;
    private String secondUserNode;//二级代理节点
    private Date lockTime;
    private int wrongPasswordCount;
    
    public String getSecondUserNode() {
		return secondUserNode;
	}

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public void setSecondUserNode(String secondUserNode) {
		this.secondUserNode = secondUserNode;
	}

	private UserEntityInfo userEntityInfo;
    private OemTypeEnum oemTypeEnum = OemTypeEnum.SQIANBAO;
//    private String userId2;
//    private String entityId;
    private String teamId;
  
    @SuppressWarnings("deprecation")   
    public UserLoginInfo(String username, String password, Collection<? extends GrantedAuthority> authorities)   
        throws IllegalArgumentException {   
        super(username,password, authorities);   
    }   
  
  
    public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getRealName() {
		return realName;
	}


	public void setRealName(String realName) {
		this.realName = realName;
	}


	public String getTelNo() {
		return telNo;
	}


	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}




	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
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

    //	public String getUserId2() {
//		return userId2;
//	}
//
//
//	public void setUserId2(String userId2) {
//		this.userId2 = userId2;
//	}
//
//
//	public String getEntityId() {
//		return entityId;
//	}
//
//
//	public void setEntityId(String entityId) {
//		this.entityId = entityId;
//	}
//
//
	public String getTeamId() {
		return teamId;
	}


	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static long getSerialVersionUID() {   
        return serialVersionUID;   
    }


	public UserEntityInfo getUserEntityInfo() {
		return userEntityInfo;
	}


	public void setUserEntityInfo(UserEntityInfo userEntityInfo) {
		this.userEntityInfo = userEntityInfo;
	}

	public OemTypeEnum getOemTypeEnum() {
		return oemTypeEnum;
	}

	public void setOemTypeEnum(OemTypeEnum oemTypeEnum) {
		this.oemTypeEnum = oemTypeEnum;
	}

	@Override
	public String toString() {
		return "UserLoginInfo{" +
				"telNo='" + telNo + '\'' +
				", teamId='" + teamId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
    	if (o == null){
    		return false;
		}
		return this.toString().equals(o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), telNo, teamId);
	}
}