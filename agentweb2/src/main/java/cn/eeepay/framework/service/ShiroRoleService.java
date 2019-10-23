package cn.eeepay.framework.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ShiroRole;
import cn.eeepay.framework.model.UserInfo;

public interface ShiroRoleService {

	List<ShiroRole> findAllRoles(Integer usable);
	
	/**
	 * 银行家剥离
	 * @param usable
	 * @param entityId
	 * @return
	 */
	List<ShiroRole> findAllRolesApi(Integer usable,String entityId);

	public class ShiroRole2 extends ShiroRole {
		private static final long serialVersionUID = 1L;
		private Integer userCount;

		public boolean getCanDelete() {
			final Integer id = getId();
			return userCount == 0 && id != 1 && id != 5 && id !=6;
		}

		public Integer getUserCount() {
			return userCount;
		}

		public void setUserCount(Integer userCount) {
			this.userCount = userCount;
		}
	}

	public class QueryParam{
		private String roleName;
		private String roleCode;
		private String roleRemake;
		private String agentNo;
		
		public String getRoleNameStr(){
			if(StringUtils.isBlank(roleName))
				return "%";
			else
				return "%"+roleName+"%";
		}
		
		public String getRoleCodeStr(){
			if(StringUtils.isBlank(roleCode))
				return "%";
			else
				return "%"+roleCode+"%";
		}
		
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		public String getRoleCode() {
			return roleCode;
		}
		public void setRoleCode(String roleCode) {
			this.roleCode = roleCode;
		}
		public String getRoleRemake() {
			return roleRemake;
		}
		public void setRoleRemake(String roleRemake) {
			this.roleRemake = roleRemake;
		}
		public String getAgentNo() {
			return agentNo;
		}
		public void setAgentNo(String agentNo) {
			this.agentNo = agentNo;
		}
	}
	
	ShiroRole findShiroRoleByRoleCode(String roleCode)  throws Exception;
	ShiroRole findShiroRoleById(Integer id)  throws Exception;
	/**
	 * 获取所有角色集合
	 * @return
	 * @throws Exception
	 */
	List<ShiroRole> findAllShiroRole()  throws Exception;
	/**
	 * 获取管理员角色集合
	 * @return
	 * @throws Exception
	 */
	List<ShiroRole> findAdminShiroRole()  throws Exception;
	/**
	 * 获取代理商角色集合
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	List<ShiroRole> findAgentShiroRole(String agentId)  throws Exception;
	/**
	 * 
	 * @param shiroRole
	 * @return
	 */
	int updateShiroRole(ShiroRole shiroRole);
	int insertShiroRole(ShiroRole shiroRole);
	int deleteShiroRoleById(Integer roleId);
	
	/**
	 * 获取代理商角色集合，附加上id为5、6的角色
	 * @param entityId
	 * @return
	 */
	List<ShiroRole> findUsableAgentShiroRole(String entityId) throws Exception;
	
	/**
	 * 获取代理商角色集合
	 * 
	 * @param agentNo
	 * @param roleName
	 *            模糊查询
	 * @param roleCode
	 *            模糊查询
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<ShiroRole2> findRoles(QueryParam param, Page<ShiroRole2> page);
	String getRoleName(UserInfo item);
}
