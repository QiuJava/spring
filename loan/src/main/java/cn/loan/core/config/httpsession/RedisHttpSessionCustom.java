package cn.loan.core.config.httpsession;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * HttpSession放Redis
 * 
 * @author qiujian
 *
 */
@Configuration
@EnableRedisHttpSession
public class RedisHttpSessionCustom {

}
