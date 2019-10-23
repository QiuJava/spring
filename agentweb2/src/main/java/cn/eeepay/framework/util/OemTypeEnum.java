package cn.eeepay.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by 666666 on 2017/8/24.
 */
public enum OemTypeEnum {
    SQIANBAO("YS",true),   // 盛钱包          http://agent.sqianbao.cn
    DIANFU("YS"),     // 点付，点付秘书  http://zhbpay.dianfupay.cn
    ZYFPAY("YS"),     // 自由付-自由代   http://zyf.yfbpay.cn
    YABPAY("YS"),     // 宏博宏宇-安信宝 http://axbpay.yfbpay.cn
    YLSTCZB("YS"),          // 移联商通成长版 http://ylstczbpay.yfbpay.cn
    YPOSPAY("YS"),          // 银pos 银惠宝 http://ypospay.yfbpay.cn
    REPAY("DEFAULT"),      // 信用卡超级还款  http://www.olvip.vip
    SUPERBANK("DEFAULT"),	// 超级银行家  	http://superbank.yfbpay.cn　
    REDEM("DEFAULT"),	    // 积分兑换  	    http://jfdh.yfbpay.cn　
    REDEMACTIVE("DEFAULT"),	    // 积分兑换激活版  	    http://jfdhjhb.yfbpay.cn　
    PERAGENT("DEFAULT"),	    // 人人代理
    ADMIN("DEFAULT"),      // 超级管理员
    ZHFPAY("ZF"),     // 中和付,中代宝   http://zhfpay.yfhpay.cn
    ZHBPAY("ZF"),     // 中和宝,返现宝   http://zhbpay.yfbpay.cn
    ZHZFPAY("ZF"),     // 中和支付-中和合伙人 http://zhzfpay.yfbpay.cn
    ZHFPLUSPAY("ZF"),     // 超代宝  http://zhfppay.yfbpay.cn;
	THREE("THREE");	// 三方查询  http://agent.sqianbao.cn;
//    public final static String YS_TITLE = "银盛支付管理系统";
//    public final static String ZF_TITLE = "中付支付管理系统";
//    public final static String DEFAULT_TITLE = "代理商系统";

    private String transType;   // 交易通道,YS(银盛)/ZF(中付)/DEFAULT(默认)
    private Boolean hasSafePassword;// true 需要资金密码,false 不需要资金密码

    public Boolean getHasSafePassword() {
        return hasSafePassword;
    }

    OemTypeEnum(String transType) {
        this(transType, false);
    }

    OemTypeEnum(String transType,Boolean hasSafePassword) {
        this.transType = transType;
        this.hasSafePassword = hasSafePassword;
    }

    public String getTransType() {
        return transType;
    }

    public static OemTypeEnum getOemType(String oemType){
        try {
            return valueOf(StringUtils.upperCase(oemType));
        }catch (Exception e){
            return SQIANBAO;
        }
    }
}
