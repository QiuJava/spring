package cn.pay.core.service;

import java.math.BigDecimal;
import java.util.List;

import cn.pay.core.domain.business.Borrow;
import cn.pay.core.domain.business.BorrowAuditHistroy;
import cn.pay.core.obj.qo.BorrowQo;
import cn.pay.core.obj.vo.PageResult;

/**
 * 借款
 * 
 * @author Administrator
 *
 */
public interface BorrowService {

	/**
	 * 判断是否申请借款
	 * 
	 * @return
	 */
	boolean isApplyBorrow();

	/**
	 * 申请借款
	 * 
	 * @param borrow
	 */
	void apply(Borrow borrow);

	/**
	 * 投标
	 * 
	 * @param borrowId
	 * @param amount
	 */
	void bid(Long borrowId, BigDecimal amount);

	/**
	 * 查询借款列表
	 * 
	 * @param qo
	 * @return
	 */
	PageResult list(BorrowQo qo);

	/**
	 * 发标前审核
	 * 
	 * @param id
	 * @param state
	 * @param remark
	 */
	void publishAudit(Long id, int state, String remark);

	/**
	 * 满标一审
	 * 
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit1Audit(Long id, String remark, int state);

	/**
	 * 满标2审
	 * 
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit2Audit(Long id, String remark, int state);

	/**
	 * 获取借款
	 * 
	 * @param id
	 * @return
	 */
	Borrow get(Long id);

	/**
	 * 获取这个借款的审核历史
	 * 
	 * @param id
	 * @return
	 */
	List<BorrowAuditHistroy> getAuthHistroys(Long id);

	/**
	 * 保存或者更新 并进行乐观锁检查
	 * 
	 * @param borrow
	 */
	void update(Borrow borrow);

	List<Borrow> listByState(int... state);
}
