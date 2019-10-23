package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.redemActive.MerInfoTotal;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/9.
 */
public interface RedemMerchantService {

    /**
     * 用户管理查询
     * @param userRedemMerchant
     * @param page
     * @return
     */
    List<RedemMerchantBean> selectByUserRedemMerchant(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent,Page<ResponseBean> page);

    /**
     * 用户统计
     * @param userRedemMerchant
     * @param loginAgent
     * @param page
     * @return
     */
    MerInfoTotal selectSum(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent, Page<ResponseBean> page);

    /**
     * 获取商户信息(为新增二级代理商做准备)
     * @param merchantNo 商户编号
     * @param loginAgent 登陆代理商
     */
    RedemMerchantBean queryAddMerchantInfo(String merchantNo, AgentInfo loginAgent);

    /**
     * 获取商户信息(为修改二级代理商做准备)
     * @param merchantNo 商户编号
     * @param loginAgent 登陆代理商
     */
    RedemMerchantBean queryUpdateMerchantInfo(String merchantNo, AgentInfo loginAgent);

    /**
     * 新增代理商
     * @param userRedemMerchant 用户信息
     * @param loginAgent        登陆代理商
     */
    boolean userAddAgent(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent);

    /**
     * 修改代理商
     * @param userRedemMerchant 用户信息
     * @param loginAgent        登陆代理商
     */
    boolean userUpdateAgent(RedemMerchantBean userRedemMerchant, AgentInfo loginAgent);

    /**
     * 获取用户的详情信息
     * @param merchantNo    用户编号
     * @param loginAgent    登陆代理商
     */
    Map<String, Object> queryMerchantDetails(String merchantNo, AgentInfo loginAgent);

    /**
     * 获取帐号明细
     */
    List<Map<String,Object>> listBalanceHis(String startTime, String endTime, String service, String merchantNo, Page<List<Map<String, Object>>> page);

}
