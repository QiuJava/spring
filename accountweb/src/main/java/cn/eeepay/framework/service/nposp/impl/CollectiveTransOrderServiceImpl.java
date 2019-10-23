package cn.eeepay.framework.service.nposp.impl;

import java.util.List;





import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.service.nposp.CollectionTransOrderService;


@Service("collectionTransOrderService")
@Transactional("nposp")
public class CollectiveTransOrderServiceImpl implements CollectionTransOrderService{
	private static final Logger log = LoggerFactory.getLogger(CollectiveTransOrderServiceImpl.class);
	@Resource
	public  CollectiveTransOrderMapper  collectiveTransOrderMapper;
	
	
	@Override
	public List<CollectiveTransOrder> queryTransOrder(String acqEnname,String beginTime,String endtime) {
		List<CollectiveTransOrder>  ct = collectiveTransOrderMapper.findCheckData(acqEnname, "PURCHASE", beginTime, endtime);
		return ct;
	}

	@Override
	public CollectiveTransOrder queryByAcqFastTransInfo(String acqOrderNo,
			String acqEnname) {
		CollectiveTransOrder ct = collectiveTransOrderMapper.findFastAccountDetailByTransInfo(acqOrderNo,acqEnname);
		return ct;
	}

	@Override
	public CollectiveTransOrder getTranDataByOrderNo(String orderNo, String acqEnname) {
		CollectiveTransOrder ct = collectiveTransOrderMapper.getTranDataByOrderNo(orderNo,acqEnname);
		return ct;
	}

	@Override
	public CollectiveTransOrder getTranDataByOrderNo1(String acqReferenceNo, String acqEnname) {
		CollectiveTransOrder ct = collectiveTransOrderMapper.getTranDataByOrderNo1(acqReferenceNo,acqEnname);
		return ct;
	}

	@Override
	public CollectiveTransOrder selectByOrderNo(int agentNode, String orderNo) {
		return collectiveTransOrderMapper.selectByOrderNo(agentNode, orderNo);
	}
	@Override
	public int updateFreezeStatusByOrderNo(String orderNo, String freezeStatus) {
		return collectiveTransOrderMapper.updateFreezeStatusByOrderNo(orderNo, freezeStatus);
	}
}
