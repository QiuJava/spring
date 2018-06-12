package cn.pay.core.obj.aop;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import cn.pay.core.obj.annotation.RequestLimit;
import cn.pay.core.util.LogicException;

/**
 * 请求限制的AOP工具类
 * 
 * @author Qiujian
 *
 */
@Aspect
@Component
public class RequestLimitAopUtil {
	private static final Logger logger = LoggerFactory.getLogger(RequestLimitAopUtil.class);

	@Resource
	private ValueOperations<String, Integer> valueOperations;

	@Before("execution(* cn.pay..web.controller.*Controller.*(..)) && @annotation(limit)")
	public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws LogicException {
		try {
			Object[] args = joinPoint.getArgs();
			HttpServletRequest request = null;
			// 拿到HttpServletRequest
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof HttpServletRequest) {
					request = (HttpServletRequest) args[i];
					break;
				}
			}
			if (request == null) {
				throw new LogicException("方法中缺失HttpServletRequest参数");
			}
			String ip = request.getLocalAddr();
			String url = request.getRequestURL().toString();
			String key = "req_limit_".concat(url).concat(ip);
			if (valueOperations.get(key) == null || valueOperations.get(key) == 0) {
				valueOperations.set(key, 1);
			} else {
				valueOperations.set(key, valueOperations.get(key) + 1);
			}
			int count = valueOperations.get(key);
			if (count > 0) {
				// 创建一个定时器
				Timer timer = new Timer();
				TimerTask timerTask = new TimerTask() {
					@Override
					public void run() {
						valueOperations.getOperations().delete(key);
					}
				};
				// 这个定时器设定在time规定的时间之后会执行上面的remove方法，也就是说在这个时间后它可以重新访问
				timer.schedule(timerTask, limit.time());
			}
			if (count > limit.count()) {
				logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
				throw new LogicException("超过" + limit.count() + "次");
			}
		} catch (LogicException e) {
			throw e;
		} catch (Exception e) {
			logger.error("发生异常", e);
		}
	}
}