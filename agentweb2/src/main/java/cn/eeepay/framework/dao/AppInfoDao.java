package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.MobileVerInfo;

import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.*;

@ReadOnlyDataSource
public interface AppInfoDao {

	@Select("SELECT version,app_url,down_flag,ver_desc,lowest_version" +
			" FROM `mobile_ver_info` " +
			" WHERE app_type=#{appType} and platform=#{platform}" +
			" ORDER BY version desc limit 1")
	@ResultType(MobileVerInfo.class)
    MobileVerInfo getVersion(MobileVerInfo currentVer);


}
