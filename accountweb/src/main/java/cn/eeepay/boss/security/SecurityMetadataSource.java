//package cn.eeepay.boss.security;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//
//import cn.eeepay.framework.model.SysMenu;
//import cn.eeepay.framework.service.SysMenuService;
//import cn.eeepay.framework.util.AntUrlPathMatcher;
//import cn.eeepay.framework.util.UrlMatcher;
//
///**
// * 资源元数据类
// *
// * 负责提供资源数据信息
// * 
// * by zouruijin
// * email rjzou@qq.com zrj@eeepay.cn
// * 2016年4月12日13:45:54
// *
// */
//public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
//    private UrlMatcher urlMatcher = new AntUrlPathMatcher();
//    @Resource
//	public SysMenuService sysMenuService;
//    //private ResourceDao resourceDao = new ResourceDao();
//    private static Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
//
//    public SecurityMetadataSource() {
//        //resourceMap = resourceDao.getResourceMap();
//    	
//    }
//
//
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object object) throws  IllegalArgumentException {
//	    	try {
//		    	List<SysMenu> sysMenus = sysMenuService.findAllNotBlankUrlSysMenu();
//		     	for (final SysMenu sysMenu : sysMenus) {
//		     		String url = sysMenu.getMenuUrl();
//		     		if (StringUtils.isNotBlank(url)) {
//						url = "/" + url + "**";
//					}
//		     		resourceMap.put(url, new ArrayList<ConfigAttribute>(){{
//		                 add(new SecurityConfig(sysMenu.getMenuCode()));
//		             }});
//		 		}
//	    	} catch (Exception e) {
//				e.printStackTrace();
//			}
//	    	String url = ((FilterInvocation)object).getRequestUrl();
//	    	
//	    	if(url.indexOf("?") > 0){
//	    		String [] urlArray = url.split("\\?");
//	    		url = urlArray[0];
//	    	}
//	        for(String resURL : resourceMap.keySet()) {
//	            if (resURL != null && urlMatcher.pathMatchesUrl(resURL, url)) {
//	                return resourceMap.get(resURL);
//	            }
//	        }
//	        return null;
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return true;
//    }
//}
