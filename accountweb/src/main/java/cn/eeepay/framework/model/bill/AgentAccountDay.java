package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询代理商单日入账出账汇总
 *
 */
public class AgentAccountDay implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private String agentNo;		
	private String agentName;		
	private String agentNode;
	private String agentLevel;
	private BigDecimal recordAuountSum;
	private BigDecimal balance;
	private BigDecimal avaliBalance;
	private String accountNo;
	private String subjectNo;
	private String accountStatus;
	private Date createTime;
	private BigDecimal controlAmount;
	private Date recordDate;
	private BigDecimal nowBalance;
	private BigDecimal befBalance;

	public BigDecimal getNowBalance() {
		return nowBalance;
	}

	public void setNowBalance(BigDecimal nowBalance) {
		this.nowBalance = nowBalance;
	}

	public BigDecimal getBefBalance() {
		return befBalance;
	}

	public void setBefBalance(BigDecimal befBalance) {
		this.befBalance = befBalance;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public BigDecimal getControlAmount() {
		return controlAmount;
	}

	public void setControlAmount(BigDecimal controlAmount) {
		this.controlAmount = controlAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public BigDecimal getRecordAuountSum() {
		return recordAuountSum;
	}

	public void setRecordAuountSum(BigDecimal recordAuountSum) {
		this.recordAuountSum = recordAuountSum;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAvaliBalance() {
		return avaliBalance;
	}

	public void setAvaliBalance(BigDecimal avaliBalance) {
		this.avaliBalance = avaliBalance;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}

