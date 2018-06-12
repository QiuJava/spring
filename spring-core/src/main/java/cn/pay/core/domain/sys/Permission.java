package cn.pay.core.domain.sys;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "permission")
public class Permission extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/** 权限名称 */
	@Column(name = "name")
	private String name;
	/** 权限描述 */
	@Column(name = "descritpion")
	private String descritpion;
	/** 权限连接 */
	@Column(name = "url")
	private String url;
	/** 父节点ID */
	private Long parentId;
	/** 拥有该权限的角色 */
	private List<Role> roleList = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}
	
	@Transient
	public List<Role> getRoleList() {
		return roleList;
	}
}
