package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.InvitePrizesMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.InvitePrizesMerchantBean;
import cn.eeepay.framework.service.InvitePrizesMerchantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/19.
 */
@Service
public class InvitePrizesMerchantServiceImpl implements InvitePrizesMerchantService {

    @Resource
    private InvitePrizesMerchantDao invitePrizesMerchantDao;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Override
    public List<InvitePrizesMerchantBean> listInvitePrizesMerchant(InvitePrizesMerchantBean bean, String loginAgentNo, Page<InvitePrizesMerchantBean> page) {
        AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
        if (StringUtils.isNotBlank(bean.getAgentNo())){
            AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getAgentNo());
            if (searchAgent != null){
                bean.setAgentNode(searchAgent.getAgentNode());
            }
        }
        return invitePrizesMerchantDao.listInvitePrizesMerchant(bean, loginAgent.getAgentNode(), page);
    }

    @Override
    public Map<String, String> countInvitePrizesMerchant(InvitePrizesMerchantBean bean, String loginAgentNo) {
        AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
        if (StringUtils.isNotBlank(bean.getAgentNo())){
            AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getAgentNo());
            if (searchAgent != null){
                bean.setAgentNode(searchAgent.getAgentNode());
            }
        }
        return invitePrizesMerchantDao.countInvitePrizesMerchant(bean, loginAgent.getAgentNode());
    }
}
