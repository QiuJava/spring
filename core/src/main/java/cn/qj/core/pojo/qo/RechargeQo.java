package cn.qj.core.pojo.qo;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 线下充值查询对象
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
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
