//package cn.eeepay.boss.action;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import cn.eeepay.framework.model.NoticeInfo;
//import cn.eeepay.framework.model.UserLoginInfo;
//import cn.eeepay.framework.service.NoticeInfoService;
//import cn.eeepay.framework.util.RespUtil;
//
///**
// * 公告
// * @author yl
// */
//@Controller
//@RequestMapping("/admin/notice")
//public class NoticeInfoAction {
//	private static final Logger log = LoggerFactory.getLogger(NoticeInfoAction.class);
//	
//	@Autowired
//	private NoticeInfoService noticeInfoService;
//	
//	@RequestMapping(value = "/add.do", method = {RequestMethod.GET, RequestMethod.POST})
//	public void add(HttpServletResponse response, @RequestBody NoticeInfo notice) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		
//		int result = 0;
//		try {
//			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if (principal.getTeamId().equals("200010")) {
//				notice.setLoginUser(principal.getUserEntityInfo().getUserId());
//				notice.setCreateTime(new Date());
//				notice.setStatus("1");
//				notice.setAgentNo(principal.getUserEntityInfo().getEntityId());
//				result = noticeInfoService.insert(notice);
//				
//				if (result > 0) {
//					data.put("success", true);
//				} else {
//					data.put("success", false);
//					data.put("msg", "新增公告失败");
//				}
//				
//			} else {
//				data.put("success", false);
//				data.put("msg", "只有OEM组织中一级代理商才有下发公告功能");
//			}
//			
//		} catch (Exception e) {
//			log.error("新增下发公告出错: "+e);
//			data.put("success", false);
//		}
//		
//		RespUtil.renderJson(response, data);
//	}
//}
