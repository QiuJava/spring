package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.business.BorrowAuditHistroy;

/**
 * 借款审核历史
 * 
 * @author Administrator
 *
 */
public interface BorrowAuditHistroyService {

	void saveAndUpdate(BorrowAuditHistroy borrowAuditHistroy);

	/**
	 * 获取借款的审核历史
	 * 
	 * @param borrowId
	 * @return
	 */
	List<BorrowAuditHistroy> getByBorrowId(Long borrowId);
	
}
