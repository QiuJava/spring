package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditRepayOrderDetail;

import java.util.List;

/**
 * 还款订单处理流水
 * @author liuks
 */
public interface CreditRepayOrderDetailService {
    /**
     * 通告批次号查询订单子详情
     */
    List<CreditRepayOrderDetail> selectDetailList(String batchNo);

    /**
     * 分页查询还款计划流水详情
     * @param orderDetail
     * @param loginAgent
     *@param page  @return
     */
    List<CreditRepayOrderDetail> selectDetailAllList(CreditRepayOrderDetail orderDetail, AgentInfo loginAgent, Page<CreditRepayOrderDetail> page);

    /**
     *动态条件查询导出数据
     */
    List<CreditRepayOrderDetail> exportDetailAllList(CreditRepayOrderDetail orderDetail, AgentInfo loginAgent);
}
