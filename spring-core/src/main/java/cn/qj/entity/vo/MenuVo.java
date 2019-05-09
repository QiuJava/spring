package cn.qj.entity.vo;

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
public class MenuVo {

	public static final String CLOSED = "closed";
	public static final String OPEN = "open";

	private Long id;
	private String text;
	private String state;
	private MenuAttributeVo attributes;

}
