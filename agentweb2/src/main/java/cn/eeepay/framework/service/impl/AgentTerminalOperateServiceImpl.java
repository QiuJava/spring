package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentTerminalOperateDao;
import cn.eeepay.framework.service.AgentTerminalOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AgentTerminalOperateServiceImpl  implements AgentTerminalOperateService{
    private static final Logger log = LoggerFactory.getLogger(AgentTerminalOperateServiceImpl.class);

    @Resource
    private AgentTerminalOperateDao agentTerminalOperateDao;
    @Override
    public void insertAgentTerminalOperation(String agent_no, String sn, String oper_detail_type, String oper_type, Date date) {
        try {
            agentTerminalOperateDao.insertAgentTermimalOperate(agent_no,sn,oper_detail_type,oper_type,date);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("机具操作时间录入异常");
        }


    }
}
