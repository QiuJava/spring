package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideTransInfo;
import cn.eeepay.framework.model.bill.UserInfo;


public interface InsAccountService {
	boolean recordInAccountApi(Map<String, String> params) throws Exception;
	int insertInputAccount(InsAccount inputAccount)  throws Exception;
	int updateInsAccount(InsAccount inputAccount)  throws Exception;
	InsAccount exsitsInputAccount(String currencyNo, String subjectNo,String org_no)  throws Exception;
	InsAccount getInputAccountByAccountNo(String accountNo)  throws Exception;
	List<InsAccount> findInputAccountByDayBalFlag(String dayBalFlag)  throws Exception;
	InsAccount getInputAccountByParams(String orgNo,String subjectNo,String currencyNo)  throws Exception;
	Map<String, Object> createInsAccount(String orgNo,UserInfo userInfo) throws Exception;
	List<InsAccount> findInsAccountListInfo(InsAccount inputAccount,Sort sort,Page<InsAccount> page) throws Exception;	
	List<InsideTransInfo> findInsideTransList(InsideTransInfo insideTransInfo,Map<String, String> params,Sort sort,Page<InsideTransInfo> page) throws Exception;
	List<InsAccount> findInputAccountBySubjectNo(String subjectNo);

	boolean createInputAccount(Map<String, String> params)  throws Exception;
	List<InsideTransInfo> findExportInsideTransList(InsideTransInfo insideTransInfo, Map<String, String> params);
	List<InsAccount> findExportInsAccountList(InsAccount insAccount);
	
	/**
	 * 查询所有内部账账户信息
	 * @return
	 */
	List<InsAccount> findAll();
	
}
