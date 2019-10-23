package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.CurrencyMapper;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.service.bill.CurrencyService;

@Service("currencyService")
@Transactional
public class CurrencyServiceImpl implements CurrencyService{

	@Resource
	public CurrencyMapper currencyMapper;
	
	@Override
	public List<Currency> findCurrency( ) throws Exception {
		return currencyMapper.findCurrency();
	}

	@Override
	public List<Currency> findByParams(Currency c) {
		return currencyMapper.findByParams(c);
	}

	@Override
	public Currency findCurrencyNoByName(String currencyName) throws Exception {
		return currencyMapper.findCurrencyNoByName(currencyName);
	}
}
