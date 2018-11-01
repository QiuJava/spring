package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * Ip日志条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
public class IpLogQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;

	private String username;
	private Boolean isLike;

	@Override
	public String toString() {
		return "IpLogQo [username=" + username + ", isLike=" + isLike + ", state=" + state + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", currentPage=" + currentPage + ", pageSize=" + pageSize + "]";
	}

}
