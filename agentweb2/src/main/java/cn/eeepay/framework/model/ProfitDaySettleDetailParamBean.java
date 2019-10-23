package cn.eeepay.framework.model;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */
public class ProfitDaySettleDetailParamBean {
    private String startTransDate;
    private String endTransDate;
    private String startAgentProfitGroupTime;
    private String endAgentProfitGroupTime;
    private String collectionBatchNo;
    private String merchant;
    private String agentNo;
    private String pageNo;
    private String pageSize;
    private String isDeductionFee;

    public String getIsDeductionFee() {
        return isDeductionFee;
    }

    public void setIsDeductionFee(String isDeductionFee) {
        this.isDeductionFee = isDeductionFee;
    }

    public String getStartTransDate() {
        return startTransDate;
    }

    public void setStartTransDate(String startTransDate) {
        this.startTransDate = startTransDate;
    }

    public String getEndTransDate() {
        return endTransDate;
    }

    public void setEndTransDate(String endTransDate) {
        this.endTransDate = endTransDate;
    }

    public String getStartAgentProfitGroupTime() {
        return startAgentProfitGroupTime;
    }

    public void setStartAgentProfitGroupTime(String startAgentProfitGroupTime) {
        this.startAgentProfitGroupTime = startAgentProfitGroupTime;
    }

    public String getEndAgentProfitGroupTime() {
        return endAgentProfitGroupTime;
    }

    public void setEndAgentProfitGroupTime(String endAgentProfitGroupTime) {
        this.endAgentProfitGroupTime = endAgentProfitGroupTime;
    }

    public String getCollectionBatchNo() {
        return collectionBatchNo;
    }

    public void setCollectionBatchNo(String collectionBatchNo) {
        this.collectionBatchNo = collectionBatchNo;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
