package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;
import cn.eeepay.framework.model.ProfitUpdateRecord;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@WriteReadDataSource
public interface AgentShareDao {

    
    //添加分润task
//    @Insert("")
//    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="agentid", before=false, resultType=Integer.class)
    int insertAgentShareListTask(@Param("agent")AgentShareRuleTask shareList);

    /**
     * 插入修改分润记录
     * @return
     */
    Integer insertShareUpdateRecord(@Param("record") ProfitUpdateRecord record);

    AgentShareRule selectByShareId(@Param("shareId") Long shareId);

    @Select("select * from agent_share_rule_task where id = #{id}")
    AgentShareRuleTask queryAgentShareListTask(Integer id);
    //删除分润
    @Delete("delete from agent_share_rule_task where id=#{id} and efficient_date>now()")
    int deleteAgentShareTask(Integer id);
    
    //查询分润task
    @Select("select  si.service_type,asrt.*  from agent_share_rule_task asrt " +
            "LEFT JOIN agent_share_rule asr ON asr.id = asrt.share_id " +
            "LEFT JOIN service_info si ON si.service_id = asr.service_id " +
            "where share_id=#{shareId}")
    @ResultType(AgentShareRuleTask.class)
    List<AgentShareRuleTask> getAgentShareRuleTask(Integer shareId);


    //task running任务处理
    
    @Select("select * from agent_share_rule where agent_no=#{agentNo} and service_id=#{serviceId}")
    @ResultType(AgentShareRule.class)
    AgentShareRule queryByAgentNoAndServiceId(String agentNo,String serviceId);

    /**
     * 通过shareId查找组员的业务产品id
     * @param shareId shareId
     * @return
     */
    List<Long> queryMemberBpId(@Param("shareId")Long shareId);

    /**
     * 根据队员的业务id和队长的共享Id获取到队员的共享id
     * @param memberBpId        队员的业务id
     * @param leaderShareId     队长的共享Id
     */
    Long getMemberShareId(@Param("memberBpId") Long memberBpId, @Param("leaderShareId") Long leaderShareId);

    /**
     * 根据队长的主键id,获取队员的分润任务
     * @param memberShareId  队员的共享id
     * @param leaderId 队长的任务id
     * @return
     */
    Integer getMemberAgentShareTaskId(@Param("memberShareId") Long memberShareId, @Param("leaderId") Integer leaderId);

    /**
     * 查询是否有相同生效日期的任务记录
     * @param shareId       共享id
     * @param efficientDate 生效时间
     * @return 总数
     */
    int queryAgentShareListTaskByEfficientDate(@Param("shareId") Long shareId, @Param("efficientDate")Date efficientDate);

    /**
     * 获取同样类型的父级代理商的分润规则
     * @param shareId 下级的代理商的分润规则的主表id
     * @return 上级代理商同样类型的分润规则
     */
    AgentShareRule getSameTypeParentAgentShare(@Param("shareId") Long shareId);


    String findAgentNo(@Param("shareId") Long shareId);

    /**
     * 查询代理商分润列表
     * @param agentNo
     * @return
     */
    @Select("select * from agent_share_rule ar " +
            "left join service_info sr on sr.service_id=ar.service_id " +
            "where agent_no=#{agentNo} " +
            "and sr.effective_status = 1 ")
    @ResultType(AgentShareRule.class)
    List<AgentShareRule> getAgentShareList(@Param("agentNo")String agentNo);
}