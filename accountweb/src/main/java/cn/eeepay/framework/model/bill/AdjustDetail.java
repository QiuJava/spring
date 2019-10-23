package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;

public class AdjustDetail implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer adjustId;

    private String amountFrom;

    private Integer accountFlag;

    private String account;

    private BigDecimal amount;

    private String remark;
    
    private String transNo ;//交易序号
    private String childTransNo ;//子交易号
    private String journalNo ;//分录号
    
    private String debitCreditBalance ;//检查借贷平衡
    
    
    /**
     * VO字段
     * @return
     */
    private String accountType;
    private String userId;
    private String accountOwner;
    private String subjectNo;
    private String currencyNo;
    private String cardNo;

    
    
    public String getDebitCreditBalance() {
		return debitCreditBalance;
	}

	public void setDebitCreditBalance(String debitCreditBalance) {
		this.debitCreditBalance = debitCreditBalance;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getChildTransNo() {
		return childTransNo;
	}

	public void setChildTransNo(String childTransNo) {
		this.childTransNo = childTransNo;
	}

	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdjustId() {
        return adjustId;
    }

    public void setAdjustId(Integer adjustId) {
        this.adjustId = adjustId;
    }

    public String getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(String amountFrom) {
        this.amountFrom = amountFrom == null ? null : amountFrom.trim();
    }

    public Integer getAccountFlag() {
        return accountFlag;
    }

    public void setAccountFlag(Integer accountFlag) {
        this.accountFlag = accountFlag;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getCurrencyNo() {
		return currencyNo;
	}

	public void setCurrencyNo(String currencyNo) {
		this.currencyNo = currencyNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
    
    
}