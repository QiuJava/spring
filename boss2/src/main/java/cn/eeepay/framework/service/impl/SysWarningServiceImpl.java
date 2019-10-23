package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SysWarningDao;
import cn.eeepay.framework.service.SysWarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("sysWarningService")
@Transactional
public class SysWarningServiceImpl implements SysWarningService {
	private static final Logger log = LoggerFactory.getLogger(SysWarningServiceImpl.class);
	@Resource
	private SysWarningDao sysWarningDao;

	@Override
	public Map getByType(String type){
		return sysWarningDao.getByType(type);
	}

	@Override
	public int updateSysWarning(Map<String, Object> map) {
		return sysWarningDao.updateSysWarning(map);
	}
}
