package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.BusinessProductDefineDao;
import cn.eeepay.framework.dao.NoticeInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.NoticeInfo;
import cn.eeepay.framework.service.NoticeInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeInfoServiceImpl implements NoticeInfoService{
	@Autowired
	private NoticeInfoDao noticeInfoDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;

	@Resource
	private BusinessProductDefineDao businessProductDefineDao;

	@Resource
	private SeqService seqService;
	
	@Override
	public int insert(NoticeInfo notice) {
		return noticeInfoDao.insert(notice);
	}

	@Override
	public int insert(Map<String, Object> data) {
		NoticeInfo notice = (NoticeInfo) data.get("notice");
		String isAll = (String) data.get("isAll");

		//当下发对象为代理商时，清空商户下面notice相关数据
    	if("2".equals(notice.getSysType())){
    		notice.setBpId(null);
    		notice.setAgentNo(null);
    		if("2".equals(notice.getSysType())){
        		notice.setBpId(null);
        		notice.setAgentNo(null);
        		switch(isAll) {
        		case "7":  
                	notice.setReceiveType("7");
                    break;  
                case "8":  
                	notice.setReceiveType("8");
                    break;  
                default:  
                	notice.setReceiveType(null);
                }
    		}
    	}
    	Date date = new Date();
		notice.setCreateTime(date); // 当前时间为公告创建时间
		notice.setStatus("1"); // 公告状态：待下发
		notice.setIssuedOrg("0");
		return noticeInfoDao.insert(notice);
	}

	@Override
	public NoticeInfo selectById(String id) {
		NoticeInfo notice = noticeInfoDao.selectById(id);
		if(notice != null){
			if(notice.getAttachment() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, notice.getAttachment(), new Date(64063065600000L));
				notice.setAttachmentUrl(url);
			}
			if(notice.getMessageImg() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, notice.getMessageImg(), new Date(64063065600000L));
				notice.setMessageImgUrl(url);
			}
		}
		return notice;
	}

	@Override
	public List<NoticeInfo> selectByParam(NoticeInfo notice, Page<NoticeInfo> page) {
		return noticeInfoDao.selectByParam(notice, page);
	}

	@Override
	public int deliverNotice(long id) {
		// 还有两个字段未赋值
		// login_user：当前登录用户
		// issued_org：下发组织：0移联,一级代理商id
		Map<String, Object> map = new HashMap<>();
		Date data = new Date();
		map.put("issuedTime", data);
		map.put("id", id);
		return noticeInfoDao.deliverNotice(map);
	}

	@Override
	public int update(Map<String, Object> data) {
		NoticeInfo notice = (NoticeInfo) data.get("notice");
		return noticeInfoDao.update(notice);
	}

	@Override
	public NoticeInfo selectInfoById(String id) {
        NoticeInfo info = selectById(id);
		return info;
	}

//	@Override
//	public NoticeInfo getNewNoticeByAgent(String receiveType) {
//		return noticeInfoDao.getNewNoticeByAgent(receiveType);
//	}


	@Override
	public NoticeInfo getNewNoticeByAgent(AgentInfo loginAgentInfo) {
		boolean firstAgent = loginAgentInfo != null && loginAgentInfo.getAgentLevel().equals(1);
		return null;
	}

    @Override
    public List<NoticeInfo> selectReceiveNotices(NoticeInfo notice, Page<NoticeInfo> page) {
        return noticeInfoDao.selectReceiveNotices(notice, page);
    }
}
