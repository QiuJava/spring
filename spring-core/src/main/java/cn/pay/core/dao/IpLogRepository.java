package cn.pay.core.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.IpLog;

/**
 * 登录日志持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
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
