package cn.eeepay.framework.model;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */
public class ProfitDaySettleDetailBean {
    private static Gson gson = new Gson();
    private String msg;
    private String name;
    private boolean status;
    private String data;
    private Data dataObject;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        if (StringUtils.isBlank(this.data)){
            return null;
        }else{
            return gson.fromJson(this.data, Data.class);
        }
    }

    public void setData(String data) {
        this.data = data;
    }

    public Data getDataObject() {
        return dataObject;
    }

    public void setDataObject(Data dataObject) {
        this.dataObject = dataObject;
    }

    public static class Data{
        private int total;
        private List<DataList> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataList> getList() {
            return list;
        }

        public void setList(List<DataList> list) {
            this.list = list;
        }
    }

    public static class DataList{
        private double acqOutCost;
        private double acqOutProfit;
        private String agentLevel;
        private String agentName;
        private String agentNo;
        private String agentNode;
        private String agentProfitCollectionStatus;
        private double agentShareAmount;
        private int businessProductId;
        private String businessProductName;
        private String cardType;
        private int hardwareProduct;
        private String hardwareProductName;
        private double merchantFee;
        private double merCashFee;
        private String merchantName;
        private String merchantNo;
        private String merchantRate;
        private String oneAgentName;
        private String oneAgentNo;
        private String plateOrderNo;
        private String saleName;
        private int serviceId;
        private String serviceName;
        private double transAmount;
        private long transTime;
        private double daiCost;
        private double dianCost;
        private String collectionBatchNo;
        private double cashAgentShareAmount;
        private double deductionFee;
        private String superPush;//是否为超级推商户 是 否
        private String payMethod;//交易类型 刷卡交易 无卡交易

        public double getDeductionFee() {
            return deductionFee;
        }

        public void setDeductionFee(double deductionFee) {
            this.deductionFee = deductionFee;
        }

        public double getCashAgentShareAmount() {
            return cashAgentShareAmount;
        }

        public void setCashAgentShareAmount(double cashAgentShareAmount) {
            this.cashAgentShareAmount = cashAgentShareAmount;
        }

        public String getCollectionBatchNo() {
            return collectionBatchNo;
        }

        public void setCollectionBatchNo(String collectionBatchNo) {
            this.collectionBatchNo = collectionBatchNo;
        }

        public double getDaiCost() {
            return daiCost;
        }

        public void setDaiCost(double daiCost) {
            this.daiCost = daiCost;
        }

        public double getDianCost() {
            return dianCost;
        }

        public void setDianCost(double dianCost) {
            this.dianCost = dianCost;
        }

        public double getMerCashFee() {
            return merCashFee;
        }

        public void setMerCashFee(double merCashFee) {
            this.merCashFee = merCashFee;
        }

        public double getAcqOutCost() {
            return acqOutCost;
        }

        public void setAcqOutCost(double acqOutCost) {
            this.acqOutCost = acqOutCost;
        }

        public double getAcqOutProfit() {
            return acqOutProfit;
        }

        public void setAcqOutProfit(double acqOutProfit) {
            this.acqOutProfit = acqOutProfit;
        }

        public String getAgentLevel() {
            return agentLevel;
        }

        public void setAgentLevel(String agentLevel) {
            this.agentLevel = agentLevel;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getAgentNo() {
            return agentNo;
        }

        public void setAgentNo(String agentNo) {
            this.agentNo = agentNo;
        }

        public String getAgentNode() {
            return agentNode;
        }

        public void setAgentNode(String agentNode) {
            this.agentNode = agentNode;
        }

        public String getAgentProfitCollectionStatus() {
            if (StringUtils.isBlank(agentProfitCollectionStatus)){
                return "";
            }else if (StringUtils.equalsIgnoreCase(agentProfitCollectionStatus, "COLLECTED")){
                return "已汇总";
            }else if(StringUtils.equalsIgnoreCase(agentProfitCollectionStatus, "NOCOLLECT")){
                return "未汇总";
            }else{
                return "";
            }
        }

        public void setAgentProfitCollectionStatus(String agentProfitCollectionStatus) {
            this.agentProfitCollectionStatus = agentProfitCollectionStatus;
        }

        public double getAgentShareAmount() {
            return agentShareAmount;
        }

        public void setAgentShareAmount(double agentShareAmount) {
            this.agentShareAmount = agentShareAmount;
        }

        public int getBusinessProductId() {
            return businessProductId;
        }

        public void setBusinessProductId(int businessProductId) {
            this.businessProductId = businessProductId;
        }

        public String getBusinessProductName() {
            return businessProductName;
        }

        public void setBusinessProductName(String businessProductName) {
            this.businessProductName = businessProductName;
        }

        public String getCardType() {
            if(StringUtils.equalsIgnoreCase(cardType, "1")){
                return "贷记卡";
            }else if(StringUtils.equalsIgnoreCase(cardType, "2")){
                return "借记卡";
            }else{
                return "未知";
            }
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public int getHardwareProduct() {
            return hardwareProduct;
        }

        public void setHardwareProduct(int hardwareProduct) {
            this.hardwareProduct = hardwareProduct;
        }

        public String getHardwareProductName() {
            return hardwareProductName;
        }

        public void setHardwareProductName(String hardwareProductName) {
            this.hardwareProductName = hardwareProductName;
        }

        public double getMerchantFee() {
            return merchantFee;
        }

        public void setMerchantFee(double merchantFee) {
            this.merchantFee = merchantFee;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getMerchantRate() {
            return merchantRate;
        }

        public void setMerchantRate(String merchantRate) {
            this.merchantRate = merchantRate;
        }

        public String getOneAgentName() {
            return oneAgentName;
        }

        public void setOneAgentName(String oneAgentName) {
            this.oneAgentName = oneAgentName;
        }

        public String getOneAgentNo() {
            return oneAgentNo;
        }

        public void setOneAgentNo(String oneAgentNo) {
            this.oneAgentNo = oneAgentNo;
        }

        public String getPlateOrderNo() {
            return plateOrderNo;
        }

        public void setPlateOrderNo(String plateOrderNo) {
            this.plateOrderNo = plateOrderNo;
        }

        public String getSaleName() {
            return saleName;
        }

        public void setSaleName(String saleName) {
            this.saleName = saleName;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public double getTransAmount() {
            return transAmount;
        }

        public void setTransAmount(double transAmount) {
            this.transAmount = transAmount;
        }

        public String getTransTime() {
            return format.format(new Date(this.transTime));
        }

        public void setTransTime(long transTime) {
            this.transTime = transTime;
        }

        public String getSuperPush() {
            return superPush;
        }

        public void setSuperPush(String superPush) { this.superPush = superPush; }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }
    }
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
