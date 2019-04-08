package cn.qj.core.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private String authority;

	@Override
	public String getAuthority() {
		return authority;
	}

}
