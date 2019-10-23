package cn.eeepay.boss.system;

import cn.eeepay.framework.model.AgentOperLogBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentOperLogService;
import cn.eeepay.framework.util.StringUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AOP日志记录切面
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:37:42
 */
@Aspect
@Component
public class SystemLogAspect {

	private final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
	private static final Gson gson  = new Gson();
	private static final AtomicLong atomicLong = new AtomicLong();

	@Autowired
	private AgentOperLogService agentOperLogService;
	/**
	 * 系统日志切点
	 */
	@Pointcut("execution(@cn.eeepay.boss.system.SystemLog * *.*(..)) ")
    public  void systemLogPointcut() {
    }

//	/**
//	 * action 方法切点
//	 */
//	@Pointcut("execution(@org.springframework.web.bind.annotation.RequestMapping * *.*(..)) ")
//	public void requestMappingPointcut() {
//	}

//	/**
//	 * 请求方法有RequestMapping注解
//	 * 但没有SystemLog注解的,
//	 * 数据库日志存的描述就是空
//	 * @param pjp 切点
//	 * @return 切点的返回值
//	 */
//	@Around("requestMappingPointcut() && !systemLogPointcut()")
//	public Object aroundSystemlog(ProceedingJoinPoint pjp){
//		return aroundLogWithAction(pjp, null);
//	}

	/**
	 * 请求方法上有SystemLog注解的
	 * 数据库日志存的描述就是 systemLog.description()
	 * @param pjp		 切点
	 * @param systemLog	 系统日志注解
	 * @return	切点返回值
	 */
//	@Around("systemLogPointcut() && @annotation(systemLog)")
	@AfterReturning(pointcut="systemLogPointcut() && @annotation(systemLog)",returning="result")
	public void aroundSystemlog(JoinPoint pjp, SystemLog systemLog, Object result){
		aroundLogWithAction(pjp, systemLog, result);
	}

	/**
	 * 不管请求方法上面有没有 SystemLog 注解,统一做日志记录
	 * @param pjp			切点
	 * @param systemLog	    自定义注解
	 * @return 切点的返回值
	 */
	private void aroundLogWithAction(JoinPoint pjp, SystemLog systemLog, Object result){
		long startTime = System.currentTimeMillis();
		String id = "NO. " + atomicLong.getAndIncrement();
		String resultJson = "";
		String paremJson = "";
		try {
			logger.info(id + " 日志AOP:切面方法 -- " + getMethodName(pjp));
			logger.info(id + " 日志AOP:登陆代理商 -- " + getLoginAgentNo() + " 登陆ip -- " + getRemoteAddr());
			paremJson = getParamJson(pjp);
			logger.info(id + " 日志AOP:请求参数 -- " + paremJson);
			resultJson = systemLog.saveResult() ? getResultJson(result) : "";
			logger.info(id + " 日志AOP:返回值 -- " + resultJson);
		}catch (Throwable e){
			logger.error(id + " 日志AOP:发生异常 -- " , e);
			resultJson = getResultJson(e);
		}finally {
			logger.error(id + " 日志AOP:执行时间 -- "  + (System.currentTimeMillis() - startTime) + " ms");
			String description = systemLog == null ? "" : systemLog.description();
			insertAgentOperLog(paremJson, resultJson, pjp, description);
		}
	}

	/**
	 * 插日志到数据库
	 * @param paramJson	 	请求参数
	 * @param resultJson    返回值或者错误信息
	 * @param pjp			切点
	 * @param description  切点的描述.如果切点有systemLog注解,则有描述,否则没有
	 */
	private void insertAgentOperLog(String paramJson, String resultJson, JoinPoint pjp, String description) {
		try {
			if (StringUtils.isNotBlank(paramJson) && paramJson.length() > 50000){
				paramJson = paramJson.substring(0, 50000);
			}
			if (StringUtils.isNotBlank(resultJson) && resultJson.length() > 50000){
				resultJson = resultJson.substring(0, 50000);
			}
			AgentOperLogBean bean = new AgentOperLogBean();
			bean.setRequsetParams(paramJson);
			bean.setReturnResult(resultJson);
			bean.setAgentNo(getLoginAgentNo());
			bean.setAgentName(getLoginAgentName());
			bean.setMethodDesc(description);
			bean.setOperIp(getRemoteAddr());
			bean.setRequestMethod(getMethodName(pjp));
			agentOperLogService.insertLog(bean);
		} catch (Exception e) {
			logger.error("日志AOP:插入日志异常信息:", e);
		}
	}
	/**
	 * 获取返回值的json字符串
	 * @param proceed 返回值
	 * @return 返回值的字符串
	 */
	private String getResultJson(Object proceed) {
		try {
			if (proceed == null){
				return "";
			}
			return gson.toJson(proceed);
		}catch (Exception e){
            logger.error("getResultJson ==> " + e);
			return "";
		}
	}

	/**
	 * 获取请求参数的json
	 * @param pjp 切点
	 * @return 请求参数的json
	 */
	private String getParamJson(JoinPoint pjp) {
		try {
			Object[] args = pjp.getArgs();
			if (args == null || args.length == 0){
				return "";
			}
			List<Object> argList = new ArrayList<>();
			for (Object arg : args){
				if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse){
					continue;
				}
				argList.add(arg);
			}
			return gson.toJson(argList);
		}catch (Throwable e){
			logger.info("getParamJson ==> " + e);
			return "";
		}
	}

	public String getLoginAgentNo() {
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			return loginAgentNo;
		}catch (Exception e){
            logger.error("getLoginAgentNo ==> " + e);
			return "";
		}
	}

	private String getLoginAgentName() {
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return principal.getRealName();
		}catch (Exception e){
            logger.error("getLoginAgentName ==> " + e);
			return "";
		}
	}
	private String getRemoteAddr(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String xforwardedFor = request.getHeader("x-forwarded-for");
			String xRealIp = request.getHeader("X-Real-IP");
			logger.info("请求头数据: key = x-forwarded-for, value = {}", xforwardedFor);
			logger.info("请求头数据: key = X-Real-IP, value = {}", xRealIp);
			if (StringUtils.isNotBlank(xforwardedFor)) {
				return xforwardedFor.split(",")[0];
			}
			if (StringUtils.isNotBlank(xRealIp)) {
				return xRealIp;
			}
			return request.getRemoteAddr();
		}catch (Exception e){
            logger.error("getRemoteAddr ==> " + e);
			return "";
		}
	}
	private String getMethodName(JoinPoint pjp){
		try {
			return pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName() + "()";
		}catch (Exception e){
            logger.error("getMethodName ==> " + e);
			return "";
		}
	}
}
