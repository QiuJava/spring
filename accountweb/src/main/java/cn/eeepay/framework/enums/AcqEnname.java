package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */

public enum AcqEnname {
	/**
	 * neweptok
	 * 银盛
	 */
	neweptok("neweptok"),
	/**
	 * TFB_API
	 * 天付宝
	 */
	TFB_API("TFB_API"),
	
	/**
	 * mo_pay
	 * 魔宝
	 */
	mo_pay("mo_pay"),
	/**
	 * gzms
	 * 广州民生
	 */
	gzms("gzms")
	;
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private AcqEnname(String _code) {
        this.code = _code;
    }
    public static AcqEnname getEnum(String _code) {
        if (_code != null) {
          for (AcqEnname b : AcqEnname.values()) {
            if (_code.equalsIgnoreCase(b.code)) {
              return b;
            }
          }
        }
        return null;
    }
}
