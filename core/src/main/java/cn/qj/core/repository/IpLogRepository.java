package cn.qj.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.IpLog;

/**
 * 登录日志持久化相关
 * 
 * @author Qiujian
 * @date 2018/8/10
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
