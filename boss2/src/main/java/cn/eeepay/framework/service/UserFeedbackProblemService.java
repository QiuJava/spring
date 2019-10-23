package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ProblemType;
import cn.eeepay.framework.model.UserFeedbackProblem;

public interface UserFeedbackProblemService {

	List<UserFeedbackProblem> selectAllInfo(Page<UserFeedbackProblem> page,UserFeedbackProblem ufp);
	
	UserFeedbackProblem selectDetailById(int id);
	
	List<ProblemType> selectAllProblemInfo();
}
