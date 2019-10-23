package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.UserFeedbackProblemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ProblemType;
import cn.eeepay.framework.model.UserFeedbackProblem;
import cn.eeepay.framework.service.UserFeedbackProblemService;

@Service("userFeedbackProblemService")
@Transactional
public class UserFeedbackProblemServiceImpl implements UserFeedbackProblemService{

	@Resource
	private UserFeedbackProblemDao userFeedbackProblemDao;

	@Override
	public List<UserFeedbackProblem> selectAllInfo(Page<UserFeedbackProblem> page, UserFeedbackProblem ufp) {
		return userFeedbackProblemDao.selectAllInfo(page, ufp);
	}

	@Override
	public UserFeedbackProblem selectDetailById(int id) {
		return userFeedbackProblemDao.selectDetailById(id);
	}

	@Override
	public List<ProblemType> selectAllProblemInfo() {
		return userFeedbackProblemDao.selectAllProblemInfo();
	}
	
	
}
