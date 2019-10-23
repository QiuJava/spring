package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.PyIdentificationDao;
import cn.eeepay.framework.model.PyIdentification;
import cn.eeepay.framework.service.PyIdentificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("pyIdentificationService")
public class PyIdentificationServiceImpl implements PyIdentificationService{

	@Resource
	private PyIdentificationDao pyIdentificationDao;
	
	@Override
	public PyIdentification queryByCheckInfo(String name, String card, String accountNo) {
		return pyIdentificationDao.queryByCheckInfo(name, card, accountNo);
	}

	@Override
	public int insert(PyIdentification pyIdentification) {
		return pyIdentificationDao.insert(pyIdentification);
	}

	
}
