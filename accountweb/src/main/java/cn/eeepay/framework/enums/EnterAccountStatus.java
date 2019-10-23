package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年4月28日14:26:03
 * 代理商分润入账状态
 *
 */
public enum EnterAccountStatus {

	/**
	 * 已入账
	 * 
	 * */
	ENTERACCOUNTED("ENTERACCOUNTED"),

	/**
	 * 未入账
	 */
	NOENTERACCOUNT("NOENTERACCOUNT");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private EnterAccountStatus(String _code) {
        this.code = _code;
    }
    public static EnterAccountStatus getEnum(String _code) {
        if (_code != null) {
          for (EnterAccountStatus b : EnterAccountStatus.values()) {
            if (_code.equalsIgnoreCase(b.code)) {
              return b;
            }
          }
        }
        return null;
    }

}
