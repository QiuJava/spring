package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.UserInfo;

/**
 * 用户信息数据操作
 * 
 * @author qiujian
 *
 */
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {

}
