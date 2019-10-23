package cn.eeepay.framework.daoRedem;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RedemMerchantBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.redemActive.MerInfoTotal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 积分兑换(非激活版)
 * Created by 666666 on 2018/5/9.
 */
public interface RedemMerchantDao {

    /**
     * 用户管理查询
     * @param page
     * @return
     */
    List<RedemMerchantBean> selectByUserRedemMerchant(@Param("bean") RedemMerchantBean userRedemMerchant,
                                                      @Param("loginAgent") AgentInfo loginAgent, Page<ResponseBean> page);

    /**
     * 用户管理统计
     * @param page
     * @return
     */
    MerInfoTotal selectSum(@Param("bean") RedemMerchantBean userRedemMerchant,
                                 @Param("loginAgent") AgentInfo loginAgent, Page<ResponseBean> page);
    /**
     * 获取代理商的商户母码
     * @param loginAgentNo 登陆代理商编号
     * @return
     */
    String selectRootMerchantNo(@Param("loginAgentNo") String loginAgentNo);

    /**
     * 获取商户信息
     * @param merchantNo
     * @param rootMerchantNo
     */
    RedemMerchantBean queryMerchantInfoByMerchantNoAndRootMerNo(@Param("merchantNo") String merchantNo,
                                                                @Param("rootMerchantNo") String rootMerchantNo);

    /**
     * 新增代理商和积分兑换商户关系,以及分润成本比例
     * @param agentNo           代理商编号
     * @param merchantNo        商户编号
     * @param merCapa           分润级别
     * @param share             分润成本
     */
    void insertRdmpAgentShareConfig(@Param("agentNo") String agentNo,
                                    @Param("merchantNo") String merchantNo,
                                    @Param("merCapa") String merCapa,
                                    @Param("share") String share);

    /**
     * 新增分润成本
     * @param agentNo           代理商编号
     * @param merchantNo        商户编号
     */
    void insertRdmpAgentFeeConfig(@Param("agentNo") String agentNo,
                                    @Param("merchantNo") String merchantNo,
                                    @Param("shareFee") String share_fee);

    Map<String,Object> getRdmpAgentShareConfig(@Param("agentNo") String agentNo,
                                    @Param("merchantNo") String merchantNo,
                                    @Param("merCapa") String merCapa,
                                    @Param("share") String share);
    /**
     * 修改代理商和积分兑换商户关系,以及分润成本比例
     * @param agentNo           代理商编号
     * @param merchantNo        商户编号
     * @param merCapa           分润级别
     * @param share             分润成本
     */
    void updateRdmpAgentShareConfig(@Param("agentNo") String agentNo,
                                    @Param("merchantNo") String merchantNo,
                                    @Param("merCapa") String merCapa,
                                    @Param("share") String share);

    Map<String,Object> getRdmpAgentFeeConfig(@Param("agentNo") String agentNo,
                                  @Param("merchantNo") String merchantNo,
                                  @Param("shareFee") String share_fee);
    /**
     * 修改分润成本
     * @param agentNo           代理商编号
     * @param merchantNo        商户编号
     */
    void updateRdmpAgentFeeConfig(@Param("agentNo") String agentNo,
                                  @Param("merchantNo") String merchantNo,
                                  @Param("shareFee") String share_fee);

    /**
     * 更新商户代理商节点信息
     * @param merchantNode    商户编号
     * @param agentNo       代理商编号
     * @param agentNode     代理商节点
     */
    void updateMerchantAgentNode(@Param("merchantNode") String merchantNode,
                                 @Param("agentNo") String agentNo,
                                 @Param("agentNode") String agentNode);

    /**
     * 获取商户的分润成本
     * @param agentNo       代理商编号
     * @param merchantNo    商户编号
     * @param merCapa       商户级别
     */
    String selectMerchantShare(@Param("agentNo") String agentNo,
                               @Param("merchantNo") String merchantNo,
                               @Param("merCapa") String merCapa,
                               @Param("shareType") String shareType);


    /**
     * 获取商户
     * @param merchantNo       商户编号
     */
    Map<String,Object> queryMerchantInfoByMerchantNo(@Param("merchantNo") String merchantNo);

    /**
     * 查询用户数量
     * @param merchantNo 商户编号
     */
    int countDirectUserNumber(@Param("merchantNo")String merchantNo);

    /**
     * 查询会员数量
     * @param merchantNo 商户编号
     */
    int countDirectMemberNumber(@Param("merchantNo")String merchantNo);

    /**
     * 查询成为会员时间
     * @param merchantNo 商户编号
     * @param merCapa    商户类型
     */
    String selectMemberTime(@Param("merchantNo")String merchantNo,@Param("merCapa") String merCapa);

    /**
     * 获取帐号明细
     */
    List<Map<String,Object>> listBalanceHis(@Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("service") String service,
                                            @Param("merchantNo") String merchantNo,
                                            Page<List<Map<String, Object>>> page);


    /**
     * 查询成为会员时间
     * @param oemNo 超级兑组织编号
     */
    Map<String,Object> getOemConfigValue(@Param("oemNo")String oemNo);
}
