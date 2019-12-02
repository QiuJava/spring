package com.example.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色查询对象
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class RoleQo extends PageQo{
	
	private String roleName;
}
