package cn.qj.core.service;

import java.util.List;

import cn.qj.core.entity.BorrowAuditHistroy;

/**
 * 借款审核历史服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
