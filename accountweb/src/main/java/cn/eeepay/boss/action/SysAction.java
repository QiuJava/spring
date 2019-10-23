package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.MenuType;
import cn.eeepay.framework.model.bill.BlockedIp;
import cn.eeepay.framework.model.bill.Node;
import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.model.bill.ShiroRole;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.State;
import cn.eeepay.framework.model.bill.SysDept;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.bill.UserRigth;
import cn.eeepay.framework.model.bill.UserRole;
import cn.eeepay.framework.model.bill.UserSetting;
import cn.eeepay.framework.service.bill.BlockedIpService;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.RoleRigthService;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.ShiroRoleService;
import cn.eeepay.framework.service.bill.ShiroUserService;
import cn.eeepay.framework.service.bill.SysDeptService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.service.bill.UserRigthService;
import cn.eeepay.framework.service.bill.UserRoleService;
import cn.eeepay.framework.service.bill.UserSettingService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.UrlUtil;

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
	@Resource
	public ShiroUserService shiroUserService;
	@Resource
	public SysDeptService sysDeptService;
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
	@Resource
	public UserSettingService userSettingService;
	@Resource
	private RedisService redisService;
	@Resource
	private BlockedIpService blockedIpService;
	@Resource
	public SessionRegistry sessionRegistry;  
	
	private static final Logger log = LoggerFactory.getLogger(SysAction.class);
