package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;

public interface SuperPushShareDaySettleService {
	
	int insert(SuperPushShareDaySettle superPushShareDaySettle);
	
	int update(SuperPushShareDaySettle superPushShareDaySettle);
	
	int delete(Integer id);
	
	List<SuperPushShareDaySettle> findSuperPushShareDaySettleList(SuperPushShareDaySettle superPushShareDaySettle,Sort sort,Page<SuperPushShareDaySettle> page);
	
	Map<String,Object> superPushShareCollection(String createDate1,String operater) throws Exception;

	int insertSuperPushShareDaySettleBatch(List<SuperPushShareDaySettle> list)   throws Exception;
	
	Map<String,Object> insertSuperPushShareDaySettleSplitBatch(List<SuperPushShareDaySettle> superPushShareDaySettleList) throws Exception;
	
	Map<String, Object> findSuperPushShareDaySettleCollection(SuperPushShareDaySettle superPushShareDaySettle)  throws Exception;
	
	Map<String,Object> superPushBatchEnterAccount(String selectEnterId) throws Exception;

	SuperPushShareDaySettle findSuperPushShareDaySettleById(Integer id);

	List<SuperPushShareDaySettle> exportSuperPushShareDaySettleList(SuperPushShareDaySettle superPushShareDaySettle);

	Map<String, Object> judgeSuperPushShareEnterTodayAccount(String currentDate);

	Map<String, Object> superPushEnterTodayAccount(String currentDate) throws Exception;

}
