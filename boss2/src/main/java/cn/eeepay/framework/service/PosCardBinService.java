package cn.eeepay.framework.service;



import cn.eeepay.framework.model.PosCardBin;

public interface PosCardBinService {
	PosCardBin queryInfo(String accountNo);
	
	String queryBankNo(String accountNo);

	String getPoscnapsNoByBankName(String bankName);
}