//	@RequestMapping(value = "/menu.do")
//	public String menu(ModelMap model, @RequestParam Map<String, String> params){
//		List<String> list = new ArrayList<String>();
//		String str = "";
//		try {
//			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//			list.add("{ id:'"+Constants.sys_menu_root_id+"', pId:9999, name:'系统菜单', open:true}");
//			for (SysMenu sysMenu : sysMenus) {
//				String pId = Constants.sys_menu_root_id.toString(); 
//				if (sysMenu.getParentId() != null) {
//					pId = sysMenu.getParentId().toString();
//				}
//				list.add("{ id:'"+sysMenu.getId()+"', pId:'"+pId+"', name:'"+sysMenu.getMenuName()+"', open:true}");
//			}
//			str = StringUtils.join(list,",\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.put("menuTree", str);
//		return "sys/menu";
//	}
	
	@PreAuthorize("hasAuthority('menu:query')")
	@RequestMapping(value = "/menu.do")
	public String menu(ModelMap model, @RequestParam Map<String, String> params){
		return "sys/menu";
	}
	@PreAuthorize("hasAuthority('menu:query')")
	@RequestMapping(value = "/menuTree.do")
	@ResponseBody
	public List<Node> menuTree(@RequestParam(value = "menuId", required = false)Integer menuId) {
		List<Node> nodes = new ArrayList<Node>();
		Node node = new Node();
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			State state = new State(true, false);
			node = new Node(Constants.sys_menu_root_id.toString(),"#","系统菜单",state,"");
			nodes.add(node);
			boolean selected = false;
			for (SysMenu sysMenu : sysMenus) {
				node = new Node();
				node.setId(sysMenu.getId().toString());
				
				String pId = Constants.sys_menu_root_id.toString(); 
				if (sysMenu.getParentId() != null) {
					pId = sysMenu.getParentId().toString();
				}
				node.setParent(pId);
				node.setText(sysMenu.getMenuName());
				
				selected = false;
				if (menuId != null) {
					if (menuId.equals(sysMenu.getId())) {
						selected = true;
					}
				} else {
					if (sysMenu.getId().equals(5)) {
						selected = true;
					}
				}
				
				node.setState(new State(false, selected));
				node.setRigthCode(sysMenu.getMenuCode());
				nodes.add(node);
			}
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		
		return nodes;
	}
	@PreAuthorize("hasAuthority('menu:query')")
	@RequestMapping(value = "/findMenuById.do")
	@ResponseBody
	public SysMenu findMenuById(Integer menuId) throws Exception{
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
		SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
		return sysMenu;
	}
	@PreAuthorize("hasAuthority('menu.function:query')")
	@RequestMapping(value = "findMenuFunctionList.do")
	@ResponseBody
	public List<SysMenu> findMenuFunctionList(Integer menuId, @ModelAttribute("sort") Sort sort){
		List<SysMenu> sysMenus = null;
		try {
			//subjectService.findSubject(subject,sort,page);
			sysMenus = sysMenuService.findAllPageByParentId(menuId, sort);
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}	
		return sysMenus;
	}
	
	@PreAuthorize("hasAuthority('menu.function:update')")
	@RequestMapping(value = "updateFunction.do")
	@Logs(description="修改功能")
	@ResponseBody
	public Map<String,Object> updateFunction(Integer id,String menuName,String menuCode,String menuUrl,String orderNo) throws Exception {
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
			sysMenu.setRigthCode(menuCode);
			sysMenu.setMenuUrl(menuUrl);
			sysMenu.setOrderNo(orderNo);
			try {
				int i = 0;
				i = sysMenuService.updateSysMenu(sysMenu);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","修改成功");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
			}	
		}
		return msg;
	}
	
	@PreAuthorize("hasAuthority('menu.function:insert')")
	@RequestMapping(value = "addFunction.do")
	@Logs(description="新增功能")
	@ResponseBody
	public Map<String,Object> addFunction(Integer menuId,String menuName,String menuCode,String menuUrl,String orderNo) throws Exception {
		//TODO addFunction验证操作session 是否 有权限操作 
		Map<String,Object> msg=new HashMap<>();
		
		boolean isReturn = false;
		if (StringUtils.isBlank(menuName)) {
			msg.put("msg","功能名称不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if(StringUtils.isBlank(menuCode) ){
			msg.put("msg","功能编码不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if(StringUtils.isBlank(menuUrl) ){
			msg.put("msg","功能路径不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		else if(StringUtils.isBlank(orderNo) ){
			msg.put("msg","排序号不能为空");
			msg.put("status",false);
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
			sysMenu.setMenuLevel(sysMenu1.getMenuLevel() + 1);
			sysMenu.setMenuType(MenuType.PAGE.toString());
			sysMenu.setParentId(menuId);
			try {
				i = sysMenuService.insertSysMenu(sysMenu);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","保存成功");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
			}	
		}
		
		return msg;
	}
	@PreAuthorize("hasAuthority('menu.function:delete')")
	@RequestMapping(value = "deleteFunction.do")
	@Logs(description="删除功能")
	@ResponseBody
	public Map<String,Object> deleteFunction(Integer menuId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		
		boolean isReturn = false;
		if (menuId == null) {
			msg.put("msg","功能ID不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			try {
				i = sysMenuService.deleteSysMenu(menuId);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","删除成功");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
			}	
		}
		
		return msg;
	}
	@PreAuthorize("hasAuthority('user:query')")
	@RequestMapping(value = "/toUser.do")
	public String toUser(ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> userStateList = null;
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		params.put("sortname", "");
		params.put("sortorder", "");
		
		String queryParams = params.get("queryParams");
		if (StringUtils.isNotBlank(queryParams)) {
			log.info(queryParams);
		    byte[] b = Base64.decode(queryParams.getBytes());
		    String s=new String(b);
		    log.info(s);
		    Map<String, Object> queryParamsMap= UrlUtil.getUrlParams(s);
	    	for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
				params.put(entry.getKey(), entry.getValue().toString());
			}
		}
		try {
			userStateList = sysDictService.findSysDictGroup("sys_user_state");
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		model.put("userStateList", userStateList);
		model.put("params", params);
		return "sys/user";
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PreAuthorize("hasAuthority('user:update')")
	@RequestMapping(value = "/toUpdateUser.do")
	public String toUpdateUser(ModelMap model, @RequestParam Map<String, String> params){
		String id = params.get("id");
		Integer uId = Integer.valueOf(id);
		List<SysDict> userStateList = null;
		ShiroUser shiroUser = null;
		List<String> list = new ArrayList<String>();
		List<SysDept> deptList = null;
		String str = "";
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			
			deptList = sysDeptService.findAllSysDeptList();
			shiroUser = shiroUserService.findUserById(uId);
			userStateList = sysDictService.findSysDictGroup("sys_user_state");
			
			boolean checkboxAllDisabled = false;
			
			List<UserRole> userRoles = userRoleService.findUserRoleByUserId(uId);//获取用户对应的角色
			
			List<ShiroRole> srs = new ArrayList<>();//用户对应角色实体
			for (UserRole userRole : userRoles) {
				srs.add(userRole.getShiroRole());
			}
			
			List<ShiroRole> shiroRoles = new ArrayList<>();
			if (userInfo.isAdmin()) {
				shiroRoles= shiroRoleService.findAllShiroRole();
			}
			else{
				List<UserRole> currentUserRoles = userRoleService.findUserRoleByUserId(userInfo.getUserId());
				for (UserRole currentUserRole : currentUserRoles) {
					shiroRoles.add(currentUserRole.getShiroRole());
				}
				List<ShiroRole> srs1 = ListUtils.intersection(shiroRoles, srs);//交集
				List<ShiroRole> srs2 = ListUtils.subtract(srs, srs1);//相减
				if (srs2.size() > 0) {
					checkboxAllDisabled = true;
				}
				shiroRoles = ListUtils.union(shiroRoles, srs2);//合并
				shiroRoles = new ArrayList(new HashSet(shiroRoles));//去掉重复的用户真正的权限
			}
			
			list.add("{ id:'root', parent:'#', text:'用户角色', state:{ opened: true, disabled:"+checkboxAllDisabled+"}}");
			for (ShiroRole shiroRole : shiroRoles) {
				boolean checked = false;
				boolean disabled = false;
				for (ShiroRole sr : srs) {
					if (shiroRole.getRoleCode().equals(sr.getRoleCode())) {
						checked = true;
						if (checkboxAllDisabled) {
							disabled = true;
						}
						break;
					}
				}
				list.add("{ id:'"+shiroRole.getId()+"', parent:'root', text:'"+shiroRole.getRoleName()+"', state:{ selected: "+checked+" , disabled:"+disabled+"}}");
			}
			str = StringUtils.join(list,",\n");
			
		} catch (Exception e) {
			log.error("异常:",e);
		}
		log.info("userRoleTree="+ str);
		model.put("userRoleTree", str);
		model.put("user", shiroUser);
		model.put("userStateList", userStateList);
		model.put("deptList", deptList);
		model.put("params", params);
		return "sys/updateUser";
	}
	@PreAuthorize("hasAuthority('user:query')")
	@RequestMapping(value = "/findUsers.do")
	@ResponseBody
	public Page<ShiroUser> findUsers(@ModelAttribute ShiroUser shiroUser, @RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<ShiroUser> page){
		try {
			shiroUserService.findUsers(shiroUser, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}	
		return page;
	}
	

	@PreAuthorize("hasAuthority('user:insert')")
	@RequestMapping(value = "/toAddUser.do")
	public String toAddUser(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		List<SysDept> deptList = null;
		List<SysDict> userStateList = null;
		String str = "";
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			List<ShiroRole> shiroRoles = new ArrayList<>();  
			if (userInfo.isAdmin()) {
				shiroRoles = shiroRoleService.findAllShiroRole();
			}
			else{
				List<UserRole> userRoles = userRoleService.findUserRoleByUserId(userInfo.getUserId());
				for (UserRole userRole : userRoles) {
					shiroRoles.add(userRole.getShiroRole());
				}
			}
			
			deptList = sysDeptService.findAllSysDeptList();
			userStateList = sysDictService.findSysDictGroup("sys_user_state");
			list.add("{ id:'root', parent:'#', text:'用户角色', state:{opened: true}}");
			for (ShiroRole sr : shiroRoles) {
				//boolean checked = false;
				list.add("{ id:'"+sr.getId()+"', parent:'root', text:'"+sr.getRoleName()+"',state:{selected:false}}");
			}
			str = StringUtils.join(list,",\n");
			
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		log.info("userRoleTree="+ str);
		model.put("userRoleTree", str);
		model.put("deptList", deptList);
		model.put("userStateList", userStateList);
		return "sys/addUser";
	}
	@PreAuthorize("hasAuthority('user:update')")
	@RequestMapping(value = "updateUser.do")
	@Logs(description="修改用户")
	@ResponseBody
	public Map<String,Object> updateUser(String deptId,String userId,String userName,String realName,String email,String telNo,Integer state,String roleIds) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		String[] roleIdArray = roleIds.split(",");
		boolean isReturn = false;
		if (StringUtils.isBlank(userName)) {
			msg.put("msg","登录名不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if (StringUtils.isBlank(realName)) {
			msg.put("msg","真实名称不能为空");
			msg.put("state",false);
			isReturn = true;
		}else if (StringUtils.isBlank(deptId)) {
			msg.put("msg","部门不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			Integer uId = Integer.valueOf(userId);
			ShiroUser shiroUser = shiroUserService.findUserById(uId);
			if (shiroUser == null) {
				msg.put("msg","用户不存在");
				msg.put("state",false);
				log.info(msg.toString());
				return msg;
			}
			if (!shiroUser.getUserName().equals(userName)) {
				ShiroUser existShiroUser = shiroUserService.findUserByUserName(userName);
				if (existShiroUser != null) {
					msg.put("msg","登录名已经存在请更换");
					msg.put("state",false);
					log.info(msg.toString());
					return msg;
				}
			}
			int i = 0;
			shiroUser.setId(uId);
			shiroUser.setUserName(userName);
			shiroUser.setRealName(realName);
			shiroUser.setEmail(email);
			shiroUser.setTelNo(telNo);
			shiroUser.setState(state);
			shiroUser.setDeptId(Integer.valueOf(deptId));
			try {
				i = shiroUserService.updateUser(shiroUser,roleIdArray);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","修改用户成功");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("state",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('user:resetUserPwd')")
	@RequestMapping(value = "resetUserPwd.do")
	@Logs(description="重置用户密码")
	@ResponseBody
	public Map<String,Object> resetUserPwd(String userId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(userId)) {
			msg.put("msg","userId参数不能为空");
			msg.put("state",false);
			isReturn = true;
		}
		if (!isReturn) {
			Integer uId = Integer.valueOf(userId);
			ShiroUser shiroUser = shiroUserService.findUserById(uId);
			int i = 0;
			String password ="88888888";
			String newPassMd5 = DigestUtils.md5Hex(password+"{"+shiroUser.getUserName()+"}");
			try {
				i = shiroUserService.updateUserPwd(shiroUser.getId(), newPassMd5);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg",shiroUser.getUserName() + ",重置密码成功,密码重置为 "+password);
				}
				else{
					msg.put("msg","重置密码失败");
				}
			} catch (Exception e) {
				log.error("重置密码失败",e);
			}
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('user:insert')")
	@RequestMapping(value = "addUser.do")
	@Logs(description="新增用户")
	@ResponseBody
	public Map<String,Object> addUser(String deptId,String userName,String realName,String email,String telNo,String roleIds) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		String[] roleIdArray = roleIds.split(",");
		boolean isReturn = false;
		if (StringUtils.isBlank(userName)) {
			msg.put("msg","登录名不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if (StringUtils.isBlank(realName)) {
			msg.put("msg","用户名不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if (StringUtils.isBlank(deptId)) {
			msg.put("msg","部门不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			ShiroUser existShiroUser = shiroUserService.findUserByUserName(userName);
			if (existShiroUser != null) {
				msg.put("msg","登录名已经存在请更换");
				msg.put("status",false);
				log.info(msg.toString());
				return msg;
			}
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			String newPassMd5 = DigestUtils.md5Hex("88888888{"+userName+"}");
			int i = 0;
			ShiroUser shiroUser = new ShiroUser();
			shiroUser.setUserName(userName);
			shiroUser.setPassword(newPassMd5);
			shiroUser.setRealName(realName);
			shiroUser.setEmail(email);
			shiroUser.setTelNo(telNo);
			shiroUser.setState(1);
			shiroUser.setCreateOperator(userInfo.getUsername());
			shiroUser.setCreateTime(new Date());
			shiroUser.setDeptId(Integer.valueOf(deptId));
			try {
				i = shiroUserService.insertUser(shiroUser,roleIdArray);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","增加用户成功");
					log.info(msg.toString());
				}
				else{
					msg.put("status",false);
					msg.put("msg","增加用户失败");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('user:delete')")
	@RequestMapping(value = "deleteUser.do")
	@Logs(description="删除用户")
	@ResponseBody
	public Map<String,Object> deleteUser(Integer userId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (userId == null ) {
			msg.put("msg","用户ID不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		else if (userId == 1) {
			msg.put("msg","系统管理员不能删除");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			int i = 0;
			try {
				i = shiroUserService.deleteUserById(userId);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","删除用户成功");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
			}	
		}
		return msg;
	}
//	@PreAuthorize("hasAuthority('user:delete')")
//	@RequestMapping(value = "/findUserById.do")
//	@ResponseBody
//	public ShiroUser findUserById(String userId) throws Exception{
//		String[] params = userId.split(":");
//		Integer uId = Integer.valueOf(params[1]);
//		ShiroUser shiroUser = userService.findUserById(uId);
//		return shiroUser;
//	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PreAuthorize("hasAuthority('user:findUserRigth')")
	@RequestMapping(value = "findUserRigth.do")
	@ResponseBody
	public List<Node> findUserRigth(Integer parentId,Integer userId) throws Exception {
//		String[] params = userId.split(":");
		Integer uId = userId;
		System.out.println(" findUserRigth Json");
		Node node = new Node();
		List<Node> nodes = new ArrayList<>();
		
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(parentId);
			List<ShiroRigth> shiroRigths = shiroRigthService.findUserWithRolesPrivilegeRigthByParentId(uId,parentId);//角色对应的权限
			List<String> rigthCodeRoleList = new ArrayList<>();//角色对应的权限编码
    		for (ShiroRigth shiroRigth : shiroRigths) {
    			rigthCodeRoleList.add(shiroRigth.getRigthCode());
			}
    		rigthCodeRoleList = new ArrayList(new HashSet(rigthCodeRoleList));//去掉重复
    		
			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
			List<String> rigthCodeUserList = new ArrayList<>();//用户对应的权限编码
    		for (UserRigth userRigth : userRigths) {
    			rigthCodeUserList.add(userRigth.getShiroRigth().getRigthCode());
			}
    		rigthCodeUserList = new ArrayList(new HashSet(rigthCodeUserList));//去掉重复
			
			List<String> srs1 = ListUtils.intersection(rigthCodeRoleList, rigthCodeUserList);//交集
			List<String> srs2 = ListUtils.subtract(rigthCodeUserList, srs1);//相减
			
			rigthCodeRoleList = ListUtils.subtract(rigthCodeRoleList, srs1);//相减
			List<String> srs = ListUtils.union(rigthCodeRoleList, srs2);
			srs = new ArrayList(new HashSet(srs));//去掉重复的
			
			for (SysMenu sysMenu : sysMenus) {
				node = new Node();
				node.setId(sysMenu.getId().toString());
				node.setParent(parentId.toString());
				node.setText(sysMenu.getMenuName());
				node.setState(new State(false, false));
				node.setRigthCode(sysMenu.getMenuCode());
				for (String sr : srs) {
					if (sysMenu.getMenuCode().equals(sr)) {
						node.setState(new State(false, true));
						break;
					}
				}
				
				nodes.add(node);
			}
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}	
		return nodes;
	}
	@PreAuthorize("hasAuthority('user:saveUserRigth')")
	@RequestMapping(value = "saveUserRigth.do")
	@Logs(description="保存用户权限")
	@ResponseBody
	public Map<String,Object> saveUserRigth(@RequestParam Map<String, String> params) throws Exception {
		int i = 0;
		Map<String,Object> msg=new HashMap<>();
		try {
			ObjectMapper om = new ObjectMapper();
			List<Map<String, String>> list = om.readValue(params.get("dataArr"), new TypeReference<List<Map<String, String>>>() {});
			if (!list.isEmpty()) {
				i = userRigthService.saveAllUserRigth(list);
			}
			if (i>0) {
				msg.put("status",true);
				msg.put("msg","保存成功");
				log.info(msg.toString());
			}else{
				msg.put("status",false);
				msg.put("msg","没有保存任何数据");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			log.error("异常:",e);
			//e.printStackTrace();
			msg.put("state",false);
			msg.put("msg",e.getMessage());
			log.error(msg.toString());
		}	
		log.info(msg.toString());
		return msg;
	}
	
	
	@PreAuthorize("hasAuthority('user:findUserRigth')")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/toUserRigth.do")
	public String toUserRigth(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		String str = "";
		String userId = params.get("userId");
		Integer uId = Integer.valueOf(userId);
		try {
			
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			List<ShiroRigth> shiroRigths = shiroRigthService.findUserRoleRigthByUserId(uId);//角色对应的权限
			List<String> rigthCodeRoleList = new ArrayList<>();//角色对应的权限编码
    		for (ShiroRigth shiroRigth : shiroRigths) {
    			rigthCodeRoleList.add(shiroRigth.getRigthCode());
			}
    		rigthCodeRoleList = new ArrayList(new HashSet(rigthCodeRoleList));//去掉重复
    		
			List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
			List<String> rigthCodeUserList = new ArrayList<>();//用户对应的权限编码
    		for (UserRigth userRigth : userRigths) {
    			rigthCodeUserList.add(userRigth.getShiroRigth().getRigthCode());
			}
    		rigthCodeUserList = new ArrayList(new HashSet(rigthCodeUserList));//去掉重复
    		
//			List<ShiroRigth> shiroRigths2 = new ArrayList<>();
//			for (UserRigth userRigth : userRigths) {
//				shiroRigths2.add(userRigth.getShiroRigth());
//			}
			List<String> srs1 = ListUtils.intersection(rigthCodeRoleList, rigthCodeUserList);//交集
			List<String> srs2 = ListUtils.subtract(rigthCodeUserList, srs1);//相减
			
			rigthCodeRoleList = ListUtils.subtract(rigthCodeRoleList, srs1);//相减
			List<String> srs = ListUtils.union(rigthCodeRoleList, srs2);
			srs = new ArrayList(new HashSet(srs));//去掉重复的
			
			list.add("{ id:'"+Constants.sys_menu_root_id+"', parent:'#', text:'用户权限', state:{opened: true,selected:true}}");
			for (SysMenu sysMenu : sysMenus) {
				String pId = Constants.sys_menu_root_id.toString();
				if(sysMenu.getParentId() != null){
					pId = sysMenu.getParentId().toString();
				}
				
				boolean checked = false;
				for (String sr : srs) {
					if (sysMenu.getMenuCode().equals(sr)) {
						checked = true;
						break;
					}
				}
				list.add("{ id:'"+sysMenu.getId()+"', parent:'"+pId+"', text:'"+sysMenu.getMenuName()+"', state:{selected:false}}");
				
			}
			str = StringUtils.join(list,",\n");
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		ShiroUser shiroUser = null ;
		try {
			shiroUser = shiroUserService.findUserById(uId);
		}  catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		model.put("shiroUser", shiroUser) ;
		model.put("userRigthTree", str);
		model.put("params", params);
		
//		log.info("userRigthTree=" + str);
		return "sys/userRigth";
	}
	
	//返回数据
	@RequestMapping(value = "/sysRoleInfoList.do")
	@ResponseBody
	public Page<ShiroUser> sysRoleInfoList(@RequestParam Map<String, String> params){
		return null;
	}
	@PreAuthorize("hasAuthority('updatePwd:query')")
	@RequestMapping(value = "/toUpdatePwd.do")
	public String toUpdatePwd(ModelMap model, @RequestParam Map<String, String> params){
		return "sys/updatePwd";
	}
	
	@PreAuthorize("hasAuthority('updatePwd:update')")
	@RequestMapping(value = "/updatePwd.do")
	@Logs(description="修改密码")
	@ResponseBody
	public Map<String,Object> updatePwd(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		String oldPass = params.get("oldPass");
		String password = params.get("password");
		String password2 = params.get("password2");
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		
		ShiroUser shiroUser = shiroUserService.findUserById(userInfo.getUserId());
		
		String oldPassMd5 = DigestUtils.md5Hex(oldPass+"{"+shiroUser.getUserName()+"}");
		boolean isReturn = false;
		if (StringUtils.isBlank(oldPass)) {
			msg.put("msg","旧密码不能为空");
			isReturn = true;
		}else if(StringUtils.isBlank(password) ||StringUtils.isBlank(password2) ){
			msg.put("msg","新密码不能为空");
			isReturn = true;
		}
		else if (!password.equals(password2)) {
			msg.put("msg","两次输入的密码不相同");
			isReturn = true;
		}
		else if (!shiroUser.getPassword().equals(oldPassMd5)) {
			msg.put("msg","旧密码不正确");
			isReturn = true;
		}
		 
		if (!isReturn) {
			int i = 0;
			String newPassMd5 = DigestUtils.md5Hex(password+"{"+shiroUser.getUserName()+"}");
			try {
				i = shiroUserService.updateUserPwd(shiroUser.getId(), newPassMd5);
				if (i>0) {
					msg.put("state",true);
					msg.put("msg","修改密码成功");
				}
				else{
					msg.put("msg","修改密码失败");
				}
			} catch (Exception e) {
				log.error("修改密码失败",e);
			}
		}
		log.info(msg.toString());
		return msg;
	}
	
	/**
	 * 跳转到  数据字典    页面
	 * @param model
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('sysDict:query')")
	@RequestMapping(value = "/toSysDict.do")
	public String toSysDict(ModelMap model, @RequestParam Map<String, String> params){
		/*List<SysDict> userStateList = null;
		try {
			userStateList = sysDictService.findSysDictGroup("sys_user_state");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("userStateList", userStateList);*/
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		params.put("sortname", "");
		params.put("sortorder", "");
		
		String queryParams = params.get("queryParams");
		if (StringUtils.isNotBlank(queryParams)) {
			log.info(queryParams);
		    byte[] b = Base64.decode(queryParams.getBytes());
		    String s=new String(b);
		    log.info(s);
		    Map<String, Object> queryParamsMap= UrlUtil.getUrlParams(s);
	    	for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
				params.put(entry.getKey(), entry.getValue().toString());
			}
		}
		model.put("params", params);
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
	@PreAuthorize("hasAuthority('sysDict:query')")
	@RequestMapping(value = "/findSysDictList.do")
	@ResponseBody
	public Page<SysDict> findSysDictList(@ModelAttribute SysDict sysDict, @RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<SysDict> page){
		try {
			sysDictService.findSysDicts(sysDict, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}	
		return page;
	}
	
	/**
	 * 跳转到   新增数据字典   页面
	 * @param model
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('sysDict:insert')")
	@RequestMapping(value = "/toAddSysDict.do")
	public String toAddSysDict(ModelMap model, @RequestParam Map<String, String> params){
		
		return "sys/addSysDict";
	}
	
	/**
	 * 新增一个  数据字典  
	 * @param sysDict
	 * @return
	 */
	@PreAuthorize("hasAuthority('sysDict:insert')")
	@RequestMapping(value = "/addSysDict.do")
	@Logs(description="新增数字字典")
	@ResponseBody
	public Map<String,Object> addSysDict(@ModelAttribute SysDict sysDict){		
		Map<String,Object> msg=new HashMap<>();

		try{
			if(sysDictService.findSysDictExist(sysDict)){
				msg.put("state",false);
				msg.put("msg","该条记录已存在,不可再添加！");
				log.info(msg.toString());
				return msg ;
			} 
		}catch(Exception e){
				log.error("查询数据字典是否存在失败！",e);
				msg.put("state",false);
				msg.put("msg","查询数据字典是否存在失败！");
				log.info(msg.toString());
				return msg ;
		}
		
		try{
			if (sysDict.getOrderNo() == null) {
				sysDict.setOrderNo(0);
			}
			sysDictService.insertSysDict(sysDict) ;
		}catch(Exception e){
				log.error("新增数据字典失败！",e);
				msg.put("state",false);
				msg.put("msg","新增数据字典失败！");
				log.info(msg.toString());
				return msg ;
		}
		msg.put("state",true);
		msg.put("msg","新增数据字典成功！");
		log.info(msg.toString());
		return msg;
	}
	
	/**
	 * 跳转到   修改数据字典  页面
	 * @param id
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAuthority('sysDict:update')")
	@RequestMapping(value = "/toUpdateSysDict.do")
	public String toUpdateSysDict(ModelMap model, @RequestParam Map<String, String> params,Integer id){		
		System.out.println("id-->"+id);
		SysDict sysDict = sysDictService.findSysDictById(id) ;
		model.put("sysDict", sysDict) ;
		model.put("params", params);
		return "sys/updateSysDict";
	}
	@PreAuthorize("hasAuthority('sysDict:update')")
	@RequestMapping(value = "/updateSysDict.do")
	@Logs(description="修改数字字典")
	@ResponseBody
	public Map<String,Object> updateSysDict(@ModelAttribute SysDict sysDict){		
		Map<String,Object> msg=new HashMap<>();
		try{
			SysDict oldSysDict = sysDictService.findSysDictById(sysDict.getId());
			sysDictService.updateSysDict(oldSysDict,sysDict) ;
			
		}catch(Exception e){
				log.error("修改数据字典失败！",e);
				msg.put("state",false);
				msg.put("msg","修改数据字典失败！");
				log.info(msg.toString());
				return msg ;
		}
		msg.put("state",true);
		msg.put("msg","修改数据字典成功！");
		log.info(msg.toString());
		return msg;
	}
	@RequestMapping(value = "/updateStatus.do")
	@Logs(description="修改数字字典状态")
	@ResponseBody
	public Map<String,Object> updateStatus(@RequestParam(value="status", required=true)String status,@RequestParam(value="id", required=true)Integer id){		
		Map<String,Object> msg=new HashMap<>();

		try{
			SysDict oldSysDict = sysDictService.findSysDictById(id);	
			SysDict sysDict = oldSysDict;
			sysDict.setStatus(status);
			sysDictService.updateSysDict(oldSysDict,sysDict);
			
		}catch(Exception e){
				log.error("修改状态失败！",e);
				msg.put("state",false);
				msg.put("msg","修改状态失败！");
				log.error(msg.toString());
				return msg ;
		}
		msg.put("state",true);
		msg.put("msg","修改状态成功！");
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('userSetting:query')")
	@RequestMapping(value = "/toUserSetting.do")
	public String toUserSetting(ModelMap model, @RequestParam Map<String, String> params){
		UserSetting us = null;
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			Integer userId = userInfo.getUserId();
			us = userSettingService.findUserSettingByUserId(userId);
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		model.put("userSetting", us);
		return "sys/userSetting";
	}
	
	@RequestMapping(value = "saveUserSettingWithBackLastPage.do")
	@Logs(description="保存用户设置最后访问页")
	@ResponseBody
	public Map<String,Object> saveUserSettingWithBackLastPage(@ModelAttribute UserSetting userSetting) throws Exception {
		
		int i = 0;
		Map<String,Object> msg=new HashMap<>();
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			Integer userId = userInfo.getUserId();
			UserSetting us = userSettingService.findUserSettingByUserId(userId);
			if (us == null) {
				us = new UserSetting();
				us.setUserId(userId);
				us.setBackLastPage(userSetting.getBackLastPage());
				i = userSettingService.insert(us);
			}
			else{
				us.setBackLastPage(userSetting.getBackLastPage());
				i = userSettingService.update(us);
			}
			
			if (i>0) {
				msg.put("status",true);
				msg.put("msg","保存成功");
				log.info(msg.toString());
			}else{
				msg.put("status",true);
				msg.put("msg","没有保存任何数据");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			log.error("异常:",e);
			//e.printStackTrace();
			msg.put("state",false);
			msg.put("msg",e.getMessage());
			log.error(msg.toString());
		}	
		log.info(msg.toString());
		return msg;
	}
	
	@RequestMapping(value = "saveUserSettingWithCollapseMenu.do")
	@Logs(description="保存用户设置收缩菜单")
	@ResponseBody
	public Map<String,Object> saveUserSettingWithCollapseMenu(@ModelAttribute UserSetting userSetting) throws Exception {
		int i = 0;
		Map<String,Object> msg=new HashMap<>();
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			Integer userId = userInfo.getUserId();
			UserSetting us = userSettingService.findUserSettingByUserId(userId);
			if (us == null) {
				us = new UserSetting();
				us.setUserId(userId);
				us.setCollapseMenu(userSetting.getCollapseMenu());
				i = userSettingService.insert(us);
			}
			else{
				us.setCollapseMenu(userSetting.getCollapseMenu());
				i = userSettingService.update(us);
			}
			
			if (i>0) {
				msg.put("status",true);
				msg.put("msg","保存成功");
				log.info(msg.toString());
			}else{
				msg.put("status",true);
				msg.put("msg","没有保存任何数据");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			log.error("异常:",e);
			//e.printStackTrace();
			msg.put("state",false);
			msg.put("msg",e.getMessage());
			log.error(msg.toString());
		}	
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('systemSetting:query')")
	@RequestMapping(value = "/toSystemSetting.do")
	public String toSystem(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		String denyDay = DateUtil.getCurrentDate();
		List<BlockedIp> blockedIpList = blockedIpService.findDenyBlockedIpList(denyDay,new Date());
		String parentRefreshDate = "";
		Object object = redisService.select(Constants.refresh_cache_key);
		if (object instanceof Date) {
			Date date = (Date) object;
			parentRefreshDate = DateUtil.getLongFormatDate(date);
		}
		int numberOnlineUsers =  sessionRegistry.getAllPrincipals().size();
		params.put("parentRefreshDate", parentRefreshDate);
		params.put("numberOnlineUsers", String.valueOf(numberOnlineUsers));
		model.put("blockedIpList", blockedIpList);
		model.put("params", params);
		return "sys/systemSetting";
	}
	@PreAuthorize("hasAuthority('systemSetting:refreshCache')")
	@RequestMapping(value = "/refreshCache.do")
	@Logs(description="刷新缓存")
	@ResponseBody
	public Map<String,Object> refreshCache() throws Exception{
		Map<String,Object> msg=new HashMap<>();
		try {
			sysDictService.init();
			sysMenuService.init();
			userSettingService.init();
			redisService.insertString(Constants.refresh_cache_key, new Date());
			String parentRefreshDate = DateUtil.getLongCurrentDate();
			msg.put("status",true);
			msg.put("parentRefreshDate",parentRefreshDate);
			msg.put("msg","刷新成功");
			log.info(msg.toString());
		} catch (Exception e) {
			log.error("异常:",e);
			msg.put("status",false);
			msg.put("msg",e.getMessage());
			log.error(msg.toString());
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('systemSetting:deleteBlocked')")
	@RequestMapping(value = "/deleteBlocked.do")
	@Logs(description="删除黑名单")
	@ResponseBody
	public Map<String,Object> deleteBlocked(String denyIp,String denyDay) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		try {
			BlockedIp blockedIp = new BlockedIp();
			blockedIp.setDenyIp(denyIp);
			blockedIp.setDenyDay(denyDay);
			int i = blockedIpService.deleteBlockedIp(blockedIp);
			if (i > 0) {
				msg.put("status",true);
				msg.put("msg","删除成功");
				log.info(msg.toString());
			}
			else{
				msg.put("status",false);
				msg.put("msg","删除失败");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			log.error("异常:",e);
			msg.put("status",false);
			msg.put("msg",e.getMessage());
			log.error(msg.toString());
		}
		log.info(msg.toString());
		return msg;
	}
}
