package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.UserInfo;

/**
 * 用户信息持久化
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

}
