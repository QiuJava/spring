package cn.eeepay.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

import cn.eeepay.framework.encryptor.rsa.Base64;
import cn.eeepay.framework.encryptor.rsa.Base64Utils;
import cn.eeepay.framework.model.MerchantInfo;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.common.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProfitDaySettleDetailBean;
import cn.eeepay.framework.model.ProfitDaySettleDetailParamBean;

public class ClientInterface { 
	private String host;
	private Map<String,String> params;
	private Map<String,String> headers;
	private CloseableHttpClient client;
	private static final Logger log = LoggerFactory.getLogger(ClientInterface.class);
	public ClientInterface(String host,Map<String,String> headers,Map<String,String> params){
		this.host=host;
		this.params=params;
		this.headers=headers;
		this.client=HttpClients.createDefault();
	}
	public ClientInterface(String host,Map<String,String> params){
		this(host,null,params);
	}
	public String postRequest(){
		try {
			HttpPost post=new HttpPost(host);
			if(headers!=null)
				post.setHeaders(setHeaders());
			post.setEntity(setParams());
			HttpResponse res=client.execute(post);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public String postRequestUtf8(){
		try {
			HttpPost post=new HttpPost(host);
			if(headers!=null)
				post.setHeaders(setHeaders());
			post.setEntity(setParams2());
			HttpResponse res=client.execute(post);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public String postRequestBody(String param){
		try {
			HttpPost post=new HttpPost(host);
			if(headers!=null)
				post.setHeaders(setHeaders());
			post.setEntity(new StringEntity(param, "utf-8"));
			HttpResponse res=client.execute(post);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public String getRequest(){
		try {
			HttpGet get=new HttpGet(host+"?"+EntityUtils.toString(setParams(),"ISO-8859-1"));
			if(headers!=null)
				get.setHeaders(setHeaders());
			HttpResponse res=client.execute(get);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public String getRequest2(){
		try {
			HttpGet get=new HttpGet(host);
			if(headers!=null)
				get.setHeaders(setHeaders());
			HttpResponse res=client.execute(get);
			if(res.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public UrlEncodedFormEntity setParams() throws UnsupportedEncodingException{
		if(params!=null){
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			for(String key:params.keySet()){
				list.add(new BasicNameValuePair(key,params.get(key)));
			}
			return new UrlEncodedFormEntity(list);
		}
		return null;
	}
	
	public UrlEncodedFormEntity setParams2() throws UnsupportedEncodingException{
		if(params!=null){
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			for(String key:params.keySet()){
				list.add(new BasicNameValuePair(key,params.get(key)));
			}
			return new UrlEncodedFormEntity(list,"utf-8");
		}
		return null;
	}
	
	public Header[] setHeaders() throws UnsupportedEncodingException{
		if(headers!=null){
			Header[] headerArray=new Header[headers.size()];
			int i=0;
			for(String key:headers.keySet()){
				headerArray[i]=new BasicHeader(key,params.get(key));
				i++;
			}
			return headerArray;
		}
		return null;
	}
	
	/**
	 * post账号接口。给claims添加必要的参数，并转换为token，然后提交
	 * 
	 * @param url
	 * @param claims
	 * @return
	 */
	public static String postAccountApi(String url, Map<String, Object> claims) {
		final long iat = System.currentTimeMillis() / 1000; // issued at claim
		claims.put("exp", iat + 600L);
		claims.put("iat", iat);
		claims.put("jti", UUID.randomUUID().toString());

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		return new ClientInterface(url, params).postRequest();
	}

	public static void main(String[] args) {
//		Map<String,String> params=new HashMap<>();
//		params.put("baseInfo", "{'serviceName':'','serviceType':'','rateCard':'','rateHolidays':'','quotaHolidays':'','quotaCard':'','fixedRate':'','fixedQuota':'','rateCheckStatus':'','rateLockStatus':'','quotaCheckStatus':'','quotaLockStatus':''}");
//		params.put("pageNo", "1");
//		params.put("pageSize", "10");
//		System.out.println(new ClientInterface("http://192.168.3.180:8088/boss2/service/queryServiceList", params).postRequest());
//		
		Map<String,Object> claims=new HashMap<>();
		claims.put("accountType","A");
		claims.put("userId","7411");
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		claims.put("exp", iat+60L);
		claims.put("iat", iat);

		String p=new JWTSigner("zouruijin").sign(claims);
		Map<String,String> params=new HashMap<>();
		params.put("token", p);
		System.out.println(new ClientInterface(Constants.ACCOUNT_CREATE_EXT_ACCOUNT_URL, params).postRequest());
	}
	
	public static String createAgentAccount(String agentNo,String subjectNo){
		Map<String,Object> claims=new HashMap<>();
		claims.put("accountType","A");
		claims.put("userId",agentNo);
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		claims.put("exp", iat+60L);
		claims.put("iat", iat);
		claims.put("subjectNo",subjectNo);
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		return new ClientInterface(Constants.ACCOUNT_CREATE_EXT_ACCOUNT_URL, params).postRequest();
	}
	
	public static String createMerchantAccount(String merchantNo){
		Map<String,Object> claims=new HashMap<>();
		claims.put("accountType","M");
		claims.put("userId",merchantNo);
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		claims.put("exp", iat+60L);
		claims.put("iat", iat);

		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		return new ClientInterface(Constants.ACCOUNT_CREATE_EXT_ACCOUNT_URL, params).postRequest();
	}
	
	/**
	 * 获取代理商账号余额
	 * 
	 * @param agentNo
	 * @return {"msg":"查询成功","balance":"0.00","avaliBalance":0,"status":true}
	 */
	public static String getAgentAccountBalance(String agentNo,String subjectNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");

		String result = ClientInterface.postAccountApi(Constants.ACCOUNT_BALANCE_URL, claims);
		return result;
	}

	/**
	 * 获取代理商交易记录
	 * 
	 * @param agentNo
	 * @param recordDate1
	 *            起始日期
	 * @param recordDate2
	 *            截止日期
	 * @param debitCreditSide
	 *            收入\支出？
	 * @return {"msg":"查询成功","data":{"pageNum":0,"pageSize":10,"size":0,
	 *         "orderBy":null,"startRow":0,"endRow":0,"total":0,"pages":0,"list"
	 *         :[],"firstPage":0,"prePage":0,"nextPage":0,"lastPage":0,
	 *         "isFirstPage":false,"isLastPage":true,"hasPreviousPage":false,
	 *         "hasNextPage":false,"navigatePages":8,"navigatepageNums":[]},
	 *         "status":true}
	 */
	public static String selectAgentAccountTransInfoList(String agentNo, Date recordDate1, Date recordDate2,
			String debitCreditSide) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", "224106");
		claims.put("currencyNo", "1");

		claims.put("recordDate1", recordDate1);
		claims.put("recordDate2", recordDate2);
		claims.put("debitCreditSide", debitCreditSide);

		String result = ClientInterface.postAccountApi(Constants.ACCOUNT_DETAIL_URL, claims);
		return result;
	}
	
	public static String postRequest(String url){
		Map<String,String> params=new HashMap<>();
		return new ClientInterface(url, params).postRequest();
	}
	
	/**
	 * 获取商户交易记录
	 * 
	 * @param merNo
	 * @param recordDate1
	 *            起始日期
	 * @param recordDate2
	 *            截止日期
	 * @param debitCreditSide
	 *            收入\支出？
	 * @return {"msg":"查询成功","data":{"pageNum":0,"pageSize":10,"size":0,
	 *         "orderBy":null,"startRow":0,"endRow":0,"total":0,"pages":0,"list"
	 *         :[],"firstPage":0,"prePage":0,"nextPage":0,"lastPage":0,
	 *         "isFirstPage":false,"isLastPage":true,"hasPreviousPage":false,
	 *         "hasNextPage":false,"navigatePages":8,"navigatepageNums":[]},
	 *         "status":true}
	 */
	public static String selectAgentAccountTransInfoList(String merNo, String recordDate1, String recordDate2,
			String debitCreditSide,int page,int pageSize,String subjectNo,String transType) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 300L; // expires claim. In this case the token expires in 300 seconds
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		
//		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");

		claims.put("recordDate1", recordDate1);
		claims.put("recordDate2", recordDate2);
		claims.put("debitCreditSide", debitCreditSide);
		claims.put("transType", transType);
		
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize",String.valueOf(pageSize));
		return new ClientInterface(Constants.ACCOUNT_DETAIL_URL, params).postRequest();
	}
	/**
	 * 商户账户明细查询
	 * @param merNo
	 * @param recordDate1
	 * @param recordDate2
	 * @param debitCreditSide
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static String selectMerchantAccountTransInfoList(String merNo, String recordDate1, String recordDate2,
			String debitCreditSide,int page,int pageSize) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 300L; // expires claim. In this case the token expires in 300 seconds
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		
//		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		//add by tans 2017.3.31
//		claims.put("subjectNo", "224105");
		claims.put("currencyNo", "1");

		claims.put("recordDate1", recordDate1);
		claims.put("recordDate2", recordDate2);
		claims.put("debitCreditSide", debitCreditSide);
		
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize",String.valueOf(pageSize));
		log.info("商户账户明细接口地址:" + Constants.ACCOUNT_DETAIL_URL);
		return new ClientInterface(Constants.ACCOUNT_DETAIL_URL, params).postRequest();
	}

	public static String getAccountDetail(String userId, String userType, String subjectNo,
										  String recordDate1, String recordDate2,
										  String debitCreditSide,int page,int pageSize) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 300L; // expires claim. In this case the token expires in 300 seconds
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

//		claims.put("selectType", "2");
		claims.put("accountType", userType);
		claims.put("userId", userId);
		claims.put("accountOwner", "000001");
		//add by tans 2017.3.31
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");

		claims.put("recordDate1", recordDate1);
		claims.put("recordDate2", recordDate2);
		claims.put("debitCreditSide", debitCreditSide);

		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize",String.valueOf(pageSize));
		log.info("商户账户明细接口地址:" + Constants.ACCOUNT_DETAIL_URL);
		log.info("调用账户明细请求参数:{}", JSONObject.toJSONString(claims));
		return new ClientInterface(Constants.ACCOUNT_DETAIL_URL, params).postRequest();
	}

	public static String transfer(String transferId,String settleType,String userType){
		Map<String,String> params=new HashMap<>();
		params.put("transferId", transferId);
		params.put("settleType", settleType);
		params.put("userType", userType);//结算类型:1.商户,2.代理商
		return new ClientInterface(Constants.NOWTRANSFER_HOST, params).postRequest();
	}

	/**
	 * 获取 claims 自动封装好exp,iat,jti三个参数
	 * @return
	 */
	public static HashMap<String,Object> getClaims(){
		HashMap<String, Object> claims = new HashMap<String, Object>();
		long iat = System.currentTimeMillis() / 1000l;
		long exp = iat + 300L;
		String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		return claims;
	}

	public static String selectShareByDay(String agentNo,String selectAgentNo, String startTime, String endTime,
			String statu,int page,int pageSize) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l; 
		final long exp = iat + 300L;
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		claims.put("agentNo", agentNo);
		claims.put("selectAgentNo", selectAgentNo);
		claims.put("transDate1", startTime);
    	claims.put("transDate2", endTime);
    	claims.put("enterAccountStatus", statu);

		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize",String.valueOf(pageSize));
		return new ClientInterface(Constants.ACCOUNT_CASH_URL, params).postRequest();
	}
	public static String selectShareByDayCollection(String agentNo,String selectAgentNo, String startTime, String endTime,
										  String statu,int page,int pageSize) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l;
		final long exp = iat + 300L;
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		claims.put("agentNo", agentNo);
		claims.put("selectAgentNo", selectAgentNo);
		claims.put("transDate1", startTime);
		claims.put("transDate2", endTime);
		claims.put("enterAccountStatus", statu);

		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize",String.valueOf(pageSize));
		return new ClientInterface(Constants.ACCOUNT_CASH_COLLECTION_URL, params).postRequest();
	}
	public static String getSuperPushUserBalance(String merNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", "224103");
		claims.put("currencyNo", "1");
		return ClientInterface.postAccountApi(Constants.ACCOUNT_BALANCE_URL, claims);

	}

	public static ProfitDaySettleDetailBean profitDaySettleDetailList(ProfitDaySettleDetailParamBean paramBean) {
		return profitDaySettleDetailList(paramBean, false);
	}

	public static List<ProfitDaySettleDetailBean.DataList> exportProfitDaySettleDetailList(ProfitDaySettleDetailParamBean paramBean) {
		ProfitDaySettleDetailBean profitDaySettleDetailBean = profitDaySettleDetailList(paramBean, true);
		if (profitDaySettleDetailBean == null || profitDaySettleDetailBean.getData() == null){
			return null;
		}
		return profitDaySettleDetailBean.getData().getList();
	}

	public static ProfitDaySettleDetailBean profitDaySettleDetailList(ProfitDaySettleDetailParamBean paramBean, boolean isExport) {
		ProfitDaySettleDetailBean errorResult = new ProfitDaySettleDetailBean();
		errorResult.setStatus(false);
		errorResult.setMsg("查询交易分润接口失败.");
		if (StringUtils.isBlank(paramBean.getAgentNo())) {
			return errorResult;
		}
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l;
		final long exp = iat + 300L;
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		claims.put("transDate1", StringUtils.trimToEmpty(paramBean.getStartTransDate()));
		claims.put("transDate2", StringUtils.trimToEmpty(paramBean.getEndTransDate()));
		claims.put("agentProfitGroupTime1", StringUtils.trimToEmpty(paramBean.getStartAgentProfitGroupTime()));
		claims.put("agentProfitGroupTime2", StringUtils.trimToEmpty(paramBean.getEndAgentProfitGroupTime()));
		claims.put("collectionBatchNo", StringUtils.trimToEmpty(paramBean.getCollectionBatchNo()));
		claims.put("merchant", StringUtils.trimToEmpty(paramBean.getMerchant()));
		claims.put("agentNode", StringUtils.trimToEmpty(paramBean.getAgentNo()));
		claims.put("isDeductionFee", StringUtils.trimToEmpty(paramBean.getIsDeductionFee()));
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", paramBean.getPageNo());
		params.put("pageSize",paramBean.getPageSize());
		String result;
		if (isExport){
			result = new ClientInterface(Constants.EXPORT_PROFIT_DAY_SETTLE_DETAIL_LIST_URL, params).postRequest();
		}else{
			result = new ClientInterface(Constants.PROFIT_DAY_SETTLE_DETAIL_LIST_URL, params).postRequest();
		}
		Gson gson = new Gson();
		if (StringUtils.isNotBlank(result)){
			return gson.fromJson(result, ProfitDaySettleDetailBean.class);
		}else{
			return errorResult;
		}
	}

	/**
	 * 每日分润报表"分润调账统计"和"分润冻结统计"接口
	 * @param agentNo
	 * @param isFreeze
	 * true调用/agentProfitController/findAgentProfitFreezeCollection.do
	 * false调用/agentProfitController/findAgentProfitAdjustCollection.do
	 */
	public static Map<String, Object> findAgentProfitCollection(String agentNo, boolean isFreeze) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l;
		final long exp = iat + 300L;
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		claims.put("agentNo", StringUtils.trimToEmpty(agentNo));
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		String result;
		if (isFreeze){
			result = new ClientInterface(Constants.FIND_AGENT_PROFIT_FREEZE_COLLECTION, params).postRequest();
		}else{
			result = new ClientInterface(Constants.FIND_AGENT_PROFIT_ADJUST_COLLECTION, params).postRequest();
		}
		if (StringUtils.isNotBlank(result)){
			Gson gson = new Gson();
			Map<String, Object> map = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
			}.getType());
			if (map != null && map.get("data") != null && StringUtils.isNotBlank(map.get("data").toString())){
				Map<String, Object> dataMap = gson.fromJson(map.get("data").toString(), new TypeToken<Map<String, Object>>() {
				}.getType());
				return dataMap;
			}
		}

		return null;
	}
	
	//信用卡还款-查询用户金额
		public static String repayAccountAmountInfo(String merchantNo){
			final HashMap<String, String> claims = new HashMap<>();
			claims.put("merNo", merchantNo);
			log.info("请求路径：{},参数：{}",Constants.REPAY_ACCOUNT_AMOUNT_INFO, JSONObject.toJSONString(claims));
			String returnStr = new ClientInterface(Constants.REPAY_ACCOUNT_AMOUNT_INFO, claims).postRequest();
			log.info("返回结果：{}", returnStr);
			return returnStr;
		}

	public static String createAgentAccountBySubjectNo(String agentNo, String subjectNo) {
		Map<String,Object> claims=new HashMap<>();
		claims.put("accountType","A");
		claims.put("userId",agentNo);
		final long iat = System.currentTimeMillis() / 1000; // issued at claim
		claims.put("exp", iat+600L);
		claims.put("iat", iat);
		claims.put("subjectNo",subjectNo);
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		return new ClientInterface(Constants.ACCOUNT_CREATE_EXT_ACCOUNT_BY_SUBJECTNO_URL, params).postRequest();
	}
	public static String getMerchantAccountAllBalance(String merNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		//claims.put("subjectNo", "224105");
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_ALL_BALANCE_URL, claims);
	}

	public static String getBalance(String userId, String userType, String subjectNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", userType);
		claims.put("userId", userId);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");
		log.info("用户账户信息请求参数:{}", JSONObject.toJSONString(claims));
		return baseClient(Constants.ACCOUNT_BALANCE_URL, claims);
	}

	public static String baseClient(String host,Map<String,Object> map){
		final long iat = System.currentTimeMillis() / 1000l; 
		map.put("exp", iat+300L);
		map.put("iat", iat);
		map.put("jti",UUID.randomUUID().toString());
		Map<String,String> params=new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(map));
		log.info("请求路径：{},参数：{}",host, JSONObject.toJSONString(map));
		String returnStr = new ClientInterface(host, params).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}
	/**
	 * 红包余额提现
	 * @param agentNo
	 * @param redBalance
	 * @return
	 */
	public static String updateWithdrawRedBalance(String agentNo,String redBalance,Date date) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("fromSystem", "superbank");//来源系统  固定
        claims.put("transDate", sf1.format(date));//发生时间
        claims.put("amount", redBalance);//发生金额
        claims.put("fromSerialNo", "AGENT" + sf2.format(date));//来源系统流水号 唯一 可用提现订单
    	claims.put("agentNo", agentNo);//品牌商编号
        claims.put("transTypeCode", "000067");//交易码 固定 红包账户提现代码 000067
        claims.put("transOrderNo", "AGENT" + sf2.format(date));//提现订单号

        log.info("请求接口url=======>" + Constants.WHITHDRAW_REDBALANCE);
		String result = ClientInterface.postAccountApi(Constants.WHITHDRAW_REDBALANCE, claims);
		return result;
	}
	public static String savePerAgent(AgentInfo entityAgentInfo,AgentInfo paramAgentInfo,String agentNo){
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,String> claims=new HashMap<>();
		claims.put("timeStamp",sf1.format(new Date()));
		claims.put("regType","agent");//regType 必填，注册类型 app 人人代理商app 注册 h5 H5连接注册 ,boss 代理商注册,agent 代理商注册 ,merchant 商户注册
		claims.put("mobile",paramAgentInfo.getMobilephone());
//		claims.put("password",new Md5PasswordEncoder().encodePassword("123456",paramAgentInfo.getMobilephone()));
		claims.put("brandCode",entityAgentInfo.getAgentOem());//所属品牌,取一级的
		claims.put("userType","2");//用户类型 1-机构，2-大盟主，3-盟主
		claims.put("recCode",entityAgentInfo.getUserCode());//推荐编码,盟友编号传当前登录代理商的userCode
		claims.put("agentNo",agentNo);//新增代理商编号
		claims.put("agentNode",entityAgentInfo.getAgentNode() + agentNo+"-");//新增代理商节点
		claims.put("creater",entityAgentInfo.getAgentName());//创建人 UrlEncodedFormEntity setParams2
		claims.put("sign",reqSign(claims));
		log.info("===请求接口地址url=====>" + Constants.PERAGENT_TOREG);
		return new ClientInterface(Constants.PERAGENT_TOREG, claims).postRequestUtf8();
	}
	public static String reqSign(Map<String,String> param){
		String key = "f2d7caf72ff9084460e431a7122d1fe5";
		String jsonStr = JSONObject.toJSONString(param);
		SortedMap<String,String> sortedMap = JSONObject.parseObject(jsonStr,SortedMap.class);
		String str = "";
		for(String keyStr : sortedMap.keySet() ){
			if (keyStr.equals("sign")) {
				continue;
			}
			str += keyStr;
			str += "=";
			str += sortedMap.get(keyStr);
			str += "&";
		}
		str += key;
		return Md5.md5Str(str);
	}

	//商户进件,调伟哥接口
	public static String saveMerchantInfo(String sn,String merchantNo){
		Map<String,String> claims=new HashMap<>();
		claims.put("sn",sn);
		claims.put("merchantNo",merchantNo);
		log.info("===请求接口地址url=====>" + Constants.INSERT_MERCHANTINFO);
		return new ClientInterface(Constants.INSERT_MERCHANTINFO, claims).postRequestUtf8();
	}
	/**
	 * 机具下发,调明聪人人代理接口
	 * @param entityAgentInfo 当前登录代理商
	 * @param info 接收机具的代理商
	 * @param user_type 用户类型 1-机构，2-盟主，3-盟友
	 * @param sn 机具号
	 * @param userCode 
	 * @return
	 */
	public static String lowerHairTermianl(AgentInfo entityAgentInfo,AgentInfo info,String user_type,String sn,String userCode){
		Map<String,String> claims=new HashMap<>();
		claims.put("user_code",userCode);
		claims.put("current_user_code",entityAgentInfo.getUserCode());
		claims.put("agent_no",info.getAgentNo());
		claims.put("agent_node",info.getAgentNode());
		claims.put("user_type",user_type);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sn", sn);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		claims.put("sn_list_array",jsonArray.toString());//[{"sn":"12154"}]
		log.info("===请求接口地址url=====>" + Constants.LOWERHAIR_TERMIANL);
		return new ClientInterface(Constants.LOWERHAIR_TERMIANL, claims).postRequest();
	}
	
	/**
	 * 调用core接口，绑定或解绑机具
	 *
	 * @param url
	 * @param sn
	 * @param merNo
	 * @param mbpId
	 * @param type
	 * @return
	 */
	public static String bindingOrUnBindTerminal(String url, String sn, String merNo, String mbpId, String type) {
		Map<String, String> claims = new HashMap<>();
		claims.put("sn", sn);
		claims.put("merchantNo", merNo);
		claims.put("mbpId", mbpId);
		claims.put("type", type);
		log.info("调用机具的绑定或解绑,url:{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果:{}", returnStr);
		return returnStr;
	}
	/**
	 * 获取商户账号余额
	 *
	 * @param merNo
	 * @return
	 */
	public static String getMerchantAccountBalance(String merNo, String subjectNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_BALANCE_URL, claims);
	}

