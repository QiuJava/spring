package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InvitePrizesMerchantBean;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/19.
 */
@ReadOnlyDataSource
public interface InvitePrizesMerchantDao {

    /**
     * 分页获取邀请有奖的且审核通过的商户信息列表
     * @param bean 查询条件
     * @param loginAgentNode
     * @param page 分页信息
     * @return 查询列表
     */
    List<InvitePrizesMerchantBean> listInvitePrizesMerchant(@Param("bean") InvitePrizesMerchantBean bean,
                                                            @Param("loginAgentNode") String loginAgentNode,
                                                            Page<InvitePrizesMerchantBean> page);


    /**
     * 汇总获取邀请有奖的且审核通过的商户信息
     * @param bean 查询条件
     * @param loginAgentNode
     * @return 汇总信息
     */
    Map<String,String> countInvitePrizesMerchant(@Param("bean") InvitePrizesMerchantBean bean,
                                                 @Param("loginAgentNode") String loginAgentNode);
}
