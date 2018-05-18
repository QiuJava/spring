package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

}
