package cn.qj.entity.dto;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 根据Id统计
 * 
 * @author Qiujian
 * @date 2019年5月18日
 *
 */
@Getter
@Setter
public class IdCount {

	private BigInteger id;
	private BigInteger count;
	
}
