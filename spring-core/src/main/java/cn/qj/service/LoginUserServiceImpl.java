package cn.qj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.config.properties.ConstProperties;
import cn.qj.config.properties.DictProperties;
import cn.qj.entity.Dict;
import cn.qj.entity.LoginUser;
import cn.qj.repository.LoginUserRepository;

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

	@Autowired
	private ConstProperties constProperties;
	@Autowired
	private DictProperties dictProperties;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginUser user = loginUserRepository.findByUsername(username);
		if (user == null) {
			Dict dict = (Dict) hashOperations.get(constProperties.getDictHash(),
					dictProperties.getUsernamePasswordErrMsg());
			throw new UsernameNotFoundException(dict.getValue());
		}
		return user;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public LoginUser save(LoginUser loginUser) {
		return loginUserRepository.save(loginUser);
	}

	public LoginUser getByUsername(String username) {
		return loginUserRepository.findByUsername(username);
	}

}
