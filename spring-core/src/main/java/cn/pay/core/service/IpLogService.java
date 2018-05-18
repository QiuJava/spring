package cn.pay.core.service;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.obj.qo.IpLogQo;

/**
 * 用户登录日志
 * 
 * @author Administrator
 *
 */
public interface IpLogService {

	/**
	 * 获取用户登录日志列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	Page<IpLog> page(IpLogQo qo);

	/**
	 * 获取用户最新的登录日志
	 * 
	 * @param qo
	 * @return
	 */
	IpLog getNewestIpLog(String username);

	void saveAndUpdate(IpLog ipLog);

}
