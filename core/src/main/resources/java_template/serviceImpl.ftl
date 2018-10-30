package cn.qj.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.core.repository.${className}Repository;
import cn.qj.core.service.${className}Service;

@Service
public class ${className}ServiceImpl implements ${className}Service {

	@Autowired
	private ${className}Repository repository;

}
