package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.OutChannelLadderRateRebalanceMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutChannelLadderRateRebalance;
import cn.eeepay.framework.service.bill.OutChannelLadderRateRebalanceService;

@Service
public class OutChannelLadderRateRebalanceServiceImpl implements
		OutChannelLadderRateRebalanceService {
	@Autowired
	private OutChannelLadderRateRebalanceMapper outChannelLadderRateRebalanceMapper;

	@Override
	public List<OutChannelLadderRateRebalance> findOutChannelRate(
			OutChannelLadderRateRebalance obj, Sort sort,
			Page<OutChannelLadderRateRebalance> page) {
		return outChannelLadderRateRebalanceMapper.findOutChannelRate(obj, sort, page);
	}

	@Override
	public OutChannelLadderRateRebalance getById(Long id) {
		return outChannelLadderRateRebalanceMapper.getById(id);
	}

	@Override
	public int updateRecordStatus(Long id, Integer recordStatus) {
		return outChannelLadderRateRebalanceMapper.updateRecordStatus(id, recordStatus);
	}
	@Override
	public int insertOutChannelLadderRateRebalance(OutChannelLadderRateRebalance oclrr) {
		return outChannelLadderRateRebalanceMapper.insertOutChannelLadderRateRebalance(oclrr);
	}

	@Override
	public int updateRealRebalance(OutChannelLadderRateRebalance item) {
		return outChannelLadderRateRebalanceMapper.updateRealRebalance(item);
	}
}
