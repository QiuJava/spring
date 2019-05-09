package cn.qj.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

/**
 * 权限
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Entity
@Setter
@Getter
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long parentId;
	private String url;
	private String authority;
	private String name;
	private String description;
	private Date createTime;
	private Date updateTime;
	@ManyToMany
	private List<Role> roles;

	@Override
	public String getAuthority() {
		return authority;
	}

}
