package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.model.bill.Currency;


public interface CurrencyService {
	List<Currency> findCurrency() throws Exception;
	List<Currency> findByParams(Currency c) throws Exception;
	Currency findCurrencyNoByName(String currencyName) throws Exception;
}
