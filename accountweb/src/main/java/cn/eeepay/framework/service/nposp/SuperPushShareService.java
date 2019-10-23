package cn.eeepay.framework.service.nposp;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.model.nposp.SuperPushShare;

public interface SuperPushShareService {
	
	List<SuperPushShare> findSuperPushShareList(SuperPushShare superPushShare,Sort sort,Page<SuperPushShare> page);
	
	
	List<SuperPushShare> exportSuperPushShareList(SuperPushShare superPushShare);
	
	List<Map<String, Object>> findCollectionGropByShareNo(String createTime1)  throws Exception;
	
	int updateSuperPushShareBatch(List<SuperPushShare> list)   throws Exception;
	
	Map<String, Object> updateSuperPushShareSplitBatch(List<SuperPushShare> superPushShareList);


	List<SuperPushShare> findSuperPushShareListEnterByModel(SuperPushShareDaySettle superPushShareDaySettle);


	Map<String, Object> findSuperPushShareCollection(SuperPushShare superPushShareDay);


	List<SuperPushShare> findSuperPushShareListCollectionByModel(SuperPushShareDaySettle superPushShareDaySettle);


	Map<String, Object> findSuperPushShareCollectionTotalAmount(SuperPushShare superPushShare);

}

