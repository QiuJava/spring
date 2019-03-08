package cn.qj.key.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.qj.key.dao.LoginUserDao;
import cn.qj.key.entity.LoginUser;
import cn.qj.key.util.DateTimeUtil;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Component
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private LoginUserDao loginUserDao;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 查询数据库中是否有用户名不为空的用户
		long count = loginUserDao.countByUsernameNotNull();
		if (count > 0) {
			return;
		}
		Date curTime = DateTimeUtil.getDate();
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("独孤求败");
		loginUser.setPassword("123456");
		loginUser.setPhoneNum("17328375167");
		loginUser.setRegisterSource(LoginUser.WEB_SOURCE);
		loginUser.setUpdateTime(curTime);
		loginUser.setCreateTime(curTime);
		loginUser.setUserStatus(LoginUser.NORMAL_STATUS);
		loginUser.setUserType(LoginUser.MANAGE_TYPE);
		loginUserDao.save(loginUser);
	}

}
