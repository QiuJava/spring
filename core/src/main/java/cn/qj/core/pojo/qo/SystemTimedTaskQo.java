package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统定时任务条件
 * 
 * @author Qiujian
 * @date 2018/11/01
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
