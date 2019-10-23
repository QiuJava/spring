package cn.eeepay.framework.model;
public class HappyBackNotFullDeductDetailQo {

	private String sTime;
	private String eTime;
	private String curAgentNo;
	private String orderNo;
	private String agentNo;
	private int pageNo;
	private int pageSize;

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurAgentNo() {
		return curAgentNo;
	}

	public void setCurAgentNo(String curAgentNo) {
		this.curAgentNo = curAgentNo;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

}
