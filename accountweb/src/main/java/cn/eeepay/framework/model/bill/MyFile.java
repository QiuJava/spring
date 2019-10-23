package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class MyFile  implements Comparable<MyFile> , Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MyFile(){
		
	}
	private String name;
	private long size;
	private Date createDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	@JsonSerialize(using=CustomDateTimeSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public int compareTo(MyFile o) {
		return o.getCreateDate().compareTo(this.getCreateDate());
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MyFile) {
			MyFile u = (MyFile) obj;
            if (name == null) {
                return u.getName() == null;
            } else {
                return name.contains(u.getName());
            }
        } else {
            return false;
        }
	}

	

	
	
	
}
