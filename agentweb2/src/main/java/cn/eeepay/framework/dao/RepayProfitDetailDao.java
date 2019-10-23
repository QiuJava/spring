package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RepayProfitDetailBean;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 666666 on 2017/11/20.
 */
@ReadOnlyDataSource
public interface RepayProfitDetailDao {

    List<RepayProfitDetailBean> listRepayProfitDetail(@Param("bean") RepayProfitDetailBean bean,
                                                      @Param("searchAgent") AgentInfo searchAgent,
                                                      Page<RepayProfitDetailBean> page);

    List<RepayProfitDetailBean> listRepayProfitDetail(@Param("bean") RepayProfitDetailBean bean,
                                                      @Param("searchAgent") AgentInfo searchAgent);

    RepayProfitDetailBean sumRepayProfitDetail(@Param("bean") RepayProfitDetailBean bean,
                                               @Param("searchAgent") AgentInfo searchAgent);


    BigDecimal sumAllShareAmount(@Param("bean") RepayProfitDetailBean bean,@Param("searchAgent") AgentInfo searchAgent);

    BigDecimal sumChildShareAmount(@Param("bean") RepayProfitDetailBean bean,@Param("searchAgent") AgentInfo searchAgent, @Param("agentLevel") int agentLevel);
}
