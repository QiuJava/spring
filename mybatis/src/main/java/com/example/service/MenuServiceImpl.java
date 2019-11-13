package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
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
		return menuMapper.insertSelective(menu);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasMenuName(String menuName) {
		return menuMapper.countByMenuName(menuName) > 0;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Menu menu) {
		return menuMapper.updateByPrimaryKeySelective(menu);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public String getMenuNameById(Long id) {
		return menuMapper.selectMenuNameById(id);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int deleteById(Long id) {
		permissionService.deleteByMenuId(id);
		return menuMapper.deleteByPrimaryKey(id);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public PageResult<MenuListVo> listByQo(MenuQo qo) {
		Page<MenuListVo> page = PageHelper.startPage(qo.getPageNum(), qo.getPageSize(), qo.getCount());
		menuMapper.selectByQo(qo);
		return new PageResult<MenuListVo>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult());
	}
}
