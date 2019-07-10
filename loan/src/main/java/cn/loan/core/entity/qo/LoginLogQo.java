package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志查询
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class LoginLogQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	
	private Integer loginStatus;
	private String username;

}
