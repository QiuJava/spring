package cn.eeepay.framework.service.bill.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SysLogsMapper;
import cn.eeepay.framework.model.bill.SysLogs;
import cn.eeepay.framework.service.bill.SysLogsService;

@Service("sysLogsService")
@Transactional
public class SysLogsServiceImpl implements SysLogsService{
	private static final Logger log = LoggerFactory.getLogger(SysLogsServiceImpl.class);
	@Resource
	public SysLogsMapper sysLogsMapper;

	

	@Override
	public int insertSysLogs(SysLogs sysLogs) throws Exception {
		return sysLogsMapper.insertSysLogs(sysLogs);
	}
}
