package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLogBean;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * Created by 666666 on 2017/9/8.
 */
@WriteReadDataSource
public interface AgentOperLogDao {

    /**
     * 插入日志
     * @param bean	日志
     * @return
     */
    int insertLog(@Param("bean") AgentOperLogBean bean);

    /**
     * 根据id查询操作日志
     * @param id 主键id
     * @return 日志
     */
    AgentOperLogBean getAgentOperLogById(@Param("id") int id);

    /**
     * 查询代理商操作日志
     * @param bean 查询条件
     * @param page 分页信息
     * @return	查询结果
     */
    List<AgentOperLogBean> agentOperLogDao(@Param("bean") AgentOperLogBean bean, Page page);
}
