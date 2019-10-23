package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.ExtAccountHistoryBalance;
import cn.eeepay.framework.model.bill.InsAccountHistoryBalance;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.model.bill.TransImportInfo;


public interface ReportService {	
	
	List<Map<String, Object>> findSubjectDayAmountList(SubjectInfo subjectInfo,Map<String, String> params,Sort sort,Page<Map<String, Object>> page) throws Exception;
	List<Map<String, Object>> exportSubjectDayAmountList(SubjectInfo subjectInfo,Map<String, String> params) throws Exception;
	List<Map<String, Object>> findInAccountHistoryAmount(InsAccountHistoryBalance iaHistoryBalance,Map<String, String> params,Sort sort,Page<Map<String, Object>> page) throws Exception;
	List<Map<String, Object>> exportInAccountHistoryAmount(InsAccountHistoryBalance iaHistoryBalance,Map<String, String> params) throws Exception;
	List<Map<String, Object>> findExtAccountHistoryAmount(ExtAccountHistoryBalance eaHistoryBalance,Map<String, String> params,Sort sort,Page<Map<String, Object>> page,String userNoStrs) throws Exception;
	List<Map<String, Object>> exportExtAccountHistoryAmount(ExtAccountHistoryBalance eaHistoryBalance,Map<String, String> params, String userNoStrs) throws Exception;
	List<TransImportInfo> findTransFlow(TransImportInfo transImportInfo,Map<String, String> params,Sort sort,Page<TransImportInfo> page) throws Exception;
	List<TransImportInfo> exportTransFlow(TransImportInfo transImportInfo,Map<String, String> params) throws Exception;
	List<CoreTransInfo> findRecordFlow(CoreTransInfo coreTransInfo,Map<String, String> params,Sort sort,Page<CoreTransInfo> page) throws Exception;
	List<CoreTransInfo> exportRecordFlow(CoreTransInfo coreTransInfo,Map<String, String> params) throws Exception;
	
	TransImportInfo getById(Integer id);
}
