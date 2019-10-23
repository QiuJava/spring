package cn.eeepay.framework.service.impl.redemActive;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.eeepay.framework.daoRedem.redemActive.RedemSysConfigDao;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.util.WithDrawExtraCheck;

/**
 * 积分兑换激活版提现额外校验规则
 * Created by 666666 on 2018/5/30.
 */
@Component
public class RedemActiveWithdrawCheck implements WithDrawExtraCheck {
    @Resource
    private RedemSysConfigDao redemSysConfigDao;

    @Override
    public void verification(BigDecimal money, BigDecimal avaliBalance, AgentInfo agentInfo) throws AgentWebException {
        if (agentInfo == null){
            throw new AgentWebException("没找到对应代理商信息");
        }
        if (agentInfo.getAgentLevel() != 1){
            return;
        }
        BigDecimal minRaiseAmout = redemSysConfigDao.queryMinRaiseAmout(agentInfo.getAgentNo());
        if (minRaiseAmout == null){
            return;
        }
        if (avaliBalance.subtract(money).compareTo(minRaiseAmout) < 0){
            throw new AgentWebException("提现后留存金额不得低于" + minRaiseAmout.toString() + "元");
        }
    }
    
    @Override
    public void verificationApi(BigDecimal money, BigDecimal avaliBalance, AgentInfo agentInfo) throws AgentWebException {
    	if (agentInfo == null){
    		throw new AgentWebException("没找到对应代理商信息");
    	}
    	if (agentInfo.getAgentLevel() != 1){
    		return;
    	}
    	BigDecimal minRaiseAmout = redemSysConfigDao.queryMinRaiseAmout(agentInfo.getAgentNo());
    	if (minRaiseAmout == null){
    		return;
    	}
    	if (avaliBalance.subtract(money).compareTo(minRaiseAmout) < 0){
    		throw new AgentWebException("提现后留存金额不得低于" + minRaiseAmout.toString() + "元");
    	}
    }

}
