package cn.qj.core.service;

import cn.qj.core.entity.SystemAccountFlow;

/**
 * 系统账户流水
 * 
 * @author Administrator
 *
 */
public interface SystemAccountFlowService {

	/**
	 * 保存或更新
	 * 
	 * @param flow
	 */
	void saveAndUpdate(SystemAccountFlow flow);

}
