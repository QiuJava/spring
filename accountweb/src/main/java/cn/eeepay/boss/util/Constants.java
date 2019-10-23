package cn.eeepay.boss.util;

import java.io.File;

public class Constants {
	
	// 批量预调账模板路径
	public static final String BACTH_ADJUST_TEMPLATE = "template" + File.separator + "bacthAdjustTemplate.xls";
	// 批量预冻结模板路径
	public static final String BACTH_FREEZE_TEMPLATE = "template" + File.separator + "bacthFreezeTemplate.xls";
	
	// 批量预冻结模板路径
	public static final String BATCH_UNFREEZE_TEMPLATE = "template" + File.separator + "batchUnfreezeTemplate.xls";

	// 批量预调账申请模板路径
	public static final String BACTH_ACTIVITY_ADJUST_TEMPLATE = "template" + File.separator + "bacthActivityAccAdjustmentTemplate.xls";

	//调账模板路径i
	public static final String ADJUST_ACCOUNT_TEMPLATE="template"+File.separator +"adjustAccount.xls";
	//业务调账模板路径
	public static final String BUSINESS_TEMPLATE="template"+File.separator +"businessAccount.xls";
	//出账单模板
	//public static final String OUT_ACCOUNT_BILL_YINSHENG_TEMPLAT"template"+File.separator +"outbill-xinyinsheng2003.xls";
	public static final String OUT_ACCOUNT_BILL_YINSHENG_TEMPLATE="template"+File.separator +"outbill-xinyinsheng2003.xls";
	//上传调账模板路径
	public static final String UPLOAD_TEMPLATE="/tmp/accountweb/upload"+File.separator ;
	//账务临时目录
	public static final String ACCOUNT_TEMP = "/tmp/accountweb"+File.separator;
	
	//FTP
	public static final String FTP_IP = "ftp.ip";
	public static final String FTP_PORT = "ftp.port";
	public static final String FTP_USERNAME = "ftp.username";
	public static final String FTP_PASSWORD = "ftp.password";
	public static final String FTP_NEWEPTOK_UPLOAD_DIR = "ftp.neweptok_uploadDir";
	public static final String FTP_NEWEPTOK_DOWNLOAD_DIR = "ftp.neweptok_downloadDir";
	
	//出款通道-新银盛
	public static final String NEWEPTOK = "neweptok";
	
	//对账接口调用
//	public static final String HTTPCONNECT_URL = "httpconnect.url" ;
//	public static final String HTTPCONNECT_SECRET = "httpconnect.secret";
	
	
//	public static final String HTTPCONNECT_URL = "httpconnect.url" ;
}
