package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SysMenuDao;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.ShiroRigthService;
import cn.eeepay.framework.service.SysMenuService;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService{
	
	@Resource
	public SysMenuDao sysMenuDao;
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
		List<SysMenu> sysMenuNotBlankUrlList= sysMenuDao.findAllNotBlankUrlSysMenu();
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
		
		List<SysMenu> sysMenuAllList= sysMenuDao.findAllSysMenu();
		
		
		for (SysMenu sysMenu : sysMenuAllList) {
			redisService.insertSet(Constants.sys_menu_all_list_redis_key, sysMenu);
		}
		
		System.out.println("sys_menu_all_list_redis_key sysmenu");
		
	}
	@Override
	public SysMenu findSysMenuById(Integer id) throws Exception {
		return sysMenuDao.findSysMenuById(id);
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
			List<SysMenu> sysMenuList= sysMenuDao.findAllSysMenu();
			return sysMenuList;
		}
		return null;
	}
	@Override
	public List<SysMenu> findUserRolePrivilegeSysMenu(Integer userId) throws Exception {
		return sysMenuDao.findUserRolePrivilegeSysMenu(userId);
	}
	@Override
	public List<SysMenu> findAllPageSysMenuByParentId(Integer parentId) throws Exception {
		return sysMenuDao.findAllPageSysMenuByParentId(parentId);
	}
	@Override
	public SysMenu findSysMenuByMenuCode(String menuCode) throws Exception {
		return sysMenuDao.findSysMenuByMenuCode(menuCode);
	}
	@Override
	public int updateSysMenu(SysMenu sysMenu) throws Exception {
		SysMenu oldSysMenu = sysMenuDao.findSysMenuById(sysMenu.getId());
		if (oldSysMenu == null) {
			return 0;
		}
		boolean isMember = redisService.selectIsMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenuDao.findSysMenuById(sysMenu.getId()));
		if (isMember) {
			redisService.deleteMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, oldSysMenu);
			redisService.insertSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
		}
		
		boolean isMember2 = redisService.selectIsMemberForSet(Constants.sys_menu_all_list_redis_key, oldSysMenu);
		if (isMember2) {
			redisService.deleteMemberForSet(Constants.sys_menu_all_list_redis_key, oldSysMenu);
			redisService.insertSet(Constants.sys_menu_all_list_redis_key, sysMenu);
		}
		
		return sysMenuDao.updateSysMenu(sysMenu);
	}
	@Override
	public int insertSysMenu(SysMenu sysMenu) {
		int i =0,n=0 ;
		ShiroRigth shiroRigth = new ShiroRigth();
		shiroRigth.setRigthCode(sysMenu.getMenuCode());
		shiroRigth.setRigthName(sysMenu.getMenuName());
		try {
			i = sysMenuDao.insertSysMenu(sysMenu);
			redisService.insertSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
			redisService.insertSet(Constants.sys_menu_all_list_redis_key, sysMenu);
			n = shiroRigthService.insertShiroRigth(shiroRigth);
			i = i + n;
			
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage());
		}
		return i;
	}
	@Override
	public int deleteSysMenu(Integer id) {
		int i =0,n=0 ;
		//删除菜单的同时删除对应权限
		try {
			SysMenu sysMenu = this.findSysMenuById(id);
			i = sysMenuDao.deleteSysMenu(id);
			n = shiroRigthService.deleteShiroRigthByRigthCode(sysMenu.getMenuCode());
			i = i + n;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return i;
	}
	@Override
	public int deleteMenuAndChildren(Integer menuId) {
		int i = 0;
		List<SysMenu> sysMenus;
		//删除菜单的同时删除对应权限
		try {
			sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
			for (SysMenu sysMenu : sysMenus) {
				int n = sysMenuService.deleteSysMenu(sysMenu.getId());
				int m = shiroRigthService.deleteShiroRigthByRigthCode(sysMenu.getMenuCode());
				i = i + n + m;
			}
			int n = sysMenuService.deleteSysMenu(menuId);
			SysMenu sm = this.findSysMenuById(menuId);
			int m = shiroRigthService.deleteShiroRigthByRigthCode(sm.getMenuCode());
			i = i + n + m;
		} catch (Exception e) {
			e.printStackTrace();
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
			List<SysMenu> sysMenuList= sysMenuDao.findAllNotBlankUrlSysMenu();
			return sysMenuList;
		}
		return null;
	}
	@Override
	public List<SysMenu> findAllSysMenuAndChildrenList() throws Exception {
		return sysMenuDao.findAllSysMenuAndChildrenList();
	}
	







}