	public static String getRegisterSource(String url, String token,String projectName,Map<String,Object> map,List<Map<String, Object>> list) throws UnsupportedEncodingException {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("distinct_id", map.get("user_id"));
		jsonObject.put("time", new Date());
		jsonObject.put("type", "profile_set");

		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("merchant_no", map.get("merchant_no"));
		claims.put("gender", map.get("gender"));
		claims.put("submit_time", sf1.format(map.get("submit_time")));
		claims.put("$city", map.get("$city"));
		for(int i=0;i<list.size();i++) {
			claims.put("business_product"+(i+1), list.get(i).get("bp_name"));
			claims.put("product_type"+(i+1), list.get(i).get("type_name"));
		}
		claims.put("agent_no", map.get("agent_no"));
		claims.put("merchant_status", map.get("merchant_status"));
		claims.put("sales", map.get("sales"));
		claims.put("user_type", map.get("user_type"));
		claims.put("user_id", map.get("user_id"));
		claims.put("$province", map.get("$province"));
		claims.put("bank_name", map.get("bank_name"));
		claims.put("recmand_source", map.get("recmand_source"));
		claims.put("sign_source", map.get("sign_source"));
		claims.put("orgnize_id", map.get("orgnize_id"));
		claims.put("vip_level", map.get("vip_level"));
		claims.put("first_level_agent_no", map.get("first_level_agent_no"));
		claims.put("registration_time", sf1.format(map.get("registration_time")));
		claims.put("birthday", map.get("birthday"));
		claims.put("birthyear", map.get("birthyear"));
		claims.put("teamEntryId", map.get("teamEntryId"));
		claims.put("source_system", map.get("source_system"));
		claims.put("merchant_type", map.get("merchant_type"));

		jsonObject.put("properties", claims);

		log.info("请求路径：{},参数：{}",url, JSONObject.toJSONString(jsonObject));
		String base64= Base64.encode(jsonObject.toJSONString());
		String urlEncode=URLEncoder.encode(base64,"UTF-8");
		final String finalUrl=String.format("%s?project=%s&token=%s&data=%s&zip=0",url,projectName,token,urlEncode);
		log.info("===请求接口地址url=====>" + finalUrl);
		String returnStr=new ClientInterface(finalUrl, null).getRequest2();
		log.info("返回结果:{}", returnStr);
		return returnStr;
	}


