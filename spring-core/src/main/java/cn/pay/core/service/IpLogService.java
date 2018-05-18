package cn.pay.core.service;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.obj.qo.IpLogQo;

/**
 * 用户登录日志服务
 * 
 * @author Administrator
 *
 */
public interface IpLogService {

	/**
	 * 根据登录日志查询条件对象登录历史页面结果集
	 * 
	 * @param qo
	 * @return
	 */
	Page<IpLog> page(IpLogQo qo);

	/**
	 * 获取用户最新的登录日志信息
	 * 
	 * @param qo
	 * @return
	 */
	IpLog getNewestIpLog(String username);

	/**
	 * 保存或者更新
	 * 
	 * @param ipLog
	 */
	void saveAndUpdate(IpLog ipLog);

}
