package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ShiroRigthMapper;
import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.service.bill.ShiroRigthService;
@Service("shiroRigthService")
@Transactional
public class ShiroRigthServiceImpl implements ShiroRigthService {
	@Resource
	public ShiroRigthMapper shiroRigthMapper;

	@Override
	public ShiroRigth findShiroRigthById(Integer id) throws Exception {
		return shiroRigthMapper.findShiroRigthById(id);
	}

	@Override
	public List<ShiroRigth> findUserRoleRigthByUserId(Integer userId) throws Exception {
		return shiroRigthMapper.findUserRoleRigthByUserId(userId);
	}
	@Override
	public List<ShiroRigth> findShiroRigthByRigthCode(String rigthCode) throws Exception {
		return shiroRigthMapper.findShiroRigthByRigthCode(rigthCode);
	}
	@Override
	public List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(Integer userId, Integer parentId) throws Exception {
		return shiroRigthMapper.findUserWithRolesPrivilegeRigthByParentId(userId, parentId);
	}

	@Override
	public List<ShiroRigth> findShiroRigthByParentId(Integer parentId) throws Exception {
		return shiroRigthMapper.findShireRigthByParentId(parentId);
	}

	@Override
	public List<ShiroRigth> findAllShiroRigth() throws Exception {
		return shiroRigthMapper.findAllShiroRigth();
	}

	@Override
	public List<ShiroRigth> findRolePrivilegeRigthByParentId(Integer roleId,Integer parentId) throws Exception {
		return shiroRigthMapper.findRolePrivilegeRigthByParentId(roleId,parentId);
	}

	@Override
	public int insertShiroRigth(ShiroRigth shiroRigth) throws Exception {
		return shiroRigthMapper.insertShiroRigth(shiroRigth);
	}

	@Override
	public int deleteShiroRigthByRigthCode(String rigthCode) throws Exception {
		return shiroRigthMapper.deleteShiroRigthByRigthCode(rigthCode);
	}

	@Override
	public int updateShiroRigth(ShiroRigth shiroRigth) throws Exception {
		return shiroRigthMapper.updateShiroRigth(shiroRigth);
	}
}
