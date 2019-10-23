package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.BatchesMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Batches;
import cn.eeepay.framework.service.bill.BatchesService;

@Service
public class BatchesServiceImpl implements BatchesService {
	@Autowired
	private BatchesMapper batchesMapper;

	@Override
	public List<Batches> findBatchesList(String start, String end, Sort sort,
			Page<Batches> page) throws Exception {
		return batchesMapper.findBatchesList(start, end, sort, page);
	}

}
