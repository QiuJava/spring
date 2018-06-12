package cn.pay.core.domain.sys;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	private List<LoginInfo> loginInfoList = new ArrayList<>();
	private List<Permission> permissionList = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}
	
	@Transient
	public List<LoginInfo> getLoginInfoList() {
		return loginInfoList;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "permission_id") })
	public List<Permission> getPermissionList() {
		return permissionList;
	}
}
