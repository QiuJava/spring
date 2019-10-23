package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.NoticeInfo;

public interface NoticeInfoService {
	public int insert(NoticeInfo notice);

    int insert(Map<String, Object> data);
	
	NoticeInfo selectById(String id);

	int update(Map<String, Object> data);

	List<NoticeInfo> selectByParam( NoticeInfo notice, Page<NoticeInfo> page);

	int deliverNotice(long id);

	NoticeInfo selectInfoById(String id);

//	public NoticeInfo getNewNoticeByAgent(String receiveType);

	/**
	 * 根据登陆代理商获取最新的公告
	 * @param loginAgentInfo
	 * @return
	 */
    NoticeInfo getNewNoticeByAgent(AgentInfo loginAgentInfo);

	/**
	 * 查询代理商收到的公告
	 * @param notice 公告
	 * @param page   分页信息
	 * @return
	 */
	List<NoticeInfo> selectReceiveNotices(NoticeInfo notice, Page<NoticeInfo> page);
}
