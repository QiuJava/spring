package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.BidRepository;
import cn.pay.core.domain.business.Bid;
import cn.pay.core.service.BidService;

@Service
public class BidServiceImpl implements BidService {

	@Autowired
	private BidRepository repository;

	@Override
	@Transactional
	public void saveAndUpdate(Bid bid) {
		repository.saveAndFlush(bid);
	}

}
