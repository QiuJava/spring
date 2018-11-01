package cn.qj.core.pojo.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 投标参数封装
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
public class BidDto {

	private Long borrowId;
	private BigDecimal amount;
	private Long loginInfoId;

}
