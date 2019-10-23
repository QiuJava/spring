package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;

public interface ExtUserTypeSubjectService {
	/**
	 * @param subjectExt 科目类别实体
	 * @return
	 * @throws Exception
	 */
	int insertExtUserTypeSubject(ExtUserTypeSubject subjectExt)  throws Exception;
	/**
	 * 查询是否存在用户类别科目关联
	 * @param subjectExt
	 * @return
	 */
	ExtUserTypeSubject existExtUserTypeSubject(String userTypeValue,String subjectNo);
	
	/**
	 * 查询用户类型科目关联详细列表
	 * @param page 
	 * @param sort 
	 * @param subjectExt
	 * @return
	 * @throws Exception
	 */
	List<ExtUserTypeSubject> findUserTypeSubjectList(ExtUserTypeSubject subjectExt, Sort sort, Page<ExtUserTypeSubject> page) throws Exception ;
	
	/**
	 * 删除用户类别科目关联
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int deleteUserTypeSubject(String id)  throws Exception;
	
	List<ExtUserTypeSubject> findExtUserTypeSubjectByUserType(String userType)  throws Exception;

}
