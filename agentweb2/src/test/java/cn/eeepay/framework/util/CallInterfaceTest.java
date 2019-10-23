package cn.eeepay.framework.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;

public class CallInterfaceTest {

	@Ignore
	@Test
	public void testffindExtAccountBalance() throws Exception {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "1");
		claims.put("accountNo", "00000122410101000000000063");
		claims.put("accountType", "M");
		claims.put("userId", "25541100000000000081");
		claims.put("accountOwner", "000001");
		claims.put("cardNo", "");
		claims.put("subjectNo", "1002");
		claims.put("currencyNo", "1");

		String accountUrl = Constants.ACCOUNT_BALANCE_URL;

		String result = ClientInterface.postAccountApi(accountUrl, claims);
		System.out.println("result: " + result);
		JSONObject json = JSON.parseObject(result);
		Assert.assertTrue(json.getBooleanValue("status"));
	}

	@Ignore
	@Test
	public void testfindExtAccountTransInfoList() throws Exception {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "1");
		claims.put("accountNo", "00000122410101000000000063");
		claims.put("accountType", "M");
		claims.put("userId", "25541100000000000081");
		claims.put("accountOwner", "000001");
		claims.put("cardNo", "");
		claims.put("subjectNo", "1002");
		claims.put("currencyNo", "1");
		claims.put("recordDate1", null);
		claims.put("recordDate2", null);
		claims.put("debitCreditSide", null);

		String accountUrl = Constants.ACCOUNT_DETAIL_URL;

		String result = ClientInterface.postAccountApi(accountUrl, claims);
		System.out.println("result: " + result);
		JSONObject json = JSON.parseObject(result);
		Assert.assertTrue(json.getBooleanValue("status"));

	}

	static String entityId = "249";
	@Test
	public void testAgentAccountBalance() throws Exception {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", entityId);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", "224106");
		claims.put("currencyNo", "1");

		String accountUrl = Constants.ACCOUNT_BALANCE_URL;

		String result = ClientInterface.postAccountApi(accountUrl, claims);
		System.out.println("result: " + result);
		JSONObject json = JSON.parseObject(result);
		Assert.assertTrue(json.getBooleanValue("status"));
	}

	@Test
	public void testAgentAccountTransInfoList() throws Exception {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", entityId);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", "224106");
		claims.put("currencyNo", "1");

		claims.put("recordDate1", null);
		claims.put("recordDate2", null);
		claims.put("debitCreditSide", null);

		String accountUrl = Constants.ACCOUNT_DETAIL_URL;

		String result = ClientInterface.postAccountApi(accountUrl, claims);
		System.out.println("result: " + result);
		JSONObject json = JSON.parseObject(result);
		Assert.assertTrue(json.getBooleanValue("status"));
	}
	//测试新增代理商开户
	@Test
	public void testName() throws Exception {
		String agentNo = "12111";
		Map<String,Object> claims=new HashMap<>();
		claims.put("accountType","A");
		claims.put("userId",agentNo);
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		claims.put("exp", iat+60L);
		claims.put("iat", iat);
//		claims.put("subjectNo",subjectNo);
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		String accSuperBank = new ClientInterface(Constants.ACCOUNT_CREATE_EXT_ACCOUNT_URL, params).postRequest();
		System.out.println("========" + accSuperBank + "============");
		
//		String acc = ClientInterface.createAgentAccount(agentNo, "224105");
//		System.out.println("========" + acc + "============");
//		String acc2 = ClientInterface.createAgentAccount(agentNo, "224106");
//		System.out.println("========" + acc2 + "============");
//		String accSuperBank = ClientInterface.createAgentAccount(agentNo, "224116");
//		System.out.println("========" + accSuperBank + "============");
	}
}
