package cn.qj.core.service;

import java.math.BigDecimal;
import java.util.List;

import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.BorrowAuditHistroy;
import cn.qj.core.pojo.qo.BorrowQo;
import cn.qj.core.pojo.vo.PageResult;

/**
 * 借款服务
 * 
 * @author Administrator
 *
 */
public interface BorrowService {

	/**
	 * 判断是否有申请借款
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
	 * @param loginInfoId
	 */
	void bid(Long borrowId, BigDecimal amount, Long loginInfoId);

	/**
	 * 根据借款查询条件查询借款页面结果集
	 * 
	 * @param qo
	 * @return
	 */
	PageResult list(BorrowQo qo);

	/**
	 * 借款发布之前审核
	 * 
	 * @param id
	 * @param state
	 * @param remark
	 */
	void publishAudit(Long id, Integer state, String remark);

	/**
	 * 借款满标一审
	 * 
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit1Audit(Long id, String remark, Integer state);

	/**
	 * 借款满标二审
	 * 
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit2Audit(Long id, String remark, Integer state);

	/**
	 * 根据借款ID获取借款
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

	/**
	 * 根据借款的一些状态获取借款列表
	 * 
	 * @param state
	 * @return
	 */
	List<Borrow> listByState(int... state);

	/**
	 * 获取一个小时内即将要流标的借款并放到Redis中
	 */
	void getFailBorrow();

	/**
	 * 每隔一秒执行流标
	 */
	void failBorrow();

}
