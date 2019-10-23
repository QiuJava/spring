package cn.eeepay.framework.db.pagination;

import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 数据库方言工厂,产生方言对象
 * @author 沙
 *
 */
public class DialectFactory {
	
	private static final Logger log = LoggerFactory.getLogger(DialectFactory.class);

    public static String dialectClass = null;

    public static Dialect buildDialect(Configuration configuration) {
        if (dialectClass == null) {
            synchronized (DialectFactory.class) {
                if (dialectClass == null) {
                    dialectClass = configuration.getVariables().getProperty("dialectClass");
                }
            }
        }
        Dialect dialect = null;
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
        	log.error("异常:",e);
            System.err.println("请检查 mybatis-config.xml 中  dialectClass 是否配置正确?");
        }
        return dialect;
    }
}
