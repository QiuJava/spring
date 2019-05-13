package cn.qj.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import cn.qj.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
public class Permission implements GrantedAuthority {

	public static final Integer MENU = 1;
	public static final Integer FUNCATION = 2;

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long parentId;
	private String url;
	private String authority;
	private String name;
	private String description;
	@DateTimeFormat(pattern = DateUtil.DATATIME_PATTERN)
	private Date createTime;
	private Date updateTime;
	private Integer type;

	@Override
	public String getAuthority() {
		return authority;
	}

	public Long get_parentId() {
		return parentId;
	}

}
