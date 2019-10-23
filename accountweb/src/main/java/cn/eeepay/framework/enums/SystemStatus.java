package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum SystemStatus {
	
	/**
	 * 日间运行状态
	 * */
	NORMAL("N"),
	/**
	 * 日切运行状态
	 * */
	CUTOFF("C"),
	/**
	 * 追帐运行状态
	 */
	APPEND("A"),
	/**
	 * 系统关闭
	 */
	SHUTDOWN("S");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private SystemStatus(String _code) {
        this.code = _code;
    } 
	
}
