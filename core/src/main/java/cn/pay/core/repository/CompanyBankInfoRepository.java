package cn.pay.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.entity.business.CompanyBankInfo;

/**
 * 公司银行账号信息持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface CompanyBankInfoRepository extends JpaRepository<CompanyBankInfo, Long> {

}
