package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.BusinessRequireItemDao;
import cn.eeepay.framework.service.BusinessRequireItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("businessRequireItemService")
public class BusinessRequireItemServiceImpl implements BusinessRequireItemService {

	@Resource
	private BusinessRequireItemDao businessRequireItemDao;
	@Override
	public List<String> findByProduct(String bpId) {
		return businessRequireItemDao.findByProduct(bpId);
	}
	@Override
	public List<String> findMerItem(String bpId, String itemId) {
		// TODO Auto-generated method stub
		return businessRequireItemDao.findMerItem(bpId, itemId);

	}

}
