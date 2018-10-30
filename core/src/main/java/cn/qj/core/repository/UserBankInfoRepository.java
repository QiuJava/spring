package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.UserBankInfo;

/**
 * 用户银行卡信息持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */

public interface UserBankInfoRepository extends JpaRepository<UserBankInfo, Long> {

	/**
	 * 查询这个用户绑定银行卡信息
	 * 
	 * @param id
	 * @return
	 */
	UserBankInfo findByLoginInfoId(Long id);

}
