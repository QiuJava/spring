package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;

import java.util.List;

/**
 * Created by 666666 on 2017/10/27.
 */
public interface ProviderService {
    /**
     * 查询服务商信息(一级代理商)
     * @param providerBean 查询条件
     * @param page 分页条件
     * @param loginAgent
     * @return
     */
    List<ProviderBean> listProvider(ProviderBean providerBean, Page<AgentInfo> page, AgentInfo loginAgent);

    /**
     * 开通信用卡超级还款功能
     * @param agentNoList
     * @param loginAgent
     * @return
     */
    boolean openSuperRepayment(List<String> agentNoList, AgentInfo loginAgent);

    /**
     * 修改服务成本
     * @param bean
     * @param loginAgent
     * @return
     */
    boolean updateServiceCost(ProviderBean bean, AgentInfo loginAgent);
}
