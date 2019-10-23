package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.MenuType;
import cn.eeepay.framework.model.Node;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.model.ShiroRole;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.UserRigth;
import cn.eeepay.framework.model.UserRole;
import cn.eeepay.framework.service.RoleRigthService;
import cn.eeepay.framework.service.ShiroRigthService;
import cn.eeepay.framework.service.ShiroRoleService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.SysMenuService;
import cn.eeepay.framework.service.UserRigthService;
import cn.eeepay.framework.service.UserRoleService;
import cn.eeepay.framework.util.Constants;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

@Controller
@RequestMapping(value = "/sysAction")
public class SysAction {
//	@Resource
//	public UserInfoService userService;
//	@Resource
//	public SysDeptService sysDeptService;
	@Resource
	public SysMenuService sysMenuService;
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public ShiroRoleService shiroRoleService;
	@Resource
	public UserRigthService userRigthService;
	@Resource
	public RoleRigthService roleRigthService;
	@Resource
	public UserRoleService userRoleService;
	@Resource
	public SysDictService sysDictService;
//	@Resource
//	public UserSettingService userSettingService;
	
	private static final Logger log = LoggerFactory.getLogger(SysAction.class);
	
	@RequestMapping("/menuTree.do")
	@ResponseBody
	public Object menuTree() throws Exception{
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		if (userInfo.getUserEntityInfo().getEntityId().equals("ALL")) {
			//List<SysMenu> list = sysMenuService.findAllSysMenuAndChildrenList();
			//System.out.println(JSONObject.toJSONString(list));
			return sysMenuService.findAllSysMenuAndChildrenList();
		}else{
			Set<String> permits = new HashSet<String>();
    		for(GrantedAuthority item : userInfo.getAuthorities()){
    			// 新增的角色不拥有系统管理菜单		
    			if (item.getAuthority().equals("sys")) {
					continue;
				}
    			permits.add(item.getAuthority());
    		}
    		List<SysMenu> list = sysMenuService.findAllSysMenuAndChildrenList();
			filterMenu(list, permits);
			return list;
		}
	}
	
	private void filterMenu(Collection<SysMenu> list, Set<String> permits) {
		Iterator<SysMenu> it = list.iterator();
		while(it.hasNext()){
			SysMenu menu = it.next();
			if(!permits.contains(menu.getMenuCode())){
				it.remove();
				continue;
			}
			final List<SysMenu> children = menu.getChildren();
			if(children !=null && !children.isEmpty()) {
				filterMenu(children, permits);
			}
		}
	}
	@RequestMapping("/menuList.do")
	@ResponseBody
	public Object menuList() throws Exception{
		return sysMenuService.findAllSysMenu();
	}
	
