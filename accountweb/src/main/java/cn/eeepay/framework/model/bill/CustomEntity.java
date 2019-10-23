package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class CustomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer A;
	private Date B;
	private String C;
	public Integer getA() {
		return A;
	}
	public void setA(Integer a) {
		A = a;
	}
	public Date getB() {
		return B;
	}
	public void setB(Date b) {
		B = b;
	}
	public String getC() {
		return C;
	}
	public void setC(String c) {
		C = c;
	}
	
	
}
