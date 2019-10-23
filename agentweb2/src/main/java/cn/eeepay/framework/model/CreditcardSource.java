package cn.eeepay.framework.model;

/**
 * 信用卡银行
 * @author Administrator
 *
 */
public class CreditcardSource {

	private Long id;
	
	private String bankName;
	
	private String showOrder;
	
	private String status;
	
	private String showLogo;
	
	private String h5Link;
	
	private String sendLink;
	
	private String bankBonus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShowLogo() {
		return showLogo;
	}

	public void setShowLogo(String showLogo) {
		this.showLogo = showLogo;
	}

	public String getH5Link() {
		return h5Link;
	}

	public void setH5Link(String h5Link) {
		this.h5Link = h5Link;
	}

	public String getSendLink() {
		return sendLink;
	}

	public void setSendLink(String sendLink) {
		this.sendLink = sendLink;
	}

	public String getBankBonus() {
		return bankBonus;
	}

	public void setBankBonus(String bankBonus) {
		this.bankBonus = bankBonus;
	}
	
}
