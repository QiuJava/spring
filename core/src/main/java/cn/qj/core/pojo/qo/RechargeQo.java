package cn.qj.core.pojo.qo;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 充值条件
 * 
 * @author Qiujian
 * @date 2018/11/01
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

	@Override
	public String toString() {
		return "RechargeQo [applierId=" + applierId + ", bankInfoId=" + bankInfoId + ", tradeCode=" + tradeCode
				+ ", state=" + state + ", beginDate=" + beginDate + ", endDate=" + endDate + ", currentPage="
				+ currentPage + ", pageSize=" + pageSize + "]";
	}

}
