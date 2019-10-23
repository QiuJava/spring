package cn.eeepay.framework.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
	public static final String sys_config_list_redis_key = "sys_config_list_redis_key";

	public static final String PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIn2zWqU7K/2qm5pOpq5bp9R+3MTnStWTfJU9nC/Vo7UKH9dITPvrELCTK+qlqpx5Fes+l0GY7n6u4n4jyiw4ejsvkZYQ5ww477yLOn2FcoEGuZEwPgSCmfTST0OFUgQqn+/J11k9L92jEHyieE3qmhMkMt0UsVUSJwx/nZxo30ZAgMBAAECgYBD3YHigeuEC4R+14iaf8jo2j0kuGtB3Cxvnlez0otTqw1YyYkBsU49cLKkXvfKVEgM0Ow/QltgKvSBxCE31PrrDka5TygVMqqA/IM7NrDvjUcGLjyoeNmLA8660fWcDxUTlAGN5kxIvUATayVwKVflpWPWu0FPKsWrZustnEo+4QJBAMCmYsWqAKWYMVRXFP3/XGRfio8DV793TOckyBSN9eh8UhgoZyT3u7oeHmDJEwm4aNMHlg1Pcdc6tNsvi1FRCiUCQQC3VNzfF4xOtUgX7vWPL8YVljLuXmy12iVYmg6ofu9l31nwM9FLQ1TRFglvF5LWrIXTQb07PgGd5DJMAQWGsqLlAkAPE7Z9M73TN+L8b8hDzJ1leZi1cpSGdoa9PEKwYR/SrxAZtefEm+LEQSEtf+8OfrEtetWCeyo0pvKKiOEFXytFAkEAgynL/DC0yXsZYUYtmYvshHU5ayFTVagFICbYZeSrEo+BoUDxdI9vl0fU6A5NmBlGhaZ65G+waG5jLc1tTrlvoQJAXBEoPcBNAosiZHQfYBwHqU6mJ9/ZacJh3MtJzGGebfEwJgtln5b154iANqNWXpySBLvkK+Boq7FYRiD83pqmUg==";
	public static final String USER_NO_SEQ ="user_no_seq";
	public static final String TEAM_ID ="999";
	public static final String PER_AGENT_TEAM_ID = "998";//人人代理组织id
	public static final String PER_AGENT_SUB_TYPE = "17";//人人代理机具款项余额提现
	
	//服务编号  service_name=SERXXX
	public static final String SERVICE_SEQ="service_name";
	public static final String SERVICE_SEQ_PR="SER";
	
	public static final String REQUIRE_SEQ = "require_name";
	
	public static final String PRODUCT_SEQ = "product_name";
	
	public static final String NOTICE_SEQ = "notice_name";
	
	public static final String BANNER_SEQ = "banner_name";
	
	public static final String USER_VALUE ="1000000000000000000";
	/*阿里云存储boss附件临时bucket*/
	public static final String ALIYUN_OSS_TEMP_TUCKET="boss-temp";
	/*阿里云存储boss附件bucket*/
	public static final String ALIYUN_OSS_ATTCH_TUCKET="agent-attch";
	/*阿里云存储移小宝电子小票签名bucket*/
	public static final String ALIYUN_OSS_SIGN_TUCKET="sign-img";
	/*阿里云存储bag附件bucket*/
	public static final String ALIYUN_OSS_BAG_TUCKET="eeepaybag";
	/*商户进件模块>进件成功提示语*/
	public static final String MER_ADD_SUCCESS="商户进件成功";
	/*商户进件模块>根据产品无服务*/
	public static final String MER_ADD_NOSERVICE="当前业务产品无服务";
	/*商户进件模块>根据产品无费率*/
	public static final String MER_ADD_NORATE="当前业务产品无费率";
	/*商户进件模块>根据产品无限额*/
	public static final String MER_ADD_NOQUOTA="当前业务产品无限额";
	/*商户进件模块>使用过的手机号*/
	public static final String MER_ADD_USERMOBILEPHONE="已使用相同手机号注册过";
	/*商户进件模块>使用过的身份证号*/
	public static final String MER_ADD_USERIDCARD="已使用相同身份证号注册过";
	
	public static final String MER_ADD_REQUIRE="当前业务产品无示例进件项";
	
	public static final String sys_dict_list_redis_key = "agent2:sys_dict_list_redis_key";
	public static final String sys_menu_not_blank_url_list_redis_key = "agent2:sys_menu_not_blank_url_list_redis_key";
	public static final String sys_menu_all_list_redis_key = "agent2:sys_menu_all_list_redis_key";
	public static final String user_setting_list_redis_key = "agent2:user_setting_list_redis_key";
	public static final Integer sys_menu_root_id = 0;

	public static final String AGENT_USER_SEQ = "user_no_seq";

	public static final String SUPER_BANK_SUBJECT_NO_MER = "224115";
	public static final String SUPER_BANK_SUBJECT_NO_AGENT = "224116";

	// 开通代理商全部帐号
	public static String ACCOUNT_CREATE_EXT_ACCOUNT_URL="http://192.168.3.30:7025/extAccountController/createDefaultExtAccount.do";

	public static String ACCOUNT_BALANCE_URL = "http://192.168.3.180:7025/extAccountController/findExtAccountBalance.do";

	public static String ACCOUNT_DETAIL_URL = "http://192.168.3.180:7025/extAccountController/findExtAccountTransInfoList.do";
	public static String NOWTRANSFER_HOST = "http://192.168.3.180:7030/flowmoney/transfer/nowTransfer";
	//	public static String NOWTRANSFER_HOST = "http://192.168.3.41:8888/flowmoney/transfer/nowTransfer";
	// 分润报表
	public static String ACCOUNT_CASH_URL = "http://192.168.3.180:7025/agentProfitController/findAgentProfitDaySettleList.do";
	// 分润报表汇总
	public static String ACCOUNT_CASH_COLLECTION_URL = "http://192.168.3.180:7025/agentProfitController/findAgentProfitDaySettleCollection.do";
	// 分润明细
	public static String PROFIT_DAY_SETTLE_DETAIL_LIST_URL = "http://192.168.3.180:7025/agentProfitController/findAgentProfitDaySettleDetailList.do";
	// 分润明细-导出
	public static String EXPORT_PROFIT_DAY_SETTLE_DETAIL_LIST_URL = "http://192.168.3.180:7025/agentProfitController/exportAgentProfitDaySettleDetailList.do";
	public static String ROUTE_CONFIG ;

	@Value("${route.config}")
	public void setRouteConfig(String routeConfig) {
		ROUTE_CONFIG = routeConfig;
	}

	//调用core，对机具进行绑定或解绑
	public static String BIND_OR_UNBIND = "/zfMerchant/merBindOrUnBindTmstpos";
		
	public static final String ACCOUNT_API_SECURITY="zouruijin";

	//超级推进件时调用core
	public static final String CJT_REGIST = "/cjt/merToCjtMer";

	public static String FIND_AGENT_PROFIT_ADJUST_COLLECTION = "http://192.168.1.182:7025/agentProfitController/findAgentProfitAdjustCollection.do";
	public static String FIND_AGENT_PROFIT_FREEZE_COLLECTION = "http://192.168.3.180:7025/agentProfitController/findAgentProfitFreezeCollection.do";
	public static String ACCOUNT_ALL_BALANCE_URL = "http://192.168.3.30:7025/extAccountController/findExtAccountAllBalance.do";
	public static String WHITHDRAW_REDBALANCE = null;
	public static String PERAGENT_TOREG = null;//注册人人代理
	public static String INSERT_MERCHANTINFO = null;//商户进件,人人代理
	public static String LOWERHAIR_TERMIANL = null;//下发机具,人人代理

	public static void setAccountCreateExtAccountUrl(String accountCreateExtAccountUrl) {
		ACCOUNT_CREATE_EXT_ACCOUNT_URL = accountCreateExtAccountUrl;
	}

	@Value("${SERVER.LOWERHAIR_TERMIANL}")
	public void setLOWERHAIR_TERMIANL(String lOWERHAIR_TERMIANL) {
		LOWERHAIR_TERMIANL = lOWERHAIR_TERMIANL;
	}

	@Value("${SERVER.INSERT_MERCHANTINFO}")
	public void setINSERT_MERCHANTINFO(String iNSERT_MERCHANTINFO) {
		INSERT_MERCHANTINFO = iNSERT_MERCHANTINFO;
	}

	@Value("${SERVER.PERAGENT_TOREG}")
	public void setPERAGENT_TOREG(String pERAGENT_TOREG) {
		PERAGENT_TOREG = pERAGENT_TOREG;
	}

	@Value("${SERVER.WHITHDRAW_REDBALANCE}")
	public void setWHITHDRAW_REDBALANCE(String wHITHDRAW_REDBALANCE) {
		WHITHDRAW_REDBALANCE = wHITHDRAW_REDBALANCE;
	}
	
	@Value("${SERVER.ACCOUNT_ALL_BALANCE_URL}")
	public void setACCOUNT_ALL_BALANCE_URL(String aCCOUNT_ALL_BALANCE_URL) {
		ACCOUNT_ALL_BALANCE_URL = aCCOUNT_ALL_BALANCE_URL;
	}
	
	@Value("${SERVER.FIND_AGENT_PROFIT_ADJUST_COLLECTION}")
	public void setFindAgentProfitAdjustCollection(String findAgentProfitAdjustCollection) {
		FIND_AGENT_PROFIT_ADJUST_COLLECTION = findAgentProfitAdjustCollection;
	}
	@Value("${SERVER.FIND_AGENT_PROFIT_FREEZE_COLLECTION}")
	public void setFindAgentProfitFreezeCollection(String findAgentProfitFreezeCollection) {
		FIND_AGENT_PROFIT_FREEZE_COLLECTION = findAgentProfitFreezeCollection;
	}
	@Value("${SERVER.ACCOUNT_CASH_COLLECTION_URL}")
	public void setAccountCashCollectionUrl(String accountCashCollectionUrl) {
		ACCOUNT_CASH_COLLECTION_URL = accountCashCollectionUrl;
	}

	@Value("${SERVER.ACCOUNT_CASH_URL}")
	public void setACCOUNT_CASH_URL(String aCCOUNT_CASH_URL) {
		Constants.ACCOUNT_CASH_URL = aCCOUNT_CASH_URL;
	}

	@Value("${SERVER.PROFIT_DAY_SETTLE_DETAIL_LIST_URL}")
	public void setProfitDaySettleDetailListUrl(String profitDaySettleDetailListUrl) {
		PROFIT_DAY_SETTLE_DETAIL_LIST_URL = profitDaySettleDetailListUrl;
	}
	@Value("${SERVER.EXPORT_PROFIT_DAY_SETTLE_DETAIL_LIST_URL}")
	public void setExportProfitDaySettleDetailListUrl(String exportProfitDaySettleDetailListUrl) {
		EXPORT_PROFIT_DAY_SETTLE_DETAIL_LIST_URL = exportProfitDaySettleDetailListUrl;
	}

	@Value("${SERVER.ACCOUNT_CREATE_EXT_ACCOUNT_URL}")
	public void setACCOUNT_CREATE_EXT_ACCOUNT_URL(String aCCOUNT_CREATE_EXT_ACCOUNT_URL) {
		Constants.ACCOUNT_CREATE_EXT_ACCOUNT_URL = aCCOUNT_CREATE_EXT_ACCOUNT_URL;
	}
	@Value("${SERVER.ACCOUNT_BALANCE_URL}")
	public void setACCOUNT_BALANCE_URL(String aCCOUNT_BALANCE_URL) {
		Constants.ACCOUNT_BALANCE_URL = aCCOUNT_BALANCE_URL;
	}
	@Value("${SERVER.ACCOUNT_DETAIL_URL}")
	public void setACCOUNT_DETAIL_URL(String aCCOUNT_DETAIL_URL) {
		Constants.ACCOUNT_DETAIL_URL = aCCOUNT_DETAIL_URL;
	}
	@Value("${SERVER.NOWTRANSFER_HOST}")
	public void setNOWTRANSFER_HOST(String nOWTRANSFER_HOST) {
		Constants.NOWTRANSFER_HOST = nOWTRANSFER_HOST;
	}

	/**
	 * 代理商预冻结查询
	 */
	public static String PRELIMINARY_FREEZE_QUERY_URL = "";
	@Value("${SERVER.PRELIMINARY_FREEZE_QUERY_URL}")
	public void setPRELIMINARY_FREEZE_URL(String pRELIMINARY_FREEZE_QUERY_URL){Constants.PRELIMINARY_FREEZE_QUERY_URL = pRELIMINARY_FREEZE_QUERY_URL;}
	/**
	 * 代理商预冻结导出(导出时，不需要传pageSize等)
	 */
	public static String PRELIMINARY_FREEZE_EXPORT_URL = "";
	@Value("${SERVER.PRELIMINARY_FREEZE_EXPORT_URL}")
	public void setPRELIMINARY_FREEZE_EXPORT_URL(String pRELIMINARY_FREEZE_EXPORT_URL){Constants.PRELIMINARY_FREEZE_EXPORT_URL = pRELIMINARY_FREEZE_EXPORT_URL;}

	/**
	 * 解冻明细查询
	 */
	public static String UN_FREEZE_QUERY_URL = "";
	@Value("${SERVER.UN_FREEZE_QUERY_URL}")
	public void setUN_FREEZE_QUERY_URL(String uN_FREEZE_QUERY_URL){Constants.UN_FREEZE_QUERY_URL = uN_FREEZE_QUERY_URL;}
	/**
	 * 解冻明细导出
	 */
	public static String UN_FREEZE_EXPORT_URL = "";
	@Value("${SERVER.UN_FREEZE_EXPORT_URL}")
	public void setUN_FREEZE_EXPORT_URL(String uN_FREEZE_EXPORT_URL){Constants.UN_FREEZE_EXPORT_URL = uN_FREEZE_EXPORT_URL;}

	/**
	 * 代理商预调账查询
	 */
	public static String PRELIMINARY_ADJUST_QUERY_URL = "";
	@Value("${SERVER.PRELIMINARY_ADJUST_QUERY_URL}")
	public void setPRELIMINARY_ADJUST_URL(String pRELIMINARY_ADJUST_QUERY_URL){Constants.PRELIMINARY_ADJUST_QUERY_URL = pRELIMINARY_ADJUST_QUERY_URL;}
	/**
	 * 代理商预调账导出(导出时，不需要传pageSize等)
	 */
	public static String PRELIMINARY_ADJUST_EXPORT_URL = "";

	@Value("${SERVER.PRELIMINARY_ADJUST_EXPORT_URL}")
	public void setPRELIMINARY_ADJUST_EXPORT_URL(String pRELIMINARY_ADJUST_EXPORT_URL){Constants.PRELIMINARY_ADJUST_EXPORT_URL = pRELIMINARY_ADJUST_EXPORT_URL;}

	//信用卡还款，查询账户金额信息
	//public static String REPAY_ACCOUNT_AMOUNT_INFO = "http://192.168.4.13:8082/merchant/accInfo";
	public static String REPAY_ACCOUNT_AMOUNT_INFO = "";
	@Value("${SERVER.REPAY_ACCOUNT_AMOUNT_INFO}")
	public void setREPAY_ACCOUNT_AMOUNT_INFO(String REPAY_ACCOUNT_AMOUNT_INFO) {
		Constants.REPAY_ACCOUNT_AMOUNT_INFO = REPAY_ACCOUNT_AMOUNT_INFO;
	}
	// 根据科目号开户
	public static String ACCOUNT_CREATE_EXT_ACCOUNT_BY_SUBJECTNO_URL = "";
	@Value("${SERVER.ACCOUNT_CREATE_EXT_ACCOUNT_BY_SUBJECTNO_URL}")
	public void setAccountCreateExtAccountBySubjectnoUrl(String accountCreateExtAccountBySubjectnoUrl) {
		ACCOUNT_CREATE_EXT_ACCOUNT_BY_SUBJECTNO_URL = accountCreateExtAccountBySubjectnoUrl;
	}
	
	public static String accountTransTypeByAgentType="";
	@Value("${SERVER.accountTransTypeByAgentType}")
	public void setAccountTransTypeByAgentType(String accountTransTypeByAgentType) {
		Constants.accountTransTypeByAgentType = accountTransTypeByAgentType;
	}
	
	public static String happyBackNotFullDeductUrl="";
	@Value("${SERVER.happyBackNotFullDeductUrl}")
	public void setHappyBackNotFullDeductUrl(String happyBackNotFullDeductUrl) {
		Constants.happyBackNotFullDeductUrl = happyBackNotFullDeductUrl;
	}
	public static String happyBackNotFullDeductSumUrl="";
	@Value("${SERVER.happyBackNotFullDeductSumUrl}")
	public void setHappyBackNotFullDeductSumUrl(String happyBackNotFullDeductSumUrl) {
		Constants.happyBackNotFullDeductSumUrl = happyBackNotFullDeductSumUrl;
	}
	public static String happyBackNotFullDeductExportUrl="";
	@Value("${SERVER.happyBackNotFullDeductExportUrl}")
	public void setHappyBackNotFullDeductExportUrl(String happyBackNotFullDeductExportUrl) {
		Constants.happyBackNotFullDeductExportUrl = happyBackNotFullDeductExportUrl;
	}
	
}
