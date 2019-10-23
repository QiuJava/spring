package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.FunctionManagerDao;
import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.service.FunctionManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("functionManagerService")
public class FunctionManagerServiceImpl implements FunctionManagerService {

	@Resource
	private FunctionManagerDao functionManagerDao;

	@Override
	public FunctionManager getFunctionManagerByNum(String funcNum) {
		return functionManagerDao.getFunctionManagerByNum(funcNum);
	}

}
