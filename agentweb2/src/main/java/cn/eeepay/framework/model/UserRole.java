package cn.eeepay.framework.model;

import java.io.Serializable;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class UserRole  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer userId;
    private Integer roleId;
    private ShiroRole shiroRole;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public ShiroRole getShiroRole() {
		return shiroRole;
	}

	public void setShiroRole(ShiroRole shiroRole) {
		this.shiroRole = shiroRole;
	}


    
    
}