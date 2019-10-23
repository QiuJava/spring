package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.CoreTransInfo;


public interface CoreTransInfoService {
	int insertTransInfo(CoreTransInfo transInfo)  throws Exception;
//	int updateTransInfo(TransInfo transInfo)  throws Exception;
	List<CoreTransInfo> findAllTransInfo(CoreTransInfo transInfo,Map<String, String> params,Sort sort,Page<CoreTransInfo> page) throws Exception;
	CoreTransInfo findSubjectAllTransAmount(String parentSubjectNo,String debitCreditSide,Date transDate);
}
