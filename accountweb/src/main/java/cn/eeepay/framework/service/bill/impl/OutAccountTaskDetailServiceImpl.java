package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.OutAccountTaskDetailMapper;
import cn.eeepay.framework.dao.bill.OutAccountTaskMapper;
import cn.eeepay.framework.dao.bill.OutBillMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTaskDetail;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.OutAccountTaskDetailService;



@Service("outAccountTaskDetailService")
@Transactional
public class OutAccountTaskDetailServiceImpl implements OutAccountTaskDetailService{
	
	@Resource
	public OutAccountTaskDetailMapper outAccountTaskDetailMapper;
	@Resource
	private OutAccountTaskMapper outAccountTaskMapper;
	@Resource
	private OutBillMapper outBillMapper;

	@Override
	public List<OutAccountTaskDetail> findOutAccountTaskDetailList(OutAccountTaskDetail outAccountTaskDetail, Sort sort,
			Page<OutAccountTaskDetail> page) {
		return outAccountTaskDetailMapper.findOutAccountTaskDetailList(outAccountTaskDetail, sort, page);
	}

	@Override
	public OutAccountTaskDetail findOutAccountTaskDetailById(Integer id) {
		return outAccountTaskDetailMapper.findOutAccountTaskDetailById(id);
	}

	@Override
	public List<OutAccountTaskDetail> findOutAccountTaskDetailByTaskId(Integer outAccountTaskId) {
		return outAccountTaskDetailMapper.findOutAccountTaskDetailByTaskId(outAccountTaskId);
	}

	@Override
	public List<OutAccountTaskDetail> findOutAccountTaskUpdateList(OutAccountTaskDetail outAccountTaskDetail, Sort sort,
			Page<OutAccountTaskDetail> page) throws Exception {
		return outAccountTaskDetailMapper.findOutAccountTaskUpdateList(outAccountTaskDetail, sort, page);
	}

	@Override
	public BigDecimal updateOutAccountDetailAmount(OutAccountTaskDetail outAccountTaskDetail,UserInfo userInfo) {
		OutAccountTaskDetail oldDetail = outAccountTaskDetailMapper.findOutAccountTaskDetailById(outAccountTaskDetail.getId());
		
		List<OutAccountTaskDetail> oldDetailList = outAccountTaskDetailMapper.findOutAccountTaskDetailByTaskId(oldDetail.getOutAccountTaskId());
		BigDecimal temp = BigDecimal.ZERO;
		for (OutAccountTaskDetail item : oldDetailList) {
			if (item.getId().equals(oldDetail.getId())) {
				//该条出账任务明细记录是更新金额的记录
				temp = temp.add(outAccountTaskDetail.getOutAccountAmount());
			} else {
				temp = temp.add(item.getOutAccountAmount());
			}
		}
		String uname = userInfo.getUsername();//获得更新该记录名字
		outAccountTaskMapper.updateOutAccountTaskAmount(oldDetail.getOutAccountTaskId(), temp, uname);
		outAccountTaskDetailMapper.updateOutAccountAmount(outAccountTaskDetail);
		
		return temp;
	}

	@Override
	public int insertBatch(List<OutAccountTaskDetail> list) {
		return outAccountTaskDetailMapper.insertBatch(list);
	}

	@Override
	public int deleteByTaskId(Integer taskId) {
		return outAccountTaskDetailMapper.deleteByTaskId(taskId);
	}

	@Override
	public int insert(OutAccountTaskDetail outAccountTaskDetail) {
		return outAccountTaskDetailMapper.insert(outAccountTaskDetail);
	}



	
}
