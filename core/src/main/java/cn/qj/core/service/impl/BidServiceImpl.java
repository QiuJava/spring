package cn.qj.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.Bid;
import cn.qj.core.repository.BidRepository;
import cn.qj.core.service.BidService;

/**
 * 投标服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
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
