package cn.eeepay.framework.service;

import cn.eeepay.framework.model.CollectiveTransOrder;

import java.util.List;
import java.util.Map;

public interface CollectiveTransOrderService {

	CollectiveTransOrder selectByOrderNo(int agentNode,String orderNo);

    List<Map<String, String>> batchSelectPayMethod(List<String> orderNos);
}
