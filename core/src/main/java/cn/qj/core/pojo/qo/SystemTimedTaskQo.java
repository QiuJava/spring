package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统作业查询对象
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
public class SystemTimedTaskQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;
	
	private String groupName;

	@Override
	public String toString() {
		return "SystemTimedTaskQo [groupName=" + groupName + ", currentPage=" + currentPage + ", pageSize=" + pageSize
				+ "]";
	}
	
}
