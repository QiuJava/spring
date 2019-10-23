package cn.eeepay.framework.service;




import java.util.List;

import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.MerchantRequireItem;

public interface MerchantRequireItemService {

	public MerchantRequireItem selectByMriId(String mriId,String merId);
	
	public int updateBymriId(Long id,String status);
	
	public int updateByMbpId(MerchantRequireItem record);
	
	int insertInfo(MerchantRequireItem record);
	
	MerchantRequireItem selectByAccountNo(String merId);
	
	MerchantRequireItem findInfo(String merId);

	List<AddRequireItem> selectPhotoAddress();
	
	public MerchantRequireItem selectByNoAndId(String merchantNo,String mriId);

}
