package cn.eeepay.framework.enums;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年12月10日18:17:44
 * 出账记账状态
 */
public enum OutBillRecordStatus {
	/**
	 * 记账成功
	 */
	SUCCESS("SUCCESS"),
	/**
	 * 记账失败
	 */
	FAILURE("FAILURE"),
	/**
	 * 未记账
	 */
	NORCORD("NORECORD"),
	
	/**
	 * 已冲正
	 */
	CHONGZHENGED("CHONGZHENGED");
	
	public String toString() {
		return code.toString();
	}
	// 定义私有变量
    private String code ;

    // 构造函数，枚举类型只能为私有
    private OutBillRecordStatus(String _code) {
        this.code = _code;
    }	
}
