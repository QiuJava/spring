package cn.eeepay.framework.daoRedem.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActivationCodeBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface RedemActivationCodeDao {

    @SelectProvider(type=SqlProvider.class, method = "listActivationCode")
    List<RedemActivationCodeBean> listActivationCode(@Param("bean") RedemActivationCodeBean bean, @Param("loginAgent") AgentInfo loginAgentInfo, Page<RedemActivationCodeBean> page);

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
            "where yac.id between #{startId} and #{endId} " +
            "and yac.status=1 " +
            "and yac.parent_id = #{loginAgent.agentNo}")
    int countActivationCodeByRecovery(@Param("startId") long startId, @Param("endId") long endId, @Param("loginAgent") AgentInfo loginAgentInfo);

    @Update("update yfb_activation_code " +
            "set agent_no = #{agent.agentNo}, " +
            "agent_node=#{agent.agentNode}," +
            "parent_id = #{loginAgent.agentNo} " +
            "where id between #{startId} and #{endId} " +
            "and status = 1 " +
            "and agent_node = #{loginAgent.agentNode}")
    @ResultType(Long.class)
    long allotActivationCode2Agent(@Param("startId") long startId,
                                   @Param("endId") long endId,
                                   @Param("agent") AgentInfo info,
                                   @Param("loginAgent") AgentInfo loginAgent);

    @Update("update yfb_activation_code " +
            "set agent_no = #{loginAgent.agentNo}, " +
            "agent_node=#{loginAgent.agentNode}, " +
            "parent_id = #{loginAgent.parentId} " +
            "where id between #{startId} and #{endId} " +
            "and status = 1 " +
            "and parent_id = #{loginAgent.agentNo}")
    @ResultType(Long.class)
    long recoveryActivationCode(@Param("startId") long startId,
                                @Param("endId") long endId,
                                @Param("loginAgent") AgentInfo loginAgentInfo);

    @Select("SELECT COUNT(1) FROM yfb_agent_share_config WHERE agent_no = #{agentNo}")
    int countAgentShareConfig(@Param("agentNo") String agentNo);


    public static class SqlProvider{

        public String listActivationCode(Map<String, Object> param){
            final RedemActivationCodeBean bean = (RedemActivationCodeBean) param.get("bean");
            final AgentInfo loginAgent = (AgentInfo) param.get("loginAgent");
            SQL sql = new SQL(){{
                SELECT("yac.id, yac.uuid_code,yac.mer_no," +
                        "yac.agent_no,yac.parent_id,\n" +
                        "yac.status,yac.activate_time,\n" +
                        "yac.create_time,yac.update_time");
                FROM("yfb_activation_code yac");
                WHERE("yac.agent_node like concat(#{loginAgent.agentNode},'%')");
                if (StringUtils.isNotBlank(bean.getAgentNode())
                        && !StringUtils.equals(bean.getAgentNode(), loginAgent.getAgentNode())){
                    WHERE("yac.agent_node like CONCAT(#{bean.agentNode}, '%')");
                }
                if (StringUtils.isNotBlank(bean.getStatus())){
                    WHERE("yac.status = #{bean.status}");
                }
                if (StringUtils.isNotBlank(bean.getMerNo())){
                    WHERE("yac.mer_no = #{bean.merNo}");
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
