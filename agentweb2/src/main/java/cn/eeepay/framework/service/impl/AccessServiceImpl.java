package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.ActivityOrderInfoService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.TransInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class AccessServiceImpl implements AccessService {

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Resource
    private MerchantInfoDao merchantInfoDao;
    @Resource
    private TerminalInfoDao terminalInfoDao;
    @Resource
    private TransInfoDao transInfoDao;
    @Resource
    private TransInfoService transInfoService;
    @Resource
    private ActivityOrderInfoService activityOrderInfoService;

    @Override
    public boolean canAccessTheAgent(String agentNo, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            return false;
        }
        if (isOwn){
            return StringUtils.equals(agentNo, loginAgent.getAgentNo());
        }
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
        if (agentInfo == null) {
            return false;
        }
        return agentInfo.getAgentNode().startsWith(loginAgent.getAgentNode());
    }
    @Override
    public boolean canAccessTheAgentApi(String entityId,String agentNo, boolean isOwn) {
//    	AgentInfo loginAgent = agentInfoService.selectByPrincipal();
    	AgentInfo loginAgent = agentInfoDao.selectByAgentNo(entityId);
    	if (loginAgent == null) {
    		return false;
    	}
    	if (isOwn){
    		return StringUtils.equals(agentNo, loginAgent.getAgentNo());
    	}
    	AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
    	if (agentInfo == null) {
    		return false;
    	}
    	return agentInfo.getAgentNode().startsWith(loginAgent.getAgentNode());
    }

    @Override
    public boolean canAccessTheTransOrder(String orderId, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            return false;
        }
        CollectiveTransOrder collectiveTransOrder = transInfoDao.queryInfoByOrderNo(orderId);
        if (collectiveTransOrder == null || StringUtils.isBlank(collectiveTransOrder.getAgentNode())){
            return false;
        }
        return isOwn ? StringUtils.equals(loginAgent.getAgentNode(), collectiveTransOrder.getAgentNode()) :
                collectiveTransOrder.getAgentNode().startsWith(loginAgent.getAgentNode());
    }

    @Override
    public boolean canAccessTheMerchant(String merchantNo, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            return false;
        }
        MerchantInfo merchantInfo = merchantInfoDao.selectMerchantById(merchantNo);
        if (merchantInfo == null || StringUtils.isBlank(merchantInfo.getParentNode())){
            return false;
        }
        return isOwn ? StringUtils.equals(loginAgent.getAgentNode(), merchantInfo.getParentNode()) :
                merchantInfo.getParentNode().startsWith(loginAgent.getAgentNode());
    }

    @Override
    public boolean canAccessTheTerminalBySn(String terminalSn, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            return false;
        }
        TerminalInfo terminalInfo = terminalInfoDao.querySn(terminalSn);
        if (terminalInfo == null || StringUtils.isBlank(terminalInfo.getAgentNode())){
            return false;
        }
        return isOwn ? StringUtils.equals(terminalInfo.getAgentNode(), loginAgent.getAgentNode()) :
                terminalInfo.getAgentNode().startsWith(loginAgent.getAgentNode());
    }

    @Override
    public boolean canAccessTheTerminalById(long terminalId, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            return false;
        }
        TerminalInfo terminalInfo = terminalInfoDao.selectById(terminalId);
        if (terminalInfo == null || StringUtils.isBlank(terminalInfo.getAgentNode())){
            return false;
        }
        return isOwn ? StringUtils.equals(terminalInfo.getAgentNode(), loginAgent.getAgentNode()) :
                terminalInfo.getAgentNode().startsWith(loginAgent.getAgentNode());
    }

    @Override
    public boolean canQueryTheCollectiveTransOrderInfo(String orderNo,boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        String agentNode = loginAgent.getAgentNode();
        CollectiveTransOrder transInfo = transInfoService.selectByOrderNo(orderNo);
        if(transInfo == null){
            return false;
        }
        return isOwn ? StringUtils.equals(transInfo.getAgentNode(), agentNode) :
                transInfo.getAgentNode().startsWith(agentNode);
    }

    @Override
    public boolean canQueryProfitSettleDetailList(String agentNo, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        AgentInfo agentInfo = agentInfoService.selectByagentNo(agentNo);
        String agentNode = agentInfo.getAgentNode();
        return isOwn ? StringUtils.equals(agentNode, loginAgent.getAgentNode()) :
                agentNode.startsWith(loginAgent.getAgentNode());
    }

    @Override
    public boolean canQueryActOrderInfo(String orderNo, boolean isOwn) {
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        Map<String, Object> actOrderInfo = activityOrderInfoService.selectByOrderNo(orderNo);
        if(actOrderInfo == null){
            return false;
        }
        String agentNode = (String)actOrderInfo.get("agentNode");
        return isOwn ? StringUtils.equals(loginAgent.getAgentNode(), agentNode) : agentNode.startsWith(loginAgent.getAgentNode());
    }

}
