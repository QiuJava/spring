package com.example.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单查询
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class MenuQo {

	private String menuName;
	private Long parentId;
}
