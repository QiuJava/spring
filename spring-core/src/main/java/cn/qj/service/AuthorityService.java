package cn.qj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.entity.Authority;
import cn.qj.repository.AuthorityRepository;

/**
 * 权限服务
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	public List<Authority> getAll() {
		return authorityRepository.findAll();
	}

}
