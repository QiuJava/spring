package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalApply;

public interface TerminalApplyService {

	List<TerminalApply> queryAllInfo(Page<TerminalApply> page, TerminalApply terminalApply, String loginAgentNo);

	TerminalApply queryInfoDetail(String id);

	/**
	 * 处理机具申请
	 * @param terminalApply	 机具申请信息
	 * @param loginAgentNo   登陆代理商
	 */
    void dealWithTerminalApply(TerminalApply terminalApply, String loginAgentNo);
}
