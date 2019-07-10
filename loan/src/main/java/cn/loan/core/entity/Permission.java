package cn.loan.core.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

import cn.loan.core.util.StringUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 权限
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
@Entity	
@EqualsAndHashCode
public class Permission implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String authority;
	/**
	 * 指定双向的关系
	 */
	@ManyToMany(mappedBy = StringUtil.PERMISSIONS)
	private Set<LoginUser> users;

	@Override
	public String getAuthority() {
		return authority;
	}

}
