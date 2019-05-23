package cn.qj.config.listener;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import cn.qj.config.properties.ConstProperties;
import cn.qj.config.security.AuthenticationProviderImpl;
import cn.qj.entity.Dict;
import cn.qj.entity.LoginUser;
import cn.qj.entity.Permission;
import cn.qj.service.DictService;
import cn.qj.service.LoginUserServiceImpl;
import cn.qj.service.PermissionService;
import cn.qj.util.DateUtil;

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
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private DictService dictService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private LoginUserServiceImpl loginUserService;

	@Autowired
	private ConstProperties constProperties;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		String dictHash = constProperties.getDictHash();
		String permissionHash = constProperties.getPermissionHash();
		hashOperations.delete(dictHash, hashOperations.keys(dictHash).toArray());
		hashOperations.delete(permissionHash, hashOperations.keys(permissionHash).toArray());
		
		List<Dict> dicts = dictService.getAll();
		for (Dict dict : dicts) {
			hashOperations.put(constProperties.getDictHash(), dict.getCode(), dict);
		}
		List<Permission> permissions = permissionService.getAll();
		for (Permission permission : permissions) {
			hashOperations.put(constProperties.getPermissionHash(), permission.getUrl(), permission);
		}

		// 创建超级管理员
		String admin = constProperties.getAdmin();
		LoginUser loginUser = loginUserService.getByUsername(admin);
		if (loginUser == null) {
			Date newDate = DateUtil.getNewDate();
			loginUser = new LoginUser();
			String password = constProperties.getPassword();
			loginUser.setUsername(admin);
			loginUser.setPassword(AuthenticationProviderImpl.B_CRYPT.encode(password));
			loginUser.setUserStatus(LoginUser.NORMAL);
			loginUser.setPasswordExpiration(DateUtils.addMonths(newDate, 6));
			loginUser.setAccountExpiration(DateUtils.addMonths(newDate, 6));
			loginUser.setCreateTime(newDate);
			loginUser.setUpdateTime(newDate);
			loginUserService.save(loginUser);
		}
	}

}
