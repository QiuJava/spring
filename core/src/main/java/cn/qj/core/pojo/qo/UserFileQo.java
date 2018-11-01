package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户材料条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
public class UserFileQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long loginInfoId;

	@Override
	public String toString() {
		return "UserFileQo [loginInfoId=" + loginInfoId + ", state=" + state + ", beginDate=" + beginDate + ", endDate="
				+ endDate + ", currentPage=" + currentPage + ", pageSize=" + pageSize + "]";
	}

}