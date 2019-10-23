package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;

import cn.eeepay.framework.model.bill.ExtAccount;

public interface ExtAccountHistoryBalanceService {
	/**
	 * 从外部账账户表备份数据到外部账历史余额表
	 * @param list: 外部账账户数据集合
	 * @return
	 */
	public int insertBatch(List<ExtAccount> list);
	
	/**
	 * 从外部账账户表备份数据到外部账历史余额表
	 * @return
	 */
	public int insertInto(Date transDate);
}
