package cn.qj.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.qj.entity.Authority;
import cn.qj.entity.vo.MenuAttributeVo;
import cn.qj.entity.vo.MenuVo;
import cn.qj.repository.AuthorityRepository;

/**
 * 权限服务
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	public List<Authority> getAll() {
		return authorityRepository.findAll();
	}

	public List<MenuVo> getchildrenMenu(Long parentId) {
		List<Authority> authorities = authorityRepository.findByParentId(parentId);

		// 根据用户所拥有的权限进行过滤
		Collection<? extends GrantedAuthority> curAuthorities = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
		List<Authority> curMenu = new ArrayList<>();
		for (Authority authority : authorities) {
			for (GrantedAuthority grantedAuthority : curAuthorities) {
				// 有权限才继续获取
				if (authority.getAuthority().equals(grantedAuthority.getAuthority())) {
					curMenu.add(authority);
				}
			}
		}
		List<MenuVo> menuVos = new ArrayList<>();
		MenuVo menuVo = new MenuVo();
		MenuAttributeVo menuAttributeVo = new MenuAttributeVo();
		for (Authority authority : curMenu) {
			menuVo.setId(authority.getId());
			menuVo.setText(authority.getName());
			// 如果该权限下面没有权限 state 为 open
			Long id = authority.getId();
			long count = authorityRepository.countByParentId(id);
			if (count > 0) {
				menuVo.setState(MenuVo.CLOSED);
			} else {
				menuVo.setState(MenuVo.OPEN);
			}
			menuAttributeVo.setUrl(authority.getUrl());
			menuAttributeVo.setAuthority(authority.getAuthority());
			menuVo.setAttributes(menuAttributeVo);
			menuVos.add(menuVo);
		}

		return menuVos;
	}

}
