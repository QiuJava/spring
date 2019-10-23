package cn.eeepay.framework.dao;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@WriteReadDataSource
public interface AgentTerminalOperateDao {
    //String agent_no, String sn, String oper_detail_type, String oper_type, Date date
    @Insert(" replace  into agent_terminal_operate(agent_no,sn,oper_detail_type,oper_type,create_time)"
            + "values(#{agent_no},#{sn},#{oper_detail_type},#{oper_type},#{date})")
    int insertAgentTermimalOperate(@Param("agent_no") String agent_no,@Param("sn") String sn,@Param("oper_detail_type") String oper_detail_type,@Param("oper_type") String oper_type,@Param("date") Date date);
}
