package cn.eeepay.framework.service.bill.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ExtAccountMapper;
import cn.eeepay.framework.dao.bill.ExtAccountOpRecordMapper;
import cn.eeepay.framework.model.bill.ExtAccountOpRecord;
import cn.eeepay.framework.service.bill.CommonService;
import cn.eeepay.framework.service.bill.CoreTransInfoService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.ExtAccountOpRecordService;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SystemInfoService;
@Service("extAccountOpRecordService")
@Transactional
public class ExtAccountOpRecordServiceImpl  implements ExtAccountOpRecordService{
	private static final Logger log = LoggerFactory.getLogger(ExtAccountOpRecordServiceImpl.class);
	@Resource
	public ExtAccountMapper outAccountMapper;
	@Resource
	public ShadowAccountService shadowAccountService;
	@Resource
	public SystemInfoService systemInfoService;
	
	@Resource
	public CurrencyService  currencyService;
	@Resource
	public SubjectService  subjectService;
	@Resource
	public CoreTransInfoService  coreTransInfoService;
	@Resource
	public OrgInfoService orgInfoService;
	@Resource
	public  GenericTableService  genericTableService;
	@Resource
	public CommonService commonService;
	@Resource
	public ExtAccountOpRecordService extAccountOpRecordService;
	@Resource
	public ExtAccountOpRecordMapper opRecordDao ;
	
	@Override
	public int insertExtAccountOpRecord(ExtAccountOpRecord extAccountOpRecord) throws Exception {
		return opRecordDao.insertExtAccountOpRecord(extAccountOpRecord);
	}
	@Override
	public int updateExtAccountOpRecord(ExtAccountOpRecord extAccountOpRecord) throws Exception {
		return opRecordDao.updateExtAccountOpRecord(extAccountOpRecord);
	}
	
}
