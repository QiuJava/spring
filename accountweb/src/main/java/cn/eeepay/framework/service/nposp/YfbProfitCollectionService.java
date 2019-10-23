package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.YfbProfitCollection;

import java.util.List;
import java.util.Map;

public interface YfbProfitCollectionService {

	public int insertServiceShareDaySettleBatch(List<YfbProfitCollection> list) throws Exception;

	public Map<String, Object> insertServiceShareDaySettleSplitBatch(
			List<YfbProfitCollection> yfbProfitCollections) throws Exception;

	List<YfbProfitCollection> findServiceShareDaySettleByCollectionBatchNo(String collectionBatchNo);

	List<YfbProfitCollection> findServiceShareList(YfbProfitCollection yfbProfitCollection, Sort sort, Page<YfbProfitCollection> page);

	List<YfbProfitCollection> exportServiceInAccountList(YfbProfitCollection yfbProfitCollection);

    Map<String,Object> serviceInAccountCollectionDataCount(YfbProfitCollection yfbProfitCollection);
}

