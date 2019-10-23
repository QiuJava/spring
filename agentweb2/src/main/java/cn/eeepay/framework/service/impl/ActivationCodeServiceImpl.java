package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ActivationCodeDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.ProviderDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.service.ActivationCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 666666 on 2017/10/26.
 */
@Service
public class ActivationCodeServiceImpl implements ActivationCodeService {

    @Resource
    private ActivationCodeDao activationCodeDao;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private ProviderDao providerDao;

    @Override
    public List<ActivationCodeBean> listActivationCode(ActivationCodeBean bean, AgentInfo loginAgentInfo, Page<ActivationCodeBean> page) {
        bean.setAgentNode(bean.getAgentNo());
        if (StringUtils.isNotBlank(bean.getAgentNo())){
            AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getAgentNo());
            if (searchAgent != null){
                bean.setAgentNode(searchAgent.getAgentNode());
            }
        }
        return activationCodeDao.listActivationCode(bean, loginAgentInfo, page);
    }

    @Override
    public long divideActivationCode(long startId, long endId, String agentNo, AgentInfo loginAgentInfo) {
        if (StringUtils.isBlank(agentNo)){
            throw new AgentWebException("未选择服务商.");
        }
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
        if (agentInfo == null || !StringUtils.equals(agentInfo.getParentId(), loginAgentInfo.getAgentNo())){
            throw new AgentWebException("只能分配给直属服务商.");
        }
        ProviderBean providerBean = providerDao.queryRepayServiceCost(agentNo);
        if (providerBean == null){
            throw new AgentWebException("该服务商未开通超级还款功能,不能下发激活码.");
        }

        if ((startId + "").length() != 12){
            throw new AgentWebException("开始编号格式不正确");
        }
        if ((endId + "").length() != 12){
            throw new AgentWebException("开始编号格式不正确");
        }
        if (endId < startId){
            throw new AgentWebException("结束编号不能小于开始编号");
        }
//        int count = activationCodeDao.countActivationCodeInStorage(startId, endId, loginAgentInfo);
//        if (endId - startId + 1 != count){
//            throw new AgentWebException("输入的编号存在不能下发的激活码");
//        }

        return activationCodeDao.allotActivationCode2Agent(startId, endId, agentInfo, loginAgentInfo);
    }

    @Override
    public long recoveryActivation(long startId, long endId, AgentInfo loginAgentInfo) {
//        int count = activationCodeDao.countActivationCodeByRecovery(startId, endId, loginAgentInfo);
//        if (endId - startId + 1 != count){
//            throw new AgentWebException("输入的编号存在不能回收的激活码");
//        }
        if ((startId + "").length() != 12){
            throw new AgentWebException("开始编号格式不正确");
        }
        if ((endId + "").length() != 12){
            throw new AgentWebException("开始编号格式不正确");
        }
        if (endId < startId){
            throw new AgentWebException("结束编号不能小于开始编号");
        }
        return activationCodeDao.recoveryActivationCode(startId, endId, loginAgentInfo);
    }
}
