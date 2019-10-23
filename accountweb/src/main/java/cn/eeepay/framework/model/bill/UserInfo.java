package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;   
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class UserInfo extends User  implements Serializable{   
    private static final long serialVersionUID = 1L;   
  
    private Integer userId;   
    private String realName;
    private String telNo;
    private String email;
    private Integer state;
    private String theme;
    private boolean isAdmin;
    private String loginIp;
    private Date lastRequest;
    private String sessionId;
    private String lastRequestUrl;
  
    @SuppressWarnings("deprecation")   
    public UserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities)   
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


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
	}


	public String getLoginIp() {
		return loginIp;
	}


	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}


	public Date getLastRequest() {
		return lastRequest;
	}


	public void setLastRequest(Date lastRequest) {
		this.lastRequest = lastRequest;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getLastRequestUrl() {
		return lastRequestUrl;
	}

	public void setLastRequestUrl(String lastRequestUrl) {
		this.lastRequestUrl = lastRequestUrl;
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
     
}  