	@RequestMapping(value = "/menu.do")
	public String menu(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		String str = "";
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			list.add("{ id:'"+Constants.sys_menu_root_id+"', pId:9999, name:'系统菜单', open:true}");
			for (SysMenu sysMenu : sysMenus) {
				String pId = Constants.sys_menu_root_id.toString(); 
				if (sysMenu.getParentId() != null) {
					pId = sysMenu.getParentId().toString();
				}
				list.add("{ id:'"+sysMenu.getId()+"', pId:'"+pId+"', name:'"+sysMenu.getMenuName()+"', open:true}");
			}
			str = StringUtils.join(list,",\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("menuTree", str);
		return "sys/menu";
	}
	
	@RequestMapping(value = "/findMenuById.do")
	@ResponseBody
	public SysMenu findMenuById(Integer menuId) throws Exception{
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
		SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
		return sysMenu;
	}
	@RequestMapping(value = "findMenuPageList.do")
	@ResponseBody
	public List<SysMenu> findMenuPageList(Integer menuId){
		List<SysMenu> sysMenus = null;
		try {
			//subjectService.findSubject(subject,sort,page);
			sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return sysMenus;
	}
	
	@RequestMapping("/addMenu.do")
	@ResponseBody
	public Object addMenu(SysMenu sysMenu){
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			int i = sysMenuService.insertSysMenu(sysMenu);
			if(i > 0){
				ret.put("state", true);
				ret.put("msg", "增加菜单成功");
				ret.put("newMenu", sysMenu);
			}else{
				ret.put("state", false);
				ret.put("msg", "增加菜单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("state", false);
			ret.put("msg", "增加菜单失败");
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "updateFunction.do")
	@ResponseBody
	public Map<String,Object> updateFunction(Integer id,String menuName,String menuCode,String menuUrl,String orderNo) throws Exception {
		//TODO updateFunction验证操作session 是否 有权限操作 
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(menuName)) {
			msg.put("msg","功能名称不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if(StringUtils.isBlank(menuCode) ){
			msg.put("msg","功能编码不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if(StringUtils.isBlank(menuUrl) ){
			msg.put("msg","功能路径不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if(StringUtils.isBlank(orderNo) ){
			msg.put("msg","排序号不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			SysMenu sysMenu = sysMenuService.findSysMenuById(id);
			sysMenu.setMenuName(menuName);
			sysMenu.setMenuCode(menuCode);
			sysMenu.setMenuUrl(menuUrl);
			sysMenu.setOrderNo(orderNo);
			try {
				int i = 0;
				i = sysMenuService.updateSysMenu(sysMenu);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","修改成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
			}	
		}
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "addFunction.do")
	@ResponseBody
	public Map<String,Object> addFunction(Integer menuId,String menuName,String menuCode,String menuUrl,String orderNo) throws Exception {
		//TODO addFunction验证操作session 是否 有权限操作 
		Map<String,Object> msg=new HashMap<>();
		
		boolean isReturn = false;
		if (StringUtils.isBlank(menuName)) {
			msg.put("msg","功能名称不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if(StringUtils.isBlank(menuCode) ){
			msg.put("msg","功能编码不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if(StringUtils.isBlank(menuUrl) ){
			msg.put("msg","功能路径不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		else if(StringUtils.isBlank(orderNo) ){
			msg.put("msg","排序号不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			SysMenu sysMenu = new SysMenu();
			sysMenu.setMenuName(menuName);
			sysMenu.setMenuCode(menuCode);
			sysMenu.setRigthCode(menuCode);
			sysMenu.setMenuUrl(menuUrl);
			sysMenu.setOrderNo(orderNo);
			SysMenu sysMenu1 = sysMenuService.findSysMenuById(menuId);
			sysMenu.setMenuLevel(sysMenu1.getMenuLevel()+1);
			sysMenu.setMenuType(MenuType.PAGE.toString());
			sysMenu.setParentId(menuId);
			try {
				i = sysMenuService.insertSysMenu(sysMenu);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","保存成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
			}	
		}
		
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "deleteFunction.do")
	@ResponseBody
	public Map<String,Object> deleteFunction(Integer menuId) throws Exception {
		//TODO deleteFunction验证操作session 是否 有权限操作 
		Map<String,Object> msg=new HashMap<>();
		
		boolean isReturn = false;
		if (menuId == null) {
			msg.put("msg","功能ID不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			try {
				i = sysMenuService.deleteSysMenu(menuId);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","删除成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
			}	
		}
		
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "deleteMenu.do")
	@ResponseBody
	public Map<String,Object> deleteMenu(Integer menuId) throws Exception {
		//TODO deleteMenu验证操作session 是否 有权限操作 
		Map<String,Object> msg=new HashMap<>();
		
		boolean isReturn = false;
		if (menuId == null) {
			msg.put("msg","功能ID不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			try {
				sysMenuService.deleteMenuAndChildren(menuId);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","删除成功");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
			}	
		}
		
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "updateMenu.do")
	@ResponseBody
	public Map<String,Object> updateMenu(Integer menuId,String menuName,String menuCode,Integer parentId,String menuUrl,String orderNo) throws Exception {
		//TODO updateMenu验证操作session 是否 有权限操作 
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (menuId == null) {
			msg.put("msg","功能ID不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if (StringUtils.isBlank(menuName)) {
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
			}	
		}
		
		return msg;
	}
	
	
	
	@RequestMapping(value = "/user.do")
	public String user(ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> userStateList = null;
		try {
			userStateList = sysDictService.findSysDictGroup("sys_user_state");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("userStateList", userStateList);
		return "sys/user";
	}
	
//	@RequestMapping(value = "/toUpdateUser.do")
//	public String toUpdateUser(ModelMap model, @RequestParam Map<String, String> params){
//		String id = params.get("id");
//		Integer uId = Integer.valueOf(id);
//		List<SysDict> userStateList = null;
//		ShiroUser shiroUser = null;
//		List<String> list = new ArrayList<String>();
//		List<SysDept> deptList = null;
//		String str = "";
//		try {
//			deptList = sysDeptService.findAllSysDeptList();
//			shiroUser = userService.findUserById(uId);
//			userStateList = sysDictService.findSysDictGroup("sys_user_state");
//			
//			List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
//			List<UserRole> userRoles = userRoleService.findUserRoleByUserId(uId);//获取用户对应的角色
//			
//			List<ShiroRole> srs = new ArrayList<>();//用户对应角色实体
//			for (UserRole userRole : userRoles) {
//				srs.add(userRole.getShiroRole());
//			}
//			
//			list.add("{ id:'root', pId:0, name:'用户角色', open:true}");
//			for (ShiroRole shiroRole : shiroRoles) {
//				boolean checked = false;
//				for (ShiroRole sr : srs) {
//					if (shiroRole.getRoleCode().equals(sr.getRoleCode())) {
//						checked = true;
//						break;
//					}
//				}
//				list.add("{ id:'"+shiroRole.getId()+"', pId:'root', name:'"+shiroRole.getRoleName()+"', checked:'"+checked+"'}");
//			}
//			str = StringUtils.join(list,",\n");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("userRoleTree", str);
//		model.put("user", shiroUser);
//		model.put("userStateList", userStateList);
//		model.put("deptList", deptList);
//		return "sys/updateUser";
//	}
//	
//	@RequestMapping(value = "/findUsers.do")
//	@ResponseBody
//	public Page<ShiroUser> findUsers(@ModelAttribute ShiroUser shiroUser, @RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<ShiroUser> page){
//		try {
//			userService.findUsers(shiroUser, sort, page);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return page;
//	}
//	
	@RequestMapping(value = "findUserRolePrivilege.do")
	@ResponseBody
	public List<Node> findUserRolePrivilege(String userId) throws Exception {
		String[] params = userId.split(":");
		Integer uId = Integer.valueOf(params[1]);
		//System.out.println(" findUserRolePrivilege Json");
		Node node = new Node();
		List<Node> nodes = new ArrayList<>();
		
		try {
			//List<ShiroRigth> shiroRigths = shiroRigthService.findAllShiroRigth();
			//List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
			List<UserRole> userRoles = userRoleService.findUserRoleByUserId(uId);//获取用户对应的角色
			
			
			List<ShiroRole> srs = new ArrayList<>();//用户对应角色实体
			for (UserRole userRole : userRoles) {
				srs.add(userRole.getShiroRole());
			}
			
			node = new Node("root","0","true","用户角色","true","true");
			nodes.add(node);
			for (ShiroRole shiroRole : shiroRoles) {
				node = new Node();
				node.setId(shiroRole.getId().toString());
				String pId = "root";
//				if(sysMenu.getParentId() != null){
//					pId = sysMenu.getParentId().toString();
//				}
				node.setpId(pId);
				node.setIsParent("false");
				node.setOpen("true");
				node.setName(shiroRole.getRoleName());
				node.setChecked("false");
				for (ShiroRole sr : srs) {
					if (shiroRole.getRoleCode().equals(sr.getRoleCode())) {
						node.setChecked("true");
						break;
					}
				}
				nodes.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return nodes;
	}
	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "saveUserRole.do")
//	@ResponseBody
//	public Map<String,Object> saveUserRole(String userId,String roleId) throws Exception {
//		String[] roleIdArray = roleId.split(",");
//		
//		//TODO insertUserRigth验证操作session 是否 有权限操作 
////		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(userId);
//		Map<String,Object> msg=new HashMap<>();
//		int i = 0;
//		try {
//			i = userRoleService.saveUserRole(uId, roleIdArray);
//			if (i>0) {
//				msg.put("state",true);
//				msg.put("msg","保存角色成功");
//			}
//		} catch (Exception e) {
//			//e.printStackTrace();
//			msg.put("state",false);
//			msg.put("msg",e.getMessage());
//		}	
//		return msg;
//	}
//	
//	@RequestMapping(value = "/toAddUser.do")
//	public String toAddUser(ModelMap model, @RequestParam Map<String, String> params){
//		List<String> list = new ArrayList<String>();
//		List<SysDept> deptList = null;
//		String str = "";
//		try {
//			deptList = sysDeptService.findAllSysDeptList();
//			List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
//			list.add("{ id:'root', pId:0, name:'用户角色', open:true}");
//			for (ShiroRole shiroRole : shiroRoles) {
//				boolean checked = false;
//				list.add("{ id:'"+shiroRole.getId()+"', pId:'root', name:'"+shiroRole.getRoleName()+"', checked:'"+checked+"'}");
//			}
//			str = StringUtils.join(list,",\n");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("userRoleTree", str);
//		model.put("deptList", deptList);
//		return "sys/addUser";
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "updateUser.do")
//	@ResponseBody
//	public Map<String,Object> updateUser(String dept,String userId,String userName,String realName,String email,String telNo,Integer state,String roleIds) throws Exception {
//		Map<String,Object> msg=new HashMap<>();
//		String[] roleIdArray = roleIds.split(",");
//		boolean isReturn = false;
//		if (StringUtils.isBlank(userName)) {
//			msg.put("msg","用户名不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(realName)) {
//			msg.put("msg","真实名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(dept)) {
//			msg.put("msg","部门不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//		//TODO saveRole验证操作session 是否 有权限操作 
////			String[] params = userId.split(":");
//			Integer uId = Integer.valueOf(userId);
//			int i = 0;
//			ShiroUser shiroUser = new ShiroUser();
//			shiroUser.setId(uId);
//			shiroUser.setUserName(userName);
//			shiroUser.setRealName(realName);
//			shiroUser.setEmail(email);
//			shiroUser.setTelNo(telNo);
//			shiroUser.setState(state);
//			shiroUser.setDeptId(Integer.valueOf(dept));
//			try {
//				i = userService.updateUser(shiroUser,roleIdArray);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","修改用户成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "addUser.do")
//	@ResponseBody
//	public Map<String,Object> addUser(String dept,String userName,String realName,String email,String telNo,String roleIds) throws Exception {
//		Map<String,Object> msg=new HashMap<>();
//		String[] roleIdArray = roleIds.split(",");
//		boolean isReturn = false;
//		if (StringUtils.isBlank(userName)) {
//			msg.put("msg","用户名不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(realName)) {
//			msg.put("msg","真实名称不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}else if (StringUtils.isBlank(dept)) {
//			msg.put("msg","部门不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
//				    .getAuthentication()
//				    .getPrincipal();
//			String newPassMd5 = DigestUtils.md5Hex("88888888{"+userName+"}");
//			//TODO addRole验证操作session 是否 有权限操作 
//			int i = 0;
//			ShiroUser shiroUser = new ShiroUser();
//			shiroUser.setUserName(userName);
//			shiroUser.setPassword(newPassMd5);
//			shiroUser.setRealName(realName);
//			shiroUser.setEmail(email);
//			shiroUser.setTelNo(telNo);
//			shiroUser.setState(1);
//			shiroUser.setCreateOperator(userInfo.getUsername());
//			shiroUser.setCreateTime(new Date());
//			shiroUser.setDeptId(Integer.valueOf(dept));
//			try {
//				i = userService.insertUser(shiroUser,roleIdArray);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","增加用户成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		return msg;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "deleteUser.do")
//	@ResponseBody
//	public Map<String,Object> deleteUser(Integer userId) throws Exception {
//		Map<String,Object> msg=new HashMap<>();
//		boolean isReturn = false;
//		if (userId == null ) {
//			msg.put("msg","用户ID不能为空");
//			msg.put("state",false);
//			isReturn = true;
//		}
//		if (!isReturn) {
//			int i = 0;
//			try {
//				i = userService.deleteUserById(userId);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","删除用户成功");
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				msg.put("state",false);
//				msg.put("msg",e.getMessage());
//			}	
//		}
//		return msg;
//	}
//	
//	@RequestMapping(value = "/findUserById.do")
//	@ResponseBody
//	public ShiroUser findUserById(String userId) throws Exception{
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		ShiroUser shiroUser = userService.findUserById(uId);
//		return shiroUser;
//	}
	
	@RequestMapping(value = "findUserMenuRigth.do")
	@ResponseBody
	public List<Node> findUserMenuRigth(String userId) throws Exception {
		String[] params = userId.split(":");
		Integer uId = Integer.valueOf(params[1]);
		//System.out.println(" findUserMenuRigth Json");
		Node node = new Node();
		List<Node> nodes = new ArrayList<>();
		
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			List<ShiroRigth> shiroRigths = shiroRigthService.findUserRolePrivilegeRigth(uId);//角色对应的权限
			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
			List<ShiroRigth> shiroRigths2 = new ArrayList<>();
			for (UserRigth userRigth : userRigths) {
				shiroRigths2.add(userRigth.getShiroRigth());
			}
			List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
			List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
			
			shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
			List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
			srs = new ArrayList(new HashSet(srs));//去掉重复的
			
			node = new Node("root","0","true","用户权限","true","true");
			nodes.add(node);
			for (SysMenu sysMenu : sysMenus) {
				node = new Node();
				node.setId(sysMenu.getId().toString());
				String pId = "root";
				if(sysMenu.getParentId() != null){
					pId = sysMenu.getParentId().toString();
				}
				node.setpId(pId);
				node.setIsParent("false");
				node.setOpen("true");
				node.setName(sysMenu.getMenuName());
				node.setChecked("false");
				for (ShiroRigth shiroRigth : srs) {
					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
						node.setChecked("true");
						break;
					}
					
				}
				
				nodes.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return nodes;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "findUserRigth.do")
	@ResponseBody
	public List<Node> findUserRigth(Integer parentId,Integer userId) throws Exception {
//		String[] params = userId.split(":");
		Integer uId = userId;
		//System.out.println(" findUserRigth Json");
		Node node = new Node();
		List<Node> nodes = new ArrayList<>();
		
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(parentId);
			List<ShiroRigth> shiroRigths = shiroRigthService.findUserWithRolesPrivilegeRigthByParentId(uId,parentId);//角色对应的权限
			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
			List<ShiroRigth> shiroRigths2 = new ArrayList<>();
			for (UserRigth userRigth : userRigths) {
				shiroRigths2.add(userRigth.getShiroRigth());
			}
			List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
			List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
			
			shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
			List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
			srs = new ArrayList(new HashSet(srs));//去掉重复的
			
			
			for (SysMenu sysMenu : sysMenus) {
				node = new Node();
				node.setId(sysMenu.getId().toString());
				node.setpId(parentId.toString());
				node.setName(sysMenu.getMenuName());
				node.setChecked("false");
				node.setRigthCode(sysMenu.getMenuCode());
				for (ShiroRigth shiroRigth : srs) {
					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
						node.setChecked("true");
						break;
					}
				}
				
				nodes.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return nodes;
	}
//	@RequestMapping(value = "deleteUserRigth.do")
//	@ResponseBody
//	public int deleteUserRigth(String userId,String rigthCode) throws Exception {
//		
//		//TODO deleteUserRigth验证操作session 是否 有权限操作 
//		
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		int i = 0;
//		try {
//			ShiroRigth shiroRigth = shiroRigthService.findShiroRigthByRigthCode(rigthCode);
//			i = userRigthService.deleteUserRigth(uId, shiroRigth.getId());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return i;
//	}
//	
//	@RequestMapping(value = "insertUserRigth.do")
//	@ResponseBody
//	public int insertUserRigth(String userId,String rigthCode) throws Exception {
//		
//		//TODO insertUserRigth验证操作session 是否 有权限操作 
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		int i = 0;
//		try {
//			ShiroRigth shiroRigth = shiroRigthService.findShiroRigthByRigthCode(rigthCode);
//			i = userRigthService.insertUserRigth(uId, shiroRigth.getId());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return i;
//	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "saveUserRigth.do")
	@ResponseBody
	public Map<String,Object> saveUserRigth(Integer userId,Integer parentId,String rigthCode) throws Exception {
		String[] rigthCodeArray = rigthCode.split(",");
		
		//TODO insertUserRigth验证操作session 是否 有权限操作 
//		String[] params = userId.split(":");
		Integer uId = userId;
		int i = 0;
		Map<String,Object> msg=new HashMap<>();
		try {
			i = userRigthService.saveUserRigth(uId,parentId,rigthCodeArray);
			if (i>0) {
				msg.put("state",true);
				msg.put("msg","保存成功");
			}else{
				msg.put("state",true);
				msg.put("msg","没有保存任何数据");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			msg.put("state",false);
			msg.put("msg",e.getMessage());
		}	
		return msg;
	}
	
	
	
	@RequestMapping(value = "/toUserRigth.do")
	public String toUserRigth(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		String str = "";
		String userId = params.get("userId");
		Integer uId = Integer.valueOf(userId);
		try {
			
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			List<ShiroRigth> shiroRigths = shiroRigthService.findUserRolePrivilegeRigth(uId);//角色对应的权限
			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
			List<ShiroRigth> shiroRigths2 = new ArrayList<>();
			for (UserRigth userRigth : userRigths) {
				shiroRigths2.add(userRigth.getShiroRigth());
			}
			List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
			List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
			
			shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
			List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
			srs = new ArrayList(new HashSet(srs));//去掉重复的
			
			list.add("{ id:'"+Constants.sys_menu_root_id+"', pId:9999, name:'用户权限', open:true}");
			for (SysMenu sysMenu : sysMenus) {
				String pId = Constants.sys_menu_root_id.toString();
				if(sysMenu.getParentId() != null){
					pId = sysMenu.getParentId().toString();
				}
				
				boolean checked = false;
				for (ShiroRigth shiroRigth : srs) {
					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
						checked = true;
						break;
					}
				}
				list.add("{ id:'"+sysMenu.getId()+"', pId:'"+pId+"', name:'"+sysMenu.getMenuName()+"', checked:'"+checked+"', open:true}");
				
			}
			str = StringUtils.join(list,",\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("userRigthTree", str);
		model.put("params", params);
		return "sys/userRigth";
	}
	
	//返回数据
	
//	@RequestMapping("sysUserInfoList")
//	@ResponseBody
//	public Page<ShiroUser> getAllUser(@ModelAttribute("shiroUser")ShiroUser user,@ModelAttribute("sort")Sort sort,
//			@ModelAttribute("page")Page<ShiroUser> page) throws Exception{
//		userService.findUsers(user,sort,page);
//		return page;
//	}
//	@RequestMapping(value = "/sysRoleInfoList.do")
//	@ResponseBody
//	public Page<ShiroUser> sysRoleInfoList(@RequestParam Map<String, String> params){
//		return null;
//	}
	@RequestMapping(value = "/toUpdatePwd.do")
	public String toUpdatePwd(ModelMap model, @RequestParam Map<String, String> params){
		return "sys/updatePwd";
	}
//	
//	@RequestMapping(value = "/updatePwd.do")
//	@ResponseBody
//	public Map<String,Object> updatePwd(ModelMap model, @RequestParam Map<String, String> params){
//		Map<String,Object> msg=new HashMap<>();
//		String oldPass = params.get("oldPass");
//		String password = params.get("password");
//		String password2 = params.get("password2");
//		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
//			    .getAuthentication()
//			    .getPrincipal();
//		
//		ShiroUser shiroUser = userService.findUserById(userInfo.getUserId());
//		
//		String oldPassMd5 = DigestUtils.md5Hex(oldPass+"{"+shiroUser.getUserName()+"}");
//		boolean isReturn = false;
//		if (StringUtils.isBlank(oldPass)) {
//			msg.put("msg","旧密码不能为空");
//			isReturn = true;
//		}else if(StringUtils.isBlank(password) ||StringUtils.isBlank(password2) ){
//			msg.put("msg","新密码不能为空");
//			isReturn = true;
//		}
//		else if (!password.equals(password2)) {
//			msg.put("msg","两次输入的密码不相同");
//			isReturn = true;
//		}
//		else if (!shiroUser.getPassword().equals(oldPassMd5)) {
//			msg.put("msg","旧密码不正确");
//			isReturn = true;
//		}
//		 
//		if (!isReturn) {
//			int i = 0;
//			String newPassMd5 = DigestUtils.md5Hex(password+"{"+shiroUser.getUserName()+"}");
//			try {
//				i = userService.updateUserPwd(shiroUser.getId(), newPassMd5);
//				if (i>0) {
//					msg.put("state",true);
//					msg.put("msg","修改密码成功");
//				}
//				else{
//					msg.put("msg","修改密码失败");
//				}
//			} catch (Exception e) {
//				log.error("修改密码失败",e);
//			}
//		}
//		return msg;
//	}
	
	/**
	 * 跳转到  数据字典    页面
	 * @param model
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/toSysDict.do")
	public String toSysDict(ModelMap model, @RequestParam Map<String, String> params){
		/*List<SysDict> userStateList = null;
		try {
			userStateList = sysDictService.findSysDictGroup("sys_user_state");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("userStateList", userStateList);*/
		return "sys/sysDict";
	}
	
	/**
	 *获取到所有的  数据字典  
	 * @param sysDict
	 * @param params
	 * @param sort
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/findSysDictList.do")
	@ResponseBody
	public Page<SysDict> findSysDictList(@ModelAttribute SysDict sysDict, @RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<SysDict> page){
		try {
			sysDictService.findSysDicts(sysDict, sort, page) ;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return page;
	}
	
//	/**
//	 * 跳转到   新增数据字典   页面
//	 * @param model
//	 * @param params
//	 * @return
//	 */
//	@RequestMapping(value = "/toAddSysDict.do")
//	public String toAddSysDict(ModelMap model, @RequestParam Map<String, String> params){
//		
//		return "sys/addSysDict";
//	}
	
//	/**
//	 * 新增一个  数据字典  
//	 * @param sysDict
//	 * @return
//	 */
//	@RequestMapping(value = "/addSysDict.do")
//	@ResponseBody
//	public Map<String,Object> addSysDict(@ModelAttribute SysDict sysDict){		
//		Map<String,Object> msg=new HashMap<>();
//
//		try{
//			if(sysDictService.findSysDictExist(sysDict)){
//				msg.put("state",false);
//				msg.put("msg","该条记录已存在,不可再添加！");
//				return msg ;
//			} 
//		}catch(Exception e){
//				log.error("查询数据字典是否存在失败！",e);
//				msg.put("state",false);
//				msg.put("msg","查询数据字典是否存在失败！");
//				return msg ;
//		}
//		
//		try{
//			sysDictService.insertSysDict(sysDict) ;
//		}catch(Exception e){
//				log.error("新增数据字典失败！",e);
//				msg.put("state",false);
//				msg.put("msg","新增数据字典失败！");
//				return msg ;
//		}
//		msg.put("state",true);
//		msg.put("msg","新增数据字典成功！");
//		return msg;
//	}
//	
//	/**
//	 * 跳转到   修改数据字典  页面
//	 * @param id
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value = "/toUpdateSysDict.do")
//	public String toUpdateSysDict(Integer id,ModelMap model){		
//		System.out.println("id-->"+id);
//		SysDict sysDict = sysDictService.findSysDictById(id) ;
//		model.put("sysDict", sysDict) ;
//		
//		return "sys/updateSysDict";
//	}
//	
//	@RequestMapping(value = "/updateSysDict.do")
//	@ResponseBody
//	public Map<String,Object> updateSysDict(@ModelAttribute SysDict sysDict){		
//		Map<String,Object> msg=new HashMap<>();
//		try{
//			SysDict oldSysDict = sysDictService.findSysDictById(sysDict.getId());
//			sysDictService.updateSysDict(oldSysDict,sysDict) ;
//			
//		}catch(Exception e){
//				log.error("修改数据字典失败！",e);
//				msg.put("state",false);
//				msg.put("msg","修改数据字典失败！");
//				return msg ;
//		}
//		msg.put("state",true);
//		msg.put("msg","修改数据字典成功！");
//		return msg;
//	}
//	
//	@RequestMapping(value = "/updateStatus.do")
//	@ResponseBody
//	public Map<String,Object> updateStatus(@RequestParam(value="status", required=true)String status,@RequestParam(value="id", required=true)Integer id){		
//		Map<String,Object> msg=new HashMap<>();
//
//		try{
//			SysDict oldSysDict = sysDictService.findSysDictById(id);	
//			SysDict sysDict = oldSysDict;
//			sysDict.setStatus(status);
//			sysDictService.updateSysDict(oldSysDict,sysDict);
//			
//		}catch(Exception e){
//				log.error("修改状态失败！",e);
//				msg.put("state",false);
//				msg.put("msg","修改状态失败！");
//				return msg ;
//		}
//		msg.put("state",true);
//		msg.put("msg","修改状态成功！");
//		return msg;
//	}
	
}
