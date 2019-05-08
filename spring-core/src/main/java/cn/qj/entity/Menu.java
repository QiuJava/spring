package cn.qj.entity;

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
 * 菜单
 * 
 * @author Qiujian
 * @date 2019年5月8日
 *
 */
@Getter
@Setter
@Entity
public class Menu {

	public static final String CLOSED = "closed";
	public static final String OPEN = "open";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String text;
	private String state;
	private String code;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Menu> children;

}
