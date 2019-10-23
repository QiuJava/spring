package cn.eeepay.framework.service.bill;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AdjustAccount;
import cn.eeepay.framework.model.bill.AdjustDetail;


public interface AdjustAccountService {

	String insertAdjust(AdjustAccount adjustAccount, File file)throws Exception;
	
	String updateAdjust(AdjustAccount adjustAccount, File file)throws Exception;
	
	AdjustAccount getAdjustAccount(Integer id) throws Exception;

	List<AdjustAccount> findAdjustAccountApprove(AdjustAccount adjustAccount,Map<String, String> params,Sort sort,Page<AdjustAccount> page) throws Exception;
	
	List<AdjustAccount> findAdjustAccount(AdjustAccount adjustAccount,Map<String, String> params,Sort sort,Page<AdjustAccount> page) throws Exception;
	
	List<AdjustDetail> findAdjustDetail(AdjustDetail adjustDetail,Map<String, String> params,Sort sort,Page<AdjustDetail> page) throws Exception;

	
//	AdjustAccount getAdjustAccount(int ID) throws Exception;
	
	Map<String,Object> updateadjustExamine(AdjustAccount adjustAccount)  throws Exception;
	int updateadjustExamineDate(AdjustAccount adjustAccount)  throws Exception;

	List<AdjustDetail> findAdjustDetailByAdjustId(AdjustAccount adjustAccount) throws Exception;

	String updateAdjustAccount(AdjustAccount adjustAccount)throws Exception;
	
}
