package cn.pay.core.obj.qo;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
public class RechargeQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long applierId;
	/** 平台账户Id */
	private Long bankInfoId = -1L;
	private String tradeCode;

	public String getTradeCode() {
		return StringUtils.hasLength(tradeCode) ? tradeCode : null;
	}
}
