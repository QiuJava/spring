/**
 * 
 */
package cn.eeepay.framework.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.convert.StringToDataTypeConverter;

import cn.eeepay.framework.encryptor.md5.Md5;



/**
 * 
 * 发送短信接口
 */
public class Sms {

	private static final Logger log = LoggerFactory.getLogger(Sms.class);
	
	private final static String ENCODE = "UTF-8";
	private final static String key = "ruewoo1398543p";
	private final static String url = "http://msg.yfbpay.cn/msgplatform/sms/sendSms";
	
	/**
	 * 短信
	 * @param mobile
	 * @param context
	 * @return
	 */
	

	public static int sendMsg(String mobile, String context) {
		int sendStatus = 0;
		CloseableHttpClient client = null;
		try {
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			List<BasicNameValuePair> param = new ArrayList<>();
			param.add(new BasicNameValuePair("mobile", mobile));
			param.add(new BasicNameValuePair("context", context));
			param.add(new BasicNameValuePair("platform", "core2"));
			param.add(new BasicNameValuePair("mac",
					Md5.md5Str(("mobile" + "&&" + mobile + "platform" + "&&" + "core2") + key)));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param,Consts.UTF_8);
			log.info("发送短信:手机号:{},内容:{}", new Object[] { mobile, context });
			post.setEntity(entity);
			post.addHeader("Content-type",
					"application/x-www-form-urlencoded; charset=" + ENCODE);
			CloseableHttpResponse res = client.execute(post);
			sendStatus=res.getStatusLine().getStatusCode();
			System.out.println(sendStatus);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return sendStatus;
	}
	
	public static void main(String[] args) throws ParseException {
	    sendMsg("15217743522","验证码123456");
	}
}
