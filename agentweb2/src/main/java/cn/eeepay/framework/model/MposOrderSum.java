package cn.eeepay.framework.model;

import java.math.BigDecimal;

public class MposOrderSum {

    private long orderCount;        //订单总数
    private BigDecimal orderAmountCount;    //订单总金额
    private long goodCount;         //商品总数量

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getOrderAmountCount() {
        return orderAmountCount;
    }

    public void setOrderAmountCount(BigDecimal orderAmountCount) {
        this.orderAmountCount = orderAmountCount;
    }

    public long getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(long goodCount) {
        this.goodCount = goodCount;
    }
}
