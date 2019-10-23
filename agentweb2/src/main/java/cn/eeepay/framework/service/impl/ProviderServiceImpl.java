package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ProviderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.service.ProviderService;
import cn.eeepay.framework.util.ClientInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/27.
 */
@Service
public class ProviderServiceImpl implements ProviderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderServiceImpl.class);
    @Resource
    private ProviderDao providerDao;

    @Override
    public List<ProviderBean> listProvider(ProviderBean providerBean, Page<AgentInfo> page, AgentInfo loginAgent) {
        return providerDao.listProvider(providerBean, page, loginAgent);
    }

    @Override
    public boolean openSuperRepayment(List<String> agentNoList, AgentInfo loginAgent) {
        if (CollectionUtils.isEmpty(agentNoList)){
            LOGGER.error("请选择需要开通的服务商");
            throw new AgentWebException("请选择需要开通的服务商.");
        }

        ProviderBean loginProviderBean = providerDao.queryRepayServiceCost(loginAgent.getAgentNo());
        if (loginProviderBean == null){
            LOGGER.error("登陆代理商没有开通此功能");
            throw new AgentWebException("登陆代理商没有开通此功能.");
        }

        int count = providerDao.chechAgentNoIsDirectChildren(agentNoList, loginAgent);
        if (count != agentNoList.size()){
            LOGGER.error("选中服务商已经开通此功能或不是登陆代理商的直属下级");
            throw new AgentWebException("选中服务商已经开通此功能或不是登陆代理商的直属下级.");
        }

        List<ProviderBean> wantAddAgent = new ArrayList<>();
        for (String agentNo : agentNoList){
            wantAddAgent.add(new ProviderBean(agentNo, loginProviderBean.getRate(), loginProviderBean.getSingleAmount(),
                    loginProviderBean.getFullRepayRate(), loginProviderBean.getFullRepaySingleAmount(),
                    loginProviderBean.getPerfectRepayRate(), loginProviderBean.getPerfectRepaySingleAmount()));
            try {
                 ClientInterface.createAgentAccountBySubjectNo(agentNo, "224114");
            }catch (Exception e){
                LOGGER.error("代理商" + agentNo + "开户(224114)失败.");
            }
        }
        providerDao.openSuperRepayment(wantAddAgent);
        return true;
    }

    @Override
    public boolean updateServiceCost(ProviderBean bean, AgentInfo loginAgent) {
        // 商户交易成本
        ProviderBean oemService = providerDao.queryRepayOemServiceCost(loginAgent.getOneLevelId());
        if (oemService == null){
            oemService = providerDao.queryRepayOemServiceCost("default");
            if (oemService == null){
                LOGGER.error("请配置默认的交易服务费率");
                throw new AgentWebException("请配置默认的交易服务费率.");
            }
        }

        // 登陆代理商的成本
        ProviderBean loginAgentProvider = providerDao.queryRepayServiceCost(loginAgent.getAgentNo());
        if (loginAgentProvider == null){
            LOGGER.error("登陆代理商没有开通此功能");
            throw new AgentWebException("登陆代理商没有开通此功能.");
        }

        if (loginAgentProvider.getRate() == null ||
                loginAgentProvider.getSingleAmount() == null ||
                loginAgentProvider.getFullRepayRate() == null ||
                loginAgentProvider.getFullRepaySingleAmount() == null ||
                loginAgentProvider.getPerfectRepayRate() == null ||
                loginAgentProvider.getPerfectRepaySingleAmount() == null){
            throw new AgentWebException("您的服务商成本未设置，请联系上级代理商设置服商成本后再设置下级成本");

        }

        // 费率和固定金额不能大于商户交易成本且不能小于登陆代理商的成本
        if (bean.getRate().compareTo(loginAgentProvider.getRate()) < 0 ||
                bean.getRate().compareTo(oemService.getRate()) > 0 ){
            String msg = String.format("修改后的费率成本不能大于商户交易成本%s %%且不能小于上级代理商的成本%s %%",
                    oemService.getRate().multiply(new BigDecimal(100)).setScale(4),
                    loginAgentProvider.getRate().multiply(new BigDecimal(100)).setScale(4));
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }

        if (bean.getSingleAmount().compareTo(loginAgentProvider.getSingleAmount()) < 0 ||
                bean.getSingleAmount().compareTo(oemService.getSingleAmount()) > 0 ){
            String msg = String.format("修改后的固定成本不能大于商户交易成本%s 且不能小于上级代理商的成本%s ",
                    oemService.getSingleAmount(),
                    loginAgentProvider.getSingleAmount());
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }


        // 全额还款费率和固定金额不能大于商户交易成本且不能小于登陆代理商的成本
        if (bean.getFullRepayRate().compareTo(loginAgentProvider.getFullRepayRate()) < 0 ||
                bean.getFullRepayRate().compareTo(oemService.getFullRepayRate()) > 0 ){
            String msg = String.format("修改后的全额还款费率成本不能大于商户交易成本%s %%且不能小于上级代理商的成本%s %%",
                    oemService.getFullRepayRate().multiply(new BigDecimal(100)).setScale(4),
                    loginAgentProvider.getFullRepayRate().multiply(new BigDecimal(100)).setScale(4));
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }

        if (bean.getFullRepaySingleAmount().compareTo(loginAgentProvider.getFullRepaySingleAmount()) < 0 ||
                bean.getFullRepaySingleAmount().compareTo(oemService.getFullRepaySingleAmount()) > 0 ){
            String msg = String.format("修改后的全额还款固定成本不能大于商户交易成本%s 且不能小于上级代理商的成本%s ",
                    oemService.getFullRepaySingleAmount(),
                    loginAgentProvider.getFullRepaySingleAmount());
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }

        //完美还款费率和固定金额不能大于商户交易成本且不能小于登录代理成本
        if(bean.getPerfectRepayRate().compareTo(loginAgentProvider.getPerfectRepayRate()) < 0 ||
                bean.getPerfectRepayRate().compareTo(oemService.getPerfectRepayRate()) > 0 ){
            String msg = String.format("修改后的完美还款费率成本不能大于商户交易成本%s %%且不能小于上级代理商的成本%s ",
                    oemService.getPerfectRepayRate().multiply(new BigDecimal(100)).setScale(4),
                    loginAgentProvider.getPerfectRepayRate().multiply(new BigDecimal(100)).setScale(4));
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }
        if (bean.getPerfectRepaySingleAmount().compareTo(loginAgentProvider.getPerfectRepaySingleAmount()) < 0 ||
                bean.getPerfectRepaySingleAmount().compareTo(oemService.getPerfectRepaySingleAmount()) > 0 ){
            String msg = String.format("修改后的完美还款固定成本不能大于商户交易成本%s 且不能小于上级代理商的成本%s ",
                    oemService.getPerfectRepaySingleAmount(),
                    loginAgentProvider.getPerfectRepaySingleAmount());
            LOGGER.error(msg);
            throw new AgentWebException(msg);
        }

        //如果存在下级代理商,修改后的成本必须小于所有下级代理商成本（即小于所有下级代理商成本的最小值）
        //查询出登录代理商的节点
        String agentNode = providerDao.queryAgentNoByAgentNo(bean.getAgentNo());
        ProviderBean lastAgentProvider = providerDao.queryMinCostOfLastAgent(agentNode, bean.getAgentNo());
        if(lastAgentProvider != null){
            //费率和固定金额不能大于下级代理商的最小值
            if (lastAgentProvider.getRate() != null &&
                    bean.getRate().compareTo(lastAgentProvider.getRate()) > 0 ){
                String msg = String.format("修改后的分期还款费率成本不能大于下级代理商的最小交易成本%s %%",
                        lastAgentProvider.getRate().multiply(new BigDecimal(100)).setScale(4));
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }

            if (lastAgentProvider.getSingleAmount() != null &&
                    bean.getSingleAmount().compareTo(lastAgentProvider.getSingleAmount()) > 0 ){
                String msg = String.format("修改后的分期还款固定成本不能大于下级代理商的最小成本%s",
                        lastAgentProvider.getSingleAmount());
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }

            //全额还款费率和固定值不能大于下级代理商的最小值
            if (lastAgentProvider.getFullRepayRate() != null &&
                    bean.getFullRepayRate().compareTo(lastAgentProvider.getFullRepayRate()) > 0 ){
                String msg = String.format("修改后的全额还款费率成本不能大于下级代理商的最小交易成本%s %%",
                        lastAgentProvider.getFullRepayRate().multiply(new BigDecimal(100)).setScale(4));
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }

            if (lastAgentProvider.getFullRepaySingleAmount() != null &&
                    bean.getFullRepaySingleAmount().compareTo(lastAgentProvider.getFullRepaySingleAmount()) > 0 ){
                String msg = String.format("修改后的全额还款固定成本不能大于下级代理商的最小成本%s",
                        lastAgentProvider.getFullRepaySingleAmount());
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }

            //完美还款费率和固定值不能大于下级代理商的最小值
            if (lastAgentProvider.getPerfectRepayRate() != null &&
                    bean.getPerfectRepayRate().compareTo(lastAgentProvider.getPerfectRepayRate()) > 0 ){
                String msg = String.format("修改后的完美还款费率成本不能大于下级代理商的最小交易成本%s %%",
                        lastAgentProvider.getPerfectRepayRate().multiply(new BigDecimal(100)).setScale(4));
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }

            if (lastAgentProvider.getFullRepaySingleAmount() != null &&
                    bean.getFullRepaySingleAmount().compareTo(lastAgentProvider.getFullRepaySingleAmount()) > 0 ){
                String msg = String.format("修改后的完美还款固定成本不能大于下级代理商的最小成本%s",
                        lastAgentProvider.getFullRepaySingleAmount());
                LOGGER.error(msg);
                throw new AgentWebException(msg);
            }
        }
        return providerDao.updateServiceCost(bean) == 1;
    }
}
