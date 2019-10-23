package cn.eeepay.framework.daoRedem.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActiveOrderBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 积分兑换(激活版)
 * Created by 666666 on 2018/5/7.
 */
public interface RedemActiveOrderDao {

    /**
     * 查询积分兑换订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listDeclareOrder(@Param("bean") RedemActiveOrderBean redemOrderBean,
                                                @Param("loginAgent") AgentInfo loginAgent, Page<RedemActiveOrderBean> page);
    /**
     * 汇总积分兑换订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     */
    RedemActiveOrderBean countDeclareOrder(@Param("bean") RedemActiveOrderBean redemOrderBean,
                                           @Param("loginAgent") AgentInfo loginAgent);
    /**
     * 查询分润订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listShareOrder(@Param("bean") RedemActiveOrderBean redemOrderBean,
                                        @Param("loginAgent") AgentInfo loginAgent, Page<RedemActiveOrderBean> page);

    /**
     * 获取机构列表
     */
    List<Map<String,Object>> listOrgCode();

    /**
     * 查询商户收款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listReceiveOrder(@Param("bean") RedemActiveOrderBean bean,
                                                @Param("loginAgent") AgentInfo loginAgent,
                                                Page<RedemActiveOrderBean> page);

    /**
     * 汇总商户收款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     */
    RedemActiveOrderBean countReceiveOrder(@Param("bean") RedemActiveOrderBean bean,
                                           @Param("loginAgent") AgentInfo loginAgent);
    /**
     * 查询信用卡还款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listRepayOrder(@Param("bean") RedemActiveOrderBean bean,
                                              @Param("loginAgent") AgentInfo loginAgent, Page<RedemActiveOrderBean> page);
    /**
     * 汇总信用卡还款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     */
    RedemActiveOrderBean countRepayOrder(@Param("bean") RedemActiveOrderBean bean,
                                         @Param("loginAgent") AgentInfo loginAgent);
}
