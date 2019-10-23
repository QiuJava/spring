package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantServiceDao;
import cn.eeepay.framework.model.MerchantService;
import cn.eeepay.framework.service.MerchantServiceProService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("merchantServiceProService")
public class MerchantServiceProServiceImpl implements MerchantServiceProService{

	@Resource
	private MerchantServiceDao merchantServiceDao;
	
	@Override
	public int updateByPrimaryKey(MerchantService record) {
		return merchantServiceDao.updateByPrimaryKey(record);
	}

	@Override
	public List<MerchantService> selectByMerId(String merId) {
		return merchantServiceDao.selectByMerId(merId);
	}

	@Override
	public List<String> selectServiceTypeByMerId(String merId) {
		return merchantServiceDao.selectServiceTypeByMerId(merId);
	}

}
