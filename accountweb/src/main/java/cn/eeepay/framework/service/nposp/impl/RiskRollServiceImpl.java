package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.RiskRollMapper;
import cn.eeepay.framework.model.nposp.RiskRoll;
import cn.eeepay.framework.service.nposp.RiskRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("riskRollService")
public class RiskRollServiceImpl implements RiskRollService {

    @Autowired
    private RiskRollMapper riskRollMapper;


    @Override
    public RiskRoll findMerchantBlackByMerchantNo(String merchantNo) {
        return riskRollMapper.findMerchantBlackByMerchantNo(merchantNo);
    }
}
