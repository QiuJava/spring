package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.CompanyBankCard;

/**
 * 公司银行卡
 * 
 * @author qiujian
 *
 */
public interface CompanyBankCardDao extends JpaRepository<CompanyBankCard, Long> {

}
