package cn.eeepay.boss.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.SysConfigDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.util.OemTypeEnum;
import cn.eeepay.framework.util.ThreadUtil;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.UserRigth;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.ShiroRigthService;
import cn.eeepay.framework.service.UserEntityInfoService;
import cn.eeepay.framework.service.UserInfoService;
import cn.eeepay.framework.service.UserRigthService;


/**
 * 用户详细信息类
 *
 * 负责以{@link UserDetails}方式提供用户信息
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	@Resource
	public UserInfoService userInfoService;
	@Resource
	public UserEntityInfoService userEntityInfoService;
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public UserRigthService userRigthService;
	@Resource
	private HttpServletRequest request;
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private SysDictDao sysDictDao;
	
	@Autowired
	private AgentInfoService agentInfoService;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	String oemType = request.getParameter("oemType");
    	String teamId = request.getParameter("teamid");
    	String param = ThreadUtil.getParam();
    	if (StringUtils.isBlank(teamId)) {
    		teamId = param.substring(0, param.indexOf(","));
    	}
    	if (StringUtils.isBlank(oemType)) {
    		oemType = param.substring(param.indexOf(",") + 1, param.length());
		}
    	ThreadUtil.removeThread();
    	String ip = request.getRemoteAddr();
    	String key = "agentweb2:blocked:"+ip;
        if (userInfoService.isBlocked(key)) {
            throw new RuntimeException("blocked");
        }
    	UserLoginInfo  userInfo = null;
    	UserInfo userInfo2 = null;
    	if(userName.contains("@")){
    		userInfo2 = userInfoService.findUserInfoByEmailAndTeamId(userName, teamId);
    	} else {
    		userInfo2 = userInfoService.findUserInfoByMobilePhoneAndTeamId(userName, teamId);
    	}
    	if(userInfo2!=null && "0".equals(userInfo2.getStatus())){
			return null;
//    		throw new RuntimeException("close");
    	}

    	try{
	    	if (userInfo2 != null) {
	    		UserEntityInfo currentUserEntityInfo = userEntityInfoService.findAgentUserEntityInfoByUserId(userInfo2.getUserId());
	    		
	    		String agentNo = currentUserEntityInfo.getEntityId();
	    		if (org.springframework.util.StringUtils.hasLength(agentNo) && OemTypeEnum.THREE.getTransType().equals(oemType)) {
	    			// 不是一级代理商直接抛出用户名或密码错误
	        		boolean isOneAgent = agentInfoService.isOneAgent(agentNo);
	        		if (!isOneAgent) {
	        			request.setAttribute("error", "forbid");
	        			throw new BadCredentialsException("");
	    			}
	    		}


	    		if("1".equals(sysDictDao.getByKey("AGENT_OEM_ID_SWITCH").getSysValue())){
					//登录时 校验agentOem 字段 ，和 agent_info
					//表 agent_oem 字段比较 不一致时 提示
					//您不是该品牌代理商，暂不支持登录
					String hostName = request.getServerName();
					if(hostName.contains(":")){
						hostName=hostName.substring(0,hostName.indexOf(":"));
					}
					if(hostName.contains("www.")){
						hostName=hostName.replace("www.","");
					}
					//String agentOem = sysDictDao.getAgentOem(hostName);
					AgentInfo agent = agentInfoDao.selectByAgentNo(agentNo);
					if(agent!=null){

						if(sysDictDao.checkExistsAgentOemId(hostName,agent.getAgentOem())>0){

						}else{
							request.setAttribute("error", "notoem");
							throw new BadCredentialsException("您不是该品牌代理商，暂不支持登录");
						}

						//上送的 是 200010 的时候  需要同时支持  200010  和 100070
					/*if("200010".equals(agentOem)&&"100070".equals(agent.getAgentOem())){

					}else if("100070".equals(agentOem)&&"200010".equals(agent.getAgentOem())){

					}else if(agent.getAgentOem()==null||!agent.getAgentOem().equals(agentOem)){
						request.setAttribute("error", "notoem");
						throw new BadCredentialsException("您不是该品牌代理商，暂不支持登录");
					}*/
					}
				}





	    		if (currentUserEntityInfo != null) {
		    		Integer uId = currentUserEntityInfo.getId();
		    		List<ShiroRigth> shiroRigths = shiroRigthService.findUserRolePrivilegeRigth(uId);//角色对应的权限
		    		
		    		Set<GrantedAuthority> authorities= new HashSet<GrantedAuthority>();
		    		List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
					List<ShiroRigth> shiroRigths2 = new ArrayList<>();
					for (UserRigth userRigth : userRigths) {
						shiroRigths2.add(userRigth.getShiroRigth());
					}
					AgentInfo agentInfo = null;
					// 非admin创建的角色屏蔽掉系统管理菜单 
					//boolean isAdminCreateRole = shiroRigthService.isAdminCreateRole(uId);
					// 是代理商用户根据代理商类型和品牌 获取对应角色的权限
					if (currentUserEntityInfo.getIsAgent().equals("1") && !"ALL".equals(agentNo)) {
							List<ShiroRigth> oemRigthList = shiroRigthService.findRigthByOem(agentNo);
							if (oemRigthList.size()>0) {
								shiroRigths = oemRigthList;
							}
					}else if(currentUserEntityInfo.getIsAgent().equals("0")) {
						// 找到上级代理商
						 agentInfo = agentInfoService.getOneAgentByAgentNo(agentNo);
						 List<ShiroRigth> oemRigthList = shiroRigthService.findRigthByOem(agentInfo.getAgentNo());
						 // 权限过滤
						 shiroRigths = ListUtils.intersection(shiroRigths, oemRigthList);//交集
					}
					List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
					List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
					
					//shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
					List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
					srs = new ArrayList(new HashSet(srs));//去掉重复的用户真正的权限
	        		for (ShiroRigth sr : srs) {
	        			/*if (!isAdminCreateRole&& !currentUserEntityInfo.getEntityId().equals("ALL") && sr.getRigthCode().equals("sys") ) {
							continue;
						}*/
	        			authorities.add(new SimpleGrantedAuthority(sr.getRigthCode()));
					}
		        	userInfo = new UserLoginInfo(userInfo2.getMobilephone(), userInfo2.getPassword(), authorities);
	        		userInfo.setUserInfoId(userInfo2.getUserId());
					userInfo.setLockTime(userInfo2.getLockTime());
					userInfo.setWrongPasswordCount(userInfo2.getWrongPasswordCount());
		        	userInfo.setUserId(userInfo2.getId());
		        	userInfo.setRealName(userInfo2.getUserName());
		        	userInfo.setTelNo(userInfo2.getMobilephone());
		        	userInfo.setStatus(userInfo2.getStatus());
		        	userInfo.setUserEntityInfo(currentUserEntityInfo);
		        	userInfo.setSecondUserNode(userInfo2.getSecondUserNode());
//		        	.setEntityId(currentUserEntityInfo.getEntityId());
//		        	userInfo.setUserId2(currentUserEntityInfo.getUserId());
		        	userInfo.setTeamId(userInfo2.getTeamId());
					userInfo.setOemTypeEnum(OemTypeEnum.getOemType(request.getParameter(ClientTeamIdUtil.CLIENT_FIELD_OEM_TYPE)));
	    		}
			}
	    } catch (Exception e) {
			e.printStackTrace();
		}
        return userInfo;
    }
}
