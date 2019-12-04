package com.example.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.entity.Menu;
import com.example.entity.MenuTree;
import com.example.mapper.MenuMapper;
import com.example.qo.MenuQo;
import com.example.vo.MenuTreeVo;

/**
 * 菜单服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class MenuServiceImpl {

	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private PermissionServiceImpl permissionService;

	public List<MenuTree> listMenuTreeByAll() {
		return menuMapper.selectMenuTreeByParentId(null);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Menu menu) {
		String menuName = menu.getMenuName();

		Date date = new Date();
		menu.setCreateTime(date);
		menu.setUpdateTime(date);
		// 菜单名称不能重复
		boolean hasByMenuName = this.hasByMenuName(menuName);
		if (hasByMenuName) {
			throw new LogicException("菜单名称已存在");
		}

		return menuMapper.insert(menu);
	}

	public boolean hasByMenuName(String menuName) {
		return menuMapper.countByMenuName(menuName) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Menu menu) {
		Date date = new Date();
		menu.setUpdateTime(date);
		String oldMenuName = menuMapper.selectMenuNameById(menu.getId());
		String menuName = menu.getMenuName();
		if (!menuName.equals(oldMenuName)) {
			// 菜单名称不能重复
			boolean hasMenuName = this.hasByMenuName(menuName);
			if (hasMenuName) {
				throw new LogicException("菜单名称已存在");
			}
		} else {
			menu.setMenuName(null);
		}

		return menuMapper.updateById(menu);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		// 删除角色分配的权限
		permissionService.deleteByMenuId(id);
		// 删除下级菜单
		menuMapper.deleteByParentId(id);

		return menuMapper.deleteById(id);
	}

	public List<Menu> listByAll() {
		return menuMapper.selectByParentId(null);
	}

	public List<Menu> listByQo(MenuQo qo) {
		return menuMapper.selectByQo(qo);
	}

	public List<MenuTreeVo> listMenuTreeVoByAll(Long roleId) {
		List<MenuTreeVo> menuTreeVoList = menuMapper.selectMenuTreeVoByParentId(null);

		// 获取当前角色所有的菜单ID
		List<Long> menuTreeIdList = menuMapper.selectMenuTreeByRoleId(roleId);

		// 设置是否选中
		this.setMenuTreeVoListChecked(menuTreeVoList, menuTreeIdList);
		return menuTreeVoList;
	}

	private void setMenuTreeVoListChecked(List<MenuTreeVo> menuTreeVoList, List<Long> menuTreeIdList) {
		for (MenuTreeVo menuTreeVo : menuTreeVoList) {
			Long id = menuTreeVo.getId();
			if (menuTreeIdList.contains(id)) {
				menuTreeVo.setChecked(true);
			}
			List<MenuTreeVo> children = menuTreeVo.getChildren();
			if (children.size() > 0) {
				setMenuTreeVoListChecked(children, menuTreeIdList);
			}
		}
	}

}
