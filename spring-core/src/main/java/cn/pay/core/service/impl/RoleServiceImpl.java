package cn.pay.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.RoleRepository;
import cn.pay.core.domain.sys.Role;
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

	@Override
	public List<Role> listAll() {
		return repository.findAll();
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public Role saveRole(Role role) {
		return repository.saveAndFlush(role);
	}

	@Override
	public void deleteAll(List<Role> roleListAll) {
		repository.delete(roleListAll);
	}

	@Override
	public Role updateRole(Role role) {
		return repository.saveAndFlush(role);
	}

}
