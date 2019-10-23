package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ExtTransInfoMapper;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.service.bill.ExtTransInfoService;

@Transactional
@Service
public class ExtTransInfoServiceImpl implements ExtTransInfoService{
	@Resource
	public ExtTransInfoMapper extTransInfoMapper;
	
	@Override
	public int insertExtTransInfo(ExtTransInfo extTransInfo) throws Exception {
		return extTransInfoMapper.insertExtTransInfo(extTransInfo);
	}
	@Override
	public int updateExtTransInfo(ExtTransInfo extTransInfo) throws Exception {
		return extTransInfoMapper.updateExtTransInfo(extTransInfo);
	}
	@Override
	public BigDecimal countTransMoney(String accountNo, Date date) {
		BigDecimal money = extTransInfoMapper.findByAccountNoAndDate(accountNo, date);
		return money;
	}
}
