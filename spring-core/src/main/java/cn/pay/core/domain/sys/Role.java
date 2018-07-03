package cn.pay.core.domain.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@Column(name = "parent_id")
	private Long parentId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}
	
}
