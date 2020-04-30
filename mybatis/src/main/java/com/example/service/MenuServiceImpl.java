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

	public List<MenuTree> listAllMenuTree() {
		return menuMapper.listMenuTreeByParentId(null);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Menu menu) {

		Date date = new Date();
		menu.setCreateTime(date);
		menu.setUpdateTime(date);

		return menuMapper.insertSelective(menu);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Menu menu) {
		Date date = new Date();
		menu.setUpdateTime(date);

		return menuMapper.updateByPrimaryKeySelective(menu);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		// 删除角色分配的权限
		permissionService.deleteByMenuId(id);
		// 删除下级菜单
		menuMapper.deleteByParentId(id);

		return menuMapper.deleteById(id);
	}

	public List<Menu> listAll() {
		return menuMapper.listByParentId(null);
	}

	public List<Menu> listByQo(MenuQo qo) {
		return menuMapper.listByQo(qo);
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
