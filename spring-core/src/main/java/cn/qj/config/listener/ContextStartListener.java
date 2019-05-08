package cn.qj.config.listener;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import cn.qj.config.security.AuthenticationProviderImpl;
import cn.qj.entity.Authority;
import cn.qj.entity.DataDict;
import cn.qj.entity.LoginUser;
import cn.qj.service.AuthorityService;
import cn.qj.service.DataDictService;
import cn.qj.service.LoginUserServiceImpl;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Component
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	public static final String DATA_DICT = "DATA_DICT";
	public static final String AUTHORITY = "AUTHORITY";
	public static final String ADMIN = "admin";

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private DataDictService dataDictService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private LoginUserServiceImpl loginUserService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<DataDict> dicts = dataDictService.getAll();
		for (DataDict dataDict : dicts) {
			hashOperations.put(DATA_DICT, dataDict.getDictKey(), dataDict);
		}
		List<Authority> authorities = authorityService.getAll();
		valueOperations.set(AUTHORITY, authorities);

		// 创建超级管理员
		LoginUser loginUser = loginUserService.getByUsername(ADMIN);
		if (loginUser == null) {
			loginUser = new LoginUser();
			loginUser.setUsername(ADMIN);
			loginUser.setPassword(AuthenticationProviderImpl.B_CRYPT.encode("123"));
			loginUser.setUserStatus(LoginUser.NORMAL);
			loginUser.setPasswordExpiration(DateUtils.addMonths(new Date(), 6));
			loginUser.setCreateTime(new Date());
			loginUserService.save(loginUser);
		}
		// 创建基础菜单
	}

}
