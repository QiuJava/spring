package cn.pay.core.service;

import cn.pay.core.domain.business.Bid;

/**
 * 投标
 * 
 * @author Administrator
 *
 */
public interface BidService {
	
	void saveAndUpdate(Bid bid);
	
}
