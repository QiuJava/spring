package cn.eeepay.framework.service.nposp.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.BusinessProductDefineMapper;
import cn.eeepay.framework.model.nposp.BusinessProductDefine;
import cn.eeepay.framework.service.nposp.BusinessProductDefineService;

@Service("businessProductDefineService")
@Transactional("nposp")
public class BusinessProductDefineServiceImpl implements BusinessProductDefineService{
	@Resource
	private BusinessProductDefineMapper businessProductDefineMapper;

	@Override
	public BusinessProductDefine getById(Integer bpId) {
		return businessProductDefineMapper.getById(bpId);
	}
	
	
}
