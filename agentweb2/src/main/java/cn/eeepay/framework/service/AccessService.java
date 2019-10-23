package cn.eeepay.framework.service;

public interface AccessService {

    /**
     * 登陆代理商是否能操作该代理商
     * @param agentNo       被操作代理商
     * @param isOwn         true: 该代理商是自己, false 该代理商是自己或者自己下级
     * @return
     * isOwn = true: 被操作代理商必须是自己才能操作
     * isOwn = false: 被操作代理商只要是自己或者自己的下级才能被操作
     */
    boolean canAccessTheAgent(String agentNo, boolean isOwn);
    
    /**
     * 银行家剥离
     * 登陆代理商是否能操作该代理商
     * @param agentNo       被操作代理商
     * @param isOwn         true: 该代理商是自己, false 该代理商是自己或者自己下级
     * @return
     * isOwn = true: 被操作代理商必须是自己才能操作
     * isOwn = false: 被操作代理商只要是自己或者自己的下级才能被操作
     */
    boolean canAccessTheAgentApi(String entityId, String agentNo, boolean isOwn);

    /**
     * 登陆代理商是否能操作该交易订单
     * @param orderId       被操作交易订单
     * @param isOwn         true: 属于自己的订单,false 属于自己或者下级的订单
     * @return
     * isOwn = true: 被操作交易订单必须是自己才能操作
     * isOwn = false: 被操作交易订单只要是自己或者自己的下级才能被操作
     */
    boolean canAccessTheTransOrder(String orderId, boolean isOwn);

    /**
     * 登陆代理商是否能操作该商户
     * @param merchantNo       被操作商户编号
     * @param isOwn         true: 属于自己的商户,false 属于自己或者下级的商户
     * @return
     * isOwn = true: 被操作商户必须是自己才能操作
     * isOwn = false: 被操作商户只要是自己或者自己的下级才能被操作
     */
    boolean canAccessTheMerchant(String merchantNo, boolean isOwn);

    /**
     * 登陆代理商是否能操作该机具
     * @param terminalSn    被操作机具sn
     * @param isOwn         true: 属于自己的机具,false 属于自己或者下级的机具
     * @return
     * isOwn = true: 被操作机具必须是自己才能操作
     * isOwn = false: 被操作机具只要是自己或者自己的下级才能被操作
     */
    boolean canAccessTheTerminalBySn(String terminalSn, boolean isOwn);

    /**
     * 登陆代理商是否能操作该机具
     * @param terminalId    被操作机具id
     * @param isOwn         true: 属于自己的机具,false 属于自己或者下级的机具
     * @return
     * isOwn = true: 被操作机具必须是自己才能操作
     * isOwn = false: 被操作机具只要是自己或者自己的下级才能被操作
     */
    boolean canAccessTheTerminalById(long terminalId, boolean isOwn);

    /**
     * 登录代理商是否能查询该交易详情
     * @param orderNo 下级订单号 true:不包含  false：包含
     * @param isOwn 是否包含下级
     * @return
     */
    boolean canQueryTheCollectiveTransOrderInfo(String orderNo, boolean isOwn);

    /**
     * 能否查询交易分润明细列表
     * @param isOwn 是否可以查询下级 false:可以
     * @return
     */
    boolean canQueryProfitSettleDetailList(String agentNo,boolean isOwn);

    /**
     * 查看购买记录详情
     * @param isOwn isOwn 是否可以查询下级 false:可以 true:不可以
     * @return
     */
    boolean canQueryActOrderInfo(String orderNo, boolean isOwn);

}
