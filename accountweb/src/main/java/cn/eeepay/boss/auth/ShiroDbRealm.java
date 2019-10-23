//package cn.eeepay.boss.auth;
//
//import java.util.Date;
//
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * 登录系统后，对用户进行检验，包括严重和授权
// * 
// * @author dj
// * 
// */
//@Component
//public class ShiroDbRealm extends AuthorizingRealm {
//
//	private static final Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);
//
//	// 用户验证
//	@Override
//	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
//		return null;
//	}
//
//	private boolean isUpdatePw(Date updateTime) {
//		if (updateTime == null) {
//			return false;
//		}
//		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
//		long diff = new Date().getTime() - updateTime.getTime();
//		if ((diff / nd) > 30) {
//			return false;
//		}
//		return true;
//
//	}
//
//	// 用户授权
//	// TODO
//	@Override
//	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
////		BossUser bu = (BossUser) principals.fromRealm(getName()).iterator().next();
////		if (bu == null) {
////			return null;
////		}
////
////		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
////		List<BossAuth> bas;
////		try {
////			bas = userService.getAuthList(bu.getId());
////			for (BossAuth b : bas) {
////				info.addStringPermission(StringUtils.trim(b.getAuthCode()));
////			}
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
//		return null;
//	}
//
//	@Override
//	public boolean supports(AuthenticationToken token) {
//		return super.supports(token);
//	}
//
//}
