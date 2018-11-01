package cn.qj.core.service;

import cn.qj.core.entity.SystemAccountFlow;

/**
 * 系统账户流水服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface SystemAccountFlowService {

	/**
	 * 保存或更新
	 * 
	 * @param flow
	 */
	void saveAndUpdate(SystemAccountFlow flow);

}
