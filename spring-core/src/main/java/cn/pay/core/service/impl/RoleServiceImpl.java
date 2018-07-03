package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import cn.pay.core.dao.RoleRepository;
import cn.pay.core.domain.sys.Role;
import cn.pay.core.redis.service.ConfigAttributeRedisService;
import cn.pay.core.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository repository;

	@Autowired
	private ConfigAttributeRedisService configAttributeRedisService;

	// @Cacheable("getAll")
	@Override
	public List<Role> getAll() {
		return repository.findAll();
	}

	@Transactional
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
