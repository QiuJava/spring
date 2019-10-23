package cn.eeepay.framework.service.impl.redemActive;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.daoRedem.redemActive.RedemActiveOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActiveOrderBean;
import cn.eeepay.framework.service.redemActive.RedemActiveOrderService;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/7.
 */
@Service
public class RedemActiveOrderServiceImpl implements RedemActiveOrderService {
    @Resource
    private RedemActiveOrderDao redemActiveOrderDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<RedemActiveOrderBean> listDeclareOrder(RedemActiveOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page) {
        List<RedemActiveOrderBean> redemOrderBeans = redemActiveOrderDao.listDeclareOrder(redemOrderBean, loginAgent, page);
//        if (redemOrderBeans != null && !redemOrderBeans.isEmpty()){
//            for (RedemActiveOrderBean bean : redemOrderBeans){
//                bean.setMobile(StringUtil.filterNull(bean.getMobile()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//            }
//        }
        return redemOrderBeans;
    }

    @Override
    public RedemActiveOrderBean countDeclareOrder(RedemActiveOrderBean redemOrderBean, AgentInfo loginAgent) {
        return redemActiveOrderDao.countDeclareOrder(redemOrderBean, loginAgent);
    }

    @Override
    public List<RedemActiveOrderBean> listShareOrder(RedemActiveOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page) {
        AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(redemOrderBean.getAgentNo());
        if (searchAgentInfo != null && !StringUtils.equals(searchAgentInfo.getAgentNode(), loginAgent.getAgentNode())){
            redemOrderBean.setAgentNode(searchAgentInfo.getAgentNode());
        }
        List<RedemActiveOrderBean> redemOrderBeans = redemActiveOrderDao.listShareOrder(redemOrderBean, loginAgent, page);
//        if (redemOrderBeans != null && !redemOrderBeans.isEmpty()){
//            for (RedemActiveOrderBean bean : redemOrderBeans){
//                bean.setMobile(StringUtil.filterNull(bean.getMobile()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//            }
//        }
        return redemOrderBeans;
    }

    @Override
    public List<Map<String, Object>> listOrgCode() {
        return redemActiveOrderDao.listOrgCode();
    }

    @Override
    public List<RedemActiveOrderBean> listReceiveOrder(RedemActiveOrderBean bean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page) {
        AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(bean.getAgentNo());
        if (searchAgentInfo != null && !StringUtils.equals(searchAgentInfo.getAgentNode(), loginAgent.getAgentNode())){
            bean.setAgentNode(searchAgentInfo.getAgentNode());
        }
        return redemActiveOrderDao.listReceiveOrder(bean, loginAgent, page);
    }

    @Override
    public RedemActiveOrderBean countReceiveOrder(RedemActiveOrderBean bean, AgentInfo loginAgent) {
        AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(bean.getAgentNo());
        if (searchAgentInfo != null && !StringUtils.equals(searchAgentInfo.getAgentNode(), loginAgent.getAgentNode())){
            bean.setAgentNode(searchAgentInfo.getAgentNode());
        }
        return redemActiveOrderDao.countReceiveOrder(bean, loginAgent);
    }

    @Override
    public List<RedemActiveOrderBean> listRepayOrder(RedemActiveOrderBean bean, AgentInfo loginAgent, Page<RedemActiveOrderBean> page) {
        AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(bean.getAgentNo());
        if (searchAgentInfo != null && !StringUtils.equals(searchAgentInfo.getAgentNode(), loginAgent.getAgentNode())){
            bean.setAgentNode(searchAgentInfo.getAgentNode());
        }
        return redemActiveOrderDao.listRepayOrder(bean, loginAgent, page);
    }

    @Override
    public RedemActiveOrderBean countRepayOrder(RedemActiveOrderBean bean, AgentInfo loginAgent) {
        AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(bean.getAgentNo());
        if (searchAgentInfo != null && !StringUtils.equals(searchAgentInfo.getAgentNode(), loginAgent.getAgentNode())){
            bean.setAgentNode(searchAgentInfo.getAgentNode());
        }
        return redemActiveOrderDao.countRepayOrder(bean, loginAgent);
    }
}
