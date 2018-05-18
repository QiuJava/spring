package cn.pay.core.obj.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 材料认证查询
 * 
 * @author Administrator
 */
@Setter
@Getter
public class UserFileQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long loginInfoId;
}