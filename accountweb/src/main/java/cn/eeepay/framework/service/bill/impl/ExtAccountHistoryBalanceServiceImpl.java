package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.ExtAccountHistoryBalanceMapper;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.service.bill.ExtAccountHistoryBalanceService;
import cn.eeepay.framework.util.DateUtil;

@Service
public class ExtAccountHistoryBalanceServiceImpl implements ExtAccountHistoryBalanceService{
	@Autowired
	private ExtAccountHistoryBalanceMapper extAccountHistoryBalanceMapper;

	@Override
	public int insertBatch(List<ExtAccount> list) {
		String billDate = DateUtil.getCurrentDate();
		//先删除当前时间的数据
		extAccountHistoryBalanceMapper.deleteByBillDate(billDate);
		return extAccountHistoryBalanceMapper.insertBatch(list);
	}

	@Override
	public int insertInto(Date transDate) {
		String billDate = DateUtil.getFormatDate("yyyy-MM-dd", transDate);
		//先删除当前时间的数据
		extAccountHistoryBalanceMapper.deleteByBillDate(billDate);
		return extAccountHistoryBalanceMapper.insertInto(billDate);
	}
}
