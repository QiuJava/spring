package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ExaminationsLogDao;
import cn.eeepay.framework.model.ExaminationsLog;
import cn.eeepay.framework.service.ExaminationsLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ExaminationsLogService")
public class ExaminationsLogServiceImpl implements ExaminationsLogService {

	@Resource
	private ExaminationsLogDao examinationsLogDao;

	@Override
	public int insert(ExaminationsLog record) {
		return examinationsLogDao.insert(record);
	}

	@Override
	public List<ExaminationsLog> selectByMerchantId(String merchantId) {
		return examinationsLogDao.selectByMerchantId(merchantId);
	}
	
	
}
