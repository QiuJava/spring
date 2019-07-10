package cn.qj.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String descritpion;
	private String url;
	private String authority;
	@CreatedDate
	private Date createTime;
	@LastModifiedDate
	private Date updateTime;
	@CreatedBy
	private String createUser;
	@LastModifiedDate
	private String updateUser;
	@ManyToMany(mappedBy = "roles")
	private List<LoginInfo> loginInfos;

	@Override
	public String getAuthority() {
		return authority;
	}

}
