package cn.eeepay.framework.enums;

/**
 * 调账类型
 * @author Administrator
 *
 */
public enum AccountTypeEnum {
	ONE("1479696814454"),
	TWO("1479697001500"),
	THREE("1479697574809");
	
	public String getValue() {
		return code;
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private AccountTypeEnum(String _code) {
        this.code = _code;
    }
}
