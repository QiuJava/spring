package cn.qj.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.qj.core.config.listener.ContextStartListener;
import cn.qj.core.entity.DataDict;
import cn.qj.core.entity.LoginUser;
import cn.qj.core.repository.LoginUserRepository;
import cn.qj.core.util.DictUtil;

/**
 * 登录用户服务
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Service
public class LoginUserServiceImpl implements UserDetailsService {

	@Autowired
	private LoginUserRepository loginUserRepository;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginUser user = loginUserRepository.findByUsername(username);
		if (user == null) {
			DataDict dataDict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT,
					DictUtil.USERNAME_PASSWORD_ERR_MSG);
			throw new UsernameNotFoundException(dataDict.getDictValue());
		}
		return user;
	}

	public void save(LoginUser loginUser) {
		loginUserRepository.save(loginUser);
	}

}
