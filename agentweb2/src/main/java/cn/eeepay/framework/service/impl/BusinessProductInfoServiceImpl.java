package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.BusinessProductInfoDao;
import cn.eeepay.framework.model.BusinessProductInfo;
import cn.eeepay.framework.service.BusinessProductInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("businessProductInfoService")
public class BusinessProductInfoServiceImpl implements BusinessProductInfoService {

	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	@Override
	public List<BusinessProductInfo> selectInfoByBpId(String bpId) {
		return businessProductInfoDao.selectInfoByBpId(bpId);
	}

}
