package cn.pay.core.obj.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 材料认证查询对象
 * 
 * @author Administrator
 */
@Setter
@Getter
@ToString
public class UserFileQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long loginInfoId;
}