package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.sys.Role;

public interface RoleService {

	List<Role> getAll();

	void save(Role p);

}
