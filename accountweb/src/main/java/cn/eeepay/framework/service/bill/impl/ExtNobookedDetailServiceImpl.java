package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ExtNobookedDetailMapper;
import cn.eeepay.framework.model.bill.ExtNobookedDetail;
import cn.eeepay.framework.service.bill.ExtNobookedDetailService;



@Service("extNobookedDetailService")
@Transactional
public class ExtNobookedDetailServiceImpl implements ExtNobookedDetailService{
	
	@Resource
	public ExtNobookedDetailMapper extNobookedDetailMapper;

	@Override
	public int insertExtNobookedDetail(ExtNobookedDetail extNobookedDetail) throws Exception {
		return extNobookedDetailMapper.insertExtNobookedDetail(extNobookedDetail);
	}

	@Override
	public int updateExtNobookedDetail(ExtNobookedDetail extNobookedDetail) throws Exception {
		return extNobookedDetailMapper.updateExtNobookedDetail(extNobookedDetail);
	}

	@Override
	public List<ExtNobookedDetail> findExtNobookedDetailByParams(String accountNo, Date recordDate,
			String bookedFlag) throws Exception {
		return extNobookedDetailMapper.findExtNobookedDetailByParams(accountNo, recordDate, bookedFlag);
	}
	
	
}
