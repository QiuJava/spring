package cn.eeepay.framework.service.nposp.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.HardwareProductMapper;
import cn.eeepay.framework.model.nposp.HardwareProduct;
import cn.eeepay.framework.service.nposp.HardwareProductService;

@Service("hardwareProductService")
@Transactional("nposp")
public class HardwareProductServiceImpl implements HardwareProductService {
	@Resource
	private HardwareProductMapper hardwareProductMapper;
	
	@Override
	public HardwareProduct getById(Long id) {
		return hardwareProductMapper.getById(id);
	}

}
