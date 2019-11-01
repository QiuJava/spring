package com.example.quartz;

import com.example.qo.BaseQo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务查询对象
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class JobDetailsQo extends BaseQo {

	private String jobGroupName;

}
