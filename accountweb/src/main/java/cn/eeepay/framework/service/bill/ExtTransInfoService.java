package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.Date;

import cn.eeepay.framework.model.bill.ExtTransInfo;

public interface ExtTransInfoService {
	int insertExtTransInfo(ExtTransInfo extTransInfo) throws Exception;
	int updateExtTransInfo(ExtTransInfo extTransInfo) throws Exception;
	
	BigDecimal countTransMoney(String accountNo, Date date);
}
