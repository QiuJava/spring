package cn.eeepay.framework.service.impl;


import cn.eeepay.framework.dao.RiskRuleDao;
import cn.eeepay.framework.service.RiskRuleService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by 666666 on 2017/12/29.
 */
@Service
public class RiskRuleServiceImpl implements RiskRuleService{
    @Resource
    private RiskRuleDao riskRuleDao;


    @Override
    public Map<String, Object> getRiskRule(String ruleNo) {
        return riskRuleDao.selectRiskRule(ruleNo);
    }
}
