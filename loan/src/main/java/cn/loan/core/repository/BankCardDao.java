package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.BankCard;

/**
 * 银行卡数据操作
 * 
 * @author qiujian
 *
 */
public interface BankCardDao extends JpaRepository<BankCard, Long> {

	/**
	 * 根据登录用户ID查找银行卡
	 * 
	 * @param id
	 * @return
	 */
	BankCard findByLoginUserId(Long id);

}
