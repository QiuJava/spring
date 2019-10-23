package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ExtUserTypeSubjectMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;
import cn.eeepay.framework.service.bill.ExtUserTypeSubjectService;



@Service("extUserTypeSubjectService")
@Transactional
public class ExtUserTypeSubjectServiceImpl implements ExtUserTypeSubjectService{
	
	@Resource
	public ExtUserTypeSubjectMapper extUserTypeSubjectMapper;

	@Override
	public int insertExtUserTypeSubject(ExtUserTypeSubject extUserTypeSubject) throws Exception {
		extUserTypeSubject.setCreateTime(new Date());
		return extUserTypeSubjectMapper.insertExtUserTypeSubject(extUserTypeSubject);
	}

	@Override
	public ExtUserTypeSubject existExtUserTypeSubject(String userTypeValue,String subjectNo) {
		return extUserTypeSubjectMapper.existExtUserTypeSubject(userTypeValue,subjectNo);
	}

	@Override
	public List<ExtUserTypeSubject> findUserTypeSubjectList(ExtUserTypeSubject subjectExt, Sort sort, Page<ExtUserTypeSubject> page) throws Exception {
		return extUserTypeSubjectMapper.findUserTypeSubjectList(subjectExt,sort,page);
	}

	@Override
	public int deleteUserTypeSubject(String id) throws Exception {
		return extUserTypeSubjectMapper.deleteUserTypeSubject(id);
	}
	@Override
	public List<ExtUserTypeSubject> findExtUserTypeSubjectByUserType(String userType) throws Exception {
		return extUserTypeSubjectMapper.findExtUserTypeSubjectByUserType(userType);
	}
}
