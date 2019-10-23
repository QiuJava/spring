package cn.eeepay.framework.model;

/**
 * 保险公司表
 *
 * @author panfuhao
 * @date 2018/7/23
 */
public class InsuranceCompany {

    private Long companyNo;          //保险公司ID
    private String companyName;         //保险公司名称
    private String companyNickName;     //保险公司别称
    private String source;              //来源
    private String showLogo;            //显示logo
    private Integer createOrderType;    //订单创建方式,1:实际回调创建,2:批量导入创建
    private String ruleCode;            //导入匹配规则编码
    private String shareRuleRemark;     //分润规则备注
    private Integer status;             //状态,0:关闭,1:开启
    private String createBy;            //创建人
    private String updateBy;            //修改人

    public Long getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(Long companyNo) {
        this.companyNo = companyNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNickName() {
        return companyNickName;
    }

    public void setCompanyNickName(String companyNickName) {
        this.companyNickName = companyNickName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getShowLogo() {
        return showLogo;
    }

    public void setShowLogo(String showLogo) {
        this.showLogo = showLogo;
    }

    public Integer getCreateOrderType() {
        return createOrderType;
    }

    public void setCreateOrderType(Integer createOrderType) {
        this.createOrderType = createOrderType;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getShareRuleRemark() {
        return shareRuleRemark;
    }

    public void setShareRuleRemark(String shareRuleRemark) {
        this.shareRuleRemark = shareRuleRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
