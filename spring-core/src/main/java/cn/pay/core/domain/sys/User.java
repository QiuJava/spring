package cn.pay.core.domain.sys;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import cn.pay.core.domain.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@Entity
// @Table(name = "user")
public class User extends BaseDomain {
	private static final long serialVersionUID = 1L;

	// @Column(name = "login_info_id")
	private Long loginInfoId;
	/** 用户名称 */
	// @Column(name = "name")
	private String name;
	/** 用户描述 */
	// @Column(name = "descritpion")
	private String descritpion;
	/** 用户状态 */
	// @Column(name = "state")
	private Integer state;
	private List<Role> roleList = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.id;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	public List<Role> getRoleList() {
		return roleList;
	}
}
