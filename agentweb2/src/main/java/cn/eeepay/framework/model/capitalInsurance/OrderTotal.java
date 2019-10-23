package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/7/24/024.
 * @author  liuks
 * 订单汇总
 */
public class OrderTotal {

    private Integer countTotal;//保险订单总笔数
    private BigDecimal nPrmTotal;//保费-售价汇总

    private BigDecimal shareAmountTotal;//分润汇总
    private BigDecimal shareAmountAccTotal;//已入账
    private BigDecimal shareAmountNoAccTotal;//未入账


    public Integer getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }

    public BigDecimal getnPrmTotal() {
        return nPrmTotal;
    }

    public void setnPrmTotal(BigDecimal nPrmTotal) {
        this.nPrmTotal = nPrmTotal;
    }

    public BigDecimal getShareAmountTotal() {
        return shareAmountTotal;
    }

    public void setShareAmountTotal(BigDecimal shareAmountTotal) {
        this.shareAmountTotal = shareAmountTotal;
    }

    public BigDecimal getShareAmountAccTotal() {
        return shareAmountAccTotal;
    }

    public void setShareAmountAccTotal(BigDecimal shareAmountAccTotal) {
        this.shareAmountAccTotal = shareAmountAccTotal;
    }

    public BigDecimal getShareAmountNoAccTotal() {
        return shareAmountNoAccTotal;
    }

    public void setShareAmountNoAccTotal(BigDecimal shareAmountNoAccTotal) {
        this.shareAmountNoAccTotal = shareAmountNoAccTotal;
    }
}
