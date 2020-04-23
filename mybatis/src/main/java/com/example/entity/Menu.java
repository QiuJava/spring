package com.example.entity;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class Menu  {

	private Integer id;
	private String menuName;
	private String mappingAddress;
	private Date createTime;
	private Date updateTime;
	private Long parentId;
	private List<Menu> children;

}
