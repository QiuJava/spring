package cn.eeepay.framework.dao.bill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;

import cn.eeepay.framework.model.bill.SysLogs;
/**
 * 操作日志
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年11月30日08:57:45
 *
 */
public interface SysLogsMapper {
	@Insert("insert into sys_logs(description,type,method,args,ip,create_operator,create_time)"
			+ " values(#{sysLogs.description},#{sysLogs.type},#{sysLogs.method},#{sysLogs.args},#{sysLogs.ip},#{sysLogs.createOperator},#{sysLogs.createTime})")
	@ResultMap("cn.eeepay.framework.dao.bill.SysLogsMapper.BaseResultMap")
	int insertSysLogs(@Param("sysLogs")SysLogs sysLogs);
}
