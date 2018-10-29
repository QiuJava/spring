package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.entity.business.UserInfo;

/**
 * 用户信息持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

}
