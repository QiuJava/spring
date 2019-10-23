package cn.eeepay.boss.aop;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.model.bill.SysLogs;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.SysLogsService;
import cn.eeepay.framework.util.IPUtil;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author zouruijin
 * zrj@eeepay.cn rjzou@qq.com
 * 2016年11月30日15:40:46
 * 
 */
@Aspect // for aop
public class SysLogsInterceptor {
	private static final Logger log = LoggerFactory.getLogger(SysLogsInterceptor.class);
	@Resource
	private HttpServletRequest request;
	@Resource
	private SysLogsService sysLogsService;
	//action层切入点
	@Pointcut("execution(* cn.eeepay.boss.action.*.add*(..)) || execution(* cn.eeepay.boss.action.*.update*(..)) "
			+ "|| execution(* cn.eeepay.boss.action.*.delete*(..)) || execution(* cn.eeepay.boss.action.*.save*(..)) "
			+ "|| execution(* cn.eeepay.boss.action.*.reset*(..)) || execution(* cn.eeepay.boss.action.*.refresh*(..)) "
			+ "|| execution(* cn.eeepay.boss.action.*.confirm*(..))")
	public void actionAspect() {
	};
	//service层切入点
//    @Pointcut("execution(* cn.eeepay.framework.service.bill.*.insert*(..)) || execution(* cn.eeepay.framework.service.bill.*.delete*(..)) "
//    		+ "|| execution(* cn.eeepay.framework.service.bill.*.update*(..))")  
//    public void serviceAspect() {  
//    }  

	@Before("actionAspect()")
	public void before(JoinPoint jp) {
		// 获取请求ip  
		String ip = IPUtil.getIpAddr(request); 
		String userName = "anonymousUser";
		if(SecurityContextHolder.getContext().getAuthentication()!= null){
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
				userName = "anonymousUser";
			}
			else{
				// 获取登陆用户信息  
				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
					    .getAuthentication()
					    .getPrincipal();
				userName = userInfo.getUsername();
			}
		}
        
		String params = "";  
        Object[] args = jp.getArgs();  
        if (args != null) {  
            JSONArray jsonArray = new JSONArray();  
            jsonArray.add(args);  
            params = jsonArray.toString();  
        }
        try {  
            // *========控制台输出=========*//  
        	log.info("=====前置操作开始=====");  
            log.info("请求方法:" + (jp.getTarget().getClass().getName() + "."  
                            + jp.getSignature().getName() + "()"));  
            log.info("方法描述:" + getMethodDescription(jp));  
            log.info("操作人:" + userName);  
            log.info("操作IP:" + ip);  
            // *========数据库日志=========*//  
            SysLogs sysLogs = new SysLogs();  
            sysLogs.setDescription(getMethodDescription(jp)); 
            sysLogs.setType("action");
            sysLogs.setMethod((jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()"));
            sysLogs.setArgs(params);
            sysLogs.setIp(ip);  
            sysLogs.setCreateOperator(userName);
            sysLogs.setCreateTime(new Date());
            // 保存数据库  
            sysLogsService.insertSysLogs(sysLogs);
            log.info("=====前置操作结束=====");  
        } catch (Exception e) {  
            // 记录本地异常日志  
        	log.error("==前置通知异常==");  
        	log.error("异常信息:{}", e);  
        }  
	}
	
	/** 
     * 异常通知 用于拦截service层记录异常日志 
     * @param joinPoint 
     * @param e 
     */  
//    @AfterThrowing(pointcut="serviceAspect()", throwing="e")  
//    public void doAfterThrowing(JoinPoint jp, Throwable e) {  
//    	// 获取请求ip  
//		String ip = IPUtil.getIpAddr(request); 
//		String userName = "anonymousUser";
//		if(SecurityContextHolder.getContext().getAuthentication()!= null){
//			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
//				userName = "anonymousUser";
//			}
//			else{
//				// 获取登陆用户信息  
//				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
//					    .getAuthentication()
//					    .getPrincipal();
//				userName = userInfo.getUsername();
//			}
//		}  
//        // 获取用户请求方法的参数并序列化为JSON格式字符串  
//        String params = "";  
//        Object[] args = jp.getArgs();  
//        if (args != null) {  
//            JSONArray jsonArray = new JSONArray();  
//            jsonArray.add(args);  
//            params = jsonArray.toString();  
//        }  
//        try {  
//            /* ========控制台输出========= */  
//            log.info("=====异常通知开始=====");  
//            log.info("异常代码:" + e.getClass().getName());  
//            log.info("异常信息:" + e.getMessage());  
//            log.info("异常方法:" + (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()"));  
//            log.info("方法描述:" + getMethodDescription(jp));  
//            log.info("请求人:" + userName);  
//            log.info("请求IP:" + ip);  
//            log.info("请求参数:" + params);  
//            /* ==========数据库日志========= */  
//            SysLogs sysLogs = new SysLogs();  
//            sysLogs.setDescription(getMethodDescription(jp));
//            sysLogs.setType("service");
//            sysLogs.setMethod((jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()"));
//            sysLogs.setArgs(params);
//            sysLogs.setIp(ip);  
//            sysLogs.setCreateOperator(userName);
//            sysLogs.setCreateTime(new Date());
//            // 保存数据库  
//            sysLogsService.insertSysLogs(sysLogs);
//            log.info("=====异常通知结束=====");  
//        } catch (Exception ex) {  
//            // 记录本地异常日志  
//        	log.info("==异常通知异常==");  
//        	log.info("异常信息:{}", ex);  
//        }  
//  
//    }
	/** 
     * 获取注解中对方法的描述信息
     * @param jp 切点  
     * @return 方法描述  
     * @throws Exception  
     */  
    public static String getMethodDescription(JoinPoint jp) throws Exception{  
        //获取目标类名  
        String targetName = jp.getTarget().getClass().getName();  
        //获取方法名  
        String methodName = jp.getSignature().getName();  
        //获取相关参数  
        Object[] arguments = jp.getArgs();  
        //生成类对象  
        Class targetClass = Class.forName(targetName);  
        //获取该类中的方法  
        Method[] methods = targetClass.getMethods();  
          
        String description = "";  
          
        for(Method method : methods) {  
            if(!method.getName().equals(methodName)) {  
                continue;  
            }  
            Class[] clazzs = method.getParameterTypes();  
            if(clazzs.length != arguments.length) {  
                continue;  
            }  
            if (method.isAnnotationPresent(Logs.class)) {
            	description = method.getAnnotation(Logs.class).description();  
            }
        }  
        return description;  
    }
}