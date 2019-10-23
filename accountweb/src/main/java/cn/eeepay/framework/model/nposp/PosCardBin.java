package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.util.Date;

/**
 * 卡bin表
 * @author zouruijin
 * 2016年8月17日11:18:12
 *
 */
public class PosCardBin  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String bankName; 
    private String cardName; 
    private Integer cardLength; 
    private String cardType; 
    private Integer verifyLength; 
    private Integer verifyCode;
    private Date createTime;
    private String bankNo; 
    private String bankDesc; 
    private String bankCode;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public Integer getCardLength() {
		return cardLength;
	}
	public void setCardLength(Integer cardLength) {
		this.cardLength = cardLength;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Integer getVerifyLength() {
		return verifyLength;
	}
	public void setVerifyLength(Integer verifyLength) {
		this.verifyLength = verifyLength;
	}
	public Integer getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(Integer verifyCode) {
		this.verifyCode = verifyCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankDesc() {
		return bankDesc;
	}
	public void setBankDesc(String bankDesc) {
		this.bankDesc = bankDesc;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	} 
}