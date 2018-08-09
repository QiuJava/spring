package cn.pay.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.util.StringUtils;

/**
 * 登录日志查询对象
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
public class IpLogQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private String username;
	private boolean like = false;

	public String getUsername() {
		return !StringUtils.hasLength(username) ? null : username;
	}
}
