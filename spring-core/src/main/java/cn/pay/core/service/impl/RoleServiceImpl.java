package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.RoleRepository;
import cn.pay.core.domain.sys.Role;
import cn.pay.core.redis.service.ConfigAttributeRedisService;
import cn.pay.core.service.RoleService;

/**
 * 角色服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository repository;

	@Autowired
	private ConfigAttributeRedisService configAttributeRedisService;

	@Override
	public List<Role> getAll() {
		return repository.findAll();
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public void save(Role r) {
		// 判断该权限在Redis中已存在
		if (configAttributeRedisService.get(r.getUrl()) == null) {
			Role role = repository.save(r);
			Collection<ConfigAttribute> con = new ArrayList<>();
			con.add(new SecurityConfig(role.getName()));
			configAttributeRedisService.put(role.getUrl(), con, -1);
		}
	}

}
