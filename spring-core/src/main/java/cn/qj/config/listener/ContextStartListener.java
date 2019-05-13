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
import cn.qj.entity.Permission;
import cn.qj.entity.Dict;
import cn.qj.entity.LoginUser;
import cn.qj.service.PermissionService;
import cn.qj.service.DictService;
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

	public static final String DICT = "DICT";
	public static final String PERMISSION = "PERMISSION";
	public static final String ADMIN = "admin";

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private DictService dictService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private LoginUserServiceImpl loginUserService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<Dict> dicts = dictService.getAll();
		for (Dict dict : dicts) {
			hashOperations.put(DICT, dict.getCode(), dict);
		}
		List<Permission> permissions = permissionService.getAll();
		valueOperations.set(PERMISSION, permissions);

		// 创建超级管理员
		LoginUser loginUser = loginUserService.getByUsername(ADMIN);
		if (loginUser == null) {
			loginUser = new LoginUser();
			loginUser.setUsername(ADMIN);
			loginUser.setPassword(AuthenticationProviderImpl.B_CRYPT.encode("123"));
			loginUser.setUserStatus(LoginUser.NORMAL);
			loginUser.setPasswordExpiration(DateUtils.addMonths(new Date(), 6));
			loginUser.setAccountExpiration(DateUtils.addMonths(new Date(), 6));
			loginUser.setCreateTime(new Date());
			loginUser.setUpdateTime(new Date());
			loginUserService.save(loginUser);
		}
	}

}
