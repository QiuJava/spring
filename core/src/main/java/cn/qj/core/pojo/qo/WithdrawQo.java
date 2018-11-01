package cn.qj.core.pojo.qo;

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
public class WithdrawQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "WithdrawQo [state=" + state + ", beginDate=" + beginDate + ", endDate=" + endDate + ", currentPage="
				+ currentPage + ", pageSize=" + pageSize + "]";
	}

}
