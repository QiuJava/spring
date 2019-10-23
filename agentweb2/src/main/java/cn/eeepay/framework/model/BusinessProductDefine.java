package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * table business_product_define
 * desc 业务产品表
 * @author tans
 *
 */

public class BusinessProductDefine {

    private Long bpId;

    private String bpName;

	@JSONField(format = "yyyy-MM-dd")
    private Date saleStarttime;

	@JSONField(format = "yyyy-MM-dd")
    private Date saleEndtime;

    private String proxy;

    private String bpType;

    private String isOem;

    private String ownBpId;

    private String twoCode;
    
    private String twoCodeUrl;

    private String remark;

    private String bpImg;
    
    private String bpImgUrl;
    
    private String notCheck;
    
    private String teamId;
    
    //关联：所属组织名称
    private String teamName;
    
    //关联：自营业务产品名称
    private String ownBpName;
    
  //关联：硬件产品
    private String[] hpId;
    
    private String allowWebItem;
    
    public String[] getHpId() {
		return hpId;
	}

	public void setHpId(String[] hpId) {
		this.hpId = hpId;
	}

    private String limitHard;
    
	public String getLimitHard() {
		return limitHard;
	}

	public void setLimitHard(String limitHard) {
		this.limitHard = limitHard;
	}
	
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId == null ? null : teamId.trim();
	}

	public String getBpImg() {
		return bpImg;
	}

	public void setBpImg(String bpImg) {
		this.bpImg = bpImg == null ? null : bpImg.trim();
	}

	public String getNotCheck() {
		return notCheck;
	}

	public void setNotCheck(String notCheck) {
		this.notCheck = notCheck == null ? null : notCheck.trim();
	}

	public String getOwnBpName() {
		return ownBpName;
	}

	public void setOwnBpName(String ownBpName) {
		this.ownBpName = ownBpName == null ? null : ownBpName.trim();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		 this.teamName = teamName == null ? null : teamName.trim();
	}

    public Long getBpId() {
        return bpId;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId ;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName == null ? null : bpName.trim();
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

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy == null ? null : proxy.trim();
    }

    public String getBpType() {
        return bpType;
    }

    public void setBpType(String bpType) {
        this.bpType = bpType == null ? null : bpType.trim();
    }

    public String getIsOem() {
        return isOem;
    }

    public void setIsOem(String isOem) {
        this.isOem = isOem == null ? null : isOem.trim();
    }

    public String getOwnBpId() {
        return ownBpId;
    }

    public void setOwnBpId(String ownBpId) {
        this.ownBpId = ownBpId == null ? null : ownBpId.trim();
    }

    public String getTwoCode() {
        return twoCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode == null ? null : twoCode.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getTwoCodeUrl() {
		return twoCodeUrl;
	}

	public void setTwoCodeUrl(String twoCodeUrl) {
		this.twoCodeUrl = twoCodeUrl;
	}

	public String getBpImgUrl() {
		return bpImgUrl;
	}

	public void setBpImgUrl(String bpImgUrl) {
		this.bpImgUrl = bpImgUrl;
	}

	public String getAllowWebItem() {
		return allowWebItem;
	}

	public void setAllowWebItem(String allowWebItem) {
		this.allowWebItem = allowWebItem;
	}
    
}