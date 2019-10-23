package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;

import cn.eeepay.framework.model.bill.InsAccount;

public interface InsAccountHistoryBalanceService {
	/**
	 * 从内部账账户表备份数据到内部账户历史余额表
	 * @param list:内部账账户表数据集合
	 * @return
	 */
	public int insertBatch(List<InsAccount> list);
	
	/**
	 * 从内部账账户表备份数据到内部账户历史余额表
	 * @return
	 */
	public int insertInto(Date transDate);
}
