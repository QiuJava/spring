package cn.loan.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.JSONObject;

import cn.loan.core.entity.vo.CompanyBankCardVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 公司银行卡
 * 
 * @author Qiujian
 * 
 */
@Setter
@Getter
@Entity
public class CompanyBankCard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String bankName;
	/** 账户名称 */
	private String accountName;
	/** 银行卡号 */
	private String cardNumber;
	/** 开户支行名称 */
	private String bankForkName;

	public String getJsonString() {
		CompanyBankCardVo card = new CompanyBankCardVo();
		card.setId(id);
		card.setAccountName(accountName);
		card.setBankForkName(bankForkName);
		card.setBankName(bankName);
		card.setCardNumber(cardNumber);
		return JSONObject.toJSONString(card);
	}
}
