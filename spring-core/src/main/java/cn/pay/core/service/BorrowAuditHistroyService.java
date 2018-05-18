package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.business.BorrowAuditHistroy;

/**
 * 借款审核历史服务
 * 
 * @author Administrator
 *
 */
public interface BorrowAuditHistroyService {

	/**
	 * 借款审核历史保存或更新
	 * 
	 * @param borrowAuditHistroy
	 */
	void saveAndUpdate(BorrowAuditHistroy borrowAuditHistroy);

	/**
	 * 根据借款ID获取借款的审核历史
	 * 
	 * @param borrowId
	 * @return
	 */
	List<BorrowAuditHistroy> getByBorrowId(Long borrowId);

}
