package framework.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.auth0.jwt.JWTSigner;

import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.util.HttpConnectUtil;

public class AccountTranQueryTest  extends BaseTest {
	@Resource
	public SystemInfoService systemInfoService;
	
	@Value("${boss.http.api.url}")  
    private String bossHttpApiUrl;
	@Value("${boss.http.api.secret}")  
    private String bossHttpApiSecret;
	@Test
	public void test1() {
		final String secret = bossHttpApiSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		JWTSigner signer = new JWTSigner(secret);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("transOrderNo", "SK885408399882831035");
		claims.put("iat", iat);
		claims.put("jti", jti);

		String token = signer.sign(claims);
		String url = bossHttpApiUrl+"/transInfoAction/accountTranQuery.do";
		System.out.println("url:" + url);
		String responseText = HttpConnectUtil.postHttp(url, "token", token);
		System.out.println("response:" + responseText);
		Map<String, Object> responseMap = JSON.parseObject(responseText, new TypeReference<Map<String, Object>>() {});
		Object msg = responseMap.get("msg");
		Boolean status = (Boolean)responseMap.get("status");
		if (status && msg != null) {
			String resultMsg = msg.toString();
			Map<String, String> msgMap = JSON.parseObject(resultMsg, new TypeReference<Map<String, String>>() {});
			String acqEnname = msgMap.get("acqEnname");
			System.out.println(acqEnname);
		}
		
	}
}
