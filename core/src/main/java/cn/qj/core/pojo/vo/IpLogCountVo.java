package cn.qj.core.pojo.vo;

import java.math.BigInteger;

import lombok.Data;

/**
 * 登录日志统计Vo
 * 
 * @author Qiujian
 * @date 2018/11/16
 */
@Data
public class IpLogCountVo {

	private BigInteger count;
	private String username;

}
