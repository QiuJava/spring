package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zl on 2015/8/27.
 */
public class User implements Serializable {
    private String name;
    private Integer age;
    private String password;
    private Date creatTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
    
    
}
