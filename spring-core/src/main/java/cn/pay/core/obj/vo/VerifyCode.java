package cn.pay.core.obj.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 手机验证码信息
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCode {
	private String phoneNumber;
	private String verifyCode;
	private Date date;
}
