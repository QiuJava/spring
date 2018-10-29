package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.entity.business.Bid;
import cn.pay.core.repository.BidRepository;
import cn.pay.core.service.BidService;

/**
 * 投标服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class BidServiceImpl implements BidService {

	@Autowired
	private BidRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void saveAndUpdate(Bid bid) {
		repository.saveAndFlush(bid);
	}

}
