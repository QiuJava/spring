package cn.pay.core.domain.sys;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "role")
public class Role extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/** 角色名称 */
	@Column(name = "name")
	private String name;
	/** 角色描述 */
	@Column(name = "descritpion")
	private String descritpion;
	/** 角色状态 */
	@Column(name = "state")
	private String state;
	/** 拥有该角色的用户列表 */
	@ManyToMany
	private List<LoginInfo> loginInfoList;
	@ManyToMany
	private List<Permission> permissionList;
}
