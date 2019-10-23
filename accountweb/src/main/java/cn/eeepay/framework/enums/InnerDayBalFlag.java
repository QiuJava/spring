package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum InnerDayBalFlag {
	
	/**
	 * 日间
	 * */
	RIJIAN("0"),
	/**
	 * 日终
	 * */
	RIZHONG("1");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private InnerDayBalFlag(String _code) {
        this.code = _code;
    } 
}
