package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.model.IndustrySwitch;
import cn.eeepay.framework.model.IndustrySwitchInfo;

public interface FunctionManagerService {

	/**
	 * 查询所有
	 * @return
	 */
	List<FunctionManager> selectFunctionManagers();

	int updateFunctionSwitch(FunctionManager info);

	int updateAgentControl(FunctionManager info);

	FunctionManager getFunctionManager(int id);

	FunctionManager getFunctionManagerByNum(String funcNum);

	IndustrySwitchInfo getIndustrySwitchInfo();

	void industrySwitchSave(IndustrySwitch data);

	void industrySwitchDelete(Long id);

	void industrySwitchUpdate(Integer industrySwitch);

}
