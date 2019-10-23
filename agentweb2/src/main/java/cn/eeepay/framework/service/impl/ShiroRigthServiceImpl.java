package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.ShiroRigthDao;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.service.ShiroRigthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service("shiroRigthService")
public class ShiroRigthServiceImpl implements ShiroRigthService {
	@Resource
	public ShiroRigthDao shiroRigthDao;
	
	@Autowired
	private AgentInfoDao agentInfoDao;

	@Override
	public ShiroRigth findShiroRigthById(Integer id) throws Exception {
		return shiroRigthDao.findShiroRigthById(id);
	}

	@Override
	public List<ShiroRigth> findUserRolePrivilegeRigth(Integer userId) throws Exception {
		return shiroRigthDao.findUserRolePrivilegeRigth(userId);
	}
	@Override
	public ShiroRigth findShiroRigthByRigthCode(String rigthCode) throws Exception {
		return shiroRigthDao.findShiroRigthByRigthCode(rigthCode);
	}
	@Override
	public List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(Integer userId, Integer parentId) throws Exception {
		return shiroRigthDao.findUserWithRolesPrivilegeRigthByParentId(userId, parentId);
	}

	@Override
	public List<ShiroRigth> findShiroRigthByParentId(Integer parentId) throws Exception {
		return shiroRigthDao.findShireRigthByParentId(parentId);
	}

	@Override
	public List<ShiroRigth> findAllShiroRigth() throws Exception {
		return shiroRigthDao.findAllShiroRigth();
	}

	@Override
	public List<ShiroRigth> findRolePrivilegeRigthByParentId(Integer roleId,Integer parentId) throws Exception {
		return shiroRigthDao.findRolePrivilegeRigthByParentId(roleId,parentId);
	}

	@Override
	public int insertShiroRigth(ShiroRigth shiroRigth) throws Exception {
		return shiroRigthDao.insertShiroRigth(shiroRigth);
	}

	@Override
	public int deleteShiroRigthByRigthCode(String rigthCode) throws Exception {
		return shiroRigthDao.deleteShiroRigthByRigthCode(rigthCode);
	}

	@Override
	public boolean isAdminCreateRole(Integer uId) {
	long count = 	shiroRigthDao.countAdminCreateRoleByUid(uId);
		return count>0;
	}

	@Override
	public List<ShiroRigth> findRigthByOem(String agentNo) {
		// 如果根据品牌和代理商类型没找到角色  则根据代理商类型来找 没找到使用默认收单角色
		List<ShiroRigth> list = shiroRigthDao.findRigthByOem(agentNo);
		AgentInfo agentInfo = agentInfoDao.findByMobilePhone(agentNo);
		if (list.size()<1) {
			list = shiroRigthDao.findRigthByAgentType(agentNo);
			if (agentInfo!=null && "101".equals(agentInfo.getAgentType()) && list.size()>1) {
				list = new ArrayList<>();
			}
		}
		if (list.size()<1) {
			list = shiroRigthDao.findRigthByDefault();
		}
		return list;
	}
}
