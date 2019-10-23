package cn.eeepay.framework.service.nposp.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.MerchantCardInfoMapper;
import cn.eeepay.framework.model.nposp.MerchantCardInfo;
import cn.eeepay.framework.service.nposp.MerchantCardInfoService;

@Service("merchantCardInfoService")
@Transactional("nposp")
public class MerchantCardInfoServiceImpl implements MerchantCardInfoService {
	@Resource
	private MerchantCardInfoMapper merchantCardInfoMapper;
	
	@Override
	public MerchantCardInfo getByMerchantNo(String merchantNo) {
		List<MerchantCardInfo> list = merchantCardInfoMapper.getByMerchantNo(merchantNo);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public MerchantCardInfo getByMobile(String mobile) {
		List<MerchantCardInfo> list = merchantCardInfoMapper.getByMobile(mobile);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

}
