package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.HardwareProductDao;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.service.HardwareProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("hardwareProductService")
public class HardwareProductServiceImpl implements HardwareProductService {

	
	@Resource
	public  HardwareProductDao hardwareProductDao;
	
	@Override
	public List<HardwareProduct> selectAllInfo(String agentNo,String agentOem) {
		return hardwareProductDao.selectAllInfo(agentNo,agentOem);
	}

	@Override
	public List<HardwareProduct> selectAllInfoByPn(String agentNo,String agentOem) {
		return hardwareProductDao.selectAllInfoByPn(agentNo,agentOem);
	}

	@Override
	public HardwareProduct selectHardwareName(String hardWareId) {
		return hardwareProductDao.findHardwareName(hardWareId);
	}

	@Override
	public List<HardwareProduct> selectAllHardwareName() {
		return hardwareProductDao.findAllHardwareName();
	}

}
