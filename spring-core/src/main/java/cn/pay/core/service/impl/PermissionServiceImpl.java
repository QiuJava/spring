package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import cn.pay.core.dao.PermissionRepository;
import cn.pay.core.domain.sys.Permission;
import cn.pay.core.redis.service.ConfigAttributeRedisService;
import cn.pay.core.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository repository;

	@Autowired
	private ConfigAttributeRedisService configAttributeRedisService;

	// @Cacheable("getAll")
	@Override
	public List<Permission> getAll() {
		return repository.findAll();
	}

	@Transactional
	@Override
	public void save(Permission p) {
		// 判断该权限在Redis中已存在
		if (configAttributeRedisService.get(p.getUrl()) == null) {
			Permission permission = repository.save(p);
			Collection<ConfigAttribute> con = new ArrayList<>();
			con.add(new SecurityConfig(permission.getName()));
			configAttributeRedisService.put(permission.getUrl(), con, -1);
		}
	}

}
