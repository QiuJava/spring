package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
public class SettleTransfer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String batchId;
    private String fileId;
    private String seqNo;
    private String fileName;
    private String settleBank;
    private String inAccNo;
    private String inAccName;
    private String inSettleBankNo;
    private String inBankNo;
    private String inBankName;
    private BigDecimal amount;
    private Date createTime;
    private String status;
    private String errCode;
    private String errMsg;
    private String bak1;
    private String bak2;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSettleBank() {
		return settleBank;
	}
	public void setSettleBank(String settleBank) {
		this.settleBank = settleBank;
	}
	public String getInAccNo() {
		return inAccNo;
	}
	public void setInAccNo(String inAccNo) {
		this.inAccNo = inAccNo;
	}
	public String getInAccName() {
		return inAccName;
	}
	public void setInAccName(String inAccName) {
		this.inAccName = inAccName;
	}
	public String getInSettleBankNo() {
		return inSettleBankNo;
	}
	public void setInSettleBankNo(String inSettleBankNo) {
		this.inSettleBankNo = inSettleBankNo;
	}
	public String getInBankNo() {
		return inBankNo;
	}
	public void setInBankNo(String inBankNo) {
		this.inBankNo = inBankNo;
	}
	public String getInBankName() {
		return inBankName;
	}
	public void setInBankName(String inBankName) {
		this.inBankName = inBankName;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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