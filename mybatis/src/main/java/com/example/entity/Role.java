package com.example.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class Role implements Serializable {
	private static final long serialVersionUID = 4899934804814955410L;

	private Long id;
	private String roleName;
	private String intro;
	private Date createTime;
	private Date updateTime;

}
