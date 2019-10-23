package cn.eeepay.boss.action;

import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CheckNum;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.util.CodeUtil;
import cn.eeepay.framework.util.Sms;

@Controller
@RequestMapping(value = "/mycode")
public class CodeAction extends BaseAction {

	private static final Logger log = LoggerFactory.getLogger(CodeAction.class);
	@Resource
	private AgentInfoService agentInfoService;

	@RequestMapping("/getCode.do")
	public void getCode(HttpServletResponse resp,HttpServletRequest req) throws Exception{
		// 调用工具类生成的验证码和验证码图片
        Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        session.setAttribute("imgcode", codeMap.get("code").toString());
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);
        resp.setContentType("image/jpeg");
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
	}
	

	@RequestMapping("/getCodeLevel")
	public void getCodeLevel(HttpServletResponse resp,HttpServletRequest req) throws Exception{
		// 调用工具类生成的验证码和验证码图片
        Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        session.setAttribute("imgcodeLevel", codeMap.get("code").toString());
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);
        resp.setContentType("image/jpeg");
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (Exception e) {
        	log.error(e.getMessage());
        }
	}
	
	@RequestMapping(value = "/sendMsg")
	@ResponseBody
	public Map<String, Object> sendMsg(@RequestParam("checkNum") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			//校验手机
			if(!isPhone(info.getPhone())){
				map.put("msg", "手机号码格式不正确");
				return map;
			}
			//校验验证码
			if(!isImgNum(info.getImgNum())){
				map.put("msg", "图形验证码有误");
				return map;
			}
			HttpSession session = req.getSession();
			String oldCode = (String)session.getAttribute("imgcode");
			
			//销毁验证码
			//session.removeAttribute("imgcode");
			
			if(StringUtils.isNotBlank(oldCode) && oldCode.equalsIgnoreCase(info.getImgNum())){
				//4位数字验证码生成验证码
		        String phoneNum = "" + getRandom(4);
				//发送验证码
	            Sms.sendMsg(info.getPhone(), String.format("验证码：%s。您正在设置安全手机号码，如非本人操作请及时登录后台查看。", phoneNum));
	            String phoneNumTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
				//存储session
	            session.setAttribute("checkphone", info.getPhone());
	            session.setAttribute("phoneNum", phoneNum);
	            session.setAttribute("phoneNumTime", phoneNumTime);
	            System.out.println("手机验证码为========================>:"+phoneNum);
				
				//响应
				map.put("status", true);
			}else{
				map.put("msg", "图形验证码有误");
			}
			
			
		} catch (Exception e) {
			log.error("获取验证码异常!",e);
		}
		return map;
	}
	
	@RequestMapping(value = "/sendOldMsg")
	@ResponseBody
	public Map<String, Object> sendOldMsg(@RequestParam("checkNum") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String oldCode = (String)session.getAttribute("imgcode");
			//销毁验证码
			//session.removeAttribute("imgcode");
			
			if(StringUtils.isNotBlank(oldCode) && oldCode.equalsIgnoreCase(info.getImgNum())){
				//4位数字验证码生成验证码
		        String phoneNum = "" + getRandom(4);
				//发送验证码
		        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String agentNo = principal.getUserEntityInfo().getEntityId();
				String safePhone = agentInfoService.getSafePhone(agentNo);
		        
	            Sms.sendMsg(safePhone, String.format("验证码：%s。您正在修改安全手机号码，如非本人操作请及时登录后台查看。", phoneNum));
	            String phoneNumTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
				//存储session
	            session.setAttribute("oldphoneNum", phoneNum);
	            session.setAttribute("oldphoneNumTime", phoneNumTime);
	            System.out.println("手机验证码为========================>:"+phoneNum);
				//响应
				map.put("status", true);
			}else{
				map.put("msg", "图形验证码有误");
			}
			
		} catch (Exception e) {
			log.error("获取验证码异常!",e);
		}
		return map;
	}
	
	@RequestMapping(value = "/sendOldMsgLevel")
	@ResponseBody
	public Map<String, Object> sendOldMsgLevel(@RequestParam("checkNum") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String oldCode = (String)session.getAttribute("imgcodeLevel");
			//销毁验证码
			session.removeAttribute("imgcodeLevel");
			
			if(StringUtils.isNotBlank(oldCode) && oldCode.equalsIgnoreCase(info.getImgNum()) && StringUtils.isNotBlank(info.getAgentNo())  ){
				//4位数字验证码生成验证码
		        String phoneNum = "" + getRandom(4);
				//发送验证码
				String safePhone = agentInfoService.getSafePhone(info.getAgentNo());
		        
	            Sms.sendMsg(safePhone, String.format("验证码：%s。您正在修改结算卡信息，如非本人操作请及时登录后台查看。", phoneNum));
	            String phoneNumTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
				//存储session
	            session.setAttribute("oldphoneNumLevel", info.getAgentNo()+"-"+phoneNum);
	            session.setAttribute("oldphoneNumTimeLevel", phoneNumTime);
	            System.out.println("手机验证码为========================>:"+phoneNum);
				//响应
				map.put("status", true);
			}else{
				map.put("msg", "图形验证码有误");
			}
			
		} catch (Exception e) {
			log.error("获取验证码异常!",e);
		}
		return map;
	}
	
	@SystemLog(description = "校验旧安全手机")
	@RequestMapping(value = "/checkOldPhone")
	@ResponseBody
	public Map<String, Object> checkOldPhone(@RequestParam("info") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String phoneNum = (String)session.getAttribute("oldphoneNum");
			String phoneNumTime = (String) session.getAttribute("oldphoneNumTime");
			String imgcode = (String) session.getAttribute("imgcode");

			//校验验证码是否匹配
			if(StringUtils.isBlank(imgcode) || !imgcode.equalsIgnoreCase(info.getImgNum())){
				map.put("msg", "图形验证码有误");
				return map;
			}  
			
			if(StringUtils.isBlank(phoneNum) || !phoneNum.equals(info.getPhoneNum())){
				map.put("msg", "短信验证码有误");
				return map;
			}
			if(Long.valueOf(phoneNumTime)-System.currentTimeMillis()<0){
				map.put("msg", "验证码超过5分钟,请重新获取");
				return map;
			}
		
			map.put("status", true);	
			session.setAttribute("checkPhoneNumTrue", true);
            String checkPhoneNumTrueTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
			session.setAttribute("checkPhoneNumTrueTime", checkPhoneNumTrueTime);
			
			//删除验证码
			session.removeAttribute("oldphoneNum");
			session.removeAttribute("oldphoneNumTime");
			session.removeAttribute("imgcode");

			
		} catch (Exception e) {
			log.error("修改安全手机异常!",e);
			map.put("msg", "修改异常");
		}
		return map;
	}
	
	
	@RequestMapping(value="/getPhone.do")
	@ResponseBody
	public Map<String, Object> getPhone() throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String agentNo = principal.getUserEntityInfo().getEntityId();
			String safePhone = agentInfoService.getSafePhone(agentNo);
			if(StringUtils.isNotBlank(safePhone)){
				safePhone = safePhone.substring(0, 3) + "*****" + safePhone.substring(8, safePhone.length());
			}
			msg.put("safephone", safePhone);
		} catch (Exception e){
			log.error("查询安全手机失败!");
			msg.put("status", false);
		}
		return msg;
	}
	private boolean isImgNum(String imgNum) {
		if(StringUtils.isBlank(imgNum) || imgNum.length()!=4){
			return false;
		}
		return true;
	}

	public static boolean isPhone(String str) {
		if(StringUtils.isBlank(str)){
			return false;
		}
		String regex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
		return match(regex, str);
		}
	
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	@SystemLog(description = "保存安全手机")
	@RequestMapping(value = "/savePhone")
	@ResponseBody
	public Map<String, Object> savePhone(@RequestParam("info") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String checkphone = (String)session.getAttribute("checkphone");
			String imgcode = (String)session.getAttribute("imgcode");
			String phoneNum = (String)session.getAttribute("phoneNum");
			String phoneNumTime = (String) session.getAttribute("phoneNumTime");
			
			if(StringUtils.isBlank(checkphone)){
				map.put("msg", "请先获取短信验证码");
				return map;
			}
			
			if(StringUtils.isBlank(imgcode) || !imgcode.equalsIgnoreCase(info.getImgNum())){
				map.put("msg", "图形验证码有误");
				return map;
			} 
			if(!checkphone.equals(info.getPhone())){
				map.put("msg", "前后手机不匹配");
				return map;
			}
			//校验手机
			if(!isPhone(info.getPhone())){
				map.put("msg", "手机号码格式不正确");
				return map;
			}
			if(StringUtils.isBlank(phoneNum) || !phoneNum.equals(info.getPhoneNum())){
				map.put("msg", "短信验证码有误");
				return map;
			}  
			//校验验证码是否匹配
			if(Long.valueOf(phoneNumTime)-System.currentTimeMillis()<0){
				map.put("msg", "验证码超过5分钟,请重新获取");
				return map;
			}
			//更新
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String agentNo = principal.getUserEntityInfo().getEntityId();
			
			String safePhone = agentInfoService.getSafePhone(agentNo);
			
			//如果存在旧手机
			if(StringUtils.isNotBlank(safePhone)){
				Boolean flag = (Boolean)session.getAttribute("checkPhoneNumTrue");
				String checkPhoneNumTrueTime = (String)session.getAttribute("checkPhoneNumTrueTime");
				
				if(null ==flag || flag !=true){
					map.put("msg", "请校验原手机");
					return map;
				}
				if(Long.valueOf(checkPhoneNumTrueTime)-System.currentTimeMillis()<0){
					map.put("msg", "验证码超过5分钟,请重新获取");
					return map;
				}
			}
			
			int  i = agentInfoService.updateSafePhoneByAgentNo(agentNo,checkphone);
			if(i>0){
				//销毁session
				session.removeAttribute("checkphone");
			    session.removeAttribute("phoneNum");
				session.removeAttribute("phoneNumTime");
				
				
				if(StringUtils.isBlank(safePhone)){
					AgentInfo ai = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
					Sms.sendMsg( ai.getMobilephone(), "安全手机号码设置成功，该手机号码可用于修改提现密码等重要操作，如非本人操作请及时登录后台查看");
				}
				
				map.put("status", true);
				map.put("msg", "安全设置已完成");
			}
		} catch (Exception e) {
			log.error("保存安全手机异常!",e);
		}
		return map;
	}
}


