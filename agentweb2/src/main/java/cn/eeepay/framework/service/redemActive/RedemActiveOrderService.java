package cn.eeepay.framework.service.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActiveOrderBean;

import java.util.List;
import java.util.Map;

/**
 * 积分兑换订单查询
 * Created by 666666 on 2018/5/7.
 */
public interface RedemActiveOrderService {
    /**
     * 查询积分兑换订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listDeclareOrder(RedemActiveOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page);
    /**
     * 汇总查询积分兑换订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     */
    RedemActiveOrderBean countDeclareOrder(RedemActiveOrderBean redemOrderBean, AgentInfo loginAgent);
    /**
     * 查询分润订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listShareOrder(RedemActiveOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page);


    /**
     * 获取机构列表
     */
    List<Map<String, Object>> listOrgCode();

    /**
     * 查询商户收款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listReceiveOrder(RedemActiveOrderBean bean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page);

    /**
     * 汇总商户收款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     */
    RedemActiveOrderBean countReceiveOrder(RedemActiveOrderBean bean, AgentInfo loginAgent);
    /**
     * 查询信用卡还款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemActiveOrderBean> listRepayOrder(RedemActiveOrderBean bean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page);
    /**
     * 查询信用卡还款订单
     * @param bean    查询条件
     * @param loginAgent        登陆代理商
     */
    RedemActiveOrderBean countRepayOrder(RedemActiveOrderBean bean, AgentInfo loginAgent);
}
