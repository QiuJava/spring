package cn.qj.key.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信校验属性
 * 
 * @author Qiujian
 * @date 2019/01/17
 */
@Setter
@Getter
@ToString
public class WechatVerify {
	/** 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。 */
	private String signature;
	/** 时间戳 */
	private String timestamp;
	/** 随机数 */
	private String nonce;
	/** 随机字符串 */
	private String echostr;
}
