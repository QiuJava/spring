package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;
import cn.eeepay.framework.model.bill.Subject;

public interface SubjectService {
		/**
		 * 
		 * @param subject
		 * @return
		 * @throws Exception
		 */
	    Map<String,Object> insertSubject(Subject subject)  throws Exception;
		/**
		 * 
		 * @return
		 * @throws Exception
		 */
		List<Subject> findSubjectListInfo(Subject subject,Sort sort,Page<Subject> page) throws Exception;
		
		/**
		 * 获取最低级别号(也就是 最大subject_level)
		 * @return
		 * @throws Exception
		 */
		int findMaxSubjectLevel() throws Exception;
		
		/**
		 * 
		 * @param subjectLevel
		 * @return
		 * @throws Exception
		 */
		List<Subject> findSubjectListBySubjectLevel(Integer subjectLevel) throws Exception;
		
		/**
		 * 
		 * @param subject
		 * @return
		 * @throws Exception
		 */
		List<Subject> findSelectSubject(Subject subject) throws Exception;
		/**
		 * 
		 * @param subjectNo
		 * @return
		 * @throws Exception
		 */
		Subject getSubject(String subjectNo) throws Exception;
		/**
		 * 
		 * @param ParentNo
		 * @return
		 * @throws Exception
		 */
		List<Subject> findChildrenSubjectListByParentSubjectNo(String ParentNo) throws Exception;
		
		/**
		 * 
		 * @param parentSubjectNo
		 * @return
		 * @throws Exception
		 */
		List<Subject> getChildSubjectList(String parentSubjectNo) throws Exception;
		/**
		 * @param subjectNo 科目编号
		 * @param subjectType 类别  内部科目编号：inner, 法定编号：legal
		 * @return
		 */
		Subject exsitsSubject(String subjectNo, String subjectType);
		
		
		Map<String,Object> updateSubject(Subject subject)  throws Exception;
		
		
		int deleteSubjectById(Integer id)  throws Exception;
		
		
		Subject getSubjectById(Integer id)  throws Exception;
		
		
		Subject getSubjectAndParentSubject(String subjectNo)  throws Exception;
		

		List<Subject> findSubjectList() throws Exception;
		
		
		Subject findSubjectAliasExist(String subjectAlias);

}
