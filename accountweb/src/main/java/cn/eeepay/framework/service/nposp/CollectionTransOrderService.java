package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.nposp.CollectiveTransOrder;


public interface CollectionTransOrderService {


	List<CollectiveTransOrder> queryTransOrder(String acqEnname,
			String jhTimeStart, String jhTimeEnd);	
	CollectiveTransOrder queryByAcqFastTransInfo(String acqOrderNo,String acqEnname);
	CollectiveTransOrder getTranDataByOrderNo(String orderNo,String acqEnname);

	CollectiveTransOrder getTranDataByOrderNo1(String acqReferenceNo,String acqEnname);

	CollectiveTransOrder selectByOrderNo(int agentNode,String orderNo);
	int updateFreezeStatusByOrderNo(String orderNo,String freezeStatus);
}
