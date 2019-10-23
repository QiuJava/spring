package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.RoleRigthDao;
import cn.eeepay.framework.dao.ShiroRoleDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ShiroRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
@Service("shiroRoleService")
public class ShiroRoleServiceImpl implements ShiroRoleService {
	private static final Logger log = LoggerFactory.getLogger(ShiroRoleService.class);
	@Resource
	public ShiroRoleDao shiroRoleDao;
	@Resource
	private RoleRigthDao roleRigthDao; 
	
	@Autowired
	private AgentInfoDao agentInfoDao;

	@Override
	public ShiroRole findShiroRoleByRoleCode(String roleCode) throws Exception {
		return shiroRoleDao.findShiroRoleByRoleCode(roleCode);
	}

	@Override
	public ShiroRole findShiroRoleById(Integer id) throws Exception {
		return shiroRoleDao.findShiroRoleById(id);
	}


	@Override
	public List<ShiroRole> findAllShiroRole() throws Exception {
		return shiroRoleDao.findAllShiroRole();
	}

	@Override
	public int updateShiroRole(ShiroRole shiroRole) {
		return shiroRoleDao.updateShiroRole(shiroRole);
	}

	@Override
	public int insertShiroRole(ShiroRole shiroRole) {
		return shiroRoleDao.insertShiroRole(shiroRole);
	}
	
	@Override
	public List<ShiroRole> findAdminShiroRole() throws Exception {
		return shiroRoleDao.findAdminShiroRole();
	}
	@Override
	public List<ShiroRole> findAgentShiroRole(String agentId) throws Exception {
		return shiroRoleDao.findAgentShiroRole(agentId);
	}

	@Override
	public List<ShiroRole> findUsableAgentShiroRole(String agentId) throws Exception {
		return shiroRoleDao.findUsableAgentShiroRole(agentId);
	}
	
	@Override
	public int deleteShiroRoleById(Integer roleId) {
		roleRigthDao.deleteRoleRigthByRoleId(roleId);
		int i = shiroRoleDao.deleteShiroRoleById(roleId);
		return i;
	}

	@Override
	public List<ShiroRole2> findRoles(QueryParam param, Page<ShiroRole2> page) {
		return shiroRoleDao.findRoles(param, page);
	}

	@Override
	public List<ShiroRole> findAllRoles(Integer usable) {

		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ShiroRole> shiroRoleList = null;
		try {
			if (userInfo.getUserEntityInfo().getEntityId().equals("ALL")) {
                shiroRoleList = findAdminShiroRole();
            } else {
               /* if(usable != null && usable == 1)
                    shiroRoleList = findUsableAgentShiroRole(userInfo.getUserEntityInfo().getEntityId());
                else*/
                    shiroRoleList = findAgentShiroRole(userInfo.getUserEntityInfo().getEntityId());
            }
		} catch (Exception e) {
			log.error("查询角色列表错误", e);
		}
		return shiroRoleList;
	}
	@Override
	public List<ShiroRole> findAllRolesApi(Integer usable,String entityId) {
//		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ShiroRole> shiroRoleList = null;
		try {
			if (entityId.equals("ALL")) {
				shiroRoleList = findAdminShiroRole();
			} else {
				shiroRoleList = findAgentShiroRole(entityId);
			}
		} catch (Exception e) {
			log.error("查询角色列表错误", e);
		}
		return shiroRoleList;
	}

	@Override
	public String getRoleName(UserInfo item) {
		
		 UserEntityInfo userEntityInfo = item.getUserEntityInfos().get(0);
		 boolean isAgent = userEntityInfo.getIsAgent().equals("1");
		 String agentNo = userEntityInfo.getEntityId();
		 if (isAgent && !"ALL".equals(userEntityInfo.getEntityId())) {
			 String roleName = "";
			 AgentInfo agentInfo = agentInfoDao.findByMobilePhone(agentNo);
			 if (agentInfo !=null ) {
				 roleName= shiroRoleDao.findRoleNameByOem(agentInfo.getAgentType(),agentInfo.getAgentOem());
			
				 if (!StringUtils.hasLength(roleName)) {
					 roleName= shiroRoleDao.findRoleNameByAgentType(agentInfo.getAgentType(),agentInfo.getAgentOem());
				 }
				 if (agentInfo !=null &&  "101".equals(agentInfo.getAgentType()) && !StringUtils.hasLength(roleName)) {
					 roleName= "";
				 }
			 }
			 if (!StringUtils.hasLength(roleName)) {
				 roleName= shiroRoleDao.findRoleNameByDefault();
			 }
			 
			 return roleName;
			 
		}else {
			String roleName = shiroRoleDao.findRoleNameByUserId(userEntityInfo.getId());
			return roleName;
		}
		
	}
}
