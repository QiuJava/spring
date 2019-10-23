package cn.eeepay.framework.model.peragent;

/**
 * @author MXG
 * create 2018/11/19
 */
public class PaTerInfo {
    private int id;
    private String userCode;
    private String agentNo;
    private int status;
    private String orderNo;
    private String sendLock;
    private String sn;
    private String merchantNo;
    private String createTime;
    private String lastUpdate;
    private String startTime;
    private int callbackLock;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSendLock() {
        return sendLock;
    }

    public void setSendLock(String sendLock) {
        this.sendLock = sendLock;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getCallbackLock() {
        return callbackLock;
    }

    public void setCallbackLock(int callbackLock) {
        this.callbackLock = callbackLock;
    }
}
