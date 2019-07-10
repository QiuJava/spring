package cn.loan.core.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.entity.Permission;
import cn.loan.core.repository.PermissionDao;

/**
 * 权限相关服务
 * 
 * @author qiujian
 *
 */
@Service
public class PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	@Transactional(rollbackFor = RuntimeException.class)
	public void batchSave(Set<Permission> permissions) {
		permissionDao.save(permissions);
	}

}
