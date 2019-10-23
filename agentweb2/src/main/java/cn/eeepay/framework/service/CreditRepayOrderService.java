package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditRepayOrder;

import java.util.List;

/**
 * 信用卡还款订单服务
 * @author liuks
 */
public interface CreditRepayOrderService {
    /**
     *动态分页查询
     */
    List<CreditRepayOrder> listCreditRepayOrder(CreditRepayOrder order, AgentInfo loginAgent, Page<CreditRepayOrder> page);
    /**
     *动态分页查询统计总金额
     */
    CreditRepayOrder countCreditRepayOrder(CreditRepayOrder order, AgentInfo loginAgent);
    /**
     *动态条件查询导出数据
     */
    List<CreditRepayOrder> exportSelectAllList(CreditRepayOrder order, AgentInfo loginAgent);

    /**
     *通告批次号查询
     */
    CreditRepayOrder selectById(String batchNo, AgentInfo loginAgent);

}
