package cn.loan.core.util;

import java.math.BigDecimal;
import java.util.List;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.SystemDictionaryItem;

/**
 * 系统字典工具
 * 
 * @author qiujian
 *
 */
public class SystemDictionaryUtil {
	private SystemDictionaryUtil() {
	}

	public static final String USER_TYPE = "user_type";
	public static final String USER_TYPE_MANAGE = "user_type_manage";
	public static final String USER_TYPE_WEBSITE = "user_type_website";

	public static final String USER_STATUS = "user_status";
	public static final String USER_STATUS_NORMAL = "user_status_normal";
	public static final String USER_STATUS_LOCK = "user_status_lock";

	public static final String INIT = "init";
	public static final String INIT_USERNAME = "init_username";
	public static final String INIT_PASSWORD = "init_password";
	public static final String INIT_CREDIT_SCORE = "init_credit_score";
	public static final String INIT_BORROW_LIMIT = "init_borrow_limit";
	public static final String INIT_MIN_BORROW_AMOUNT = "init_min_borrow_amount";
	public static final String INIT_MIN_BID_AMOUNT = "init_min_bid_amount";
	public static final String INIT_MIN_BORROW_RATE = "init_min_borrow_rate";
	public static final String INIT_MAX_BORROW_RATE = "init_max_borrow_rate";
	public static final String INIT_SERVICE_FEE_RATE = "init_service_fee_rate";
	public static final String MIN_WITHDRAW_AMOUNT = "min_withdraw_amount";

	public static final String LOGIN_STATUS = "login_status";
	public static final String LOGIN_STATUS_SUCCESS = "login_status_success";
	public static final String LOGIN_STATUS_FAILURE = "login_status_failure";

	public static final String LOGIN_LOCKING = "login_locking";
	public static final String LOGIN_LOCKING_COUNT = "login_locking_count";
	public static final String LOGIN_LOCKING_TIME_SECOND = "login_locking_time_second";

	public static final String EDUCATION_BACKGROUND = "education_background";
	public static final String INCOME_GRADE = "income_grade";

	public static final String GENDER_MAN = "gender_man";
	public static final String GENDER_WOMAN = "gender_woman";
	public static final String GENDER = "gender";

	public static final String AUDIT_NORMAL = "audit_normal";
	public static final String AUDIT_PASS = "audit_pass";
	public static final String AUDIT_REJECT = "audit_reject";
	public static final String AUDIT = "audit";

	public static final String CREDIT_FILE_TYPE = "credit_file_type";
	public static final String REAL_ESTATE_WARRANT = "real_estate_warrant";
	public static final String BUSINESS_LICENSE = "business_license";
	public static final String ACCOUNT_STATEMENT = "account statement";
	public static final String ZHIMA_CREDIT = "zhima_credit";
	public static final String GRADUATION_CERTIFICATE = "graduation_certificate";

	public static final String BORROW_STATUS = "borrow_status";
	public static final String RELEASE_BEFORE = "release_before";
	public static final String RELEASE_IN = "release_in";
	public static final String RELEASE_REJECT = "release_reject";
	public static final String PAY_OFF = "pay_off";

	public static final String REPAYMENT_METHOD = "repayment_method";
	public static final String MONTH_INSTALMENT = "month_instalment";
	public static final String MONTH_EXPIRE = "month_expire";

	public static final String AUDIT_TYPE = "audit_type";
	public static final String PUSH_AUDIT = "push_audit";
	public static final String FULL_AUDIT = "full_audit";
	public static final String BID_FULL = "bid_full";
	public static final String PAYMENT_IN = "payment_in";
	public static final String FULL_AUDIT_REJECT = "full_audit_reject";

	public static final String REPAYMENT_STATUS = "repayment_status";
	public static final String REPAYMENT_NORMAL = "repayment_normal";
	public static final String REPAYMENT_PAYBACK = "repayment_payback";
	public static final String REPAYMENT_OVERDUE = "repayment_overdue";

	public static final String ACCOUNT_FLOW_TYPE = "account_flow_type";
	public static final String BID_ACTION = "bid_action";
	public static final String SERVICE_FEE_ACTION = "service_fee_action";
	public static final String RECHARGE_ACTION = "recharge_action";
	public static final String BORROW_POST_ACTION = "borrow_post_action";
	public static final String CANCEL_BID_ACTION = "cancel_bid_action";
	public static final String BID_SUCCESS_ACTION = "bid_success_action";
	public static final String REPAYMENT_ACTION = "repayment_action";
	public static final String RECEIPT_ACTION = "receipt_action";
	public static final String WITHDRAW_APPLY_ACTION = "withdraw_apply_action";
	public static final String WITHDRAW_SUCCESS_ACTION = "withdraw_success_action";
	public static final String WITHDRAW_FAILED_ACTION = "withdraw_failed_action";
	public static final String WITHDRAW_SERVICE_FEE_ACTION = "withdraw_service_fee_action";
	
	public static final String SYSTEM_ACCOUNT_FLOW_TYPE = "system_account_flow_type";
	public static final String BORROW_SERVICE_FEE_ACTION = "borrow_service_fee_action";
	public static final String WITHDRAW_SERVICE_CHARGE_ACTION = "withdraw_service_charge_action";

	public static String getItemValue(String itemKey, SystemDictionary dict) {
		List<SystemDictionaryItem> items = dict.getSystemDictionaryItems();
		for (SystemDictionaryItem item : items) {
			if (item.getItemKey().equals(itemKey)) {
				return item.getItemValue();
			}
		}
		return null;
	}

	public static Integer getItemValue(String dictKey, String itemKey, SystemDictionaryHashService service) {
		SystemDictionary dict = service.get(dictKey);
		String itemValue = getItemValue(itemKey, dict);
		return Integer.valueOf(itemValue);
	}

	public static BigDecimal getItemValueBigDecimal(String dictKey, String itemKey,
			SystemDictionaryHashService service) {
		SystemDictionary dict = service.get(dictKey);
		String itemValue = getItemValue(itemKey, dict);
		return new BigDecimal(itemValue);
	}

	public static Integer getDictValue(String dictKey, SystemDictionaryHashService service) {
		SystemDictionary dictionary = service.get(dictKey);
		String dictValue = dictionary.getDictValue();
		return Integer.valueOf(dictValue);
	}

	public static List<SystemDictionaryItem> getItems(String dictKey, SystemDictionaryHashService service) {
		SystemDictionary dictionary = service.get(dictKey);
		return dictionary.getSystemDictionaryItems();
	}

}
