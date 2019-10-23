package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AuditorManagerDao;
import cn.eeepay.framework.model.AuditorManager;
import cn.eeepay.framework.service.AuditorManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("auditorManagerService")
public class AuditorManagerServiceImpl implements AuditorManagerService{

	@Resource
	private AuditorManagerDao auditorManagerDao;

	@Override
	public List<AuditorManager> findAllInfo(String bpId) {
		return auditorManagerDao.findAllInfo(bpId);
	}
}
