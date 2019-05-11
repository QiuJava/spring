package cn.qj.entity;

import java.util.Date;

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
@Getter
@Setter
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

	@Override
	public String getAuthority() {
		return authority;
	}

	public Long get_parentId() {
		return parentId;
	}

}
