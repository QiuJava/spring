package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantServiceQuotaDao;
import cn.eeepay.framework.model.MerchantServiceQuota;
import cn.eeepay.framework.service.MerchantServiceQuotaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("merchantServiceQuotaService")
public class MerchantServiceQuotaServiceImpl implements MerchantServiceQuotaService {

	@Resource
	private MerchantServiceQuotaDao merchantServiceQuotaDao;
	
	@Override
	public int updateByPrimaryKey(MerchantServiceQuota record) {
		return merchantServiceQuotaDao.updateByPrimaryKey(record);
	}

	//用商户限额修改查询
	public List<MerchantServiceQuota> selectByMertId(MerchantServiceQuota record) {
		return merchantServiceQuotaDao.selectByMertId(record);
	}

	//用于商户限额新增查询
	public MerchantServiceQuota addSelectInfo(MerchantServiceQuota record) {
		return null;
	}

	@Override
	public List<MerchantServiceQuota> selectByMertIdAndServiceId(String merId, String serId) {
		return merchantServiceQuotaDao.selectByMertIdAndServiceId(merId, serId);
	}

}
