package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum InnerSumFlag {
	
	/**
	 * 日间单笔
	 * */
	RIJIANDANBI("0"),
	/**
	 * 日终单笔
	 * */
	RIZHONGDANBI("1"),
	/**
	 * 日终汇总
	 * */
	RIZHONGHUIZONG("2");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private InnerSumFlag(String _code) {
        this.code = _code;
    } 
}
