package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SubjectInfo;

public interface SubjectInfoService {
	int insertSubjectInfo(SubjectInfo subjectInfo);
	int updateSubjectInfo(SubjectInfo subjectInfo);
	SubjectInfo findSubjectInfoByParams(String subjectNo,String orgNo,String currencyNo,Date createDate);
	List<SubjectInfo> findSubjectInfoByDate(Date createDate);
}
