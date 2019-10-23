package cn.eeepay.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
//import net.sf.json.JSONObject;

public class RealAuthUtil {
	private static final String IP = "http://api.eeepay.cn";
	private static final String cardAuthUrl = "/card/receive?";
	/**
	 * DES 加密密钥
	 */
	private static final String desKey = "7DD59DDE7B5745DF92A4B776C7BC0AD8";
	/**
	 * appKey
	 */
	private static final String appKey = "bxlh5o8v";
	private static final Logger log = LoggerFactory.getLogger(RealAuthUtil.class);


	public static String createOrderNo() {
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			sb.append(r.nextInt(8999) + 1000);
		}
		String orderNo = System.currentTimeMillis() + sb.toString();
		return orderNo.substring(15, orderNo.length());
	}

	/**
	 * 二三四要素鉴权<br>
	 * 手机号可传可不传，传则为四要素，反之则为三要素
	 * @param orderNo 订单号
	 * @param accountNo 银行卡号
	 * @param accountName 姓名
	 * @param idCardNo 身份证号
	 * @param mobileNo 手机号
	 * @return Map,如果resultCode值为true则为成功;retry重试;false失败,content存失败信息;
	 * @date 2017年11月10日 下午3:43:06
	 * @author ZengJA
	 */
	public static  Map<String, String> cardAuth(String orderNo, String accountNo, String accountName, String idCardNo,String mobileNo) {
		Map<String,String> result = new HashMap<String,String>();
		result.put("resultCode", "retry");
		String api = IP + cardAuthUrl;
		JSONObject js = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("orderNo", orderNo);
		data.put("accountNo", accountNo);
		data.put("accountName", accountName);
		data.put("idCardNo", idCardNo);
		data.put("mobileNo", mobileNo);

		if(StringUtils.isNotBlank(accountNo)){
			if(StringUtils.isNotBlank(accountName) && StringUtils.isBlank(idCardNo)){
				js.put("bizName", "cardAuth2a");//二要素:卡号+姓名
			}else if(StringUtils.isNotBlank(idCardNo) && StringUtils.isBlank(accountName)){
				js.put("bizName", "cardAuth2b");//二要素:卡号+身份证
			}else if(StringUtils.isNotBlank(idCardNo) && StringUtils.isNotBlank(accountName)){
				js.put("bizName", "cardAuth3");//三要素:卡号+身份证+姓名
			}else{
				result.put("content", "缺少必要参数>身份证或姓名");
				return result;
			}
			if(StringUtils.isNotBlank(mobileNo)){
				js.put("bizName", "cardAuth4");
			}
			js.put("data", data);
			String res = request(api, js);
			if(StringUtils.isNotBlank(res)){
//				js = JSONObject.fromObject(res).getJSONObject("head");
				js = JSONObject.parseObject(res).getJSONObject("head");
				String resultCode = js.getString("resultCode");
				if("FAIL".equalsIgnoreCase(resultCode)){
					result.put("resultCode", "false");
				}else if("SUCCESS".equalsIgnoreCase(resultCode)){
					result.put("resultCode", "true");
				}
				result.put("content", "SUCCESS".equalsIgnoreCase(resultCode) ? orderNo : js.getString("resultMsg"));
			}else{
				log.info("开放平台，鉴权返回空");
				result.put("content", "验证异常，请稍后重试!");
			}
		}else{
			result.put("content", "缺少必要参数>卡号");
		}
		return result;
	}

	public static  String request(String api, JSONObject js) {
		StringBuilder url = new StringBuilder(api);
		String req = js.toString();
		log.info("请求参数:{}", req);
		try {
			req = new DESPlus(desKey).encrypt(req);// 加密数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		url.append("appKey=").append(appKey).append("&data=").append(req);
		log.info("完整请求:{}", url);
		String  res = connect(url.toString());
		log.info("响应数据:{}", res);
		try {
			res = new DESPlus(desKey).decrypt(res);//
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("解密数据:{}", res);
		return res;
	}

	public static  String connect(String uri) {
		String result = null;
		InputStream in = null;
		try {
			URL url = new URL(uri);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setDoInput(true);
			urlcon.setDoOutput(true);
			urlcon.setUseCaches(false);
			urlcon.setRequestMethod("GET");
			urlcon.connect();// 获取连接
			in = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer bs = new StringBuffer();
			String line = null;
			while ((line = buffer.readLine()) != null) {
				bs.append(line);
			}
			result = bs.toString();
		} catch (Exception e) {
			log.info("[请求异常][地址：" + uri + "][错误信息：" + e.getMessage() + "]");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (Exception e2) {
				log.info("[关闭流异常][错误信息：" + e2.getMessage() + "]");
			}
		}
		return result;
	}
		public static  String sendMsg(String cardNum, String userName, String idCard, String phoneNum) {
			String resultCode = "";
			try {
					log.info("认证信息:姓名[{}],身份证[{}],银行卡号[{}],手机号[{}]", new Object[] { userName,idCard,cardNum, phoneNum });
					String orderNumber = createOrderNo();
					Map<String, String> cardAuth = cardAuth(orderNumber,cardNum,userName,idCard,phoneNum);
				    resultCode = cardAuth.get("resultCode");
				} catch (Exception e) {
					log.info("认证信息:姓名[{}],身份证[{}],银行卡号[{}],手机号[{}]:实名认证失败", new Object[] { userName,idCard,cardNum, phoneNum });
					e.printStackTrace();
				}
			return  resultCode;
		}
}