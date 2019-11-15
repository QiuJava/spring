package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.LogicException;
import com.example.common.PageResult;
import com.example.entity.Menu;
import com.example.mapper.MenuMapper;
import com.example.qo.MenuQo;
import com.example.util.DataSourceUtil;
import com.example.vo.MenuListVo;
import com.example.vo.MenuTreeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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

	public List<MenuTreeVo> listAll() {
		return menuMapper.selectMenuTreeVoByParentId(null);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Menu menu) {
		return menuMapper.insert(menu);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasByMenuName(String menuName) {
		return menuMapper.countByMenuName(menuName) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Menu menu) {
		return menuMapper.updateById(menu);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public String getMenuNameById(Long id) {
		return menuMapper.selectMenuNameById(id);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) throws LogicException {
		long countByMenuId = permissionService.countByMenuId(id);
		if (countByMenuId > 0) {
			int deleteByMenuId = permissionService.deleteByMenuId(id);
			if (deleteByMenuId != countByMenuId) {
				throw new LogicException("删除失败");
			}
		}
		return menuMapper.deleteById(id);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public PageResult<MenuListVo> listByQo(MenuQo qo) {
		Page<MenuListVo> page = PageHelper.startPage(qo.getPageNum(), qo.getPageSize(), qo.getCount());
		menuMapper.selectByQo(qo);
		return new PageResult<MenuListVo>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult());
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasById(Long menuId) {
		return menuMapper.countById(menuId) == 1;
	}
}
