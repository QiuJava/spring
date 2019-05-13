package cn.qj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色
 * 
 * @author Qiujian
 * @date 2019年5月8日
 *
 */
@Setter
@Getter
@Entity
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Date createTime;
	private Date updateTime;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Permission> permissions;
}
