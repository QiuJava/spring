package cn.pay.core.domain.sys;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 权限名称 */
	private String name;
	/** 权限描述 */
	private String descritpion;
	/** 权限连接 */
	private String url;
	/** 父节点ID */
	private Long parentId;
	@ManyToMany(mappedBy = "roles")
	private List<LoginInfo> loginInfos;

}
