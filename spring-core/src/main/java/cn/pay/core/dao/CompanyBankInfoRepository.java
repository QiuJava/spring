package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.CompanyBankInfo;

public interface CompanyBankInfoRepository extends JpaRepository<CompanyBankInfo, Long> {

}
