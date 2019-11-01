package com.example.quartz;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务键数据
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class JobKeyDo implements Serializable {
	private static final long serialVersionUID = 6703867715727416961L;

	private String jobGroupName;
	private String jobName;
}
