package cn.eeepay.framework.model.bill;

import java.io.Serializable;

public class MsgEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private String msg;
	private String balance;
	private String avaliBalance;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getAvaliBalance() {
		return avaliBalance;
	}
	public void setAvaliBalance(String avaliBalance) {
		this.avaliBalance = avaliBalance;
	}
	
	
}
