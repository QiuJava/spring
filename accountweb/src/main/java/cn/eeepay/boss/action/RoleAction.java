package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import cn.eeepay.framework.model.bill.Node;
import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.model.bill.ShiroRole;
import cn.eeepay.framework.model.bill.State;
import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.bill.UserRole;
import cn.eeepay.framework.service.bill.RoleRigthService;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.ShiroRoleService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.service.bill.UserRigthService;
import cn.eeepay.framework.service.bill.UserRoleService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.UrlUtil;
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
	public UserRoleService userRoleService;
	
	@PreAuthorize("hasAuthority('role:query')")
	@RequestMapping(value = "/role.do")
	public String role(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		String str = "";
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
			log.error("异常:",e);
//			e.printStackTrace();
		}
		model.put("roleTree", str);
		model.put("params", params);
		return "sys/role";
	}
//	@PreAuthorize("hasAuthority('role:query')")
//	@RequestMapping(value = "/findRoleById.do")
//	@ResponseBody
//	public ShiroRole findRoleById(String roleId) throws Exception{
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		ShiroRole shiroRole = shiroRoleService.findShiroRoleById(rId);
//		return shiroRole;
//	}
//	@RequestMapping(value = "findShiroRolePrivilege.do")
//	@ResponseBody
//	public List<Node> findShiroRolePrivilege(String roleId) throws Exception {
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		System.out.println(" findShiroRolePrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			//List<ShiroRigth> shiroRigths = shiroRigthService.findAllShiroRigth();
//			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//			List<RoleRigth> roleRigths = roleRigthService.findRoleRigthByRoleId(rId);//获取角色对应的权限
//			
//			List<ShiroRigth> srs = new ArrayList<>();
//			for (RoleRigth roleRigth : roleRigths) {
//				srs.add(roleRigth.getShiroRigth());
//			}
//			State state = new State(true, true);
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
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
	
//	@RequestMapping(value = "findRolePrivilege.do")
//	@ResponseBody
//	public List<Node> findRolePrivilege(String roleId,Integer menuId) throws Exception {
//		//TODO parentId 暂时未用
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		System.out.println(" findUserPrivilege Json");
//		Node node = new Node();
//		List<Node> nodes = new ArrayList<>();
//		
//		try {
//			List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
//			List<ShiroRigth> srs = shiroRigthService.findRolePrivilegeRigthByParentId(rId,menuId);//单个角色对应的权限
//			
////			if ("root".equals(parentId)) {
////				sysMenus.clear();
////				SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
////				sysMenus.add(sysMenu);
////			}
//			
//			for (SysMenu sysMenu : sysMenus) {
//				node = new Node();
//				node.setId(sysMenu.getId().toString());
//				node.setParent(menuId.toString());
//				node.setText(sysMenu.getMenuName());
//				node.setState(new State(false, false));
//				for (ShiroRigth shiroRigth : srs) {
//					if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
//						node.setState(new State(false, true));
//						break;
//					}
//				}
//				node.setRigthCode(sysMenu.getMenuCode());
//				
//				nodes.add(node);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		return nodes;
//	}
//	@PreAuthorize("hasAuthority('role:saveRoleRigth')")
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "saveRoleRigth.do")
//	@ResponseBody
//	public Map<String,Object> saveRoleRigth(String roleId,Integer menuId,String rigthCode) throws Exception {
//		String[] rigthCodeArray = rigthCode.split(",");
//		
//		String[] params = roleId.split(":");
//		Integer rId = Integer.valueOf(params[1]);
//		int i = 0;
//		Map<String,Object> msg=new HashMap<>();
//		try {
//			i = roleRigthService.saveRoleRigth(rId, menuId, rigthCodeArray);
//			if (i>0) {
//				msg.put("status",true);
//				msg.put("msg","保存角色权限成功");
//			}
//		} catch (Exception e) {
//			//e.printStackTrace();
//			msg.put("status",false);
//			msg.put("msg",e.getMessage());
//		}	
//		return msg;
//	}
	@PreAuthorize("hasAuthority('role:update')")
	@RequestMapping(value = "updateRole.do")
	@Logs(description="修改角色")
	@ResponseBody
	public Map<String,Object> updateRole(String roleId,String roleCode,String roleName,String roleRemake) throws Exception {
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
			try {
				i = shiroRoleService.updateShiroRole(shiroRole);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","修改角色成功");
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('role:delete')")
	@RequestMapping(value = "deleteRole.do")
	@Logs(description="删除角色")
	@ResponseBody
	public Map<String,Object> deleteRole(String roleId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(roleId)) {
			msg.put("msg","角色Id不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			String[] params = roleId.split(":");
			Integer rId = Integer.valueOf(params[1]);
			if (rId == 1) {
				msg.put("status",false);
				msg.put("msg","系统角色不能删除");
				log.info(msg.toString());
				return msg;
			}
			try {
				List<UserRole> UserRoleList= userRoleService.findUserRoleByRoleId(rId);
				if (UserRoleList != null && UserRoleList.size() >0) {
					msg.put("status",false);
					msg.put("msg","该角色已经被用户使用,不能删除");
					log.info(msg.toString());
					return msg;
				}
				int i = shiroRoleService.deleteShiroRoleById(rId);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","删除角色成功");
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
	
	@RequestMapping(value = "addRole.do")
	@Logs(description="新增角色")
	@ResponseBody
	public Map<String,Object> addRole(String roleCode,String roleName,String roleRemake) throws Exception {
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
		if(StringUtils.isNotBlank(roleCode) && !roleCode.startsWith("ROLE_")){
			msg.put("msg","角色编码必须以ROLE_开头");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			int i = 0;
			ShiroRole shiroRole = new ShiroRole();
			shiroRole.setRoleCode(roleCode);
			shiroRole.setRoleName(roleName);
			shiroRole.setRoleRemake(roleRemake);
			shiroRole.setCreateOperator(userInfo.getUsername());
			shiroRole.setCreateTime(new Date());
			try {
				i = shiroRoleService.insertShiroRole(shiroRole);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","新增角色成功");
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.error("异常:",e);
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.info(msg.toString());
			}	
		}
		log.info(msg.toString());
		return msg;
	}
	

	@RequestMapping(value = "findAllShiroRoleList.do")
	@ResponseBody
	public Page<ShiroRole> findAllShiroRoleList(@ModelAttribute ShiroRole shiroRole,@RequestParam Map<String, String> params ,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<ShiroRole> page){
		try {
			shiroRoleService.findAllShiroRoleList(shiroRole, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}	
		System.out.println(page);
		return page;
	}
	

	@RequestMapping(value = "updateShiroRoleById.do")
	@Logs(description="修改角色")
	@ResponseBody
	public Map<String,Object> updateShiroRoleById(String roleId,String roleCode,String roleName,String roleRemake) throws Exception {
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
			Integer rId = Integer.valueOf(roleId);
			if (rId == 1) {
				msg.put("status",false);
				msg.put("msg","系统角色不能修改");
				log.info(msg.toString());
				return msg;
			}
			
			int i = 0;
			ShiroRole shiroRole = new ShiroRole();
			shiroRole.setId(rId);
			shiroRole.setRoleCode(roleCode);
			shiroRole.setRoleName(roleName);
			shiroRole.setRoleRemake(roleRemake);
			shiroRole.setUpdateTime(new Date());
			try {
				i = shiroRoleService.updateShiroRole(shiroRole);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","修改角色成功");
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
	@PreAuthorize("hasAuthority('role:delete')")
	@RequestMapping(value = "deleteRoleById.do")
	@Logs(description="删除角色")
	@ResponseBody
	public Map<String,Object> deleteRoleById(String roleId) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		Integer rId = Integer.parseInt(roleId) ;
		boolean isReturn = false;
		if (StringUtils.isBlank(roleId)) {
			msg.put("msg","角色Id不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			if (rId == 1) {
				msg.put("status",false);
				msg.put("msg","系统角色不能删除");
				log.info(msg.toString());
				return msg;
			}
			try {
				List<UserRole> UserRoleList= userRoleService.findUserRoleByRoleId(rId);
				if (UserRoleList != null && UserRoleList.size() >0) {
					msg.put("status",false);
					msg.put("msg","该角色已经被用户使用,不能删除");
					log.info(msg.toString());
					return msg;
				}
				int i = shiroRoleService.deleteShiroRoleById(rId);
				if (i>0) {
					msg.put("status",true);
					msg.put("msg","删除角色成功");
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
	
	@PreAuthorize("hasAuthority('role:saveRoleRigth')")
	@RequestMapping(value = "/toRoleRigth.do")
	public String toRoleRigth(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		String str = "";
		String roleId = params.get("id");
		Integer rId = Integer.valueOf(roleId);
		try {
			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
			list.add("{ 'id':'"+Constants.sys_menu_root_id+"', 'parent':'#', 'text':'系统菜单', 'state':{ opened: true,selected:true}}");
			for (SysMenu sysMenu : sysMenus) {
				String pId = Constants.sys_menu_root_id.toString(); 
				if (sysMenu.getParentId() != null) {
					pId = sysMenu.getParentId().toString();
				}
				boolean selected = false;
//				if (sysMenu.getId() != null && sysMenu.getId().equals(5)) {
//					selected = true;
//				}
				String s = String.format("{ id:'%s',parent:'%s',text:'%s',state:{ opened: false,selected:%s}}", sysMenu.getId(),pId,sysMenu.getMenuName(),selected);
				list.add(s);
			}
			str = StringUtils.join(list,",\n");
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		model.put("menuTree", str);
		
		ShiroRole shiroRole = null ;
		try {
			shiroRole = shiroRoleService.findShiroRoleById(rId) ;
		}  catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		model.put("role", shiroRole) ;
		model.put("params", params);
		return "sys/roleRigth";
	}
	
	@PreAuthorize("hasAuthority('role:saveRoleRigth')")
	@RequestMapping(value = "findRolePrivilege.do")
	@ResponseBody
	public List<Node> findRolePrivilege(String roleId,Integer menuId) throws Exception {
		Integer rId = Integer.valueOf(roleId);
		System.out.println(" findUserPrivilege Json");
		Node node = new Node();
		List<Node> nodes = new ArrayList<>();
		
		List<SysMenu> sysMenus = sysMenuService.findAllPageSysMenuByParentId(menuId);
		List<ShiroRigth> srs = shiroRigthService.findRolePrivilegeRigthByParentId(rId,menuId);//单个角色对应的权限
		
//			if ("root".equals(parentId)) {
//				sysMenus.clear();
//				SysMenu sysMenu = sysMenuService.findSysMenuById(menuId);
//				sysMenus.add(sysMenu);
//			}
		
		for (SysMenu sysMenu : sysMenus) {
			node = new Node();
			node.setId(sysMenu.getId().toString());
			node.setParent(menuId.toString());
			node.setText(sysMenu.getMenuName());
			node.setState(new State(false, false));
			for (ShiroRigth shiroRigth : srs) {
				if (sysMenu.getMenuCode().equals(shiroRigth.getRigthCode())) {
					node.setState(new State(false, true));
					break;
				}
			}
			node.setRigthCode(sysMenu.getMenuCode());
			
			nodes.add(node);
		}
		return nodes;
	}
	
	@PreAuthorize("hasAuthority('role:saveRoleRigth')")
	@RequestMapping(value = "saveRoleRigth.do")
	@Logs(description="保存角色权限")
	@ResponseBody
	public Map<String,Object> saveRoleRigth(@RequestParam Map<String, String> params) throws Exception {
		int i = 0;
		Map<String,Object> msg=new HashMap<>();
		try {
			ObjectMapper om = new ObjectMapper();
			List<Map<String, String>> list = om.readValue(params.get("dataArr"), new TypeReference<List<Map<String, String>>>() {});
			if (!list.isEmpty()) {
				i = roleRigthService.saveAllRoleRigth(list);
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
			msg.put("status",false);
			msg.put("msg",e.getMessage());
			log.error(msg.toString());
		}
		log.info(msg.toString());
		return msg;
	}
	
	
}
