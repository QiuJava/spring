package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ShiroRoleMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ShiroRole;
import cn.eeepay.framework.service.bill.ShiroRoleService;
@Service("shiroRoleService")
@Transactional
public class ShiroRoleServiceImpl implements ShiroRoleService {
	@Resource
	public ShiroRoleMapper shiroRoleMapper;


	@Override
	public ShiroRole findShiroRoleByRoleCode(String roleCode) throws Exception {
		return shiroRoleMapper.findShiroRoleByRoleCode(roleCode);
	}
	@Override
	public ShiroRole findShiroRoleById(Integer id) throws Exception {
		return shiroRoleMapper.findShiroRoleById(id);
	}
	@Override
	public List<ShiroRole> findAllShiroRole() throws Exception {
		return shiroRoleMapper.findAllShiroRole();
	}
	@Override
	public int updateShiroRole(ShiroRole shiroRole) {
		return shiroRoleMapper.updateShiroRole(shiroRole);
	}
	@Override
	public int insertShiroRole(ShiroRole shiroRole) {
		return shiroRoleMapper.insertShiroRole(shiroRole);
	}
	@Override
	public int deleteShiroRoleById(Integer id) {
		return shiroRoleMapper.deleteShiroRoleById(id);
	}
	@Override
	public List<ShiroRole> findAllShiroRoleList(ShiroRole shiroRole, Sort sort, Page<ShiroRole> page) throws Exception {
		return shiroRoleMapper.findAllShiroRoleList(shiroRole, sort, page);
	}
}
