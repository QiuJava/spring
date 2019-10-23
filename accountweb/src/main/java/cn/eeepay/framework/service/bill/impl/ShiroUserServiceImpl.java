package cn.eeepay.framework.service.bill.impl;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.ShiroUserMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BlockedIp;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.User;
import cn.eeepay.framework.service.bill.BlockedIpService;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.ShiroUserService;
import cn.eeepay.framework.service.bill.UserRigthService;
import cn.eeepay.framework.service.bill.UserRoleService;
import cn.eeepay.framework.util.DateUtil;
@Service("shiroUserService")
@Transactional
public class ShiroUserServiceImpl implements ShiroUserService {
	
	private static final Logger log = LoggerFactory.getLogger(ShiroUserServiceImpl.class);
	@Resource
	public ShiroUserMapper shiroUserMapper;
	@Resource
	private RedisService redisService;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private UserRigthService userRigthService;
	@Resource
	private BlockedIpService blockedIpService;
	@Value("${login.error.times:5}")
	private int loginErrorTimes;//登录最大错误次数
	
	
	@Override
	public List<ShiroUser> findAllShiroUser() throws Exception {
		return shiroUserMapper.findAllShiroUser();
	}

	@Override
	public List<ShiroUser> findShiroUserNameByParams(ShiroUser user) throws Exception {
		return shiroUserMapper.findShiroUserNameByParams(user);
	}
	
	@Override
	public int insertUser(ShiroUser user, String[] roleIds) throws Exception{
		int i = 0,j = 0;
		try {
			i = shiroUserMapper.insert(user);
			j = userRoleService.saveUserRole(user.getId(), roleIds);
			if ( i > 0 ) {
				i = i + j;
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return i;
	}
	@Override
	public List<ShiroUser> findUsers(ShiroUser user,Sort sort,Page<ShiroUser> page) throws Exception{
		return shiroUserMapper.findUsers(user,sort,page);
	}
	@Override
	public List<ShiroUser> findUsersWithRole2() throws Exception{
		return shiroUserMapper.findUsersWithRole2();
	}
	@Override
	public ShiroUser findUserWithRolesByUserName(String userName){
		return shiroUserMapper.findUserWithRolesByUserName(userName);
	}
	
	@Override
	public ShiroUser findUserByUserName(String userName) throws Exception{
		return shiroUserMapper.findUserByUserName(userName);
	}
	
	@Override
	public ShiroUser findUserById(Integer id) throws Exception{
		return shiroUserMapper.findUserById(id);
	}
	@Override
	public int updateUser(ShiroUser shiroUser, String[] roleIds) throws Exception{
		int i = 0,j = 0,k = 0;
		i = shiroUserMapper.updateUser(shiroUser);
		k = userRigthService.deleteUserRigthByUserId(shiroUser.getId());
		j = userRoleService.saveUserRole(shiroUser.getId(), roleIds);
		if ( j > 0 ) {
			i = i + j;
		}
		if ( k > 0 ) {
			i = i + k;
		}
		return i;
	}
	@Override
	public int updateUserPwd(Integer id, String password) throws Exception{
		return shiroUserMapper.updateUserPwd(id, password);
	}
	@Override
	public boolean isBlocked(String ip){
		String denyDay = DateUtil.getCurrentDate();
		BlockedIp blockedIp = blockedIpService.findBlockedIpByIpAndDate(denyDay,ip);
        if(blockedIp != null){
        	if (blockedIp.getDenyTime().compareTo(new Date()) >0 
        			&& blockedIp.getDenyNum() >= loginErrorTimes) {
        		return true;
			}
        }
        return false;
	}
	@Override
	public int deleteUserById(Integer id) throws Exception{
		int i = 0,j= 0,k=0;
		i = shiroUserMapper.deleteUserById(id);
		j = userRigthService.deleteUserRigthByUserId(id);
		k = userRoleService.deleteUserRoleByUserId(id);
		if (i > 0 ) {
			i = i + j + k;
		}
		return i;
	}
	@Override
	public List<ShiroUser> findAllUsers() {
		return shiroUserMapper.findAllShiroUser();
	}
	@Override
	public int insertTestUser(User user) {
		return shiroUserMapper.insertTestUser(user);
	}

//	@Override
//	public AgentInfo testAgentInfo(String accountNo) throws Exception {
//		ExtAccountInfo extAccountInfo = extAccountService.getExtAccountInfoByAccountNo(accountNo);
//		AgentInfo agentInfo= agentInfoService.findAgentByUserId(extAccountInfo.getUserId());
//		return agentInfo;
//	}

}
