package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * table notice_info
 * desc 公告信息表
 * @author tans
 */
public class NoticeInfo {

    private Integer ntId;

    private String title;

    private String content;

    private String attachment;
    
    private String attachmentUrl;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date issuedTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date issuedTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date issuedTimeEnd;

    private String loginUser;

    private String sysType;

    private String receiveType;

    private String agentRole;

    private String agentNo;

    private String bpId;

    private String status;

    private String issuedOrg;
    
    private String agentName;
    
    private String link;
    
    private String messageImg;
    
    private String messageImgUrl;
    private String oemType;     // oem 类型
    private String strong;      // 置顶标志
    private String msgType;     // 消息类型
    private String startIssuedTime; // 查询条件: 下发时间
    private String endIssuedTime;  //  查询条件: 下发时间
    private String agentType;
    private String oemList;

    public String getOemList() {
		return oemList;
	}

	public void setOemList(String oemList) {
		this.oemList = oemList;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getStartIssuedTime() {
        return startIssuedTime;
    }

    public void setStartIssuedTime(String startIssuedTime) {
        this.startIssuedTime = startIssuedTime;
    }

    public String getEndIssuedTime() {
        return endIssuedTime;
    }

    public void setEndIssuedTime(String endIssuedTime) {
        this.endIssuedTime = endIssuedTime;
    }

    public String getOemType() {
        return oemType;
    }

    public void setOemType(String oemType) {
        this.oemType = oemType;
    }

    public String getStrong() {
        return strong;
    }

    public void setStrong(String strong) {
        this.strong = strong;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMessageImg() {
		return messageImg;
	}

	public void setMessageImg(String messageImg) {
		this.messageImg = messageImg;
	}

	public String getMessageImgUrl() {
		return messageImgUrl;
	}

	public void setMessageImgUrl(String messageImgUrl) {
		this.messageImgUrl = messageImgUrl;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

    public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		 this.agentName = agentName == null ? null : agentName.trim();
	}

	public Integer getNtId() {
        return ntId;
    }

    public void setNtId(Integer ntId) {
        this.ntId = ntId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser == null ? null : loginUser.trim();
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType == null ? null : sysType.trim();
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType == null ? null : receiveType.trim();
    }

    public String getAgentRole() {
        return agentRole;
    }

    public void setAgentRole(String agentRole) {
        this.agentRole = agentRole == null ? null : agentRole.trim();
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIssuedOrg() {
        return issuedOrg;
    }

    public void setIssuedOrg(String issuedOrg) {
        this.issuedOrg = issuedOrg == null ? null : issuedOrg.trim();
    }

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Date getIssuedTimeBegin() {
		return issuedTimeBegin;
	}

	public void setIssuedTimeBegin(Date issuedTimeBegin) {
		this.issuedTimeBegin = issuedTimeBegin;
	}

	public Date getIssuedTimeEnd() {
		return issuedTimeEnd;
	}

	public void setIssuedTimeEnd(Date issuedTimeEnd) {
		this.issuedTimeEnd = issuedTimeEnd;
	}
}