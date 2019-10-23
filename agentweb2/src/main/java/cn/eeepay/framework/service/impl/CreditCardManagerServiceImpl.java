package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.daoCreditCard.CreditCardManagerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditCardManagerShare;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.CreditCardManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("creditCardManagerService")
public class CreditCardManagerServiceImpl  implements CreditCardManagerService {
    @Resource
    private CreditCardManagerDao creditCardManagerDao;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Override
    public Map<String, Object> queryCreditCardManagerShareList(Map<String, Object> params, Page<CreditCardManagerShare> page) {
        Map<String, Object> msg = new HashMap<>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo parentAgent = agentInfoDao.selectByAgentNo(principal.getUserEntityInfo().getEntityId());
        params.put("agentNode", parentAgent.getAgentNode());
        params.put("agentLevel", parentAgent.getAgentLevel());
        params.put("parentId", parentAgent.getAgentNo());
        List<CreditCardManagerShare> list=creditCardManagerDao.queryCreditCardManagerShareList(params, page);

        msg.put("page", page);
        return msg;
    }

    @Override
    public List<CreditCardManagerShare> exportAllInfo(Map<String, Object> params) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo parentAgent = agentInfoDao.selectByAgentNo(principal.getUserEntityInfo().getEntityId());
        params.put("agentNode", parentAgent.getAgentNode());
        params.put("agentLevel", parentAgent.getAgentLevel());
        params.put("parentId", parentAgent.getAgentNo());
        List<CreditCardManagerShare> list=creditCardManagerDao.queryExportAllInfoList(params);
        return list;
    }

    @Override
    public String getTodayShareAmount(String entityId) {
        return creditCardManagerDao.getTodayShareAmount(entityId);
    }

    @Override
    public String getShareTotalMoney(Map<String, Object> params) {
        return creditCardManagerDao.getShareTotalMoney(params);
    }

    @Override
    public String getTradeTotalMoney(Map<String, Object> params) {
        return creditCardManagerDao.getTradeTotalMoney(params);
    }
}
