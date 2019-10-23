package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum AccountStatus {
	
	/**
	 * 正常 = 1
	 * */
	NORMAL("1"),
	/**
	 * 销户 = 1
	 * */
	DESTROY("2"),
	/**
	 * 冻结只进不出 = 3
	 */
	FREEZE_ONLY_IN_DENY_OUT("3"),
	/**
	 * 冻结不进不出 = 4
	 */
	FREEZE_DENY_IN_DENY_OUT("4");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private AccountStatus(String _code) {
        this.code = _code;
    } 
}
