package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.AgentAccPreAdjustMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentAccPreAdjust;
import cn.eeepay.framework.service.bill.AgentAccPreAdjustService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/5/23
 * Time: 14:46
 * Description: 类注释
 */
@Service("agentAccPreAdjustService")
public class AgentAccPreAdjustServiceImpl implements AgentAccPreAdjustService {

    @Resource
    private AgentAccPreAdjustMapper agentAccPreAdjustMapper;


    @Override
    public List<AgentAccPreAdjust> findAgentAccPreAdjustList(AgentAccPreAdjust agentAccPreAdjust, Map<String, String> params, Sort sort, Page<AgentAccPreAdjust> page) {

        return agentAccPreAdjustMapper.findAgentAccPreAdjustList(agentAccPreAdjust,params,sort,page);
    }

    @Override
    public List<AgentAccPreAdjust> exportAgentAccPreAdjustList(AgentAccPreAdjust agentAccPreAdjust) {
        return agentAccPreAdjustMapper.exportAgentAccPreAdjustList(agentAccPreAdjust);
    }

    @Override
    public Map<String, Object> findAdjustmentAmountCollection(AgentAccPreAdjust agentAccPreAdjust) {
        return agentAccPreAdjustMapper.findAdjustmentAmountCollection(agentAccPreAdjust);
    }
}
