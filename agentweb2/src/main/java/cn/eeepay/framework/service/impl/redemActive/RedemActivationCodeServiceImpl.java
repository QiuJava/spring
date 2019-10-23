package cn.eeepay.framework.service.impl.redemActive;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.daoRedem.redemActive.RedemActivationCodeDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActivationCodeBean;
import cn.eeepay.framework.service.redemActive.RedemActivationCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 666666 on 2017/10/26.
 */
@Service
public class RedemActivationCodeServiceImpl implements RedemActivationCodeService {

    @Resource
    private RedemActivationCodeDao redemActivationCodeDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<RedemActivationCodeBean> listActivationCode(RedemActivationCodeBean bean, AgentInfo loginAgentInfo, Page<RedemActivationCodeBean> page) {
        bean.setAgentNode(bean.getAgentNo());
        if (StringUtils.isNotBlank(bean.getAgentNo())){
            AgentInfo searchAgent = agentInfoDao.selectByAgentNo(bean.getAgentNo());
            if (searchAgent != null){
                bean.setAgentNode(searchAgent.getAgentNode());
            }
        }
        return redemActivationCodeDao.listActivationCode(bean, loginAgentInfo, page);
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

        if (redemActivationCodeDao.countAgentShareConfig(agentNo) == 0){
            throw new AgentWebException("该服务商未开通积分兑换激活版功能,不能下发激活码.");
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
//        int count = redemActivationCodeDao.countActivationCodeInStorage(startId, endId, loginAgentInfo);
//        if (endId - startId + 1 != count){
//            throw new AgentWebException("输入的编号存在不能下发的激活码");
//        }
        return redemActivationCodeDao.allotActivationCode2Agent(startId, endId, agentInfo, loginAgentInfo);
    }

    @Override
    public long recoveryActivation(long startId, long endId, AgentInfo loginAgentInfo) {
//        int count = redemActivationCodeDao.countActivationCodeByRecovery(startId, endId, loginAgentInfo);
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
        return redemActivationCodeDao.recoveryActivationCode(startId, endId, loginAgentInfo);
    }
}
