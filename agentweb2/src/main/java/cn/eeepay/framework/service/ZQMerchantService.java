package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ZQMerchantInfo;
import cn.eeepay.framework.model.ZQTransOrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface ZQMerchantService {
    /**
     * 查询直清商户
     * @param zqMerchantInfo 查询条件
     * @param agentNo 当前代理商的编号
     *@param page  分页信息  @return 查询结果
     */
    List<ZQMerchantInfo> queryZQMerchantInfo(ZQMerchantInfo zqMerchantInfo, String agentNo, Page<ZQMerchantInfo> page);

    /**
     * 查询直清商户交易信息
     * @param zqTransOrderInfo 查询条件
     * @param agentNo 当前代理商的编号
     *@param page  分页信息  @return 查询结果
     */
    List<ZQTransOrderInfo> queryZQTransOrder(ZQTransOrderInfo zqTransOrderInfo, String agentNo, Page<ZQTransOrderInfo> page);

    /**
     * 导出直清商户商户信息
     * @param zqMerchantInfo 查询条件
     * @param agentNo 当前代理商的编号
     */
    List<ZQMerchantInfo> exportZQMerchantInfo(ZQMerchantInfo zqMerchantInfo, String agentNo);

    /**
     * 导出直清商户交易信息
     * @param zqTransOrderInfo 查询条件
     * @param agentNo 当前代理商的编号
     */
    List<ZQTransOrderInfo> exportZQTransOrder(ZQTransOrderInfo zqTransOrderInfo, String agentNo);
}
