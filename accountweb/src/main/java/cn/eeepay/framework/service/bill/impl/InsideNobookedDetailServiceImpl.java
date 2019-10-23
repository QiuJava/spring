package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.InsideNobookedDetailMapper;
import cn.eeepay.framework.model.bill.InsideNobookedDetail;
import cn.eeepay.framework.service.bill.InsideNobookedDetailService;



@Service("insideNobookedDetailService")
@Transactional
public class InsideNobookedDetailServiceImpl implements InsideNobookedDetailService{
	
	@Resource
	public InsideNobookedDetailMapper insideNobookedDetailMapper;

	@Override
	public int insertInsideNobookedDetail(InsideNobookedDetail insideNobookedDetail) throws Exception {
		return insideNobookedDetailMapper.insertInsideNobookedDetail(insideNobookedDetail);
	}

	@Override
	public int updateInsideNobookedDetail(InsideNobookedDetail insideNobookedDetail) throws Exception {
		return insideNobookedDetailMapper.updateInsideNobookedDetail(insideNobookedDetail);
	}

	@Override
	public List<InsideNobookedDetail> findInsideNobookedDetailByParams(String accountNo, Date transDate,
			String bookedFlag) throws Exception {
		return insideNobookedDetailMapper.findInsideNobookedDetailByParams(accountNo, transDate, bookedFlag);
	}
	
	
}
