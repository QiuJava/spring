package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.BannerInfoDao;
import cn.eeepay.framework.dao.TeamInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.BannerInfo;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.BannerInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bannerInfoService")
public class BannerInfoServiceImpl implements BannerInfoService {

	@Resource
	private BannerInfoDao bannerInfoDao;
	
	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;
	
	@Resource
	private SeqService seqService;

	@Override
	public List<BannerInfo> selectByCondition(BannerInfo banner, Page<BannerInfo> page) {
		return bannerInfoDao.selectByCondition(banner, page);
	}

	@Override
	public int updateStatus(BannerInfo banner) {
		int num = 0;
		if (banner.getBannerId() != null) {

			if (banner.getBannerStatus() != null && "0".equals(banner.getBannerStatus())) {
				num = bannerInfoDao.openStatus(banner.getBannerId());
			}
			if (banner.getBannerStatus() != null && "1".equals(banner.getBannerStatus())) {
				num = bannerInfoDao.closeStatus(banner.getBannerId());
			}
		}
		return num;
	}

	@Override
	public BannerInfo selectDetailById(String id) {
		BannerInfo banner = bannerInfoDao.selectDetailById(id);
		if(banner != null){
			if(banner.getTeamId() == null){
				banner.setTeamName("全部");
			}
			if(banner.getAgentNo() == null){
				banner.setAgentName("全部");
			}
			if(banner.getBannerAttachment() != null){
				String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, banner.getBannerAttachment(), new Date(64063065600000L));
				banner.setBannerAttachmentUrl(url);
			}
		}
		return banner;
	}

	@Override
	public Map<String, Object> selectLinkInfo(String bannerId) {
		Map<String, Object> msg = new HashMap<>();
		List<TeamInfo> allTeam = teamInfoDao.selectTeamName();
		List<AgentInfo> allAgent = agentInfoDao.selectOneAgent();
		msg.put("allTeam", allTeam);
		msg.put("allAgent", allAgent);
		if(bannerId == null || "".equals(bannerId)){
			return msg;
		} else {
			BannerInfo banner = selectDetailById(bannerId);
			msg.put("banner", banner);
		}
		return msg;
	}

	@Override
	public int insertOrUpdate(BannerInfo banner) {
		int num = 0;
		if(banner != null){
			if(banner.getBannerId() != null){
				//update
				num = bannerInfoDao.update(banner);
			} else {
				//save
				banner.setBannerStatus("0");
		//		banner.setBannerId(seqService.createKey(Constants.BANNER_SEQ,"%01d"));
				num = bannerInfoDao.insert(banner);
			}
		}
		return num;
	}

}
