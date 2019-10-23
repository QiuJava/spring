package cn.eeepay.framework.dao.bill;
//package cn.eeepay.framework.dao;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//
//import cn.eeepay.framework.model.SysMenu;
//import cn.eeepay.framework.service.SysMenuService;
//import cn.eeepay.framework.util.Const;
//
///**
// * 角色-资源数据访问类
// *
// * 可以在此处使用需要的方式获取信息，如从数据库、XML、文本等获取
// */
//public class ResourceDao {
//	@Resource
//	public SysMenuService sysMenuService;
//	
//    private static Map<String, Collection<ConfigAttribute>> resourceMap = loadResourceMap();
//
//    private synchronized static Map<String, Collection<ConfigAttribute>> loadResourceMap() {
//        Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
//
//        final SecurityConfig scTeacher = new SecurityConfig(Const.ROLE_TEACHER);
//        final SecurityConfig scStudent = new SecurityConfig(Const.ROLE_STUDENT);
//        final SecurityConfig scNotice = new SecurityConfig(Const.ROLE_NOTICE);
//
//        resourceMap.put("/student.do", new ArrayList<ConfigAttribute>(){{
//            add(scStudent);
//        }});
//
//        resourceMap.put("/student/**", new ArrayList<ConfigAttribute>(){{
//            add(scStudent);
//        }});
//
//        resourceMap.put("/teacher.do", new ArrayList<ConfigAttribute>(){{
//            add(scTeacher);
//        }});
//
//        resourceMap.put("/class.do", new ArrayList<ConfigAttribute>(){{
//            add(scStudent);
//            add(scTeacher);
//        }});
//
//        resourceMap.put("/notice.do", new ArrayList<ConfigAttribute>(){{
//            add(scTeacher);
//            add(scNotice);
//        }});
//
//        
//        return resourceMap;
//    }
//
//    public Map<String, Collection<ConfigAttribute>> getResourceMap()  {
//    	Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
//		try {
//			List<SysMenu> sysMenus = sysMenuService.findAllSysMenu();
//		
//	    	for (final SysMenu sysMenu : sysMenus) {
//	    		resourceMap.put(sysMenu.getMenuUrl(), new ArrayList<ConfigAttribute>(){{
//	                add(new SecurityConfig(sysMenu.getRigthCode()));
//	            }});
//			}
//	    	System.out.println("getResourceMap");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        return resourceMap;
//    }
//}
