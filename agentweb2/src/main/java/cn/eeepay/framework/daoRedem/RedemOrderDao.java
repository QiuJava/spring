package cn.eeepay.framework.daoRedem;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RedemOrderBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 积分兑换(非激活版)
 * Created by 666666 on 2018/5/7.
 */
public interface RedemOrderDao {

    /**
     * 查询激活订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemOrderBean> listActivityOrder(@Param("bean") RedemOrderBean redemOrderBean,
                                           @Param("loginAgent") AgentInfo loginAgent, Page<RedemOrderBean> page);
    /**
     * 查询激活订单汇总
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     */
    Map<String,Object> summaryActivityOrder(@Param("bean") RedemOrderBean redemOrderBean,
                                            @Param("loginAgent") AgentInfo loginAgent);
    /**
     * 查询积分兑换订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemOrderBean> listDeclareOrder(@Param("bean") RedemOrderBean redemOrderBean,
                                          @Param("loginAgent") AgentInfo loginAgent, Page<RedemOrderBean> page);
    /**
     * 查询积分兑换订单汇总
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     */
    Map<String,Object> summaryDeclareOrder(@Param("bean") RedemOrderBean redemOrderBean,
                                           @Param("loginAgent") AgentInfo loginAgent);
    /**
     * 查询分润订单
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    List<RedemOrderBean> listShareOrder(@Param("bean") RedemOrderBean redemOrderBean,
                                        @Param("loginAgent") AgentInfo loginAgent, Page<RedemOrderBean> page);


    /**
     * 查询分润订单统计
     * @param redemOrderBean    查询条件
     * @param loginAgent        登陆代理商
     * @param page              分页
     */
    Map<String,Object> listShareOrderSum(@Param("bean") RedemOrderBean redemOrderBean,
                                        @Param("loginAgent") AgentInfo loginAgent, Page<RedemOrderBean> page);

    /**
     * 获取机构列表
     */
    List<Map<String,Object>> listOrgCode();


}
