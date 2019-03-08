package cn.qj.key.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 手机号码认证
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Getter
@Setter
public class PhoneNumAuth implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2769226569919814898L;
	
	/**
	 * 手机号码
	 */
	private String phoneNum;
	
	/**
	 * 发送时间
	 */
	private Date sendTime;
	
	/**
	 * 验证码有效时间
	 */
	private long validSecond;
	
	/**
	 * 验证码
	 */
	private String authCode;

}
