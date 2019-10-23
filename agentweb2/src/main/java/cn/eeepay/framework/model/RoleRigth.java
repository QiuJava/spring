package cn.eeepay.framework.model;

import java.io.Serializable;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class RoleRigth implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer roleId;
    private Integer rigthId;
    private ShiroRigth shiroRigth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRigthId() {
		return rigthId;
	}

	public void setRigthId(Integer rigthId) {
		this.rigthId = rigthId;
	}

	public ShiroRigth getShiroRigth() {
		return shiroRigth;
	}

	public void setShiroRigth(ShiroRigth shiroRigth) {
		this.shiroRigth = shiroRigth;
	}

    
    
}