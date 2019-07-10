package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 实名认证查询
 * @author qiujian
 *
 */
@Getter
@Setter
public class RealAuthQo extends BaseQo {

	private static final long serialVersionUID = 1L;
	
	private Integer auditStatus;

}
