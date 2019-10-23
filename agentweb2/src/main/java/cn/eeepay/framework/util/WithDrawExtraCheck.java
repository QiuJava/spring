package cn.eeepay.framework.util;

import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;

import java.math.BigDecimal;

/**
 * 提现额外校验接口
 * Created by 666666 on 2018/5/30.
 */
public interface WithDrawExtraCheck {

    /**
     * 如果校验失败,这抛出AgentWebException异常
     * @param money 提现金额
     * @param avaliBalance 实际可用余额
     * @param agentInfo 提现代理商编号
     * @throws AgentWebException
     */
    void verification(BigDecimal money,BigDecimal avaliBalance, AgentInfo agentInfo) throws AgentWebException;
    
    void verificationApi(BigDecimal money,BigDecimal avaliBalance, AgentInfo agentInfo) throws AgentWebException;
}
