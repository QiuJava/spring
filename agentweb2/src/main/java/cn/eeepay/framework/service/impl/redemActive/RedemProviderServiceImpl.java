package cn.eeepay.framework.service.impl.redemActive;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.RedemProviderDao;
import cn.eeepay.framework.daoRedem.redemActive.RedemAgentShareDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemProviderBean;
import cn.eeepay.framework.service.redemActive.RedemProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 666666 on 2017/10/27.
 */
@Service
public class RedemProviderServiceImpl implements RedemProviderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedemProviderServiceImpl.class);
    @Resource
    private RedemProviderDao redemProviderDao;
    @Resource
    private RedemAgentShareDao redemAgentShareDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<RedemProviderBean> listProvider(RedemProviderBean providerBean, Page<AgentInfo> page, AgentInfo loginAgent) {
        return redemProviderDao.listProvider(providerBean, page, loginAgent);
    }

    @Override
    public boolean openRedemActive(List<String> agentNoList, AgentInfo loginAgent) {
        if (CollectionUtils.isEmpty(agentNoList)){
            LOGGER.error("请选择需要开通的服务商");
            throw new AgentWebException("请选择需要开通的服务商.");
        }

        RedemProviderBean loginProviderBean = redemProviderDao.queryRedemActiveCost(loginAgent.getAgentNo());
        if (loginProviderBean == null){
            LOGGER.error("登陆代理商没有开通此功能");
            throw new AgentWebException("登陆代理商没有开通此功能.");
        }

        int count = redemProviderDao.chechAgentNoIsDirectChildren(agentNoList, loginAgent);
        if (count != agentNoList.size()){
            LOGGER.error("选中服务商已经开通此功能或不是登陆代理商的直属下级");
            throw new AgentWebException("选中服务商已经开通此功能或不是登陆代理商的直属下级.");
        }

        List<RedemProviderBean> wantAddAgent = new ArrayList<>();
        for (String agentNo : agentNoList){
            wantAddAgent.add(new RedemProviderBean(agentNo, loginProviderBean.getShareRate(), loginProviderBean.getOemFee()));
        }
        redemProviderDao.openRedemActive(wantAddAgent);
        redemAgentShareDao.openRedemActive(wantAddAgent);
        return true;
    }

    @Override
    public boolean updateServiceCost(RedemProviderBean bean, AgentInfo loginAgent) {
        RedemProviderBean loginProviderBean = redemProviderDao.queryRedemActiveCost(loginAgent.getAgentNo());
        if (loginProviderBean == null){
            LOGGER.error("登陆代理商没有开通此功能");
            throw new AgentWebException("登陆代理商没有开通此功能.");
        }
        if (bean.getShareRate().compareTo(loginProviderBean.getShareRate()) > 0){
            String msg = String.format("修改后的入账比例%s %%不得大于当前登录代理商的入账比例%s %%", bean.getShareRate(),
                    loginProviderBean.getShareRate());
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }
        if (bean.getOemFee().compareTo(loginProviderBean.getOemFee()) > 0){
            String msg = String.format("修改后的积分兑换成本%s 不得大于当前登录代理商的积分兑换成本%s ", bean.getOemFee(),
                    loginProviderBean.getOemFee());
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(bean.getAgentNo());
        if (agentInfo == null){
            LOGGER.error("待修改代理商不存在");
            throw new AgentWebException("待修改代理商不存在");
        }

        RedemProviderBean directChildrenRedemActiveCost = redemProviderDao.queryChildrenMaxRedemActiveCost(agentInfo.getAgentNode());
        if (directChildrenRedemActiveCost != null){
            if (directChildrenRedemActiveCost.getShareRate() != null &&
                    bean.getShareRate().compareTo(directChildrenRedemActiveCost.getShareRate()) < 0){
                String msg = String.format("修改后的入账比例%s %%不得小于下级代理商的最大的入账比例%s %% ", bean.getShareRate(),
                        directChildrenRedemActiveCost.getShareRate());
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }
            if (directChildrenRedemActiveCost.getOemFee() != null &&
                    bean.getOemFee().compareTo(directChildrenRedemActiveCost.getOemFee()) < 0){
                String msg = String.format("修改后的积分兑换成本%s 不得小于下级代理商的最大积分兑换成本%s ", bean.getOemFee(),
                        directChildrenRedemActiveCost.getOemFee());
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }
        }
        redemAgentShareDao.updateServiceCost(bean);
        redemProviderDao.updateServiceCost(bean);
        return true;
    }
}
