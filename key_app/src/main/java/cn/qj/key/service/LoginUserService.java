package cn.qj.key.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.key.dao.LoginUserDao;

/**
 * 用户登录服务
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Service
public class LoginUserService {

	@Autowired
	private LoginUserDao loginUserDao;

	public boolean isExistUsername(String username) {
		long count = loginUserDao.countByUsername(username);
		return count > 0;
	}

}
