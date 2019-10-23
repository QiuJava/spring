package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.BatchesDetailMapper;
import cn.eeepay.framework.model.bill.BatchesDetail;
import cn.eeepay.framework.service.bill.BatchesDetailService;

@Service
public class BatchesDetailServiceImpl implements BatchesDetailService {
	@Autowired
	private BatchesDetailMapper batchesDetailMapper;

	@Override
	public List<BatchesDetail> findByBatchesId(Integer batchesId) {
		return batchesDetailMapper.findByBatchesId(batchesId);
	}

	@Override
	public int updateStatusById(Integer status, Integer id) {
		return batchesDetailMapper.updateStatusById(status, id);
	}

	@Override
	public BatchesDetail getExecuteLog(Integer id) {
		return batchesDetailMapper.getExecuteLog(id);
	}

	@Override
	public int updateExecuteLog(Integer id, String log) {
		return batchesDetailMapper.updateExecuteLog(id, log);
	}

}
