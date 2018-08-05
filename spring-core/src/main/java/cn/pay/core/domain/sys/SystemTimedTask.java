package cn.pay.core.domain.sys;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统定时任务管理
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
@Entity
public class SystemTimedTask implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Integer PAUSE = 1;
	public static final Integer NORMAL = 0;
	public static final Integer DEL = -1;

	private Long id;
	private String jobName;
	/** 定时任务的组 */
	private String groupName;
	/** 计划任务表达式 */
	private String cronExpression;
	private String description;// 描述
	private Integer status;// 状态

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
}
