package cn.eeepay.framework.model.bill;

import java.io.Serializable;

/**
 * 币种信息
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class Currency implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String currencyNo;

    private String currencyName;

    public String getCurrencyNo() {
        return currencyNo;
    }

    public void setCurrencyNo(String currencyNo) {
        this.currencyNo = currencyNo == null ? null : currencyNo.trim();
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName == null ? null : currencyName.trim();
    }
}