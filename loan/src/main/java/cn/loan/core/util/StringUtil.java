package cn.loan.core.util;

/**
 * 字符串工具
 * 
 * @author Qiujian
 * 
 */
public class StringUtil {
	private StringUtil() {
	}

	public static final String UTF8 = "utf-8";
	public static final String EMPTY = "";
	public static final String DOT = ".";
	public static final String COLON = ":";
	public static final String USERNAME = "username";
	public static final String MSG = "msg";
	public static final String LOGIN_TIME = "loginTime";
	public static final String PER_CENT = "%";
	public static final String LOGIN_STATUS = "loginStatus";
	public static final String PAGE_RESULT = "pageResult";
	public static final String ITEM_NAME = "itemName";
	public static final String DICT_NAME = "dictName";
	public static final String ITEM_KEY = "itemKey";
	public static final String DICT_KEY = "dictKey";
	public static final String PREFIX = "ROLE_";
	public static final String PERMISSIONS = "permissions";
	public static final String SYSTEM_DICTIONARY = "systemDictionary";
	public static final String SEQUENCE = "sequence";
	public static final String QO = "qo";
	public static final String ITEMS = "items";
	public static final String USER_INFO = "userInfo";
	public static final String EDUCATION_BACKGROUNDS = "educationBackgrounds";
	public static final String INCOME_GRADES = "incomeGrades";
	public static final String CREDIT_BORROW_SCORE = "creditBorrowScore";
	public static final String INIT_BORROW_LIMIT = "initBorrowLimit";
	public static final String ACCOUNT = "account";
	public static final String LOGIN_LOG = "loginLog";
	public static final String SYSTEM_DICTIONARY_LIST = "systemDictionaryList";
	public static final String BANK_CARD = "bankCard";
	public static final String REAL_AUTH = "realAuth";
	public static final String GENDERS = "genders";
	public static final String SUBMISSION_TIME = "submissionTime";
	public static final String AUDIT_STATUS = "auditStatus";
	public static final String USER_TYPE = "userType";
	public static final String SYSTEM_DICTIONARY_HASH = "system_dictionary_hash";
	public static final String UPLOAD = "upload";
	public static final String CREDIT_FILES = "creditFiles";
	public static final String FILE_TYPES = "fileTypes";
	public static final String SESSIONID = "sessionid";
	public static final String FILE = "file";
	public static final String MIN_BORROW_AMOUNT = "minBorrowAmount";
	public static final String MIN_BID_AMOUNT = "minBidAmount";
	public static final String SUBMITTER = "submitter";
	public static final String SERIAL_NUMBER = "serialNumber";
	public static final String COMPANY_BANK_CARD = "companyBankCard";
	public static final String SELF = "self";
	public static final String BORROW = "borrow";
	public static final String COMPANY_BANKCARDS = "companyBankCards";
	public static final String BORROW_AUDIT_HISTROY_LIST = "borrowAuditHistroyList";
	public static final String BORROW_STATUS = "borrowStatus";
	public static final String APPLY_TIME = "applyTime";
	public static final String PUBLISH_TIME = "publishTime";
	public static final String RETURN_TIME = "returnTime";
	public static final String BORROWER_ID = "borrowerId";

	public static boolean hasLength(String str) {
		return str != null && !EMPTY.equals(str);
	}

}
