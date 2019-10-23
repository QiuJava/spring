package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.CreditRepayOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditRepayOrder;
import cn.eeepay.framework.service.CreditRepayOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信用卡还款订单服务实现类
 * @author liuks
 */
@Service("creditRepayOrderService")
public class CreditRepayOrderServiceImpl implements CreditRepayOrderService {
    @Resource
    private CreditRepayOrderDao creditRepayOrderDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<CreditRepayOrder> listCreditRepayOrder(CreditRepayOrder order, AgentInfo loginAgent, Page<CreditRepayOrder> page) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(order.getAgentNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        return creditRepayOrderDao.listCreditRepayOrder(order,searchAgent,page);
    }

    @Override
    public CreditRepayOrder countCreditRepayOrder(CreditRepayOrder order, AgentInfo loginAgent) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(order.getAgentNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        return creditRepayOrderDao.countCreditRepayOrder(order,searchAgent);
    }

    @Override
    public List<CreditRepayOrder> exportSelectAllList(CreditRepayOrder order, AgentInfo loginAgent) {
        AgentInfo searchAgent = agentInfoDao.selectByAgentNo(order.getAgentNo());
        if (null == searchAgent || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return null;
        }
        return creditRepayOrderDao.exportSelectAllList(order, searchAgent);
    }

    @Override
    public CreditRepayOrder selectById(String batchNo, AgentInfo loginAgent) {
        return creditRepayOrderDao.selectById(batchNo, loginAgent);
    }
}
