package cn.eeepay.framework.service.bill.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ReportMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.ExtAccountHistoryBalance;
import cn.eeepay.framework.model.bill.InsAccountHistoryBalance;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.model.bill.TransImportInfo;
import cn.eeepay.framework.service.bill.ReportService;
@Service("reportService")
@Transactional
public class ReportServiceImpl  implements ReportService{
	private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);
	@Resource
	public ReportMapper reportMapper ;
	
	
	
	@Override
	public List<Map<String, Object>> findSubjectDayAmountList(SubjectInfo subjectInfo, Map<String, String> params, Sort sort,
			Page<Map<String, Object>> page) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.findSubjectDayAmountList(subjectInfo, sort, page, params);
	}

	//导出 科目每日余额列表
	@Override
	public List<Map<String, Object>> exportSubjectDayAmountList(SubjectInfo subjectInfo, Map<String, String> params)
			throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.exportSubjectDayAmount(subjectInfo, params);
	}

	@Override
	public List<Map<String, Object>> findInAccountHistoryAmount(InsAccountHistoryBalance iaHistoryBalance,
			Map<String, String> params, Sort sort, Page<Map<String, Object>> page) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.findInAccountHistoryAmount(iaHistoryBalance, sort, page, params);
	}

	//导出  内部账户历史余额列表
	@Override
	public List<Map<String, Object>> exportInAccountHistoryAmount(InsAccountHistoryBalance iaHistoryBalance,
			Map<String, String> params) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.exportInAccountHistoryAmount(iaHistoryBalance, params);
	}

	@Override
	public List<Map<String, Object>> findExtAccountHistoryAmount(ExtAccountHistoryBalance eaHistoryBalance,
			Map<String, String> params, Sort sort, Page<Map<String, Object>> page, String userNoStrs) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.findExtAccountHistoryAmount(eaHistoryBalance, sort, page, params,userNoStrs);
	}

	@Override
	public List<Map<String, Object>> exportExtAccountHistoryAmount(ExtAccountHistoryBalance eaHistoryBalance,
			Map<String, String> params, String userNoStrs) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.exportExtAccountHistoryAmount(eaHistoryBalance, params, userNoStrs);
	}

	@Override
	public List<TransImportInfo> findTransFlow(TransImportInfo transImportInfo, Map<String, String> params, Sort sort,
			Page<TransImportInfo> page) throws Exception {
		if(!"".equals(params.get("recordBeginDate"))){
			params.put("recordBeginDate", params.get("recordBeginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("recordEndDate"))){
			params.put("recordEndDate", params.get("recordEndDate") + " 23:59:59") ;
		}
		if(!"".equals(params.get("transBeginDate"))){
			params.put("transBeginDate", params.get("transBeginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("transEndDate"))){
			params.put("transEndDate", params.get("transEndDate") + " 23:59:59") ;
		}
		return reportMapper.findTransFlow(transImportInfo, sort, page, params);
	}

	@Override
	public List<TransImportInfo> exportTransFlow(TransImportInfo transImportInfo, Map<String, String> params)
			throws Exception {
		if(!"".equals(params.get("recordBeginDate"))){
			params.put("recordBeginDate", params.get("recordBeginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("recordEndDate"))){
			params.put("recordEndDate", params.get("recordEndDate") + " 23:59:59") ;
		}
		if(!"".equals(params.get("transBeginDate"))){
			params.put("transBeginDate", params.get("transBeginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("transEndDate"))){
			params.put("transEndDate", params.get("transEndDate") + " 23:59:59") ;
		}
		return reportMapper.exportTransFlow(transImportInfo, params);
	}


	@Override
	public List<CoreTransInfo> findRecordFlow(CoreTransInfo coreTransInfo, Map<String, String> params, Sort sort,
			Page<CoreTransInfo> page) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.findRecordFlow(coreTransInfo, sort, page, params);
	}

	@Override
	public List<CoreTransInfo> exportRecordFlow(CoreTransInfo coreTransInfo, Map<String, String> params)
			throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return reportMapper.exportRecordFlow(coreTransInfo, params);
	}

	@Override
	public TransImportInfo getById(Integer id) {
		return reportMapper.getById(id);
	}

	
}
