package cn.pay.core.service;

import cn.pay.core.domain.business.SystemAccountFlow;

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
