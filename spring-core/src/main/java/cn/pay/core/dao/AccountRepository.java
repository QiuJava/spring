package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
