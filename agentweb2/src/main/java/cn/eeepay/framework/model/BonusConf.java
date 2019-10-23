package cn.eeepay.framework.model;

/**
 * 奖金配置表
 * @author panfuhao
 * @date 2018/8/17
 */
public class BonusConf {
    private Long orgId; //机构名称
    private String agencyAlias; //机构别称

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAgencyAlias() {
        return agencyAlias;
    }

    public void setAgencyAlias(String agencyAlias) {
        this.agencyAlias = agencyAlias;
    }
}
