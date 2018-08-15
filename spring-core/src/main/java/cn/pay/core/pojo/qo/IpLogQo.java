package cn.pay.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志查询对象
 * 
 * @author Administrator
 *
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
