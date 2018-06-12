package cn.pay.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pay.core.dao.PermissionRepository;
import cn.pay.core.domain.sys.Permission;
import cn.pay.core.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository repository;

	@Override
	public List<Permission> getAll() {
		return repository.findAll();
	}

}
