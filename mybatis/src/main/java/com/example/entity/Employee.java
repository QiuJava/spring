package com.example.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工
 *
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class Employee implements Serializable {
	private static final long serialVersionUID = 1221985552224614692L;

	private Long id;
	private String username;
	private String password;

}