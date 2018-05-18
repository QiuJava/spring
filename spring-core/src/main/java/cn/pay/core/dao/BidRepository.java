package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {

}
