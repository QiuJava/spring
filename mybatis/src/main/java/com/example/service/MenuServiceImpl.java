package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Menu;
import com.example.mapper.MenuMapper;

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

	public List<Menu> listAll() {
		return menuMapper.selectAll();
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Menu menu) {
		return menuMapper.insertSelective(menu);
	}

	public boolean hasMenuName(String menuName) {
		return menuMapper.countByMenuName(menuName) > 0;
	}
}
