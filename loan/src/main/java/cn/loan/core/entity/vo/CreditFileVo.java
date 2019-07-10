package cn.loan.core.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 信用材料视图
 * 
 * @author qiujian
 *
 */
@Setter
@Getter
public class CreditFileVo {
	
	private Long id;
	private String fileName;
	private String itemName;
	private String username;

}
