package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
public class SettleTransferFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String fileName;
    private String fileMd5;
    private Integer operatorId;
    private String operatorName;
    private Integer transferOperatorId;
    private String transferOperatorName;
    private Integer totalNum;
    private BigDecimal totalAmount;
    private String summary;
    private Date createTime;
    private Date transferTime;
    private String settleBank;
    private String outAccNo;
    private String outAccName;
    private String outBankNo;
    private String outBankName;
    private String outSettleBankNo;
    private String status;
    private String errCode;
    private String errMsg;
    private String bak1;
    private String bak2;
    
    private String total ;
    
    //前台查询条件字段
    private String createTime1;
    private String createTime2;
    
	public String getCreateTime1() {
		return createTime1;
	}
	public void setCreateTime1(String createTime1) {
		this.createTime1 = createTime1;
	}
	public String getCreateTime2() {
		return createTime2;
	}
	public void setCreateTime2(String createTime2) {
		this.createTime2 = createTime2;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileMd5() {
		return fileMd5;
	}
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Integer getTransferOperatorId() {
		return transferOperatorId;
	}
	public void setTransferOperatorId(Integer transferOperatorId) {
		this.transferOperatorId = transferOperatorId;
	}
	public String getTransferOperatorName() {
		return transferOperatorName;
	}
	public void setTransferOperatorName(String transferOperatorName) {
		this.transferOperatorName = transferOperatorName;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	public String getSettleBank() {
		return settleBank;
	}
	public void setSettleBank(String settleBank) {
		this.settleBank = settleBank;
	}
	public String getOutAccNo() {
		return outAccNo;
	}
	public void setOutAccNo(String outAccNo) {
		this.outAccNo = outAccNo;
	}
	public String getOutAccName() {
		return outAccName;
	}
	public void setOutAccName(String outAccName) {
		this.outAccName = outAccName;
	}
	public String getOutBankNo() {
		return outBankNo;
	}
	public void setOutBankNo(String outBankNo) {
		this.outBankNo = outBankNo;
	}
	public String getOutBankName() {
		return outBankName;
	}
	public void setOutBankName(String outBankName) {
		this.outBankName = outBankName;
	}
	public String getOutSettleBankNo() {
		return outSettleBankNo;
	}
	public void setOutSettleBankNo(String outSettleBankNo) {
		this.outSettleBankNo = outSettleBankNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getBak1() {
		return bak1;
	}
	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}
	public String getBak2() {
		return bak2;
	}
	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}
   
    
}