package cn.pay.core.domain.sys;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import cn.pay.core.domain.base.IdComponent;
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
// @Table(name = "role")
public class Role extends IdComponent {
	private static final long serialVersionUID = 1L;

	/** 权限名称 */
	// @Column(name = "name")
	private String name;
	/** 权限描述 */
	// @Column(name = "descritpion")
	private String descritpion;
	/** 权限连接 */
	// @Column(name = "url")
	private String url;
	/** 父节点ID */
	// @Column(name = "parent_id")
	private Long parentId;

	private List<LoginInfo> loginInfos;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}

	@ManyToMany(mappedBy = "roles")
	public List<LoginInfo> getLoginInfos() {
		return loginInfos;
	}

}
