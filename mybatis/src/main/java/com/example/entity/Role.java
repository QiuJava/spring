package com.example.entity;

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
public class Role {

	private Long id;
	private String roleName;
	private Date createTime;
	private Date updateTime;

}
