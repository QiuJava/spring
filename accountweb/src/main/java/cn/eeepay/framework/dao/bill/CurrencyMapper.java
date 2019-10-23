package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.ShiroUser;


/**
 * 币种表
 * @author Administrator
 *
 */

public interface CurrencyMapper {
	
	@Select( "select * from currency_info ")
	@ResultMap("cn.eeepay.framework.dao.bill.CurrencyMapper.BaseResultMap")
	List<Currency> findCurrency();
	
	@SelectProvider( type=SqlProvider.class,method="findByParams")
	@ResultMap("cn.eeepay.framework.dao.bill.CurrencyMapper.BaseResultMap")
	List<Currency> findByParams(@Param("c")Currency c);
	
	@Select("select * from currency_info where currency_name =#{currencyName}")
	@ResultMap("cn.eeepay.framework.dao.bill.CurrencyMapper.BaseResultMap")
	Currency findCurrencyNoByName(@Param("currencyName")String currencyName);
	
	public class SqlProvider{
		public String findByParams(final Map<String, Object> parameter) {
			final Currency c = (Currency) parameter.get("c");
			return new SQL(){{
				SELECT("currency_no,"
						+ "currency_name ");
				FROM("currency_info");
				if (!StringUtils.isBlank(c.getCurrencyNo()))
					WHERE(" currency_no like  \"%\"#{c.currencyNo}\"%\" or currency_name like  \"%\"#{c.currencyNo}\"%\" ");
			}}.toString();
		}
	}
}
