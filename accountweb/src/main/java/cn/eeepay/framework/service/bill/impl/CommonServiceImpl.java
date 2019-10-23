package cn.eeepay.framework.service.bill.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.service.bill.CommonService;
import cn.eeepay.framework.service.bill.GenericTableService;
@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	@Resource
	public  GenericTableService  genericTableService;
	
	private static final Logger log = LoggerFactory.getLogger(InsAccountServiceImpl.class);
	@Override
	public String getInsAccountNo(String orgNo,String subjectNo) throws Exception {
		StringBuffer sb = new StringBuffer(orgNo);										
		String serialNum = 	genericTableService.createKey();
		log.info(serialNum);			
		sb.append(subjectNo);
		sb.append(serialNum);
		return sb.toString();
	}
	@Override
	public String getExtAccountNo(String orgNo, String subjectNo) throws Exception {
		StringBuffer sb = new StringBuffer(orgNo);										
		String serialNum = 	genericTableService.createExtKey();
		log.info(serialNum);			
		sb.append(subjectNo);
		sb.append(serialNum);
		return sb.toString();
	}

}
