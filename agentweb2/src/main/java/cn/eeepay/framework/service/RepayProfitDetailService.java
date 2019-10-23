package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RepayProfitDetailBean;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/11/20.
 */
public interface RepayProfitDetailService {
    /**
     * 超级超级还款分润
     * @param bean      查询条件
     * @param loginAgent 当前登陆代理商
     * @param page 分页信息
     * @return
     */
    List<RepayProfitDetailBean> listRepayProfitDetail(RepayProfitDetailBean bean,
                                                      AgentInfo loginAgent,
                                                      Page<RepayProfitDetailBean> page);

    /**
     * 汇总超级超级还款分润（这个方法已经没有用了）
     * @param bean      查询条件
     * @param loginAgent 当前登陆代理商
     * @return
     */
    @Deprecated
    RepayProfitDetailBean sumRepayProfitDetail(RepayProfitDetailBean bean, AgentInfo loginAgent);

    /**
     * 导出超级超级还款分润
     * @param bean      查询条件
     * @param loginAgent 当前登陆代理商
     * @return
     */
    List<RepayProfitDetailBean> exportRepayProfitDetail(RepayProfitDetailBean bean, AgentInfo loginAgent);

    /**
     * 计算分润
     * @param bean      查询条件
     * @param loginAgent 当前登陆代理商
     * @return
     */
    Map<String,String> calcShareAmount(RepayProfitDetailBean bean, AgentInfo loginAgent);
}
