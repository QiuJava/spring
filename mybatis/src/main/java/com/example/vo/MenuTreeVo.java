package com.example.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单树视图
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class MenuTreeVo {
	private Long id;
	private String text;
	private boolean checked;
	private List<MenuTreeVo> children;

	/**
	 * 菜单节点状态
	 * 
	 * @return
	 */
	public String getState() {
		return children != null && children.size() > 0 ? "closed" : "open";
	}
}
