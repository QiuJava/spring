package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.sys.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByLoginInfoId(Long id);

}
