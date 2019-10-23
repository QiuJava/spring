package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table  hardware_product
 * desc  硬件产品表
 */
public class HardwareProduct {

    private Long hpId;

    private String typeName;

    private String model;

    private String versionNu;

    private Date saleStarttime;

    private Date saleEndtime;

    private Date prodStarttime;

    private Date prodEndtime;

    private Date useStarttime;

    private Date useEndtime;

    private Date repaStarttime;

    private Date repaEndtime;

    private String oemMark;

    private String oemId;

    private String manufacturer;

    private String orgId;

    public Long getHpId() {
        return hpId;
    }

    public void setHpId(Long hpId) {
        this.hpId = hpId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getVersionNu() {
        return versionNu;
    }

    public void setVersionNu(String versionNu) {
        this.versionNu = versionNu == null ? null : versionNu.trim();
    }

    public Date getSaleStarttime() {
        return saleStarttime;
    }

    public void setSaleStarttime(Date saleStarttime) {
        this.saleStarttime = saleStarttime;
    }

    public Date getSaleEndtime() {
        return saleEndtime;
    }

    public void setSaleEndtime(Date saleEndtime) {
        this.saleEndtime = saleEndtime;
    }

    public Date getProdStarttime() {
        return prodStarttime;
    }

    public void setProdStarttime(Date prodStarttime) {
        this.prodStarttime = prodStarttime;
    }

    public Date getProdEndtime() {
        return prodEndtime;
    }

    public void setProdEndtime(Date prodEndtime) {
        this.prodEndtime = prodEndtime;
    }

    public Date getUseStarttime() {
        return useStarttime;
    }

    public void setUseStarttime(Date useStarttime) {
        this.useStarttime = useStarttime;
    }

    public Date getUseEndtime() {
        return useEndtime;
    }

    public void setUseEndtime(Date useEndtime) {
        this.useEndtime = useEndtime;
    }

    public Date getRepaStarttime() {
        return repaStarttime;
    }

    public void setRepaStarttime(Date repaStarttime) {
        this.repaStarttime = repaStarttime;
    }

    public Date getRepaEndtime() {
        return repaEndtime;
    }

    public void setRepaEndtime(Date repaEndtime) {
        this.repaEndtime = repaEndtime;
    }

    public String getOemMark() {
        return oemMark;
    }

    public void setOemMark(String oemMark) {
        this.oemMark = oemMark == null ? null : oemMark.trim();
    }

    public String getOemId() {
        return oemId;
    }

    public void setOemId(String oemId) {
        this.oemId = oemId == null ? null : oemId.trim();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}