package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class Batches implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id; //'主键id',
	private String name;
	private Integer  status; //'跑批结果  0:失败  1：成功',
	private Date createTime; //'跑批时间',
	private Date currentDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
}
