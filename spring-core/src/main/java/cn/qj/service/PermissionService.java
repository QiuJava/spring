package cn.qj.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.config.listener.ContextStartListener;
import cn.qj.entity.Permission;
import cn.qj.entity.vo.MenuAttributeVo;
import cn.qj.entity.vo.MenuVo;
import cn.qj.repository.PermissionRepository;

/**
 * 权限服务
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@SuppressWarnings("unchecked")
@Service
public class PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	public List<Permission> getAll() {
		return permissionRepository.findAll();
	}

	public List<MenuVo> getchildrenMenu(Long parentId) {
		List<Permission> permissions = permissionRepository.findByParentIdAndType(parentId, Permission.MENU);

		// 根据用户所拥有的权限进行过滤
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
		List<Permission> curMenu = new ArrayList<>();
		for (Permission permission : permissions) {
			for (GrantedAuthority grantedAuthority : authorities) {
				// 有权限才继续获取
				if (permission.getAuthority().equals(grantedAuthority.getAuthority())) {
					curMenu.add(permission);
				}
			}
		}
		List<MenuVo> menuVos = new ArrayList<>();
		for (Permission permission : curMenu) {
			MenuVo menuVo = new MenuVo();
			MenuAttributeVo menuAttributeVo = new MenuAttributeVo();
			menuVo.setId(permission.getId());
			menuVo.setText(permission.getName());
			// 如果该权限下面没有权限 state 为 open
			Long id = permission.getId();
			long count = permissionRepository.countByParentIdAndType(id, Permission.MENU);
			if (count > 0) {
				menuVo.setState(MenuVo.CLOSED);
			} else {
				menuVo.setState(MenuVo.OPEN);
			}
			menuAttributeVo.setUrl(permission.getUrl());
			menuAttributeVo.setAuthority(permission.getAuthority());
			menuVo.setAttributes(menuAttributeVo);
			menuVos.add(menuVo);
		}

		return menuVos;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public Permission save(Permission permission) {
		Date date = new Date();
		Long id = null;
		if (permission.getId() == null) {
			permission.setCreateTime(date);
		} else {
			id = permission.getId();
		}
		permission.setUpdateTime(date);
		Permission p = permissionRepository.save(permission);
		List<Permission> permissions = (List<Permission>) valueOperations.get(ContextStartListener.PERMISSION);
		if (id != null) {
			for (Iterator<Permission> iterator = permissions.iterator(); iterator.hasNext();) {
				Permission per = (Permission) iterator.next();
				if (permission.getId().equals(per.getId())) {
					iterator.remove();
				}
			}
		}
		permissions.add(p);
		valueOperations.set(ContextStartListener.PERMISSION, permissions);
		return p;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void delete(Permission permission) {
		// 删除当前菜单和下级菜单
		List<Permission> deleteList = new ArrayList<>();
		deleteList.add(permission);
		getChildrenMenu(deleteList, permission.getId());
		permissionRepository.deleteInBatch(deleteList);

		// 删除缓存中的菜单
		List<Permission> permissions = (List<Permission>) valueOperations.get(ContextStartListener.PERMISSION);
		for (Iterator<Permission> iterator = permissions.iterator(); iterator.hasNext();) {
			Permission p = (Permission) iterator.next();
			for (Permission deleteP : deleteList) {
				if (p.getId().equals(deleteP.getId())) {
					iterator.remove();
				}
			}
		}
		valueOperations.set(ContextStartListener.PERMISSION, permissions);
	}

	public void getChildrenMenu(List<Permission> deleteList, Long parentId) {
		List<Permission> children = permissionRepository.findByParentId(parentId);
		if (children.size() > 0) {
			deleteList.addAll(children);
			for (Permission permission : children) {
				getChildrenMenu(deleteList, permission.getId());
			}
		}
	}

}
