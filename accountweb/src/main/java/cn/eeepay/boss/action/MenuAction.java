package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.model.bill.ComboTree;
import cn.eeepay.framework.model.bill.SysDept;
import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.service.bill.UserRigthService;
import cn.eeepay.framework.service.bill.UserSettingService;
import cn.eeepay.framework.util.Constants;
/**
 * 菜单管理
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

@Controller
@RequestMapping(value = "/menuAction")
public class MenuAction {
	private static final Logger log = LoggerFactory.getLogger(MenuAction.class);
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public UserRigthService userRigthService;
	@Resource
	public SysMenuService sysMenuService;
	@Resource
	private RedisService redisService;
	@Resource
	public UserSettingService userSettingService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/leftMenu.do")
	public String leftMenu(ModelMap model, @RequestParam Map<String, String> params,HttpServletRequest request, HttpServletResponse response) throws Exception{
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Integer userId = userInfo.getUserId();
		//log.info("userinfo:"+userInfo);
		List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
		Collection<GrantedAuthority> authorities = userInfo.getAuthorities();//用户权限集合
		List<SysMenu> userMenus = new ArrayList<>();
		for (SysMenu sysMenu : sysMenus) {
			for (GrantedAuthority ga : authorities) {
				if (sysMenu.getMenuCode().equals(ga.getAuthority())) {
					userMenus.add(sysMenu);
					break;
				}
			}
		}
		String key = Constants.user_last_menu_code + ":" + userId;
		Object object = redisService.select(key);
		String lastMenuCode = null;
		String lastParentMenuCode = null;
		if (object != null) {
			lastMenuCode = object.toString();
		}
		if (lastMenuCode != null) {
			SysMenu parentSysMenu= sysMenuService.findParentSysMenuByMenuCodeWithMenu(lastMenuCode);
			if (parentSysMenu != null) {
				lastParentMenuCode = parentSysMenu.getMenuCode();
			}
		}
//		Boolean isBackLastPage = false;
//		UserSetting userSetting = userSettingService.findUserSettingByUserId(userId);
//		if (userSetting != null && userSetting.getBackLastPage() != null && userSetting.getBackLastPage() == 1) {
//			isBackLastPage = true;
//		}
		
		String contextPath = request.getContextPath();
		StringBuffer sb = new StringBuffer();
		for (SysMenu sysMenu1 : userMenus) {
			if (sysMenu1 != null && sysMenu1.getParentId() == null || sysMenu1.getParentId().equals(Constants.sys_menu_root_id)) {
				String classActive1 = "";
				if (sysMenu1.getMenuCode().equals(lastParentMenuCode)) {
					classActive1 = "class='active'";
				}
				sb.append("		<li "+classActive1+"> \n");
				sb.append("		<a href=\"javascript:void(0);\">  \n");
				sb.append("			<i class=\"fa "+sysMenu1.getIcons()+"\"></i> \n");
				sb.append("			<span class=\"nav-label\">"+sysMenu1.getMenuName()+"</span> \n");
				sb.append("			<span class=\"fa arrow\"></span> \n");
				sb.append("		</a> \n");
				sb.append("		<ul class=\"nav nav-second-level\"> \n");
				for (SysMenu sysMenu2 : userMenus) {
					if (sysMenu2 != null && sysMenu2.getParentId() != null  && sysMenu1.getId().equals(sysMenu2.getParentId())) {
						String classActive2 = "";
						if (sysMenu2.getMenuCode().equals(lastMenuCode)) {
							classActive2 = "class='active'";
						}
						sb.append("			<li "+classActive2+"><a href=\""+contextPath + "/" + sysMenu2.getMenuUrl()+"\">"+sysMenu2.getMenuName()+"</a></li> \n");
					}
				}
				sb.append("		</ul> \n");
				sb.append("		</li> \n");
			}
		}
		model.put("sysMenus", sb.toString());
		//log.info("sysMenus=" + sb.toString());
		return "leftMenu";
	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/menuComboTree.do")
	@ResponseBody
	public List<ComboTree> menuComboTree(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		//log.info("userinfo:"+userInfo);
		List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
		
		Collection<GrantedAuthority> authorities = userInfo.getAuthorities();//用户权限集合
		
		List<SysMenu> userMenus = new ArrayList<>();
		for (SysMenu sysMenu : sysMenus) {
			for (GrantedAuthority ga : authorities) {
				if (sysMenu.getMenuCode().equals(ga.getAuthority())) {
					userMenus.add(sysMenu);
					break;
				}
			}
		}
		
		
		//对sysMenu排序
		Collections.sort(userMenus,new Comparator<SysMenu>(){
            public int compare(SysMenu arg0, SysMenu arg1) {
                return arg0.getOrderNo().compareTo(arg1.getOrderNo());
            }
        });
				
		
		ComboTree root = new ComboTree();
		root.setId(Constants.sys_menu_root_id.toString());
		root.setText("系统菜单");
		root.setState("open");
		List<ComboTree> list = new ArrayList<>();
		List<ComboTree> comboTrees = new ArrayList<>();
		for (SysMenu sysMenu1 : userMenus) {
			if (sysMenu1.getParentId() == null|| sysMenu1.getParentId().equals(Constants.sys_menu_root_id)) {
				ComboTree ct = new ComboTree();
				ct.setId(sysMenu1.getId().toString());
				ct.setText(sysMenu1.getMenuName());
				ct.setState("closed");
				List<ComboTree> childrens = new ArrayList<>();
				for (SysMenu sysMenu2 : userMenus) {
					if (sysMenu2.getParentId() != null && sysMenu1.getId().equals(sysMenu2.getParentId())) {
						ComboTree ct2 = new ComboTree();
						ct2.setId(sysMenu2.getId().toString());
						ct2.setText(sysMenu2.getMenuName());
//						ct2.setState("closed");
						childrens.add(ct2);
					}
				}
				if (childrens.size() >0) {
					ct.setChildren(childrens);
				}
				else{
					ct.setState(null);
				}
				comboTrees.add(ct);
			}
		}
		root.setChildren(comboTrees);
		list.add(root);
		return list;
	}
	@PreAuthorize("hasAuthority('menu:insert')")
	@RequestMapping(value = "/toAddMenu.do")
	public String toAddUser(ModelMap model, @RequestParam Map<String, String> params){
		List<SysDept> deptList = null;
		model.put("deptList", deptList);
		return "sys/addMenu";
	}
	@PreAuthorize("hasAuthority('menu:insert')")
	@RequestMapping(value = "addMenu.do")
	@Logs(description="新增菜单")
	@ResponseBody
	public Map<String,Object> addMenu(String menuName,String menuCode,Integer parentId,String menuUrl,String orderNo) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(menuName)) {
			msg.put("msg","菜单名称不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if (StringUtils.isBlank(menuCode)) {
			msg.put("msg","菜单编码不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if (parentId == null) {
			msg.put("msg","父菜单不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		else if (StringUtils.isBlank(orderNo)) {
			msg.put("msg","排序号不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			try {
				SysMenu sysMenu = new SysMenu();
				sysMenu.setMenuName(menuName);
				sysMenu.setMenuCode(menuCode);
				sysMenu.setRigthCode(menuCode);
				sysMenu.setMenuUrl(menuUrl);
				sysMenu.setParentId(parentId);
				sysMenu.setOrderNo(orderNo);
				sysMenu.setMenuType("menu");
				sysMenu.setMenuLevel(0);
				
				if (!parentId.equals(Constants.sys_menu_root_id)) {
					SysMenu sm = sysMenuService.findSysMenuById(parentId);
					if (sm != null) {
						sysMenu.setMenuLevel(sm.getMenuLevel()+1);
					}
				}
				i = sysMenuService.insertSysMenu(sysMenu);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","增加菜单成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.toString());
				log.error("异常:",e);
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('menu:update')")
	@RequestMapping(value = "updateMenu.do")
	@Logs(description="修改菜单")
	@ResponseBody
	public Map<String,Object> updateMenu(Integer menuId,String menuName,String menuCode,Integer parentId,String menuUrl,String orderNo) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		
		boolean isReturn = false;
		if (menuId == null) {
			msg.put("msg","菜单ID不能为空");
			msg.put("state",false);
			isReturn = true;
		} else if (menuId == 0) {
			msg.put("msg", "系统菜单不能进行修改");
			msg.put("state", false);
			isReturn = true;
		} else if (StringUtils.isBlank(menuName)) {
			msg.put("msg","菜单名称不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if (StringUtils.isBlank(menuCode)) {
			msg.put("msg","菜单编码不能为空");
			msg.put("state",false);
			isReturn = true;
		}
//		else if (parentId == null) {
//			msg.put("msg","父菜单不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
		else if (StringUtils.isBlank(orderNo)) {
			msg.put("msg","排序号不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			try {
				SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
				
				sysMenu.setMenuName(menuName);
				sysMenu.setMenuCode(menuCode);
				sysMenu.setMenuUrl(menuUrl);
				sysMenu.setParentId(parentId);
				sysMenu.setOrderNo(orderNo);
			
				i = sysMenuService.updateSysMenu(sysMenu);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","修改菜单成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
				log.error("异常:",e);
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('menu:delete')")
	@RequestMapping(value = "deleteMenu.do")
	@Logs(description="删除菜单")
	@ResponseBody
	public Map<String,Object> deleteMenu(String menuId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (menuId == null) {
			msg.put("status",false);
			msg.put("msg","请选择菜单后操作");
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			Integer mId = Integer.valueOf(menuId);
			try {
				i = sysMenuService.deleteMenuAndChildren(mId);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","删除成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error("异常:",e);
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	
}
