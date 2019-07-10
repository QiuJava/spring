package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.Bid;

/**
 * 投标数据操作
 * 
 * @author qiujian
 *
 */
public interface BidDao extends JpaRepository<Bid, Long> {

}
