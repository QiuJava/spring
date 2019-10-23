package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.ShiroRoleDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.Node;
import cn.eeepay.framework.model.RolePrivilegeTreeNode;
import cn.eeepay.framework.model.RoleRigth;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.model.ShiroRole;
import cn.eeepay.framework.model.State;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.UserRole;
import cn.eeepay.framework.service.ShiroRoleService.QueryParam;
import cn.eeepay.framework.service.ShiroRoleService.ShiroRole2;

/**
 * 角色管理
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

@Controller
@RequestMapping(value = "/roleAction")
public class RoleAction {
	private static final Logger log = LoggerFactory.getLogger(RoleAction.class);
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public UserRigthService userRigthService;
	@Resource
	public SysMenuService sysMenuService;
	@Resource
	public ShiroRoleService shiroRoleService;
	@Resource
	public RoleRigthService roleRigthService;
	@Resource
	private AgentInfoService agentInfoService;
	
	@Resource
	public UserRoleService userRoleService;
	
	@Autowired
	private ShiroRoleDao  shiroRoleDao;
	
	@RequestMapping(value = "/role.do")
	public String role(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		String str = "";
		try {
			List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
			list.add("{ id:'root', parent:'#', text:'角色', state:{opened:true}}");
			for (ShiroRole shiroRole : shiroRoles) {
				boolean selected = false;
				if (shiroRole.getId() != null && shiroRole.getId().equals(1)) {
					selected = true;
				}
				list.add("{ id:'role:"+shiroRole.getId()+"', parent:'root', text:'"+shiroRole.getRoleName()+"',state:{ opened: false,selected:"+selected+"}}");
			}
			str = StringUtils.join(list,",\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("roleTree", str);
		return "sys/role";
	}
	@RequestMapping(value = "/findAllRoles.do")
	@ResponseBody
	public List<ShiroRole> findAllRoles(
			@RequestParam(value = "usable", required = false) Integer usable) throws Exception {

		return shiroRoleService.findAllRoles(usable);
	}

	@RequestMapping(value = "/findRoles.do")
	@ResponseBody
	public Page<ShiroRole2> findRoles(@RequestParam("baseInfo")String data,@Param("page")Page<ShiroRole2> page) throws Exception {
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		QueryParam param=JSONObject.parseObject(data, QueryParam.class);
		param.setAgentNo(userInfo.getUserEntityInfo().getEntityId());
		shiroRoleService.findRoles(param, page);
		if ("ALL".equals(userInfo.getUserEntityInfo().getEntityId())) {
			List<ShiroRole2> list = page.getResult();
			for (ShiroRole2 shiroRole2 : list) {
				// 根据角色ID去映射表找记录 有就设置userCount=1
				Long count = shiroRoleDao.findCountByRoleId(shiroRole2.getId());
				if (count>0) {
					shiroRole2.setUserCount(Integer.valueOf(count.toString()));
				}
			}
		}
		return page;
	}
	
	@RequestMapping(value = "/findRoleById.do")
	@ResponseBody
	public ShiroRole findRoleById(String roleId) throws Exception{
		String[] params = roleId.split(":");
		Integer rId = Integer.valueOf(params[1]);
		ShiroRole shiroRole = shiroRoleService.findShiroRoleById(rId);
		return shiroRole;
	}
	@RequestMapping(value = "findShiroRolePrivilege.do")
	@ResponseBody
	public List<Node> findShiroRolePrivilege(String roleId) throws Exception {
		String[] params = roleId.split(":");
		Integer rId = Integer.valueOf(params[1]);
		System.out.println(" findShiroRolePrivilege Json");
		Node node = new Node();
		List<Node> nodes = new ArrayList<>();
		
		try {
			//List<ShiroRigth> shiroRigths = shiroRigthService.findAllShiroRigth();
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			List<RoleRigth> roleRigths = roleRigthService.findRoleRigthByRoleId(rId);//获取角色对应的权限
			
			List<ShiroRigth> srs = new ArrayList<>();
			for (RoleRigth roleRigth : roleRigths) {
				srs.add(roleRigth.getShiroRigth());
			}
			State state = new State(true, true);
			
			//TODO　这里需要修改
//			node = new Node(Constants.sys_menu_root_id.toString(),"#","权限列表",state,"");
//			nodes.add(node);
//			for (SysMenu sysMenu : sysMenus) {
//				node = new Node();
//				node.setId(sysMenu.getId().toString());
////				String pId = "root";
////				if(!sysMenu.getParentId().equals(0)){
////				String pId = sysMenu.getParentId().toString();
////				}
//				String pId = sysMenu.getParentId().toString();
//				node.setParent(pId);
//				node.setText(sysMenu.getMenuName());
//				node.setState(new State(false, false));
//				node.setRigthCode(sysMenu.getMenuCode());
////				for (ShiroRigth sr : srs) {
////					if (sysMenu.getMenuCode().equals(sr.getRigthCode())) {
////						node.setChecked("true");
////						break;
////					}
////				}
//				nodes.add(node);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return nodes;
	}
	
	@RequestMapping(value = "findRolePrivilege.do")
	@ResponseBody
	public List<RolePrivilegeTreeNode> findRolePrivilege(String roleId,Integer menuId) throws Exception {
		
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Collection<GrantedAuthority> authorities = userInfo.getAuthorities();
		Set<String> authoritiesSet = new HashSet<String>();
		for (GrantedAuthority ga : authorities) {
			authoritiesSet.add(ga.getAuthority());
		}
		//TODO parentId 暂时未用
		String[] params = roleId.split(":");
		Integer rId = Integer.valueOf(params[1]);
		//System.out.println(" findUserPrivilege Json");
		RolePrivilegeTreeNode node = new RolePrivilegeTreeNode();
		List<RolePrivilegeTreeNode> nodes = new ArrayList<>();
		
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
			// 根据登录用户权限过滤菜单
			for (Iterator iterator = sysMenus.iterator(); iterator.hasNext();) {
				SysMenu sysMenu = (SysMenu) iterator.next();
				if (!authoritiesSet.contains(sysMenu.getMenuCode()) && !"ALL".equals(userInfo.getUserEntityInfo().getEntityId())) {
					iterator.remove();
				}
			}
			
			List<ShiroRigth> srs = shiroRigthService.findRolePrivilegeRigthByParentId(rId,menuId);//单个角色对应的权限
			Set<String> permits = new HashSet<String>();
    		for(ShiroRigth shiroRigth : srs){
    			permits.add(shiroRigth.getRigthCode());
    		}
			
//			if ("root".equals(parentId)) {
//				sysMenus.clear();
//				SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
//				sysMenus.add(sysMenu);
//			}
			//DONE　这里需要修改
			for (SysMenu sysMenu : sysMenus) {
				node = new RolePrivilegeTreeNode();
				node.setId(sysMenu.getId().toString());
				node.setParent(menuId.toString());
				node.setText(sysMenu.getMenuName());
				node.setState(new State(false, false));
				node.setRigthCode(sysMenu.getMenuCode());
				if (permits.contains(sysMenu.getMenuCode())) {
					node.setState(new State(false, true));
				}
				
				nodes.add(node);
				/*for (ShiroRigth shiroRigth : srs) {
					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
						node.setState(new State(false, true));
						break;
					}
				}*/
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return nodes;
	}
	@SystemLog(description = "保存菜单权限")
	@RequestMapping(value = "saveRoleRigth.do")
	@ResponseBody
	public Map<String,Object> saveRoleRigth(String roleId,Integer menuId,String rigthCode) throws Exception {
		String[] rigthCodeArray;
		if(StringUtils.isNotBlank(rigthCode)){
			rigthCodeArray = rigthCode.split(",");
		}else{
			rigthCodeArray = new String[0];
		}
		
		String[] params = roleId.split(":");
		Integer rId = Integer.valueOf(params[1]);
		int i = 0;
		Map<String, Object> msg = new HashMap<>();
		try {
			// 对角色赋菜单 判断是否有权限
			UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			boolean isContains = false;
			
			boolean isAdminRole = false;
			// 判断是否拥有超级管理员角色
			List<UserRole> srs = userRoleService.findUserRoleByUserId(userInfo.getUserEntityInfo().getId());
			if (srs != null) {
				for (UserRole item : srs) {
					if (item.getRoleId() == 1) {
						isAdminRole = true;
						isContains = true;
					}
				}
			}
			
			// 判断用户是否拥有该权限
			if (!isContains) {
				List<ShiroRole> shiroRoleList;
				final String entityId = userInfo.getUserEntityInfo().getEntityId();
				if (entityId.equals("ALL")) {
					shiroRoleList = shiroRoleService.findAdminShiroRole();
				} else {
					shiroRoleList = shiroRoleService.findAgentShiroRole(entityId);
				}
				for (ShiroRole role : shiroRoleList) {
					if (role.getId().equals(rId)) {
						isContains = true;
						break;
					}
				}
			}
			if (!isContains) {
				msg.put("status", false);
				msg.put("msg", "角色无权限操作");
				return msg;
			}
			
//			List<UserRole> userRoleList = userRoleService.findUserRoleByUserId(userInfo.getUserEntityInfo().getId());
//			boolean isContains = false;
//			for (UserRole userRole : userRoleList) {
//				if (userRole.getRoleId().equals(rId)) {
//					isContains = true;
//				}
//			}
			
			if(!isAdminRole){
				isContains = true;
				Set<String> permits = new HashSet<String>();
				for (GrantedAuthority authority : userInfo.getAuthorities()) {
					permits.add(authority.getAuthority());
				}
				for (String rigth : rigthCodeArray) {
					if (!permits.contains(rigth)) {
						isContains = false;
						break;
					}
				}
			}
			
			if (isAdminRole || isContains) {
				i = roleRigthService.saveRoleRigth(rId, menuId, rigthCodeArray);
				if (i > 0) {
					msg.put("status",true);
					msg.put("msg","保存角色权限成功");
				}
			}		
			else{
				msg.put("status",false);
				msg.put("msg","无权限操作");
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			msg.put("status",false);
			msg.put("msg",e.getMessage());
		}	
		return msg;
	}

	@SystemLog(description = "修改角色")
	@RequestMapping(value = "updateRole.do")
	@ResponseBody
	public Map<String,Object> updateRole(String roleId,String roleCode,String roleName,String roleRemake,
										 @RequestParam(value="scope", required=false)String scope) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if(!StringUtils.equals(loginAgent.getAgentNo(), "0") && !loginAgent.getAgentNo().equals(scope)){
			msg.put("msg","非法操作");
			msg.put("status",false);
			isReturn = true;
		}else if (StringUtils.isBlank(roleCode)) {
			msg.put("msg","角色编码不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if(StringUtils.isBlank(roleName) ){
			msg.put("msg","角色名称不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			String[] params = roleId.split(":");
			Integer rId = Integer.valueOf(params[1]);
			int i = 0;
			//Map<String,Object> msg=new HashMap<>();
			ShiroRole shiroRole = new ShiroRole();
			shiroRole.setId(rId);
			shiroRole.setRoleCode(roleCode);
			shiroRole.setRoleName(roleName);
			shiroRole.setRoleRemake(roleRemake);
			shiroRole.setUpdateTime(new Date());
			if(StringUtils.isNotBlank(scope))
				shiroRole.setScope(scope);
			
			// 对修改角色 进行权限 判断是否有权修改该角色
			UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			
//			List<UserRole> userRoleList = userRoleService.findUserRoleByUserId(userInfo.getUserEntityInfo().getId());
//			boolean isContains = false;
//			for (UserRole userRole : userRoleList) {
//				if (userRole.getRoleId().equals(rId)) {
//					isContains = true;
//				}
//			}
			boolean isContains = false;
			List<ShiroRole> shiroRoleList;
			final String entityId = userInfo.getUserEntityInfo().getEntityId();
			if (entityId.equals("ALL")) {
				shiroRoleList =  shiroRoleService.findAdminShiroRole();
			}
			else{
				shiroRoleList =  shiroRoleService.findAgentShiroRole(entityId);
			}
			for ( ShiroRole role : shiroRoleList) {
				if(role.getId().equals(rId)){
					isContains = true;
					break;
				}
			}
			try {
				if (isContains) {
					i = shiroRoleService.updateShiroRole(shiroRole);
					if (i>0) {
						msg.put("status",true);
						msg.put("msg","修改角色成功");
					}
				}
				else{
					msg.put("status",false);
					msg.put("msg","无权限操作");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
			}	
		}
		return msg;
	}

	@SystemLog(description = "新增角色")
	@RequestMapping(value = "addRole.do")
	@ResponseBody
	public Map<String,Object> addRole(@RequestParam("roleCode") String roleCode,@RequestParam("roleName") String roleName,@RequestParam(value="roleRemake",required=false) String roleRemake) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(roleCode)) {
			msg.put("msg","角色编码不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if(StringUtils.isBlank(roleName) ){
			msg.put("msg","角色名称不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			
			//TODO addRole验证操作session 是否 有权限操作 
	//		String[] params = roleId.split(":");
	//		Integer rId = Integer.valueOf(params[1]);
			int i = 0;
			//Map<String,Object> msg=new HashMap<>();
			ShiroRole shiroRole = new ShiroRole();
	//		shiroRole.setId(rId);
			shiroRole.setRoleCode(roleCode);
			shiroRole.setRoleName(roleName);
			shiroRole.setRoleRemake(roleRemake);
			shiroRole.setCreateOperator(userInfo.getUsername());
			shiroRole.setCreateTime(new Date());
			final String entityId = userInfo.getUserEntityInfo().getEntityId();
			if (entityId.equals("ALL")) {
				shiroRole.setScope(null);
			}else{
				shiroRole.setScope(entityId);
			}
			try {
				i = shiroRoleService.insertShiroRole(shiroRole);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","新增角色成功");
				}
			} catch (Exception e) {
				log.error("保存权限失败",e);
				msg.put("status",false);
				msg.put("msg",e.getMessage());
			}	
		}
		return msg;
	}
	
	@RequestMapping(value = "checkDeleteRole.do")
	@ResponseBody
	public Object checkDeleteRole(Integer roleId) {
		Map<String, Object> msg = new HashMap<>();
		String retMsg = checkDeleteRoleInternal(roleId);
		if (StringUtils.isBlank(retMsg)) {
			msg.put("status", true);
			msg.put("msg", "可以删除");
		} else {
			msg.put("status", false);
			msg.put("msg", retMsg);
		}
		return msg;
	}

	private String checkDeleteRoleInternal(Integer roleId) {
		List<UserRole> list = null;
		try {
			list = userRoleService.findUserRoleByRoleId(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return "系统错误！";
		}
		if (list != null && !list.isEmpty()) {
			return "有用户在使用此角色，不能删除！";
		} else {
			return null;

		}
	}

	@SystemLog(description = "删除角色")
	@RequestMapping(value = "deleteRole.do")
	@ResponseBody
	public Object deleteRole(Integer roleId) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final String entityId = userInfo.getUserEntityInfo().getEntityId();
		ShiroRole role = shiroRoleService.findShiroRoleById(roleId);
		boolean isOk = false;
		final String scope = role.getScope();
		if (StringUtils.isBlank(scope)) {
			isOk = "ALL".equals(entityId);
		} else {
			isOk = scope.equals(entityId);
		}
		if (!isOk) {
			msg.put("msg", "没有权限！");
			return msg;
		}
		String msg2 = checkDeleteRoleInternal(roleId);
		if (StringUtils.isNotBlank(msg2)) {
			msg.put("msg", msg2);
			return msg;
		}
		shiroRoleService.deleteShiroRoleById(roleId);
		msg.put("status", true);
		msg.put("msg", "删除成功");
		return msg;
	}
}
