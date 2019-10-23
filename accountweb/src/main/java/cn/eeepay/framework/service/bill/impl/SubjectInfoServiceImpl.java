package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SubjectInfoMapper;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.service.bill.SubjectInfoService;



@Service("subjectInfoService")
@Transactional
public class SubjectInfoServiceImpl implements SubjectInfoService{
	
	@Resource
	public SubjectInfoMapper subjectInfoMapper;

	@Override
	public int insertSubjectInfo(SubjectInfo subjectInfo) {
		return subjectInfoMapper.insertSubjectInfo(subjectInfo);
	}

	@Override
	public int updateSubjectInfo(SubjectInfo subjectInfo) {
		return subjectInfoMapper.updateSubjectInfo(subjectInfo);
	}

	@Override
	public SubjectInfo findSubjectInfoByParams(String subjectNo, String orgNo, String currencyNo,
			Date createDate) {
		return subjectInfoMapper.findSubjectInfoByParams(subjectNo, orgNo, currencyNo, createDate);
	}

	@Override
	public List<SubjectInfo> findSubjectInfoByDate(Date createDate) {
		return subjectInfoMapper.findSubjectInfoByDate(createDate);
	}
	
	
}
