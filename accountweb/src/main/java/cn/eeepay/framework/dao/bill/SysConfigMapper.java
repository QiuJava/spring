package cn.eeepay.framework.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.SysConfig;

/**
 * 系统配置dao
 * @author yangle
 *
 */
public interface SysConfigMapper {
	@Select("select * from sys_config where param_key=#{key}")
	@ResultType(SysConfig.class)
	List<SysConfig> getByKey(@Param("key")String key);
	
	@Update("update sys_config set param_key=#{config.paramKey}, param_value=#{config.paramValue} where id=#{config.id}")
	int update(@Param("config")SysConfig config);
}
