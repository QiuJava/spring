package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentAccPreAdjust;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/5/23
 * Time: 14:46
 * Description: 类注释
 */
public interface AgentAccPreAdjustService {

    List<AgentAccPreAdjust> findAgentAccPreAdjustList(AgentAccPreAdjust agentAccPreAdjust, Map<String, String> params, Sort sort, Page<AgentAccPreAdjust> page);

    List<AgentAccPreAdjust> exportAgentAccPreAdjustList(AgentAccPreAdjust agentAccPreAdjust);

    Map<String,Object> findAdjustmentAmountCollection(AgentAccPreAdjust agentAccPreAdjust);
}
