package cn.eeepay.framework.service.bill.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ExtAccountMapper;
import cn.eeepay.framework.dao.bill.InsideTransInfoMapper;
import cn.eeepay.framework.model.bill.InsideTransInfo;
import cn.eeepay.framework.service.bill.CommonService;
import cn.eeepay.framework.service.bill.CoreTransInfoService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.InsideTransInfoService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SystemInfoService;
@Service("insideTransInfoService")
@Transactional
public class InsideTransInfoServiceImpl  implements InsideTransInfoService{
	@Resource
	public ExtAccountMapper outAccountMapper;
	@Resource
	public ShadowAccountService shadowAccountService;
	@Resource
	public SystemInfoService systemInfoService;
	private static final Logger log = LoggerFactory.getLogger(InsideTransInfoServiceImpl.class);
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
	public InsideTransInfoMapper insideTransInfoMapper;
	@Override
	public int insertInsideTransInfo(InsideTransInfo insideTransInfo) throws Exception {
		return insideTransInfoMapper.insertInsideTransInfo(insideTransInfo);
	}
	@Override
	public int updateInsideTransInfo(InsideTransInfo insideTransInfo) throws Exception {
		return insideTransInfoMapper.updateInsideTransInfo(insideTransInfo);
	}
	
}
