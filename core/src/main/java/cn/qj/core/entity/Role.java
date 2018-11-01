package cn.qj.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

/**
 * 角色
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
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
	/** 创建时间 */
	private Date gmtCreate;
	/** 修改时间 */
	private Date gmtModified;
	@ManyToMany(mappedBy = "roles")
	private List<LoginInfo> loginInfos;

}
