package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.PageBean;
import cn.eeepay.framework.service.TransInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("transInfoService")
public class TransInfoServiceImpl implements TransInfoService {

	@Resource
	private TransInfoDao transInfoDao;
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private SysDictDao sysDictDao;
	// ============ start 商户交易汇总相关代码 ====================
	@Override
	public List<CollectiveTransOrder> queryAllInfoByMerchant(PageBean page, CollectiveTransOrder param, AgentInfo loginAgent) {
		AgentInfo searchAgent = agentInfoDao.selectByAgentNo(param.getAgentNo());
		if (searchAgent == null || loginAgent == null || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
			return null;
		}
		validSearchParam(param);
		return transInfoDao.queryAllInfoByMerchant(param, searchAgent, page);
	}

	@Override
	public int countAllInfoByMerchant(CollectiveTransOrder param, AgentInfo loginAgent) {
		AgentInfo searchAgent = agentInfoDao.selectByAgentNo(param.getAgentNo());
		if (searchAgent == null || loginAgent == null || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
			return 0;
		}
		validSearchParam(param);
		return transInfoDao.countAllInfoByMerchant(param, searchAgent);
	}


	@Override
	public List<CollectiveTransOrder> exportAllInfoByMerchant(CollectiveTransOrder transInfo, AgentInfo loginAgent) {
		AgentInfo searchAgent = agentInfoDao.selectByAgentNo(transInfo.getAgentNo());
		if (searchAgent == null || loginAgent == null || !searchAgent.getAgentNode().startsWith(loginAgent.getAgentNode())){
			return null;
		}
		validSearchParam(transInfo);
		return transInfoDao.exportAllInfoByMerchant(transInfo, searchAgent);
	}
	private void validSearchParam(CollectiveTransOrder param) {
		if (param == null || StringUtils.isBlank(param.getSdate()) || StringUtils.isBlank(param.getEdate())){
			throw new AgentWebException("请输入交易时间");
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = sdf.parse(param.getSdate());
			Date endTime = sdf.parse(param.getEdate());
			if (endTime.getTime() < startTime.getTime()){
				throw new AgentWebException("交易结束时间不能大于开始时间");
			}
			if (endTime.getTime() - startTime.getTime() > 93L * 24 * 3600 * 1000){
				throw new AgentWebException("交易查询间隔时间不能超过93天");
			}
		}catch (Exception e){
			throw new AgentWebException("请输入交易时间");
		}
	}
	// ============ end 商户交易汇总相关代码 ====================
	@Override
	public List<CollectiveTransOrder> queryAllInfo(Page<CollectiveTransOrder> page, CollectiveTransOrder transInfo,int level,int level1) {
		if (StringUtils.isNotBlank(transInfo.getInitAgentNo())){
			AgentInfo agentInfo = agentInfoDao.selectByAgentNo(transInfo.getInitAgentNo());
			if (agentInfo != null){
				transInfo.setInitAgentNo(agentInfo.getAgentNode());
			}
		}
		if (StringUtils.isNotBlank(transInfo.getAgentNo())){
			AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(transInfo.getAgentNo());
            if (searchAgentInfo != null){
                transInfo.setAgentNode(searchAgentInfo.getAgentNode());
            }
		}
		return transInfoDao.queryAllInfo(page, transInfo,level,level1);
	}

	@Override
	public String queryNumAndMoney(CollectiveTransOrder transInfo,String loginAgentNo) {
		if (StringUtils.isNotBlank(loginAgentNo)){
			AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
            loginAgentNo = (loginAgent != null) ? loginAgent.getAgentNode() : loginAgentNo;
		}
		if(StringUtils.isNotBlank(transInfo.getAgentNo())){
            AgentInfo searchAgent = agentInfoDao.selectByAgentNo(transInfo.getAgentNo());
            transInfo.setAgentNode(searchAgent.getAgentNode());
        }
		return transInfoDao.queryNumAndMoney(transInfo,loginAgentNo);
	}

	@Override
	public CollectiveTransOrder queryInfoDetail(String id) {
		return transInfoDao.queryInfoDetail(id);
	}

	@Override
	public CollectiveTransOrder queryInfoDetailForSurveyOrder(String id) {
		return transInfoDao.queryInfoDetailForSurveyOrder(id);
	}

	@Override
	public List<CollectiveTransOrder> exportAllInfo(CollectiveTransOrder transInfo, int level, int level1) {
		if (StringUtils.isNotBlank(transInfo.getInitAgentNo())){
			AgentInfo agentInfo = agentInfoDao.selectByAgentNo(transInfo.getInitAgentNo());
			if (agentInfo != null){
				transInfo.setInitAgentNo(agentInfo.getAgentNode());
			}
		}
        if (StringUtils.isNotBlank(transInfo.getAgentNo())){
            AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(transInfo.getAgentNo());
            if (searchAgentInfo != null){
                transInfo.setAgentNode(searchAgentInfo.getAgentNode());
            }
        }
		return transInfoDao.exportAllInfo(transInfo,level,level1);
	}

	@Override
	public CollectiveTransOrder selectByOrderNo(String orderNo) {
		return transInfoDao.selectByOrderNo(orderNo);
	}

	@Override
	public CollectiveTransOrder selectByOrderNoAndAgentNode(String orderNo, String agentNode) {
		return transInfoDao.selectByOrderNoAndAgentNode(orderNo, agentNode);
	}

}
