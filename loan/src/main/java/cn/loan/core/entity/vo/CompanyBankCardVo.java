package cn.loan.core.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 公司银行卡视图
 * 
 * @author qiujian
 *
 */
@Setter
@Getter
public class CompanyBankCardVo {
	private Long id;
	private String bankName;
	private String accountName;
	private String cardNumber;
	private String bankForkName;
}
