package cn.eeepay.boss.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.ShiroUserService;

/**
 * 系统用户控制管理
 * @author hj
 * 2016年9月3日  下午12:10:57
 */
@Controller
@RequestMapping("/shiroUserAction")
public class ShiroUserAction {
	@Resource
	public ShiroUserService shiroUserService ;
	@Resource
	public SessionRegistry sessionRegistry;
	private static final Logger log = LoggerFactory.getLogger(ShiroUserAction.class);
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/queryShiroUserName.do")
	@ResponseBody
	public List<Map<String, String>> queryShiroUserName(String q) throws Exception {
		q = URLDecoder.decode(q, "UTF-8");
		ShiroUser user = new ShiroUser();
		user.setUserName(q);
		user.setRealName(q);
		List<ShiroUser> shiroUserList = null;
		List<Map<String, String>> maps = new ArrayList<>();
		try {
			shiroUserList = shiroUserService.findShiroUserNameByParams(user);
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}	
		Map<String, String> thenMap = null;
		for (ShiroUser s : shiroUserList) {
			thenMap = new HashMap<String, String>();
			thenMap.put("id", s.getUserName());
			thenMap.put("text", s.getRealName());
			log.info(thenMap.toString());
			maps.add(thenMap);
		}
		return maps;
	}
	/**
	 * 在线人数
	 * @return
	 */
	@RequestMapping(value = "/findNumberOnlineUsers.do")
	@ResponseBody
	public int findNumberOnlineUsers() {  
		   return sessionRegistry.getAllPrincipals().size();  
	}
	@PreAuthorize("hasAuthority('systemSetting:findOnlineUsers')")
	@RequestMapping(value = "/toOnlineUsers.do")
	public String toOnlineUsers(ModelMap model, @RequestParam Map<String, String> params) {  
		List<UserInfo> onlineUserInfoList = new ArrayList();
		for (Object principal : sessionRegistry.getAllPrincipals()) {
		   UserInfo onlineUserInfo = (UserInfo) principal;
		   for(SessionInformation session : sessionRegistry.getAllSessions(principal, false))  
		    {
			   if (!session.isExpired()) {
				   onlineUserInfo.setLastRequest(session.getLastRequest());
				   onlineUserInfo.setSessionId(session.getSessionId());
				   onlineUserInfoList.add(onlineUserInfo);
				   break;
			   }
		    }
		  
		}
		model.put("onlineUserInfoList", onlineUserInfoList);
		return "sys/onlineUserInfoList";
	}
}