	public static String cjtMerToCjtMer(String url,String merchantNo,String sn,String signKey) {
		url+="/cjt/merToCjtMer";
		final HashMap<String, String> claims = new HashMap<String, String>();
		claims.put("merchantNo", merchantNo);
		claims.put("sn", sn);
		claims.put("signData", ASCIISignUtil.sortASCIISign(claims,signKey));
		log.info("===请求接口地址url=====>" + url);
		log.info("请求路径：{},参数：{}",url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果:{}", returnStr);
		return returnStr;
	}

	public static String updateVipInfo(String url,MerchantInfo merchantInfo,String telNo,String businessNo,String teamId,String key) {
		url+="/vipinfo/updateVipInfo";
		Map<String,Object> leaguerInfo = new HashMap<>();
		leaguerInfo.put("leaguerName", merchantInfo.getMerchantName());
		leaguerInfo.put("businessNo", businessNo);
		leaguerInfo.put("originUserNo", merchantInfo.getMerchantNo());
		leaguerInfo.put("mobilePhone", telNo);
		leaguerInfo.put("newMobilePhone", merchantInfo.getMobilephone());// 修改手机号是传新手机号
		leaguerInfo.put("idCardNo", merchantInfo.getIdCardNo());
		leaguerInfo.put("teamId",teamId);
		String data = JSON.toJSONString(leaguerInfo);
		Long timestamp=DateUtil.dateToUnixTimestamp();
		String signStr="businessNo="+businessNo+"&data="+data+"&timestamp="+timestamp+key;
		//log.info("------------"+signStr);
		String sign = Md5.md5Str(signStr);
		Map<String,String> params = new HashMap<>();
		params.put("businessNo",businessNo);
		params.put("data",data);
		params.put("sign",sign);
		params.put("timestamp",timestamp+"");
		log.info("请求路径：{},参数：{}", url, params);
		String returnMsg = new ClientInterface(url, params).postRequestUtf8();
		log.info("返回结果：{}", returnMsg);
		return returnMsg;
	}
}
