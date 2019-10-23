package cn.eeepay.framework.util;

import java.util.List;

import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.model.bill.SysDict;

public class ExportFormat {
	
	public String formatSysDict(String objVal,List<SysDict> List){
		for(SysDict obj:List){
			if(obj.getSysValue().equals(objVal)){
				return obj.getSysName() ;
			}
		}
		return "" ;
	}
	
	public String formatCurrency(String objVal,List<Currency> List){
		for(Currency obj:List){
			if(obj.getCurrencyNo().equals(objVal)){
				return obj.getCurrencyName() ;
			}
		}
		return "" ;
	}
	
	public String formatTransType(String objVal,List<RecordAccountRuleTransType> List){
		for(RecordAccountRuleTransType obj:List){
			if(obj.getTransTypeCode().equals(objVal)){
				return obj.getTransTypeName() ;
			}
		}
		return "" ;
	}

	public String formatNpsopSysDict(String transGroupDetail, List<cn.eeepay.framework.model.nposp.SysDict> transGroupList) {
		for(cn.eeepay.framework.model.nposp.SysDict obj:transGroupList){
			if(obj.getSysKey().equals(transGroupDetail)){
				return obj.getRemark() ;
			}
		}
		return "" ;
	}
	
}
