package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.RepayProfitDetailDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RepayProfitDetailBean;
import cn.eeepay.framework.service.RepayProfitDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/11/20.
 */
@Service
public class RepayProfitDetailServiceImpl implements RepayProfitDetailService {
    @Resource
    private RepayProfitDetailDao profitDetailDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<RepayProfitDetailBean> listRepayProfitDetail(RepayProfitDetailBean bean,
                                                             AgentInfo loginAgent,
                                                             Page<RepayProfitDetailBean> page) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getProfitMerNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        return profitDetailDao.listRepayProfitDetail(bean, searchAgent, page);
    }

    @Override
    public RepayProfitDetailBean sumRepayProfitDetail(RepayProfitDetailBean bean, AgentInfo loginAgent) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getProfitMerNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        return profitDetailDao.sumRepayProfitDetail(bean, searchAgent);
    }

    @Override
    public List<RepayProfitDetailBean> exportRepayProfitDetail(RepayProfitDetailBean bean, AgentInfo loginAgent) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getProfitMerNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        return profitDetailDao.listRepayProfitDetail(bean, searchAgent);
    }

    @Override
    public Map<String, String> calcShareAmount(RepayProfitDetailBean bean, AgentInfo loginAgent) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getProfitMerNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        BigDecimal allShareAmount = profitDetailDao.sumAllShareAmount(bean, searchAgent);
        BigDecimal childShareAmount = profitDetailDao.sumChildShareAmount(bean, searchAgent, searchAgent.getAgentLevel() + 1);
        Map<String, String> result = new HashMap<>();
        result.put("allShareAmount", allShareAmount.toString());
        result.put("childShareAmount", childShareAmount.toString());
        result.put("selfShareAmount", allShareAmount.subtract(childShareAmount).toString());
        return result;
    }
}
