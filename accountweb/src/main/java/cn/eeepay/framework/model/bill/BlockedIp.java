package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年9月10日15:29:18
 *
 */
public class BlockedIp implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String denyIp;
    private String denyDay;
    private Integer denyNum;
    private Date denyTime;
    
	public String getDenyIp() {
		return denyIp;
	}
	public void setDenyIp(String denyIp) {
		this.denyIp = denyIp;
	}
	public Integer getDenyNum() {
		return denyNum;
	}
	public void setDenyNum(Integer denyNum) {
		this.denyNum = denyNum;
	}
	public Date getDenyTime() {
		return denyTime;
	}
	public void setDenyTime(Date denyTime) {
		this.denyTime = denyTime;
	}
	public String getDenyDay() {
		return denyDay;
	}
	public void setDenyDay(String denyDay) {
		this.denyDay = denyDay;
	}

	
	
}
