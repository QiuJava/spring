package cn.loan.core.entity.qo;

import cn.loan.core.entity.LoginUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 信用材料查詢
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class CreditFileQo extends BaseQo {

	private static final long serialVersionUID = 1L;
	
	private Integer auditStatus;
	private LoginUser submitter;
}
