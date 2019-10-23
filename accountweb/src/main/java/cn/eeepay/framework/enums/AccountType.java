package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

public enum AccountType {
	/**
	 * 商户
	 */
	MERCHANT("M"),
	/**
	 * 代理商
	 */
	AGENT("A"),
	/**
	 * 收单
	 */
	ACQ("Acq");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private AccountType(String _code) {
        this.code = _code;
    }
}
