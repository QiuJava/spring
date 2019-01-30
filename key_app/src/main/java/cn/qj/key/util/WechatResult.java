package cn.qj.key.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信返回结果
 * 
 * @author Qiujian
 * @date 2019/01/30
 */
@Getter
@Setter
public class WechatResult {

	public static final Integer OK = 0;

	private Integer errcode;
	private String errmsg;
}
