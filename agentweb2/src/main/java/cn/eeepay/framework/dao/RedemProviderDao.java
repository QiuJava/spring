package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemProviderBean;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 666666 on 2017/10/27.
 */
@WriteReadDataSource
public interface RedemProviderDao {

    /**
     * 查询服务商
     */
    List<RedemProviderBean> listProvider(@Param("bean") RedemProviderBean providerBean,
                                         Page<AgentInfo> page,
                                         @Param("loginAgent") AgentInfo loginAgent);

    /**
     * 查询代理商的积分兑换激活版成本
     */
    RedemProviderBean queryRedemActiveCost(@Param("agentNo") String agentNo);

    /**
     * 检查代理商编号集合时候为登陆代理商直接下级
     */
    int chechAgentNoIsDirectChildren(@Param("list") List<String> agentNoList, @Param("loginAgent") AgentInfo loginAgent);

    /**
     * 开通积分兑换激活版功能
     */
    void openRedemActive(@Param("list") List<RedemProviderBean> wantAddAgent);

    /**
     * 更新积分兑换激活版功能
     */
    int updateServiceCost(@Param("bean") RedemProviderBean bean);

    /**
     * 查询下级代理商的积分兑换成本
     */
    RedemProviderBean queryChildrenMaxRedemActiveCost(@Param("agentNode") String agentNode);
}
