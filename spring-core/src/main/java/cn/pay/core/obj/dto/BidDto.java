package cn.pay.core.obj.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 投标参数封装
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
public class BidDto {
	
	private Long borrowId;
	private BigDecimal amount;
	private Long loginInfoId;

}
