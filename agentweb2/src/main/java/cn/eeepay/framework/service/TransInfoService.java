package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.PageBean;

public interface TransInfoService {
	
	List<CollectiveTransOrder> queryAllInfo(Page<CollectiveTransOrder> page,CollectiveTransOrder transInfo,int level,int level1);
	
	String queryNumAndMoney(CollectiveTransOrder transInfo,String loginAgentNo);
	
	CollectiveTransOrder queryInfoDetail(String id);

	List<CollectiveTransOrder> exportAllInfo(CollectiveTransOrder transInfo, int level, int level1);

	List<CollectiveTransOrder> queryAllInfoByMerchant(PageBean page, CollectiveTransOrder param, AgentInfo loginAgentNo);

	List<CollectiveTransOrder> exportAllInfoByMerchant(CollectiveTransOrder transInfo, AgentInfo loginAgent);

    int countAllInfoByMerchant(CollectiveTransOrder tis, AgentInfo loginAgent);

    CollectiveTransOrder queryInfoDetailForSurveyOrder(String id);

	CollectiveTransOrder selectByOrderNo(String orderNo);

    CollectiveTransOrder selectByOrderNoAndAgentNode(String orderNo, String agentNode);

}
