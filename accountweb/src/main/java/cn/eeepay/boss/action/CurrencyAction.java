package cn.eeepay.boss.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.service.bill.CurrencyService;

/**
 * 币种控制管理
 * 
 * by yangle
 * 2016年5月18日16:49:01
 *
 */
@Controller
@RequestMapping("/currency")
public class CurrencyAction {
	@Resource
	public CurrencyService currencyService;
	
	private static final Logger log = LoggerFactory.getLogger(CurrencyAction.class);
	
	@RequestMapping(value = "/queryCurrency.do")
	@ResponseBody
	public List<Map<String, String>> queryCurrency(String q) throws Exception {
		q = URLDecoder.decode(q, "UTF-8");
		Currency c = new Currency();
		c.setCurrencyName(q);
		c.setCurrencyNo(q);
		List<Currency> currencyList = null;
		List<Map<String, String>> maps = new ArrayList<>();
		try {
			currencyList = currencyService.findByParams(c);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		Map<String, String> thenMap = null;
		for (Currency s : currencyList) {
			thenMap = new HashMap<String, String>();
			thenMap.put("id", s.getCurrencyNo());
			thenMap.put("text", s.getCurrencyName());
			maps.add(thenMap);
		}
		return maps;
	}
}
