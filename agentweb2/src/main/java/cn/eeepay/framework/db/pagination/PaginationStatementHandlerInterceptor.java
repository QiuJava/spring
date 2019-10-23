package cn.eeepay.framework.db.pagination;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.util.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 沙
 *
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationStatementHandlerInterceptor implements Interceptor {

    private final static Logger logger = LoggerFactory.getLogger(PaginationStatementHandlerInterceptor.class);

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        ParameterHandler parameterHandler = statementHandler.getParameterHandler();
        BoundSql boundSql = statementHandler.getBoundSql();

        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
        // 没有分页参数
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }

        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        Dialect dialect = DialectFactory.buildDialect(configuration);
        String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        // 获取总记录数
        Page<?> page = (Page<?>) rowBounds;
        //如果查询第一页，需要查询统计，并将统计结果放入redis缓存

        //如果不是第一页，取缓存的统计结果，缓存取不到，则从数据库查询统计
        String countSql = dialect.getCountString(originalSql);
        Connection connection = (Connection) invocation.getArgs()[0];
        int total = getTotal(parameterHandler, connection, countSql, statementHandler, page.getFirst());
        page.setTotalCount(total);

        // 设置物理分页语句
        metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, page.getFirst(), page.getPageSize()));
        // 屏蔽mybatis原有分页
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        logger.info("\n分页SQL： "+boundSql.getSql());
        logger.info("\n统计SQL： "+countSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 获取总计录
     *
     * @param parameterHandler
     * @param connection
     * @param countSql
     * @param statementHandler
     * @param pageFirst
     * @return
     * @throws Exception
     */
    private int getTotal(ParameterHandler parameterHandler, Connection connection, String countSql, StatementHandler statementHandler, int pageFirst) throws Exception {
        // MetaObject metaStatementHandler =
        // MetaObject.forObject(parameterHandler);
        // Object parameterObject =
        // metaStatementHandler.getValue("parameterObject");
        // TODO 缓存具有相同SQL语句和参数的总数
        if(pageFirst > 1) {
            Integer redisCount = getRedisCount(countSql, statementHandler);
            if(redisCount != null) {
                return redisCount;
            }
        }

        PreparedStatement prepareStatement = connection.prepareStatement(countSql);
        parameterHandler.setParameters(prepareStatement);
        ResultSet rs = prepareStatement.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        rs.close();
        prepareStatement.close();
        insertCountRedis(countSql, statementHandler, count);
        return count;
    }

    /**
     * 将count存入redis
     * @param countSql
     * @param statementHandler
     * @param count
     * @throws Exception
     */
    private void insertCountRedis(String countSql, StatementHandler statementHandler, int count) throws Exception {
        try {
            String redisKey = getCountKey(countSql, statementHandler);
            RedisService redisService = SpringContextHolder.getBean(RedisService.class);
            redisService.insertString(redisKey, String.valueOf(count), 30 * 60L);
        } catch (Exception e) {
            logger.error("存入缓存失败", e);
        }

    }

    /**
     * 从redis获取count
     * @param countSql
     * @param statementHandler
     * @return
     */
    private Integer getRedisCount(String countSql, StatementHandler statementHandler){
        Integer count = null;
        try {
            String redisKey = getCountKey(countSql, statementHandler);
            RedisService redisService = SpringContextHolder.getBean(RedisService.class);
            Object value = redisService.select(redisKey.toString());
            if(value == null) {
                return null;
            }
            count = Integer.valueOf(String.valueOf(value));
        } catch (Exception e) {
            logger.error("查询统计获取缓存失败", e);
        }
        return count;
    }

    /**
     * 获取count在redis里面的key
     * key = agentWeb2_ + functionName + md5(countSql)
     * @param countSql
     * @param statementHandler
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String getCountKey(String countSql, StatementHandler statementHandler) throws Exception {
        StringBuilder redisKey = new StringBuilder("agentWeb2_");
        String functionName = "";
        String countSqlStr = "";
        Field delegate = RoutingStatementHandler.class.getDeclaredField("delegate");
        if (delegate != null) {
            delegate.setAccessible(true);
            StatementHandler o = (StatementHandler)delegate.get(statementHandler);
            Field mappedStatement = BaseStatementHandler.class.getDeclaredField("mappedStatement");
            mappedStatement.setAccessible(true);
            MappedStatement mapper = (MappedStatement)mappedStatement.get(o);
            functionName = mapper.getId();
            Field parameterHandler = BaseStatementHandler.class.getDeclaredField("parameterHandler");
            parameterHandler.setAccessible(true);
            Object parameterHandlerValue = parameterHandler.get(o);
            Method getParameterObject = parameterHandlerValue.getClass().getDeclaredMethod("getParameterObject");
            getParameterObject.setAccessible(true);
            Object invoke = getParameterObject.invoke(parameterHandlerValue);
            countSqlStr = JSONObject.toJSONString(invoke);
        }
        redisKey.append(functionName);
        String md5CountSql = Md5.md5Str(countSqlStr);
        redisKey.append("_").append(md5CountSql);
        return redisKey.toString();
    }
}
