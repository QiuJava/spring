package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantCardInfoDao;
import cn.eeepay.framework.model.MerchantCardInfo;
import cn.eeepay.framework.service.MerchantCardInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("merchantCardInfoService")
public class MerchantCardInfoServiceImpl implements MerchantCardInfoService {

	
	@Resource
	private MerchantCardInfoDao merchantCardInfoDao;
	

	@Override
	public int insert(MerchantCardInfo record) {
		return merchantCardInfoDao.insert(record);
	}


	@Override
	public MerchantCardInfo selectByMertId(String mertId) {
		return merchantCardInfoDao.selectByMertId(mertId);
	}


	@Override
	public int updateById(MerchantCardInfo record) {
		return merchantCardInfoDao.updateById(record);
	}

}
