package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.service.AgentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.TradeSumInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TradeSumInfo;
import cn.eeepay.framework.model.TradeSumInfoQo;
import cn.eeepay.framework.model.UserLoginInfo;

import javax.annotation.Resource;

@Service
public class TradeSumInfoService {

	@Autowired
	private TradeSumInfoMapper tradeSumInfoMapper;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private AgentInfoDao agentInfoDao;

	public void query(Page<TradeSumInfo> page, TradeSumInfoQo qo) {
		setQo(qo);
		tradeSumInfoMapper.page(page, qo);
		List<TradeSumInfo> list = page.getResult();
		levelForward(list);
	}

	public void levelForward (List<TradeSumInfo> list){
		String agentNo = 	agentInfoService.getCurAgentNo();
		for (TradeSumInfo tradeSumInfo : list) {
			// 查询当前登录代理商 三方级别
			Integer level = tradeSumInfoMapper.findByAgentLink(agentNo);

			String oneLevel = tradeSumInfo.getOneLevel();
			String twoLevel = tradeSumInfo.getTwoLevel();
			String threeLevel = tradeSumInfo.getThreeLevel();
			String fourLevel = tradeSumInfo.getFourLevel();
			String fiveLevel = tradeSumInfo.getFiveLevel();

			if (null != level && level > 0){
				switch (level){
					case 1:
						tradeSumInfo.setBranch(oneLevel);
						tradeSumInfo.setOneLevel(twoLevel);
						tradeSumInfo.setTwoLevel(threeLevel);
						tradeSumInfo.setThreeLevel(fourLevel);
						tradeSumInfo.setFourLevel(fiveLevel);
						tradeSumInfo.setFiveLevel(null);
						break;
					case 2:
						tradeSumInfo.setBranch(twoLevel);
						tradeSumInfo.setOneLevel(threeLevel);
						tradeSumInfo.setTwoLevel(fourLevel);
						tradeSumInfo.setThreeLevel(fiveLevel);
						tradeSumInfo.setFourLevel(null);
						tradeSumInfo.setFiveLevel(null);
						break;
					case 3:
						tradeSumInfo.setBranch(threeLevel);
						tradeSumInfo.setOneLevel(fourLevel);
						tradeSumInfo.setTwoLevel(fiveLevel);
						tradeSumInfo.setThreeLevel(null);
						tradeSumInfo.setFourLevel(null);
						tradeSumInfo.setFiveLevel(null);
						break;
					case 4:
						tradeSumInfo.setBranch(fourLevel);
						tradeSumInfo.setOneLevel(fiveLevel);
						tradeSumInfo.setTwoLevel(null);
						tradeSumInfo.setThreeLevel(null);
						tradeSumInfo.setFourLevel(null);
						tradeSumInfo.setFiveLevel(null);
						break;
					case 5:
						tradeSumInfo.setBranch(fiveLevel);
						tradeSumInfo.setOneLevel(null);
						tradeSumInfo.setTwoLevel(null);
						tradeSumInfo.setThreeLevel(null);
						tradeSumInfo.setFourLevel(null);
						tradeSumInfo.setFiveLevel(null);
						break;
				}
			}
		}
	}


	public void getAllLookAgentNo(List<String> agentList, String agentNo) {
		List<String> findConfigAgentNo = tradeSumInfoMapper.findLookAgentNo(agentNo);
		for (String str : findConfigAgentNo) {
			if (agentList.contains(str)) {
				continue;
			}
			agentList.add(str);
			getAllLookAgentNo(agentList, str);
		}
	}

	private void setQo(TradeSumInfoQo qo) {
		UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String curAgentNo = principal.getUserEntityInfo().getEntityId();
		List<String> agentList = new ArrayList<>();
		agentList.add(curAgentNo);
		getAllLookAgentNo(agentList, curAgentNo);
		qo.setAgentNoList(agentList);
	}

	public String sum(TradeSumInfoQo qo) {
		setQo(qo);
		return tradeSumInfoMapper.sum(qo);
	}

	public List<TradeSumInfo> list(TradeSumInfoQo qo) {
		setQo(qo);
		List<TradeSumInfo> list = tradeSumInfoMapper.findByQo(qo);
		levelForward(list);
		return list;
	}

}
