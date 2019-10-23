package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InvitePrizesMerchantBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface InvitePrizesMerchantService {
    /**
     * 分页获取邀请有奖的且审核通过的商户信息列表
     * @param bean 查询条件
     * @param loginAgentNo  登陆代理商编号
     * @param page 分页信息
     * @return 查询列表
     */
    List<InvitePrizesMerchantBean> listInvitePrizesMerchant(InvitePrizesMerchantBean bean, String loginAgentNo, Page<InvitePrizesMerchantBean> page);

    /**
     * 汇总获取邀请有奖的且审核通过的商户信息
     * @param bean 查询条件
     * @param loginAgentNo  登陆代理商编号
     * @return 汇总信息
     */
    Map<String,String> countInvitePrizesMerchant(InvitePrizesMerchantBean bean, String loginAgentNo);
}
