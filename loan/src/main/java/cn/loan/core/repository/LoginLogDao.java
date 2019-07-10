package cn.loan.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.LoginLog;

/**
 * 登录日志数据操作
 * 
 * @author qiujian
 *
 */
public interface LoginLogDao extends JpaRepository<LoginLog, Long>, JpaSpecificationExecutor<LoginLog> {

	/**
	 * 根据用户名获取最新登录日志
	 * 
	 * @param username
	 * 
	 * @param pageable
	 * @return
	 */
	List<LoginLog> findByUsernameOrderByLoginTimeDesc(String username, Pageable pageable);

}
