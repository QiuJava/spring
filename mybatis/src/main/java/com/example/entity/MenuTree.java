package com.example.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单树
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class MenuTree implements Serializable {
	private static final long serialVersionUID = -5849453039222592646L;
	private Integer id;
	private String text;
	private String url;

	private List<MenuTree> children;

	/**
	 * 菜单节点状态
	 * 
	 * @return
	 */
	public String getState() {
		return children != null && children.size() > 0 ? "closed" : "open";
	}

}
