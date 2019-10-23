package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.ComboTree;
import cn.eeepay.framework.model.SysDept;
import cn.eeepay.framework.model.SysMenu;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.ShiroRigthService;
import cn.eeepay.framework.service.SysMenuService;
import cn.eeepay.framework.service.UserRigthService;
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
	
	@RequestMapping(value = "/leftMenu.do")
	public String menu(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		log.info("userinfo:"+userInfo);
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
//		//对sysMenu排序
//		Collections.sort(userMenus,new Comparator<SysMenu>(){
//            public int compare(SysMenu arg0, SysMenu arg1) {
//                return arg0.getOrderNo().compareTo(arg1.getOrderNo());
//            }
//        });
		
		StringBuffer sb = new StringBuffer();
		for (SysMenu sysMenu1 : userMenus) {
			if (sysMenu1.getParentId() == null || sysMenu1.getParentId().equals(Constants.sys_menu_root_id)) {
				sb.append("		<li> \n");
				sb.append("		<a href=\"javascript:void(0);\">  \n");
				sb.append("			<i class=\"fa "+sysMenu1.getIcons()+"\"></i> \n");
				sb.append("			<span class=\"nav-label\">"+sysMenu1.getMenuName()+"</span> \n");
				sb.append("			<span class=\"fa arrow\"></span> \n");
				sb.append("		</a> \n");
				sb.append("		<ul class=\"nav nav-second-level\"> \n");
				for (SysMenu sysMenu2 : userMenus) {
					if (sysMenu2.getParentId() != null  && sysMenu1.getId().equals(sysMenu2.getParentId())) {
						sb.append("			<li><a href=\""+sysMenu2.getMenuUrl()+"\">"+sysMenu2.getMenuName()+"</a></li> \n");
					}
				}
				sb.append("		</ul> \n");
				sb.append("		</li> \n");
			}
		}
		model.put("sysMenus", sb.toString());
		return "leftMenu";
	}
	@RequestMapping(value = "/menuComboTree.do")
	@ResponseBody
	public List<ComboTree> menuComboTree(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		log.info("userinfo:"+userInfo);
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
				ct.setChildren(childrens);
				
				comboTrees.add(ct);
			}
		}
		root.setChildren(comboTrees);
		list.add(root);
		return list;
	}
	@RequestMapping(value = "/toAddMenu.do")
	public String toAddUser(ModelMap model, @RequestParam Map<String, String> params){
		List<String> list = new ArrayList<String>();
		List<SysDept> deptList = null;
		String str = "";
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("deptList", deptList);
		return "sys/addMenu";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "addMenu.do")
	@ResponseBody
	public Map<String,Object> addMenu(String menuName,String menuCode,Integer parentId,String menuUrl,String orderNo) throws Exception {
		//TODO addMenu验证操作session 是否 有权限操作 
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
			}	
		}
		
		return msg;
	}
}
