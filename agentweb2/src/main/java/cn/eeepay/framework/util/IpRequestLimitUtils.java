package cn.eeepay.framework.util;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.exception.RequestLimitException;
/**
 * ip控制访问次数t
 * @author Administrator
 *
 */
@Component
public class IpRequestLimitUtils {
	private static final Logger logger = LoggerFactory.getLogger("requestLimitLogger");

	@Resource
	private SysDictDao sysDictDao;
	
	@Resource
	private RedisTemplate<String, String> redisTemplate;

	public void ipRequestLimit(HttpServletRequest request) throws RequestLimitException {	
		logger.info("=========进入IP限制处理方法===============");
		if (request == null) {
			throw new RequestLimitException("方法中缺失HttpServletRequest参数");
		}
		String ip = request.getLocalAddr();
		String url = request.getRequestURL().toString();
		String limitCount = sysDictDao.SelectServiceId("LIMIT_COUNT");// 限定的次数
		final String key = "agentweb2_req_limit_".concat(url).concat(ip);
		
		String value = redisTemplate.opsForValue().get(key);
		if(StringUtils.isBlank(value)){
			logger.info("=========第一次请求===============");
			redisTemplate.opsForValue().set(key, "1", 60, TimeUnit.SECONDS);
		}else if(Integer.valueOf(value) >= Integer.valueOf(limitCount)){
			logger.info("=========过于频繁操作,超过限定次数==============");
			throw new RequestLimitException();
		}else{
			Long expire = redisTemplate.getExpire(key);
			long timeout = 60;
			if(expire != null || expire > 0L){
				timeout = expire;
			}
			redisTemplate.opsForValue().set(key, (Integer.valueOf(value) + 1) + "", timeout, TimeUnit.SECONDS);
		}
		logger.info("========IpRequestLimitUtils  IP限制请求方法执行完成==============");
//        try {
//            Object[] args = joinPoint.getArgs();
//            HttpServletRequest request = null;
//            for (int i = 0; i < args.length; i++) {
//                if (args[i] instanceof HttpServletRequest) {
//                    request = (HttpServletRequest) args[i];
//                    break;
//                }
//            }
//            if (request == null) {
//                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
//            }
//            String ip = request.getLocalAddr();
//            String url = request.getRequestURL().toString();
//            final String key = "req_limit_".concat(url).concat(ip);
//            if (redisTemplate.get(key) == null || redisTemplate.get(key) == 0) {
//                redisTemplate.put(key, 1);
//            } else {
//                redisTemplate.put(key, redisTemplate.get(key) + 1);
//            }
//            int count = redisTemplate.get(key);
//            if (count > 0) {
//                //创建一个定时器
//                Timer timer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        redisTemplate.remove(key);
//                    }
//                };
//                //这个定时器设定在time规定的时间之后会执行上面的remove方法，也就是说在这个时间后它可以重新访问
//                timer.schedule(timerTask, limit.time());
//            }
//            Integer limitCount = 0;
//            //如果注解上没有配置次数,就查数据字典的值
//            if (limit.count() < 1) {
//            	limitCount = Integer.valueOf(sysDictDao.SelectServiceId("LIMIT_COUNT"));
//			}else{
//				limitCount = limit.count();
//			}
//            if (count > limitCount) {//limit.count()
//                logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limitCount + "]");
//                throw new RequestLimitException();
//            }
//        }catch (RequestLimitException e){
//            throw e;
//        }catch (Exception e){
//            logger.error("发生异常",e);
//        }
    }
}
