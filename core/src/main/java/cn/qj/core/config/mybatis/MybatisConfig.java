package cn.qj.core.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置
 * 
 * @author Qiujian
 * @date 2018/11/29
 */
@Configuration
@MapperScan("cn.qj.core.mapper")
public class MybatisConfig {

}
