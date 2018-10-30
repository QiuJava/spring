package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.Bid;

/**
 * 投标持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface BidRepository extends JpaRepository<Bid, Long> {

}
