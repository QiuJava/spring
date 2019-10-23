package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentOperLogDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentOperLogBean;
import cn.eeepay.framework.service.AgentOperLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by 666666 on 2017/9/8.
 */
@Service
public class AgentOperLogServiceImpl implements AgentOperLogService {

    @Resource
    private AgentOperLogDao agentOperLogDao;

    @Override
    public int insertLog(AgentOperLogBean bean) {
        return agentOperLogDao.insertLog(bean);
    }

    @Override
    public AgentOperLogBean getAgentOperLogById(int id) {
        return agentOperLogDao.getAgentOperLogById(id);
    }

    @Override
    public List<AgentOperLogBean> listAgentOperLog(AgentOperLogBean bean, Page page) {
        return agentOperLogDao.agentOperLogDao(bean, page);
    }
}
