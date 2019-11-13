package com.example.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 菜单列表视图
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class MenuListVo {

	private String menuName;
	private String intro;
	private Date createTime;
	private Date updateTime;
	private String parentMenuName;

}
