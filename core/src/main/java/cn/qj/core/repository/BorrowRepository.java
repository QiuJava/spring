package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.Borrow;

/**
 * 借款持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface BorrowRepository extends JpaRepository<Borrow, Long>, JpaSpecificationExecutor<Borrow> {

}
