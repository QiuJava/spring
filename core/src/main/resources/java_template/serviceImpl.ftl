package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pay.core.repository.${className}Repository;
import cn.pay.core.service.${className}Service;

@Service
public class ${className}ServiceImpl implements ${className}Service {

	@Autowired
	private ${className}Repository repository;

}
