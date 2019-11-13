package com.example.vo;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限菜单视图
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class PermissionVo implements GrantedAuthority {
	private static final long serialVersionUID = 4982680879444713572L;
	private Long id;
	private String authority;
	private String url;

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PermissionVo)) {
			return false;
		}
		PermissionVo o = (PermissionVo) obj;
		return this.id.equals(o.getId());
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(this.id.toString()) * 31;
	}

}
