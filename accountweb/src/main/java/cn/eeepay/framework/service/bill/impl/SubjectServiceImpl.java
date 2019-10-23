package cn.eeepay.framework.service.bill.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SubjectMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.service.bill.SubjectService;

@Service("subjectService")
@Transactional
public class SubjectServiceImpl implements SubjectService{
	private static final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);
	@Resource
	public SubjectMapper subjectMapper;
	
	@Override
	public Map<String,Object> insertSubject(Subject subject) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		
		subject.setAddBalanceFrom(subject.getBalanceFrom());
		//subject.setInnerDayBalFlag(InnerDayBalFlag.RIJIAN.toString()); //页面上已加此选项
		//subject.setInnerSumFlag(InnerSumFlag.RIJIANDANBI.toString()); //页面上已加此选项
		//查询科目编号是否存在
		Subject subjectQ = subjectMapper.findSubjectNoExist(subject.getSubjectNo()) ;
		if(subjectQ != null){
			msg.put("state",false);
			msg.put("msg", "新增失败！科目编号已存在!");
			return msg ;
		}
		subject.setSubjectAlias(subject.getSubjectAlias().trim());
		subjectMapper.insertSubject(subject) ;
		msg.put("state",true);
		msg.put("msg", "保存成功!");
		return msg;
		
	}

	@Override
	public List<Subject> findSubjectListInfo(Subject subject,Sort sort,Page<Subject> page) throws Exception {
		return subjectMapper.findSubjectListInfo(subject,sort,page);
	}

	@Override
	public Subject getSubject(String subjectNo) throws Exception {
		return subjectMapper.getSubject(subjectNo);
				
	}


	@Override
	public Subject exsitsSubject(String subjectNo, String subjectType) {
		return subjectMapper.exsitsSubject(subjectNo, subjectType);
	}

	@Override
	public List<Subject> findSelectSubject(Subject subject) throws Exception {
		return subjectMapper.findSelectSubject(subject);
	}

	@Override
	public Map<String,Object> updateSubject(Subject subject) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try{
			subjectMapper.updateSubject(subject);
		}catch(Exception e){
			log.error("异常:",e);
			msg.put("state",false);
			msg.put("msg", "修改异常，失败!");
			return msg ;
		}
		msg.put("state",true);
		msg.put("msg", "修改成功!");
		return msg ;
	}

	@Override
	public List<Subject> findSubjectList() throws Exception {
		return subjectMapper.findAllSubjectList();
	}

	@Override
	public List<Subject> findChildrenSubjectListByParentSubjectNo(String parentSubjectNo) throws Exception {
		return subjectMapper.findChildrenSubjectListByParentSubjectNo(parentSubjectNo);
	}

	@Override
	public int findMaxSubjectLevel() throws Exception {
		return subjectMapper.findMaxSubjectLevel();
	}

	@Override
	public List<Subject> findSubjectListBySubjectLevel(Integer subjectLevel) throws Exception {
		return subjectMapper.findSubjectListBySubjectLevel(subjectLevel);
	}

	@Override
	public List<Subject> getChildSubjectList(String parentSubjectNo) throws Exception {
		return subjectMapper.getChildSubjectList(parentSubjectNo);
	}

	@Override
	public int deleteSubjectById(Integer id) throws Exception {
		return subjectMapper.deleteSubjectById(id);
	}

	@Override
	public Subject getSubjectById(Integer id) throws Exception {
		return subjectMapper.getSubjectById(id);
	}

	@Override
	public Subject getSubjectAndParentSubject(String subjectNo) throws Exception {
		return subjectMapper.getSubjectAndParentSubject(subjectNo);
	}
	@Override
	public Subject findSubjectAliasExist(String subjectAlias) {
		return subjectMapper.findSubjectAliasExist(subjectAlias);
	}
}
