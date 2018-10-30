package cn.qj.core.service;

import cn.qj.core.entity.Bid;

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
