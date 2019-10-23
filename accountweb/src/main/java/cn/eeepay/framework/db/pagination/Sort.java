package cn.eeepay.framework.db.pagination;

import java.io.Serializable;

public class Sort implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ASC="asc";
	public static final String DESC="DESC";
	private String sidx;//排序字段
	private String sord=ASC;//升降序
	public Sort() {
	}
	/**
	 * @return the sidx
	 */
	public String getSidx() {
		return sidx;
	}
	/**
	 * @return the sord
	 */
	public String getSord() {
		return sord;
	}
	/**
	 * @param sidx the sidx to set
	 */
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	/**
	 * @param sord the sord to set
	 */
	public void setSord(String sord) {
		this.sord = sord;
	}
	

}
