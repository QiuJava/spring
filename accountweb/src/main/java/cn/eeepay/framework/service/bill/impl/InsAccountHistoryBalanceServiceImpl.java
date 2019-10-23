package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.InsAccountHistoryBalanceMapper;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.service.bill.InsAccountHistoryBalanceService;
import cn.eeepay.framework.util.DateUtil;

@Service
public class InsAccountHistoryBalanceServiceImpl implements InsAccountHistoryBalanceService{
	@Autowired
	private InsAccountHistoryBalanceMapper insAccountHistoryBalanceMapper;

	@Override
	public int insertBatch(List<InsAccount> list) {
		String billDate = DateUtil.getCurrentDate();
		//先删除当前时间的数据
		insAccountHistoryBalanceMapper.deleteByBillDate(billDate);
		return insAccountHistoryBalanceMapper.insertBatch(list);
	}

	@Override
	public int insertInto(Date transDate) {
		String billDate = DateUtil.getFormatDate("yyyy-MM-dd", transDate);
		//先删除当前时间的数据
		insAccountHistoryBalanceMapper.deleteByBillDate(billDate);
		return insAccountHistoryBalanceMapper.insertInto(billDate);
	}

}
