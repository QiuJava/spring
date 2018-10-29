package cn.pay.core.service;

import cn.pay.core.entity.sys.IpLog;
import cn.pay.core.pojo.qo.IpLogQo;
import cn.pay.core.pojo.vo.PageResult;

/**
 * 用户登录日志服务
 * 
 * @author Administrator
 *
 */
public interface IpLogService {

	/**
	 * 根据查询对象进行分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult pageQueryIpLog(IpLogQo qo);

	/**
	 * 根据用户名获取最新的登录相关信息
	 * 
	 * @param username
	 * @return
	 */
	IpLog getNewestIpLogByUsername(String username);

	/**
	 * 保存登录日志
	 * 
	 * @param ipLog
	 * @return
	 */
	IpLog saveIpLog(IpLog ipLog);

	/**
	 * 更新登录日志
	 * 
	 * @param ipLog
	 * @return
	 */
	IpLog updateIpLog(IpLog ipLog);

}
