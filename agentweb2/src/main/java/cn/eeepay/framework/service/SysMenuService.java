package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.SysMenu;

public interface SysMenuService {
		void init()  throws Exception;
		int insertSysMenu(SysMenu sysMenu)  throws Exception;
		int updateSysMenu(SysMenu sysMenu)  throws Exception;
		int deleteSysMenu(Integer id)  throws Exception;
		SysMenu findSysMenuById(Integer id) throws Exception;
		List<SysMenu> findAllSysMenu() throws Exception;
		List<SysMenu> findAllNotBlankUrlSysMenu() throws Exception;
		List<SysMenu> findAllPageSysMenuByParentId(Integer parentId)  throws Exception;
		List<SysMenu> findUserRolePrivilegeSysMenu(Integer userId) throws Exception;
		SysMenu findSysMenuByMenuCode(String menuCode) throws Exception;
		int deleteMenuAndChildren(Integer menuId);
		List<SysMenu> findAllSysMenuAndChildrenList() throws Exception;
}
