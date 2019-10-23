package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SuperPush;
import cn.eeepay.framework.model.SuperPushShare;

public interface SuperPushService {
    /**
     * 分页查询微创业商户信息
     * @param superPush 查询条件
     * @param loginAgentNo 登陆代理商
     *@param page 分页信息  @return 查询结果
     */
    List<SuperPush> listSuperPushMerchant(SuperPush superPush, String loginAgentNo, Page<SuperPush> page);

    /**
     * 根据商户编号获取微创业详情
     * @param merchantNo 商户编号
     * @return 商户详情
     */
    Map<String,Object> getSuperPushMerchantDetail(String merchantNo);

    /**
     * 微创业分润明细
     * @param superPushShare 查询结果
     * @param loginAgentNo 登陆代理商
     * @param page  分页信息
     * @return 查询结果
     */
    List<SuperPushShare> listSuperPushShare(SuperPushShare superPushShare, String loginAgentNo, Page<SuperPushShare> page);

    /**
     * 微创业分润明细
     * @param superPushShare 查询结果
     * @param loginAgentNo 登陆代理商
     * @return 查询结果
     */
    Map<String,Object> countSuperPushShare(SuperPushShare superPushShare, String loginAgentNo);

    /**
     * 导出微创业分润明细
     * @param superPushShare 查询结果
     * @param loginAgentNo 登陆代理商
     * @return 查询结果
     */
    List<SuperPushShare> exportSuperPushShare(SuperPushShare superPushShare, String loginAgentNo);

    /**
     * 获取登陆代理商今日微创业收益
     * @param loginAgentNo 登陆代理商
     * @return
     */
    String getTodaySuperPushAmount(String loginAgentNo);
//
//	/**
//	 * 超级推活动查询
//	 * @param page
//	 * @param phone
//	 * @param superPush
//	 * @return
//	 */
//	List<SuperPush> selectSuperPush(Page<SuperPush> page,String phone,SuperPush superPush);
//
//	/**
//	 * 商户详情
//	 * @param mertId
//	 * @return
//	 */
//	Map<String, Object> selectMerchantDetail(String mertId);
//
//	/**
//	 * 提现详情
//	 * @param mertId
//	 * @return
//	 */
//	Map<String, Object> selectCashDetail(String mertId);
//
//	/**
//	 * 分润详情
//	 * @param page
//	 * @param info
//	 * @return
//	 */
//	List<SuperPushShare> selectShareDetail(Page<SuperPush> page,SuperPushShare info);
//
//	/**
//	 * 导出分润详情
//	 * @param info
//	 * @return
//	 */
//	List<SuperPushShare> exportTransInfo(SuperPushShare info);
//
//	SuperPush getCashMerchantDetail(String merchantNo);
//
//	BigDecimal getTotalAmount(String merchantNo);
//
//	BigDecimal getSuperPushUserBalance(String merchantNo);
//
//	List<SettleOrderInfo> getCashPage(SettleOrderInfo settleOrderInfo, Page<SuperPush> page);
//
}
