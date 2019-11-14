package com.example.vo;

import java.io.Serializable;
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
public class MenuListVo implements Serializable {
	private static final long serialVersionUID = 8802358866790936967L;
	private Long id;
	private String menuName;
	private String intro;
	private Date createTime;
	private Date updateTime;
	private String parentMenuName;

}
