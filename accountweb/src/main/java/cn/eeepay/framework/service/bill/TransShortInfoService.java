package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.TransShortInfo;

public interface TransShortInfoService {
	int insertTransShortInfo(TransShortInfo transShortInfo)  throws Exception;
	int updateTransShortInfo(TransShortInfo transShortInfo) throws Exception;
	int deleteTransShortInfo(String plateOrderNo) throws Exception;
	List<TransShortInfo> findAllTransShortInfo() throws Exception;
	TransShortInfo findTransShortInfoByPlateOrderNo(String plateOrderNo) throws Exception;
	List<TransShortInfo> findTransShortInfoList(TransShortInfo transShortInfo, Sort sort,
			Page<TransShortInfo> page);
	
	List<TransShortInfo> findAllTransShortInfoByTransTime(String transDate1);

	public int updateTransShortInfoByDate(Map<String,String> param);

	Map<String,Object> updateTransShortInfoSplitBatch(List<TransShortInfo> transShortInfoList) throws Exception;
	List<TransShortInfo> exportAgentsProfitTransShortInfoList(TransShortInfo transShortInfo);
	
	List<TransShortInfo> findNoCollectTransShortInfo(Map<String, String> params);
	
	
	
}
