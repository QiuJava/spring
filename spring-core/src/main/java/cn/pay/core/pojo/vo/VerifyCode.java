package cn.pay.core.pojo.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 手机验证码信息
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCode {
	private String phoneNumber;
	private String verifyCode;
	private Date date;
}
