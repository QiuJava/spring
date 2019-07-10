package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 借款查询
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class BorrowQo extends BaseQo {

	private static final long serialVersionUID = 1L;

	private Integer borrowStatus;
	private Integer[] borrowStatusList;

}
