package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.AgentTransCollect;
import cn.eeepay.framework.util.WriteReadDataSource;

@WriteReadDataSource
public interface AgentTransCollectDao {

    int deleteByPrimaryKey(Long id);

    int insert(AgentTransCollect record);

    AgentTransCollect selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AgentTransCollect record);

    int updateByPrimaryKey(AgentTransCollect record);
}