package cn.eeepay.framework.model.bill;

import java.io.Serializable;


public class UserSetting  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer userId;

    private Integer backLastPage;//返回最后访问页面
    private String collapseMenu;//收缩菜单开关

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBackLastPage() {
		return backLastPage;
	}

	public void setBackLastPage(Integer backLastPage) {
		this.backLastPage = backLastPage;
	}

	public String getCollapseMenu() {
		return collapseMenu;
	}

	public void setCollapseMenu(String collapseMenu) {
		this.collapseMenu = collapseMenu;
	}
    
}