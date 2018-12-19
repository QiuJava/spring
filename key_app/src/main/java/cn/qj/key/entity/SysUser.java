package cn.qj.key.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.qj.key.util.DateTimeUtil;

public class SysUser {
    private Long id;

    private String username;

    private String password;

    @DateTimeFormat(pattern = DateTimeUtil.DATATIME_PATTERN)
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}