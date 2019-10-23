package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Batches;

public interface BatchesService {
	List<Batches> findBatchesList(String start,String end, Sort sort, Page<Batches> page) throws Exception;
}
