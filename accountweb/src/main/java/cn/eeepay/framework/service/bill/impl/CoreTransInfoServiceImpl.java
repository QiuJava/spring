package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.CoreTransInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.service.bill.CoreTransInfoService;
@Service("coreTransInfoService")
@Transactional
public class CoreTransInfoServiceImpl  implements CoreTransInfoService{
	
	private static final Logger log = LoggerFactory.getLogger(CoreTransInfoServiceImpl.class);
	
	@Resource
	public CoreTransInfoMapper  coreTransInfoMapper;
	
	@Override
	public int insertTransInfo(CoreTransInfo transInfo) throws Exception {
		return coreTransInfoMapper.insertTransInfo(transInfo);
	}
//	@Override
//	public int updateTransInfo(TransInfo transInfo) throws Exception {
//		return transInfoDao.updateTransInfo(transInfo);
//	}
	@Override
	public List<CoreTransInfo> findAllTransInfo(CoreTransInfo transInfo,Map<String, String> params, Sort sort, Page<CoreTransInfo> page) throws Exception {
		return coreTransInfoMapper.findAllTransInfo(transInfo,params, sort, page);
	}
	@Override
	public CoreTransInfo findSubjectAllTransAmount(String parentSubjectNo, String debitCreditSide,Date transDate) {
		return coreTransInfoMapper.findSubjectAllTransAmount(parentSubjectNo, debitCreditSide,transDate);
	}
}
