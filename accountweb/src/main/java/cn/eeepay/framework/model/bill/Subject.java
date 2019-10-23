package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 科目实体类
 * @author Administrator
 *
 */
public class Subject  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
    private String subjectNo; //科目内部编号

    //private String subjectLegalNo; //科目法定编号

    private String subjectName; //科目名称

    private Integer subjectLevel; //科目级别

    private String parentSubjectNo; //上级科目内部编号 	

    private String subjectType; //科目类型：1-资产类科目，2-负债类科目，3-所有者权益类科目，4-收入类科目，5-支出类科目，6-共同类科目

    private String balanceFrom; //余额方向：debit-借方,credit-贷方

    private String addBalanceFrom; //额增加借贷方向：debit-借方,credit-贷方

    private String debitCreditFlag; //参加借贷平衡检查标志：0-没参加，1-参加

    private String isInnerAccount; //是否开立内部账户：0-否，1-是

    private String innerDayBalFlag; //内部账日终修改余额标志：0-日间，1-日终

    private String innerSumFlag; //内部账汇总入明细标志：0-日间单笔，1-日终单笔，2-日终汇总
    
    private Subject parentSubject;
    
    private String creator;
    
    private Date createTime;
    
    private String updator;
    
    private Date updateTime;
    
    private String subjectAlias ;//科目别名



    public String getSubjectAlias() {
		return subjectAlias;
	}

	public void setSubjectAlias(String subjectAlias) {
		this.subjectAlias = subjectAlias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Subject getParentSubject() {
		return parentSubject;
	}

	public void setParentSubject(Subject parentSubject) {
		this.parentSubject = parentSubject;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName == null ? null : subjectName.trim();
    }

    public Integer getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(Integer subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    public String getParentSubjectNo() {
        return parentSubjectNo;
    }

    public void setParentSubjectNo(String parentSubjectNo) {
        this.parentSubjectNo = parentSubjectNo == null ? null : parentSubjectNo.trim();
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType == null ? null : subjectType.trim();
    }

    public String getBalanceFrom() {
        return balanceFrom;
    }

    public void setBalanceFrom(String balanceFrom) {
        this.balanceFrom = balanceFrom == null ? null : balanceFrom.trim();
    }

    public String getAddBalanceFrom() {
        return addBalanceFrom;
    }

    public void setAddBalanceFrom(String addBalanceFrom) {
        this.addBalanceFrom = addBalanceFrom == null ? null : addBalanceFrom.trim();
    }

    public String getDebitCreditFlag() {
        return debitCreditFlag;
    }

    public void setDebitCreditFlag(String debitCreditFlag) {
        this.debitCreditFlag = debitCreditFlag == null ? null : debitCreditFlag.trim();
    }

    public String getIsInnerAccount() {
        return isInnerAccount;
    }

    public void setIsInnerAccount(String isInnerAccount) {
        this.isInnerAccount = isInnerAccount == null ? null : isInnerAccount.trim();
    }

    public String getInnerDayBalFlag() {
        return innerDayBalFlag;
    }

    public void setInnerDayBalFlag(String innerDayBalFlag) {
        this.innerDayBalFlag = innerDayBalFlag == null ? null : innerDayBalFlag.trim();
    }

    public String getInnerSumFlag() {
        return innerSumFlag;
    }

    public void setInnerSumFlag(String innerSumFlag) {
        this.innerSumFlag = innerSumFlag == null ? null : innerSumFlag.trim();
    }

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
    
}