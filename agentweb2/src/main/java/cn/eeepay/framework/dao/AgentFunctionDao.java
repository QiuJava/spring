package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentFunctionBean;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 666666 on 2018/3/7.
 */
@WriteReadDataSource
public interface AgentFunctionDao {
    /**
     * 列举代理商系统功能管控规则列表
     * @return
     * @param loginAgentNo
     */
    List<AgentFunctionBean> listAgentFunction(@Param("loginAgentNo") String loginAgentNo);

    /**
     * 根据系统功能编号查询系统功能管控规则
     * @param functionNumber 功能编号
     * @return
     */
    AgentFunctionBean queryFunctionById(@Param("functionNumber") String functionNumber);

    /**
     * 新增一条功能管控记录
     */
    void insertAgentFunctionConfig(@Param("agentNo") String agentNo, @Param("oneAgentNo") String oneAgentNo, @Param("functionNumber") String functionNumber);

    /**
     * 删除一条功能管控记录
     */
    void deleteAgentFunction(@Param("agentList") List<String> agentList,@Param("functionNumber") String functionNumber);

    /**
     * 查询功能管控配置
     */
    boolean countAgentFunction(@Param("agentNo") String agentNo, @Param("functionNumber") String functionNumber);

    List<AgentInfo> listAgentFunctionConfig(@Param("oneAgentNo") String oneAgentNo, @Param("functionNumber") String functionNumber,
                                            @Param("agentInfo") AgentInfo agentInfo, Page<AgentInfo> page);

    AgentInfo findAgentInfoByAgentNo(@Param("oneAgentNo") String oneAgentNo, @Param("agentNo") String agentNo);

	List<AgentFunctionBean> selectAgentFunction(String loginAgentNo);

	boolean countAgentFunctionManage(@Param("agentNo")String agentNo, @Param("functionNumber")String functionNumber);
}
