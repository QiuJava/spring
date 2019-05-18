package cn.qj.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.config.listener.ContextStartListener;
import cn.qj.entity.Permission;
import cn.qj.entity.dto.IdCount;
import cn.qj.entity.vo.MenuAttributeVo;
import cn.qj.entity.vo.MenuVo;
import cn.qj.repository.PermissionRepository;
import cn.qj.util.QueryUtil;

/**
 * 权限服务
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Service
public class PermissionService {

	@Autowired
	private EntityManager entityManager;

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
		List<Long> ids = new ArrayList<>();

		for (Permission permission : permissions) {
			for (GrantedAuthority grantedAuthority : authorities) {
				// 有权限才继续获取
				if (permission.getAuthority().equals(grantedAuthority.getAuthority())) {
					curMenu.add(permission);
				}
			}
			ids.add(permission.getId());
		}
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT ");
		sqlSb.append("	count( * ) AS count, ");
		sqlSb.append("	parent_id AS id  ");
		sqlSb.append("FROM ");
		sqlSb.append("	permission  ");
		sqlSb.append("GROUP BY ");
		sqlSb.append("	parent_id  ");
		if (ids.size() > 0) {
			sqlSb.append("HAVING ");
			sqlSb.append("	parent_id IN ( ");
			for (Long id : ids) {
				sqlSb.append(id).append(",");
			}
			sqlSb.deleteCharAt(sqlSb.length() - 1);
			sqlSb.append(")");
		}
		List<IdCount> idCounts = QueryUtil.findListResult(entityManager, sqlSb.toString(), IdCount.class,
				new ArrayList<>());

		List<MenuVo> menuVos = new ArrayList<>();
		for (Permission permission : curMenu) {
			MenuVo menuVo = new MenuVo();
			MenuAttributeVo menuAttributeVo = new MenuAttributeVo();
			menuVo.setId(permission.getId());
			menuVo.setText(permission.getName());
			// 如果该权限下面没有权限 state 为 open
			Long id = permission.getId();
			BigInteger count = null;
			for (IdCount idCount : idCounts) {
				if (new BigInteger(id.toString()).compareTo(idCount.getId()) == 0) {
					count = idCount.getCount();
				}
			}
			// 如果该权限下没有菜单类型的权限 就为可加载否则为不能加载
			if (count != null && count.compareTo(new BigInteger("0")) == -1) {
				menuAttributeVo.setLoad(false);
				menuVo.setState(MenuVo.CLOSED);
			} else {
				if (permission.getParentId() == null) {
					menuAttributeVo.setLoad(false);
				} else {
					menuAttributeVo.setLoad(true);
				}
				menuVo.setState(MenuVo.OPEN);
			}
			menuAttributeVo.setUrl(permission.getUrl());
			menuVo.setAttributes(menuAttributeVo);
			menuVos.add(menuVo);
		}

		return menuVos;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public Permission save(Permission permission) {
		Date date = new Date();
		permission.setUpdateTime(date);
		Permission p = permissionRepository.save(permission);
		List<Permission> permissions = this.getAll();
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
		List<Permission> permissions = this.getAll();
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
