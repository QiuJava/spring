package cn.loan.core.entity.qo;

import cn.loan.core.entity.CompanyBankCard;
import cn.loan.core.entity.LoginUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 充值查询
 * 
 * @author qiujian
 *
 */
@Setter
@Getter
public class RechargeQo extends BaseQo {
	
	private Integer auditStatus;
	private LoginUser submitter;
	private CompanyBankCard companyBankCard;
	private String serialNumber;

	private static final long serialVersionUID = 1L;

}
