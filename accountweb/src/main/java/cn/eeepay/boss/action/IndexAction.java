package cn.eeepay.boss.action;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.bill.UserSetting;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.service.bill.UserSettingService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;

/**
 * 登录，首页管理
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

@Controller
@RequestMapping(value = "/")
public class IndexAction {
	@Resource
	public UserSettingService userSettingService;
	@Resource
	public SystemInfoService systemInfoService;
	@Resource
	private RedisService redisService;
	@Resource
	public SysMenuService sysMenuService;
	
	private static final Logger log = LoggerFactory.getLogger(IndexAction.class);
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/welcome.do")
	public String welcome(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Integer userId = userInfo.getUserId();
		Boolean isBackLastPage = false;
		UserSetting userSetting = userSettingService.findUserSettingByUserId(userId);
		
		if (userSetting != null && userSetting.getBackLastPage() != null && userSetting.getBackLastPage() == 1) {
			isBackLastPage = true;
		}
		
		String key = Constants.user_last_menu_code + ":" + userId;
		Object object = redisService.select(key);
		String menuCode = null;
		String lastMenuUrl = "";
		if (object != null) {
			menuCode = object.toString();
		}
		if (menuCode != null) {
			Collection<GrantedAuthority> authorities = userInfo.getAuthorities();
			for (GrantedAuthority ga : authorities) {
				if(menuCode.equals(ga.getAuthority())){
					SysMenu sysMenu= sysMenuService.findSysMenuByMenuCodeWithMenu(menuCode);
					lastMenuUrl = sysMenu.getMenuUrl();
				}
			}
		}
		model.put("userSetting", userSetting);
		model.put("userInfo", userInfo);
		if (isBackLastPage && StringUtils.isNotBlank(lastMenuUrl)) {
			return "redirect:/"+lastMenuUrl;
		}
		return "welcome";
	}
	//权限控制相关页面
    @RequestMapping(value = "/login.do")
    public String loginPage() {
        return "login";
    }

//    @PreAuthorize("permitAll")
    @RequestMapping(value = "/noRigth.do")
    public String noRigth(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String requestType = request.getHeader("X-Requested-With");  
		if (!StringUtils.isEmpty(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")) {  
			return "redirect:/noRigthJson.do";
		}  
		else {
			return "noRigth";
		}
    }
    @RequestMapping(value = "/noRigthJson.do")
    @ResponseBody
    public String noRigthJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String msg = "你没有权限操作.";
    	response.setContentType( MediaType.APPLICATION_JSON_VALUE);
    	response.setHeader("optStatus", "noRigth");  
		response.sendError(519, msg);  
		return msg;
    }

    // 展示功能相关页面

//    @RequestMapping("/index.html")
//    public String indexPage() {
//        return "index";
//    }
    //@PreAuthorize("hasPermission('#targetDomainObject','systemSetting:query')")
    @PreAuthorize("hasAuthority('userSetting:query')")
    //@PreAuthorize("denyAll")
    @RequestMapping("/student.do")
    public String studentPage() {
        return "student";
    }

    @RequestMapping("/student/detail.do")
    public String studentDetailPage() {
        return "student_detail";
    }

    @RequestMapping("/teacher.do")
    public String teacherPage() {
        return "teacher";
    }

    @RequestMapping("/class.do")
    public String classPage() {
        return "class";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/currentDate.do")
    public String sysCurrentDate(ModelMap model, @RequestParam Map<String, String> params,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SystemInfo systemInfo = systemInfoService.findSystemInfoById(1);
    	String currentDate = DateUtil.getDefaultFormatDate(systemInfo.getCurrentDate());
    	params.put("currentDate", currentDate);
    	model.put("params", params);
    	return "currentDate";
    }
}
