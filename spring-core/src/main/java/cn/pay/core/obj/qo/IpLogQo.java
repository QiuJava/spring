package cn.pay.core.obj.qo;

import lombok.Getter;
import lombok.Setter;

import org.springframework.util.StringUtils;

/**
 * 登录日志的高级查询加分页
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
public class IpLogQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private String username;
	private boolean like = false;

	public String getUsername() {
		return !StringUtils.hasLength(username) ? null : username;
	}
}
