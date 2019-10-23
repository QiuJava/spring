package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantRequireItemDao;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.MerchantRequireItem;
import cn.eeepay.framework.service.MerchantRequireItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("merchantRequireItemService")
public class MerchantRequireItemServiceImpl implements MerchantRequireItemService {

	@Resource
	private MerchantRequireItemDao merchantRequireItemDao;
	
	@Override
	public int updateBymriId(Long id, String status) {
		return merchantRequireItemDao.updateBymriId(id, status);
	}

	@Override
	public int updateByMbpId(MerchantRequireItem record) {
		return merchantRequireItemDao.updateByMbpId(record);
	}

	@Override
	public MerchantRequireItem selectByMriId(String mriId,String merId) {
		return merchantRequireItemDao.selectByMriId(mriId,merId);
	}

	@Override
	public int insertInfo(MerchantRequireItem record) {
		return merchantRequireItemDao.insertInfo(record);
	}

	@Override
	public MerchantRequireItem selectByAccountNo(String merId) {
		return merchantRequireItemDao.selectByAccountNo(merId);
	}

	@Override
	public MerchantRequireItem findInfo(String merId) {
		return merchantRequireItemDao.findInfo(merId);
	}

	@Override
	public List<AddRequireItem> selectPhotoAddress() {
		return merchantRequireItemDao.selectPhotoAddress();
	}

	@Override
	public MerchantRequireItem selectByNoAndId(String merchantNo, String mriId) {
		return merchantRequireItemDao.selectByNoAndId(merchantNo, mriId);
	}

}
