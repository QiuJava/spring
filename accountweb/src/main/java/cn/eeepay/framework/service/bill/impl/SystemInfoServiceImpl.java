package cn.eeepay.framework.service.bill.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SystemInfoMapper;
import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.service.bill.SystemInfoService;


@Service("systemInfoService")
@Transactional
public class SystemInfoServiceImpl implements SystemInfoService {

	@Resource
	public SystemInfoMapper  SystemInfoMapper;
	

	@Override
	public int updateSystemInfo(SystemInfo systemInfo) throws Exception {
		return SystemInfoMapper.updateSystemInfo(systemInfo);
	}

	@Override
	public SystemInfo findSystemInfoByCurrentDate(String currentDate) throws Exception {
		return SystemInfoMapper.findSystemInfoByCurrentDate(currentDate);
	}

	@Override
	public SystemInfo findSystemInfoById(Integer Id) throws Exception {
		return SystemInfoMapper.findSystemInfoById(Id);
	}

}
