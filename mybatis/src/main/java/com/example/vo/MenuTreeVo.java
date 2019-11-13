package com.example.vo;

import java.io.Serializable;
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
@Setter
@Getter
@ToString
public class MenuTreeVo implements Serializable {
	private static final long serialVersionUID = -5849453039222592646L;
	private Long id;
	private String menuName;
	private String intro;

	private List<PermissionVo> permissionVoList;
	private List<MenuTreeVo> children;
}
