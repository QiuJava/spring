package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutChannelLadderRateRebalance;

public interface OutChannelLadderRateRebalanceService {
	public List<OutChannelLadderRateRebalance> findOutChannelRate(OutChannelLadderRateRebalance obj, Sort sort, Page<OutChannelLadderRateRebalance> page);
	
	OutChannelLadderRateRebalance getById(Long id);
	
	int updateRecordStatus(Long id, Integer recordStatus);
	public int insertOutChannelLadderRateRebalance(OutChannelLadderRateRebalance oclrr);
	
	int updateRealRebalance(OutChannelLadderRateRebalance item);
}
