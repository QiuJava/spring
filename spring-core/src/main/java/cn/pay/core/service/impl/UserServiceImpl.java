package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pay.core.dao.UserRepository;
import cn.pay.core.domain.sys.User;
import cn.pay.core.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Override
	public User getByLoginInfoId(Long id) {
		return repository.findByLoginInfoId(id);
	}

}
