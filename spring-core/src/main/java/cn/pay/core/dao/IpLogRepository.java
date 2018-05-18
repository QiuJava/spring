package cn.pay.core.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.IpLog;

public interface IpLogRepository extends JpaRepository<IpLog, Long>, JpaSpecificationExecutor<IpLog> {

	/**
	 * 根据用户名查询用户
	 * 
	 * @param username
	 * @param pageable
	 * @return
	 */
	List<IpLog> findByUsernameOrderByLoginTimeDesc(String username, Pageable pageable);

}
