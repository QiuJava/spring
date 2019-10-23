package cn.eeepay.framework.util;

import java.io.File;
import java.util.EnumMap;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class Constants {
	public static final String sys_dict_list_redis_key = "sys_dict_list_redis_key";
	public static final String sys_menu_not_blank_url_list_redis_key = "sys_menu_not_blank_url_list_redis_key";
	public static final String sys_menu_all_list_redis_key = "sys_menu_all_list_redis_key";
	public static final String user_setting_list_redis_key = "user_setting_list_redis_key";
	public static final Integer sys_menu_root_id = 0;
	public static final String secret = "zouruijin";
	public static final String refresh_cache_key = "refresh_cache_key";
	public static final String user_last_menu_code = "user_last_menu_code";
	//调账模板路径
	public static final String ADJUST_ACCOUNT_TEMPLATE="template"+File.separator +"adjustAccount.xls";
	//出账单模板
	public static final String OUT_ACCOUNT_BILL_YINSHENG_TEMPLATE="template"+File.separator +"outbill-yinsheng.xlsx";
	//上传调账模板路径
	public static final String UPLOAD_TEMPLATE="/upload"+File.separator ;
	//账务临时目录
	public static final String ACCOUNT_TEMP = "/account.temp"+File.separator;
	
	
	//出款通道-新银盛
	public static final String NEWEPTOK = "neweptok";

	//对账接口调用
	public static final String HTTPCONNECT_URL = "httpconnect.url" ;
	public static final String HTTPCONNECT_SECRET = "httpconnect.secret";
	
 
	
	
}
