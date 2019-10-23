package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.SysConfig;

/**
 * 系统配置dao
 * @author yangle
 *
 */
@WriteReadDataSource
public interface SysConfigDao {
	@Select("select * from sys_config where param_key=#{key}")
	@ResultType(SysConfig.class)
	List<SysConfig> getByKey(@Param("key")String key);
	
	@Update("update sys_config set param_key=#{config.paramKey}, param_value=#{config.paramValue} where id=#{config.id}")
	int update(@Param("config")SysConfig config);

	@Update("update sys_config set param_value=#{config.paramValue} where param_key=#{config.paramKey}")
	int updateByParamKey(@Param("config")SysConfig config);

	@Update("update sys_config set param_value=#{value} where param_key=#{key}")
	int updateParamValueByParamKey(@Param("key")String key, @Param("value") String value);

	@Insert("insert sys_config (param_key, param_value, remark) values(#{config.paramKey}, #{config.paramValue}, #{config.remark})")
	int insert(@Param("config")SysConfig config);

	@Select("select param_value from sys_config where param_key=#{key} limit 1")
	String getStringValueByKey(@Param("key")String key);

	@Select("select * from sys_config where param_key=#{key} limit 1 ")
	@ResultType(SysConfig.class)
	SysConfig queryTheFirstByKey(@Param("key")String key);
}
