package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/3/27
 * Time: 14:20
 * Description: 类注释
 */
public class YfbPayOrder implements Serializable{

    private Integer id;
    private BigDecimal transFeeRate;
    private String acqFeeRate;
    private BigDecimal acqFee;
    private String acqCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTransFeeRate() {
        return transFeeRate;
    }

    public void setTransFeeRate(BigDecimal transFeeRate) {
        this.transFeeRate = transFeeRate;
    }

    public String getAcqFeeRate() {
        return acqFeeRate;
    }

    public void setAcqFeeRate(String acqFeeRate) {
        this.acqFeeRate = acqFeeRate;
    }

    public BigDecimal getAcqFee() {
        return acqFee;
    }

    public void setAcqFee(BigDecimal acqFee) {
        this.acqFee = acqFee;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }
}
