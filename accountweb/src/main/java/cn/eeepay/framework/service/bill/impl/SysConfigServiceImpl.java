package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.SysConfigMapper;
import cn.eeepay.framework.model.bill.SysConfig;
import cn.eeepay.framework.service.bill.SysConfigService;

@Service
public class SysConfigServiceImpl implements SysConfigService{
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Override
	public SysConfig getByKey(String key) {
		List<SysConfig> list = sysConfigMapper.getByKey(key);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public int update(SysConfig config) {
		return sysConfigMapper.update(config);
	}

}
