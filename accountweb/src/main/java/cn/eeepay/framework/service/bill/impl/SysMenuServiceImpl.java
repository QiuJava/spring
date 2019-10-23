package cn.eeepay.framework.service.bill.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SysMenuMapper;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.MenuType;
import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.util.Constants;

@Service("sysMenuService")
@Transactional
public class SysMenuServiceImpl implements SysMenuService{
	
	@Resource
	public SysMenuMapper sysMenuMapper;
	@Resource
	public SysMenuService sysMenuService;
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	private RedisService redisService;
	
	private static final Logger log = LoggerFactory.getLogger(SysMenuServiceImpl.class);

	@PostConstruct
	@Override
	public void init() throws Exception {
		List<SysMenu> sysMenuNotBlankUrlList= sysMenuMapper.findAllNotBlankUrlSysMenu();
		List<String> keys = new ArrayList<String>();
		if (redisService.exists(Constants.sys_menu_not_blank_url_list_redis_key)) {
			keys.add(Constants.sys_menu_not_blank_url_list_redis_key);
		}
		if (redisService.exists(Constants.sys_menu_all_list_redis_key)) {
			keys.add(Constants.sys_menu_all_list_redis_key);
		}
		redisService.delete(keys);
		
		for (SysMenu sysMenu : sysMenuNotBlankUrlList) {
			redisService.insertSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
		}
		
		System.out.println("sys_menu_not_blank_url_list_redis_key sysmenu");
		
		List<SysMenu> sysMenuAllList= sysMenuMapper.findAllSysMenu();
		
		
		for (SysMenu sysMenu : sysMenuAllList) {
			redisService.insertSet(Constants.sys_menu_all_list_redis_key, sysMenu);
		}
		
		System.out.println("sys_menu_all_list_redis_key sysmenu");
		
	}
	@Override
	public SysMenu findSysMenuById(Integer id) throws Exception {
		return sysMenuMapper.findSysMenuById(id);
	}
	@Override
	public List<SysMenu> findAllSysMenu() throws Exception {
		if (redisService.exists(Constants.sys_menu_all_list_redis_key)) {
			Object  sysMenuListRedis  = redisService.select(Constants.sys_menu_all_list_redis_key);
			if(sysMenuListRedis instanceof Set){
				Set sysMenuList = (Set)sysMenuListRedis;
				ArrayList<SysMenu> sysMenus = new ArrayList<SysMenu>(sysMenuList);
				//对sysMenu排序
				Collections.sort(sysMenus,new Comparator<SysMenu>(){
		            public int compare(SysMenu arg0, SysMenu arg1) {
		                return arg0.getOrderNo().compareTo(arg1.getOrderNo());
		            }
		        });
				return sysMenus;
			}
		}
		else{
			//如果redis key 没有，或者redis没启动，用数据库获取数据
			List<SysMenu> sysMenuList= sysMenuMapper.findAllSysMenu();
			return sysMenuList;
		}
		return null;
	}
	@Override
	public List<SysMenu> findUserRolePrivilegeSysMenu(Integer userId) throws Exception {
		return sysMenuMapper.findUserRolePrivilegeSysMenu(userId);
	}
	@Override
	public List<SysMenu> findAllPageSysMenuByParentId(Integer parentId) throws Exception {
		return sysMenuMapper.findAllPageSysMenuByParentId(parentId);
	}
	@Override
	public SysMenu findSysMenuByMenuCodeWithMenu(String menuCode) throws Exception {
		return sysMenuMapper.findSysMenuByMenuCodeWithMenu(menuCode);
	}
	@Override
	public SysMenu findSysMenuByMenuCodeWithPage(String menuCode) throws Exception {
		return sysMenuMapper.findSysMenuByMenuCodeWithPage(menuCode);
	}
	@Override
	public int updateSysMenu(SysMenu sysMenu) throws Exception {
		SysMenu sysMenuOld= this.findSysMenuById(sysMenu.getId());
		boolean isMember = redisService.selectIsMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenuOld);
		if (isMember) {
			redisService.deleteMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenuOld);
			redisService.insertSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
		}
		
		boolean isMember2 = redisService.selectIsMemberForSet(Constants.sys_menu_all_list_redis_key, sysMenuOld);
		if (isMember2) {
			redisService.deleteMemberForSet(Constants.sys_menu_all_list_redis_key, sysMenuOld);
			redisService.insertSet(Constants.sys_menu_all_list_redis_key, sysMenu);
		}
		
		return sysMenuMapper.updateSysMenu(sysMenu);
	}
	@Override
	public int insertSysMenu(SysMenu sysMenu) {
		int i =0,n=0 ;
		ShiroRigth shiroRigth = new ShiroRigth();
		shiroRigth.setRigthCode(sysMenu.getMenuCode());
		shiroRigth.setRigthName(sysMenu.getMenuName());
		try {
			i = sysMenuMapper.insertSysMenu(sysMenu);
			SysMenu sysMenuNew= this.findSysMenuById(sysMenu.getId());
			if (StringUtils.isNotBlank(sysMenuNew.getMenuUrl())) {
				redisService.insertSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenuNew);
			}
			if (sysMenuNew.getMenuType().equals(MenuType.MENU.toString())) {
				redisService.insertSet(Constants.sys_menu_all_list_redis_key, sysMenuNew);
			}
			n = shiroRigthService.insertShiroRigth(shiroRigth);
			i = i + n;
			
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return i;
	}
	@Override
	public int deleteSysMenu(Integer id) {
		int i =0,n=0 ;
		//删除菜单的同时删除对应权限
		try {
			SysMenu sysMenu = this.findSysMenuById(id);
			i = sysMenuMapper.deleteSysMenu(id);
			n = shiroRigthService.deleteShiroRigthByRigthCode(sysMenu.getMenuCode());
			i = i + n;
			//删除数据库之后需要删除缓存
			boolean isMember = redisService.selectIsMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
			if (isMember) {
				redisService.deleteMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
			}
			boolean isMember2 = redisService.selectIsMemberForSet(Constants.sys_menu_all_list_redis_key, sysMenu);
			if (isMember2) {
				redisService.deleteMemberForSet(Constants.sys_menu_all_list_redis_key, sysMenu);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return i;
	}
	@Override
	public int deleteMenuAndChildren(Integer menuId) {
		int i = 0;
		int m = 0;
		int n = 0;
		List<SysMenu> sysMenus;
		//删除菜单的同时删除对应权限
		try {
			sysMenus = this.findAllPageSysMenuByParentId(menuId);
			for (SysMenu sysMenu : sysMenus) {
				n = this.deleteSysMenu(sysMenu.getId());
				m = shiroRigthService.deleteShiroRigthByRigthCode(sysMenu.getMenuCode());
				i = i + n + m;
			}
			n = this.deleteSysMenu(menuId);
			i = i + n;
			
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return i;
	}
	@Override
	public List<SysMenu> findAllNotBlankUrlSysMenu() throws Exception {
		if (redisService.exists(Constants.sys_menu_not_blank_url_list_redis_key)) {
			Object  sysMenuListRedis  = redisService.select(Constants.sys_menu_not_blank_url_list_redis_key);
			if(sysMenuListRedis instanceof Set){
				Set sysMenuList = (Set)sysMenuListRedis;
				ArrayList<SysMenu> sysMenus = new ArrayList<SysMenu>(sysMenuList);
				return sysMenus;
			}
		}
		else{
			//如果redis key 没有，或者redis没启动，用数据库获取数据
			List<SysMenu> sysMenuList= sysMenuMapper.findAllNotBlankUrlSysMenu();
			return sysMenuList;
		}
		return null;
	}
	@Override
	public List<SysMenu> findAllSysMenuAndChildrenList() throws Exception {
		return sysMenuMapper.findAllSysMenuAndChildrenList();
	}
	@Override
	public List<SysMenu> findAllPageByParentId(Integer parentId, Sort sort)
			throws Exception {
		return sysMenuMapper.findAllPageByParentId(parentId, sort);
	}
	@Override
	public SysMenu findSysMenuByMenuUrlWithMenu(String menuUrl) throws Exception {
		return sysMenuMapper.findSysMenuByMenuUrlWithMenu(menuUrl);
	}
	@Override
	public SysMenu findParentSysMenuByMenuCodeWithMenu(String menuCode) {
		return sysMenuMapper.findParentSysMenuByMenuCodeWithMenu(menuCode);
	}
}
