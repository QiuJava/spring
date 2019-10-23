package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.AutoCheckRoute;
import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;

public interface AutoCheckRuleService {
	
	SysConfigAutoCheck selectByParamKey(@Param("paramKey")String paramKey);

	AutoCheckRoute selectByChannelCode(@Param("channelCode")String channelCode);
	
	int updateValue(Map<String, Object> sysConfigAutoCheck);

	List<AutoCheckRule> selectAll();

	int updateState(@Param("autoCheckRule") AutoCheckRule autoCheckRule);

	int updateIsOpen(AutoCheckRule info);
	
	int updateIsPass(AutoCheckRule info);

	List<AutoCheckRoute> listByRouteType(int i);

}
