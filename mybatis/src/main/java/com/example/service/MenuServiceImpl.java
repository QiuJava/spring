package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.annotation.DataSourceKey;
import com.example.entity.Menu;
import com.example.mapper.MenuMapper;
import com.example.util.DataSourceUtil;

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

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public List<Menu> listAll() {
		return menuMapper.selectAll();
	}
}
