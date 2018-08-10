package cn.pay.core.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import cn.pay.core.domain.business.Borrow;
import cn.pay.core.domain.business.PaymentPlan;
import cn.pay.core.domain.business.RealAuth;
import cn.pay.core.domain.business.Recharge;
import cn.pay.core.domain.business.RepaymentSchedule;
import cn.pay.core.domain.business.Withdraw;
import cn.pay.core.pojo.vo.VerifyCode;
import cn.pay.core.service.SendSmsService;
import cn.pay.core.util.DateUtil;
import cn.pay.core.util.HttpServletContext;
import cn.pay.core.util.LogicException;
import lombok.Setter;

/**
 * 发送短信服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
@ConfigurationProperties(prefix = "service.sms")
public class SendSmsServiceImpl implements SendSmsService {
	private final static String SUCCESS = "success";
	private final static long SECONDS = 180L;

	@Setter
	private String username;
	@Setter
	private String password;
	@Setter
	private String url;
	@Setter
	private String apikey;

	@Override
	public void verifyCode(String phoneNumber) {
		VerifyCode vc = HttpServletContext.getVerifyCode();
		if (vc == null || DateUtil.setBetweenDate(new Date(), vc.getDate()) > SendSmsServiceImpl.SECONDS) {
			// 生成验证码纯数字的
			String verifyCode = Integer.toString(new Random().nextInt(9999));

			StringBuffer sb = new StringBuffer(100).append("username=").append(username).append("&password=")
					.append(password).append("&apikey=").append(apikey).append("&mobile=").append(phoneNumber)
					.append("&content=").append("您的验证码是：").append(verifyCode).append(",有效期为3分钟");
			try {
				URL u = new URL(url);
				// 打开一个连接
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				// 设置请求方式，必须大写
				conn.setRequestMethod("POST");
				// 有请求体
				conn.setDoOutput(true);
				// 设置请求类容并发送
				conn.getOutputStream().write(sb.toString().getBytes());
				// 接收响应结果
				String requesString = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("UTF-8"));
				if (requesString.indexOf(SendSmsServiceImpl.SUCCESS) != 0) {
					throw new RuntimeException("发送短信失败");
				} else {
					// 创建一个额外的对象存放页面需要的值 并存放到session中
					VerifyCode code = new VerifyCode(phoneNumber, verifyCode, new Date());
					HttpServletContext.setVerifyCode(code);
				}
			} catch (Exception e) {
				throw new LogicException("发送短信失败");
			}
		} else {
			throw new LogicException("短信发送太频繁");
		}
	}

	@Override
	public void borrowSuccess(Borrow eventObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paymentSuccess(PaymentPlan eventObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void realAuthSuccess(RealAuth eventObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rechargeSuccess(Recharge eventObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void withdrawSuccess(Withdraw eventObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void repayWarn(RepaymentSchedule repaymentSchedule) {
		// TODO Auto-generated method stub

	}

}
