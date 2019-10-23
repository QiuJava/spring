package cn.eeepay.framework.model.surveyOrder;

/**
 * 催单记录
 * @author MXG
 * create 2018/09/12
 */
public class SurveyUrgeRecord {
    private String id;
    private String orderNo;
    private String agentNode;
    private String haveLookNo;
    private String operator;
    private String createTime;
    private String lastUpdateTime;
    private String transOrderNo;//订单编号
    private String surveyOrderId;//调单id
    private String time;//现在距离创建时间
    private String msg;//催单提示语，取自字典表
    private String currentTime;//当前时间

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getHaveLookNo() {
        return haveLookNo;
    }

    public void setHaveLookNo(String haveLookNo) {
        this.haveLookNo = haveLookNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getSurveyOrderId() {
        return surveyOrderId;
    }

    public void setSurveyOrderId(String surveyOrderId) {
        this.surveyOrderId = surveyOrderId;
    }
}
