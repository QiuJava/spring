package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.OutAccountTaskMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTask;
import cn.eeepay.framework.service.bill.OutAccountTaskService;



@Service("outAccountTaskService")
@Transactional
public class OutAccountTaskServiceImpl implements OutAccountTaskService{
	
	@Resource
	public OutAccountTaskMapper outAccountTaskMapper;

	@Override
	public List<OutAccountTask> findOutAccountTaskList(Map<String, Object> param, Sort sort,
			Page<OutAccountTask> page) throws Exception {
		return outAccountTaskMapper.findOutAccountTaskList(param, sort, page);
	}

	@Override
	public OutAccountTask findOutAccountTaskById(Integer id) {
		return outAccountTaskMapper.findOutAccountTaskById(id);
	}

	@Override
	public int insert(OutAccountTask task) {
		return outAccountTaskMapper.insert(task);
	}

	@Override
	public int insertOrUpdate(OutAccountTask task) {
		int result = -1;
		result = outAccountTaskMapper.insert(task);
		
		return result;
	}

	@Override
	public BigDecimal calcOutAccountTaskAmountByAcqEnname(String acqEnname, Date transTime) {
		return outAccountTaskMapper.calcOutAccountTaskAmountByAcqEnname(acqEnname, transTime);
	}

	@Override
	public List<OutAccountTask> findByTransTimeAndAcqEnname(Date transTime, String acqEnname, String outBillRange) {
		return outAccountTaskMapper.findByTransTimeAndAcqEnname(transTime, acqEnname, outBillRange);
	}

	@Override
	public int update(OutAccountTask task) {
		return outAccountTaskMapper.update(task);
	}

	@Override
	public List<OutAccountTask> findOutAccountTaskByTransTime(Date transTime) {
		return outAccountTaskMapper.findOutAccountTaskByTransTime(transTime);
	}

	@Override
	public int updateToClosedByTransTime(Date transTime) {
		return outAccountTaskMapper.updateToClosedByTransTime(transTime);
	}
}
