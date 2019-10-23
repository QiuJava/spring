package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
@WriteReadDataSource
public interface ActivationCodeDao {

    @SelectProvider(type=SqlProvider.class, method = "listActivationCode")
    List<ActivationCodeBean> listActivationCode(@Param("bean") ActivationCodeBean bean,@Param("loginAgent") AgentInfo loginAgentInfo, Page<ActivationCodeBean> page);

    /**
     * 统计要下发的激活码
     * @param startId   开始编号
     * @param endId     结束编号
     * @param loginAgentInfo 登陆代理商
     * @return 统计个数
     */
    @Select("select count(*) from yfb_activation_code where id between #{startId} and #{endId} and status=1 and agent_node = #{loginAgent.agentNode}")
    int countActivationCodeInStorage(@Param("startId") long startId, @Param("endId") long endId, @Param("loginAgent") AgentInfo loginAgentInfo);

    /**
     * 统计要回收的激活码
     * @param startId   开始编号
     * @param endId     结束编号
     * @param loginAgentInfo    登陆代理商
     * @return  统计个数
     */
    @Select("select count(*) from yfb_activation_code yac " +
            "LEFT JOIN agent_info ai ON ai.agent_no = yac.agent_no " +
            "where yac.id between #{startId} and #{endId} " +
            "and yac.status=1 " +
            "and ai.parent_id = #{loginAgent.agentNo}")
    int countActivationCodeByRecovery(@Param("startId") long startId, @Param("endId")long endId, @Param("loginAgent")AgentInfo loginAgentInfo);

    @Update("update yfb_activation_code " +
            "set agent_no = #{agent.agentNo}, " +
            "agent_node=#{agent.agentNode} " +
            "where id between #{startId} and #{endId} " +
            "and status = 1 " +
            "and agent_node = #{loginAgent.agentNode}")
    @ResultType(Long.class)
    long allotActivationCode2Agent(@Param("startId") long startId,
                                   @Param("endId") long endId,
                                   @Param("agent") AgentInfo info,
                                   @Param("loginAgent") AgentInfo loginAgent);

    @Update("update yfb_activation_code yac " +
            "set yac.agent_no = #{loginAgent.agentNo}, " +
            "yac.agent_node=#{loginAgent.agentNode} " +
            "where yac.id between #{startId} and #{endId} " +
            "and yac.status = 1 " +
            "and EXISTS ( " +
            "   SELECT 1 FROM agent_info ai " +
            "   WHERE ai.agent_no = yac.agent_no " +
            "   AND ai.parent_id = #{loginAgent.agentNo}" +
            ")")
    @ResultType(Long.class)
    long recoveryActivationCode(@Param("startId") long startId,
                                      @Param("endId") long endId,
                                      @Param("loginAgent") AgentInfo loginAgentInfo);


    public static class SqlProvider{

        public String listActivationCode(Map<String, Object> param){
            final ActivationCodeBean bean = (ActivationCodeBean) param.get("bean");
            final AgentInfo loginAgent = (AgentInfo) param.get("loginAgent");
            SQL sql = new SQL(){{
                SELECT("yac.id, yac.uuid_code,yac.unified_merchant_no," +
//                        "yrmi.user_name unifiedMerchantName,\n" +
                        "yac.agent_no,ai.agent_name,ai.parent_id,\n" +
//                        "pai.agent_no one_agent_no,pai.agent_name one_agent_name,\n" +
                        "yac.status,yac.activate_time,\n" +
                        "yac.create_time,yac.update_time");
                FROM("yfb_activation_code yac");
                LEFT_OUTER_JOIN("agent_info ai ON ai.agent_no = yac.agent_no");
//                LEFT_OUTER_JOIN("agent_info pai ON pai.agent_no = ai.one_level_id");
//                LEFT_OUTER_JOIN("yfb_repay_merchant_info yrmi ON yrmi.merchant_no = yac.unified_merchant_no");
                WHERE("ai.agent_node like concat(#{loginAgent.agentNode},'%')");
                if (StringUtils.isNotBlank(bean.getAgentNode())
                        && !StringUtils.equals(bean.getAgentNode(), loginAgent.getAgentNode())){
                    WHERE("ai.agent_node like CONCAT(#{bean.agentNode}, '%')");
                }
                if (StringUtils.isNotBlank(bean.getStatus())){
                    WHERE("yac.status = #{bean.status}");
                }
                if (StringUtils.isNotBlank(bean.getUnifiedMerchantNo())){
                    WHERE("yac.unified_merchant_no = #{bean.unifiedMerchantNo}");
                }
                if (StringUtils.isNotBlank(bean.getMinId())){
                    WHERE("yac.id >= #{bean.minId}");
                }
                if (StringUtils.isNotBlank(bean.getMaxId())){
                    WHERE("yac.id <= #{bean.maxId}");
                }
                if (StringUtils.isNotBlank(bean.getStartActivateTime())){
                    WHERE("yac.activate_time >= #{bean.startActivateTime}");
                }
                if (StringUtils.isNotBlank(bean.getEndActivateTime())){
                    WHERE("yac.activate_time <= #{bean.endActivateTime}");
                }
//                ORDER_BY("yac.id");
            }};
            return sql.toString();
        }
    }
}
