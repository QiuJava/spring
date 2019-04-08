package cn.qj.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.qj.core.entity.LoginUser;
import cn.qj.core.repository.LoginUserRepository;

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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return loginUserRepository.findByUsername(username);
	}

	public void save(LoginUser loginUser) {
		loginUserRepository.save(loginUser);
	}

}
