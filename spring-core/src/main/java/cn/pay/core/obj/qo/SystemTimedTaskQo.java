package cn.pay.core.obj.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统作业查询对象
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class SystemTimedTaskQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	
	private String groupName;
}
