package cn.pay.core.service;

import cn.pay.core.domain.business.Bid;

/**
 * 投标服务
 * 
 * @author Administrator
 *
 */
public interface BidService {

	/**
	 * 投标保存或更新
	 * 
	 * @param bid
	 */
	void saveAndUpdate(Bid bid);

}
