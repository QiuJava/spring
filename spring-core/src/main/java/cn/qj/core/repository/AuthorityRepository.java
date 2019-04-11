package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.Authority;

/**
 * 权限数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
