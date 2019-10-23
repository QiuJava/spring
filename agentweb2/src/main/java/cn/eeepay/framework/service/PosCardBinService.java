package cn.eeepay.framework.service;



import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.PosCardBin;

public interface PosCardBinService {
	PosCardBin queryInfo(String accountNo);
	String queryBankNo(String accountNo);
	List<PosCardBin> queryAllInfo(String accountNo);

	List<String> queryAcpBearBankNameByType(Integer type);

	Map queryAcpBearBankByBankName(String bankName, Integer type);
}
