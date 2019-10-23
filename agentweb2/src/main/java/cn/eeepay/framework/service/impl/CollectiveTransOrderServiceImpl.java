package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CollectiveTransOrderDao;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.service.CollectiveTransOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service("collectiveTransOrderService")
public class CollectiveTransOrderServiceImpl implements CollectiveTransOrderService{

	@Resource
	private CollectiveTransOrderDao collectiveTransOrderDao;
	@Override
	public CollectiveTransOrder selectByOrderNo(int agentNode, String orderNo) {
		return collectiveTransOrderDao.selectByOrderNo(agentNode, orderNo);
	}

	@Override
	public List<Map<String, String>> batchSelectPayMethod(List<String> orderNos) {
		return collectiveTransOrderDao.batchSelectPayMethod(orderNos);
	}
}
