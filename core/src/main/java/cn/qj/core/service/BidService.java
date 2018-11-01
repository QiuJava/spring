package cn.qj.core.service;

import cn.qj.core.entity.Bid;

/**
 * 投标服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface BidService {

	/**
	 * 投标保存或更新
	 * 
	 * @param bid
	 */
	void saveAndUpdate(Bid bid);

}
