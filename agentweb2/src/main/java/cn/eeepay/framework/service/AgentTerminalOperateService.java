package cn.eeepay.framework.service;

import java.util.Date;

public interface AgentTerminalOperateService {
    /**
     *添加机具操作时间
     * @param agent_no,sn,oper_detail_type,oper_type,date
     * @return
     */
    void insertAgentTerminalOperation(String agent_no, String sn, String oper_detail_type, String oper_type, Date date);


}
