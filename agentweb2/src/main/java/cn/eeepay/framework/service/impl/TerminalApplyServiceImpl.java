package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.TerminalApplyDao;
import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.TerminalApply;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.TerminalApplyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("terminalApplyService")
public class TerminalApplyServiceImpl implements TerminalApplyService{

	@Resource
	private TerminalApplyDao terminalApplyDao;
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private MerchantInfoDao merchantInfoDao;
	@Resource
	private TerminalInfoDao terminalInfoDao;
	@Resource
	private AccessService accessService;
	@Override
	public List<TerminalApply> queryAllInfo(Page<TerminalApply> page, TerminalApply terminalApply,String loginAgentNo) {
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(loginAgentNo);
		String loginAgentNode = loginAgentNo;
		if (agentInfo != null){
			loginAgentNode = StringUtils.isBlank( agentInfo.getAgentNode()) ? loginAgentNo : agentInfo.getAgentNode();
		}
		return terminalApplyDao.queryAllInfo(page, terminalApply,loginAgentNode);
	}

	@Override
	public TerminalApply queryInfoDetail(String id) {
		TerminalApply terminalApply = terminalApplyDao.queryInfoDetail(id);
		if (terminalApply == null || StringUtils.isBlank(terminalApply.getMerchantNo())){
			return terminalApply;
		}
		if (!accessService.canAccessTheMerchant(terminalApply.getMerchantNo(), false)) {
			return null;
		}
		MerchantInfo merchantInfo = merchantInfoDao.selectMn(terminalApply.getMerchantNo());
		terminalApply.setMerchantName(merchantInfo == null ? "" : merchantInfo.getMerchantName());
		if (merchantInfo != null && StringUtils.isNotBlank(merchantInfo.getAgentNo())){
			AgentInfo agentInfo = agentInfoDao.selectByAgentNo(merchantInfo.getAgentNo());
			terminalApply.setAgentName(agentInfo == null ? "" : agentInfo.getAgentName() + "(" + agentInfo.getAgentNo() + ")");
			terminalApply.setAgentNo(agentInfo == null ? "" : agentInfo.getAgentNo());
			terminalApply.setAgentNode(agentInfo == null ? "" : agentInfo.getAgentNode());
			if (agentInfo != null && StringUtils.isNotBlank(agentInfo.getOneLevelId())){
				AgentInfo oneAgentInfo = agentInfoDao.selectByAgentNo(agentInfo.getOneLevelId());
				terminalApply.setOneLevelId(oneAgentInfo == null ? "" : oneAgentInfo.getAgentNo());
				terminalApply.setOneAgentName(oneAgentInfo == null ? "" : oneAgentInfo.getAgentName() + "(" + oneAgentInfo.getAgentNo() + ")");
			}
		}
		return terminalApply;
	}

	@Override
	public void dealWithTerminalApply(TerminalApply terminalApply, String loginAgentNo) {
		if(terminalApply == null || StringUtils.isBlank(terminalApply.getId())){
			throw new AgentWebException("找不到该记录申请记录.");
		}
		TerminalApply result = queryInfoDetail(terminalApply.getId());
		if (result == null){
			throw new AgentWebException("找不到该记录申请记录.");
		}
		if (StringUtils.equals(result.getStatus(), "1")){
			throw new AgentWebException("该机具申请记录已经处理,无需重复处理.");
		}
//		if (StringUtils.isBlank(terminalApply.getRemark()) || terminalApply.getRemark().length() < 10){
//			throw new AgentWebException("处理备注,至少输入10个字符.");
//		}
		// 处理状态为待直属代理商处理,登陆代理商是否为直属代理商
		boolean agentCanDealWith = StringUtils.equals(result.getStatus(), "0") &&
				StringUtils.equals(loginAgentNo, result.getAgentNo());
		// 处理状态为待一级代理商处理,登陆代理商是否为一级代理商
		boolean oneAgentCanDealWith = StringUtils.equals(result.getStatus(), "2") &&
				StringUtils.equals(loginAgentNo, result.getOneLevelId());
		// 商户所属代理商不是一级代理商
		boolean oneLevelAgemtNoDiff2AgentNo = !StringUtils.equals(result.getOneLevelId(), result.getAgentNo());
		if (!(agentCanDealWith || oneAgentCanDealWith)){
			throw new AgentWebException("您无权处理该机具申请记录");
		}
		if (oneAgentCanDealWith && oneLevelAgemtNoDiff2AgentNo){
			if (StringUtils.isBlank(terminalApply.getSn())){
				throw new AgentWebException("您必须输入申请的机具SN号");
			}
			// 处理机具下发功能
			TerminalInfo terminalInfo = terminalInfoDao.checkSn(loginAgentNo, terminalApply.getSn(), "1");
			if (terminalInfo == null){
				throw new AgentWebException("该机具不存在或已经被使用或不属于登陆代理商的,无法下发.");
			}
			TerminalInfo updateInfo = new TerminalInfo();
			updateInfo.setId(terminalInfo.getId());
			updateInfo.setAgentNo(result.getAgentNo());
			updateInfo.setAgentNode(result.getAgentNode());
			updateInfo.setOpenStatus("1");
			terminalInfoDao.solutionById(updateInfo);
		}
		terminalApplyDao.updateInfo(result.getId(), "1", terminalApply.getRemark(), terminalApply.getSn());
	}
}
