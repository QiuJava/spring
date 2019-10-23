package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum DebitCreditSide {

	/**
	 * 借方
	 * 
	 * */
	DEBIT("debit"),

	/**
	 * 贷方
	 * 
	 * */
	CREDIT("credit"),
	/**
	 * 冻结
	 */
	FREEZE("freeze"),
	
	/**
	 * 解冻
	 */
	UNFREEZE("unFreeze");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private DebitCreditSide(String _code) {
        this.code = _code;
    } 
}
