package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import cn.eeepay.framework.daoSuperBank.OrgInfoDao;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.util.WithDrawExtraCheck;

/**
 * 超级银行家提现额外校验规则
 * Created by 666666 on 2018/5/30.
 */
@Component
public class SuperBankWithdrawCheck implements WithDrawExtraCheck {
	private static final Logger log = LoggerFactory.getLogger(AgentInfoServiceImpl.class);
	@Resource
	private OrgInfoDao orgInfoDao;

    @Override
    public void verification(BigDecimal money, BigDecimal avaliBalance, AgentInfo agentInfo) throws AgentWebException {
        if (agentInfo == null){
            throw new AgentWebException("没找到对应代理商信息");
        }
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String withdrawMoneyMin = orgInfoDao.selectWithdrawMoneyMinByOrgId(principal.getUserEntityInfo().getEntityId());
        if (((avaliBalance.subtract(new BigDecimal(withdrawMoneyMin))).subtract(money)).doubleValue() < 0) {
        	log.info("由于退代理费和由于普通用户办理信用卡办理贷款红包，都要从OEM分润账户中扣钱，本账户只能提走余额超过" + withdrawMoneyMin + "元的部分");
        	throw new AgentWebException("由于退代理费和由于普通用户办理信用卡办理贷款红包，"
        			+ "都要从OEM分润账户中扣钱，本账户只能提走余额超过" + withdrawMoneyMin + "元的部分");
		}
    }
    
    @Override
    public void verificationApi(BigDecimal money, BigDecimal avaliBalance, AgentInfo agentInfo) throws AgentWebException {
    	if (agentInfo == null){
    		throw new AgentWebException("没找到对应代理商信息");
    	}
//    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	String withdrawMoneyMin = orgInfoDao.selectWithdrawMoneyMinByOrgId(agentInfo.getAgentNo());
    	if (((avaliBalance.subtract(new BigDecimal(withdrawMoneyMin))).subtract(money)).doubleValue() < 0) {
    		log.info("由于退代理费和由于普通用户办理信用卡办理贷款红包，都要从OEM分润账户中扣钱，本账户只能提走余额超过" + withdrawMoneyMin + "元的部分");
    		throw new AgentWebException("由于退代理费和由于普通用户办理信用卡办理贷款红包，"
    				+ "都要从OEM分润账户中扣钱，本账户只能提走余额超过" + withdrawMoneyMin + "元的部分");
    	}
    }
}
