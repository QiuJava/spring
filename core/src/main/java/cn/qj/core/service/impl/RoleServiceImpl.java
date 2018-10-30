package cn.qj.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.Role;
import cn.qj.core.repository.RoleRepository;
import cn.qj.core.service.RoleService;

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

	@Override
	public List<Role> listAll() {
		return repository.findAll();
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public Role saveRole(Role role) {
		return repository.saveAndFlush(role);
	}
	
	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public void deleteAll(List<Role> roleListAll) {
		repository.delete(roleListAll);
	}
	
	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public Role updateRole(Role role) {
		return repository.saveAndFlush(role);
	}

}
