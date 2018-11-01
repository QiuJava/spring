package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 平台账户条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
public class CompanyBankInfoQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "CompanyBankInfoQo [currentPage=" + currentPage + ", pageSize=" + pageSize + "]";
	}

}
