package cn.eeepay.framework.service.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.model.bill.ShadowAccount;


public interface ShadowAccountService {
	
	ShadowAccount getShadowAccount(String accountNo, String accountFlag, String transDate) throws Exception;
	
	int updateShadowAccount(ShadowAccount shadowAccount) throws Exception;
	
	int insertShadowAccount(ShadowAccount shadowAccount) throws Exception;
	
	List<ShadowAccount> findShadowAccountByAccountFlag(String accountFlag) throws Exception;
}
