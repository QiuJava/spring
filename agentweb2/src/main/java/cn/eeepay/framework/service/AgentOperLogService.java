package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLogBean;

import java.util.List;

/**
 * 代理商操作日志服务
 */
public interface AgentOperLogService {

	/**
	 * 插入日志
	 * @param bean	日志
	 * @return
	 */
	int insertLog(AgentOperLogBean bean);

	/**
	 * 根据id查询操作日志
	 * @param id 主键id
	 * @return 日志
	 */
	AgentOperLogBean getAgentOperLogById(int id);

	/**
	 * 查询代理商操作日志
	 * @param bean 查询条件
	 * @param page 分页信息
	 * @return	查询结果
	 */
	List<AgentOperLogBean> listAgentOperLog(AgentOperLogBean bean, Page page);
}
