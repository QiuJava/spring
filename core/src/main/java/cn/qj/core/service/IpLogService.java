package cn.qj.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.qj.core.common.PageResult;
import cn.qj.core.entity.IpLog;
import cn.qj.core.pojo.qo.IpLogQo;
import cn.qj.core.pojo.vo.IpLogCountVo;
import cn.qj.core.pojo.vo.IpLogVo;

/**
 * Ip登录服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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

	/**
	 * 查询所有登录日志页面展示数据
	 * 
	 * @return
	 */
	List<IpLogVo> listAllVo();

	/**
	 * 原生分页
	 * 
	 * @return
	 */
	Page<IpLog> page();

	/**
	 * 统计登录日志
	 * 
	 * @return
	 */
	IpLogCountVo count();

}
