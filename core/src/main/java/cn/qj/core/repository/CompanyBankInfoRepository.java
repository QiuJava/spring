package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.CompanyBankInfo;

/**
 * 公司银行账号信息持久化相关
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
public interface CompanyBankInfoRepository extends JpaRepository<CompanyBankInfo, Long> {

}
