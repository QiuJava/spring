package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * table: superbank.loan_source
 * 贷款机构表
 * @author tans
 * @date 2017-12-5
 *
 */
public class LoanSource {
    private Long id;

    private String companyName;//公司

    private String loanProduct;//贷款产品

    private Integer showOrder;//顺序

    private String status;//开关

    private String showLogo;//显示logo

    private String h5Link;//页面h5

    private String sendLink;//申请h5

    private BigDecimal loanBonus;//贷款总奖金

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLoanProduct() {
		return loanProduct;
	}

	public void setLoanProduct(String loanProduct) {
		this.loanProduct = loanProduct;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
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

	public BigDecimal getLoanBonus() {
		return loanBonus;
	}

	public void setLoanBonus(BigDecimal loanBonus) {
		this.loanBonus = loanBonus;
	}

}