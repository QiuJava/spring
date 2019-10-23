package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.HlfAgentDebtRecord;


/**
 * 
 *
 */
public interface HlfAgentDebtRecordService {
	
	List<HlfAgentDebtRecord> findHlfAgentDebtRecordList(HlfAgentDebtRecord hlfAgentDebtRecord, Map<String, String> params, Sort sort,
			Page<HlfAgentDebtRecord> page) throws Exception;

	List<HlfAgentDebtRecord> exportHlfAgentDebtRecordList(HlfAgentDebtRecord hlfAgentDebtRecord) throws Exception;

	Map<String, BigDecimal> findHlfAgentDebtRecordListCollection(HlfAgentDebtRecord hlfAgentDebtRecord);

	List<HlfAgentDebtRecord> findHlfAgentDebtRecordAgentNo(HlfAgentDebtRecord hlfAgentDebtRecord);

	Map<String, BigDecimal> findHlfAgentDebtRecordListCollectionByList(String agentInfoList);

	Map<String, BigDecimal> findHlfAgentDebtRecordShouldDebtAmountCollection(HlfAgentDebtRecord hlfAgentDebtRecord);


}
