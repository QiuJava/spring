package cn.eeepay.framework.model.bill;

import java.io.Serializable;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class UserRigth  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer userId;
    private Integer rigthId;
    private ShiroRigth shiroRigth;

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