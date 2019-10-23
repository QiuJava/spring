package cn.eeepay.boss.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.ListUtils;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.bill.UserRigth;
import cn.eeepay.framework.model.bill.UserRole;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.ShiroUserService;
import cn.eeepay.framework.service.bill.UserRigthService;
import cn.eeepay.framework.service.bill.UserRoleService;
import cn.eeepay.framework.util.IPUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 用户详细信息类
 *
 * 负责以{@link UserDetails}方式提供用户信息
 * 
 * by zouruijin
 * email rjzou@qq.com 
 * zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	@Resource
	public ShiroUserService shiroUserService;
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public UserRigthService userRigthService;
	@Resource
	public UserRoleService userRoleService;
	@Resource
	private HttpServletRequest request;
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
    	String ip = IPUtil.getIpAddr(request);
    	
        if (shiroUserService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }
    	UserInfo  userInfo = null;
    	ShiroUser shiroUser = shiroUserService.findUserWithRolesByUserName(userName);
    	if (shiroUser == null) {
    		throw new UsernameNotFoundException("UsernameNotFound");
		}
    	if(shiroUser.getState() == 0){
    		throw new DisabledException("UserIsDisabled");
    	}
    	try{
	    	if (shiroUser != null) {
	    		Integer uId = shiroUser.getId();
	    		List<UserRole> userRoles = userRoleService.findUserRoleByUserId(uId);//用户对应的角色
	    		List<ShiroRigth> shiroRigths = shiroRigthService.findUserRoleRigthByUserId(uId);//角色对应的权限
	    		List<String> rigthCodeRoleList = new ArrayList<>();//角色对应的权限编码
	    		for (ShiroRigth shiroRigth : shiroRigths) {
	    			rigthCodeRoleList.add(shiroRigth.getRigthCode());
				}
	    		rigthCodeRoleList = new ArrayList(new HashSet(rigthCodeRoleList));//去掉重复
	    		Set<GrantedAuthority> authorities= new HashSet<GrantedAuthority>();
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
				
        		for (String sr : srs) {
        			authorities.add(new SimpleGrantedAuthority(sr));
				}
        		for (UserRole userRole : userRoles) {
        			if (userRole != null && userRole.getShiroRole() != null 
        					&& userRole.getShiroRole().getRoleCode() != null
        					&& userRole.getShiroRole().getRoleCode().trim().length() > 0) {
        				authorities.add(new SimpleGrantedAuthority(userRole.getShiroRole().getRoleCode()));
					}
				}
	        	userInfo = new UserInfo(shiroUser.getUserName(), shiroUser.getPassword(), authorities);
	        	userInfo.setUserId(shiroUser.getId());
	        	userInfo.setRealName(shiroUser.getRealName());
	        	userInfo.setTelNo(shiroUser.getTelNo());
	        	userInfo.setEmail(shiroUser.getEmail());
	        	userInfo.setState(shiroUser.getState());
	        	userInfo.setTheme(shiroUser.getTheme());
	        	userInfo.setLoginIp(ip);
	        	userInfo.setLastRequest(new Date());
	        	boolean isAdmin = isAdmin(userRoles);
	        	userInfo.setAdmin(isAdmin);
			}
	    } catch (Exception e) {
	    	log.error("异常:",e);
		}
        return userInfo;
        
    }
    private boolean isAdmin(List<UserRole> roles) {  
    	for (UserRole userRole : roles) {
    		if (userRole != null && userRole.getShiroRole() != null
    				&& userRole.getShiroRole().getRoleCode() != null) {
	    		//规定ROLE_ADMIN 为超级管理员
				if (userRole.getShiroRole().getRoleCode().equals("ROLE_ADMIN")) {
					 return true;  
				}
    		}
		}
        return false;  
    }  
}
