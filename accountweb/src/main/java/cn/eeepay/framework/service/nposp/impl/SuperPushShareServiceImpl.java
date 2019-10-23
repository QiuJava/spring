package cn.eeepay.framework.service.nposp.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.SuperPushShareMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.SuperPushShare;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.service.nposp.SuperPushShareService;
import cn.eeepay.framework.util.ListUtil;


@Service("superPushShareService")
@Transactional("nposp")
public class SuperPushShareServiceImpl  implements SuperPushShareService{
	
	private static final Logger log = LoggerFactory.getLogger(SuperPushShareServiceImpl.class);
	
	@Resource
	public SuperPushShareMapper  superPushShareMapper;
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public MerchantInfoService merchantInfoService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;

	@Override
	public List<SuperPushShare> findSuperPushShareList(SuperPushShare superPushShare, Sort sort,
			Page<SuperPushShare> page) {
		
		if (StringUtils.isNotBlank(superPushShare.getCreateTime1())) {
			superPushShare.setCreateTime1(superPushShare.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getCreateTime2())) {
			superPushShare.setCreateTime2(superPushShare.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(superPushShare.getShareTime1())) {
			superPushShare.setShareTime1(superPushShare.getShareTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getShareTime2())) {
			superPushShare.setShareTime2(superPushShare.getShareTime2() + " 23:59:59");
		}
		superPushShareMapper.findSuperPushShareList(superPushShare, sort, page);
		List<SuperPushShare> list = page.getResult();
		for (SuperPushShare sp : list) {
			sp.setShareRateStr(sp.getShareRate()==null?"":String.valueOf(sp.getShareRate().setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
			if (sp.getShareType().equals("0") || sp.getShareType().equals("1")) {
				//代理商
				String agentNo = sp.getShareNo();
				AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
				if (agentInfo != null) {
					sp.setShareName(agentInfo.getAgentName());
				}
			}else {
				//商户
				String merchantNo = sp.getShareNo();
				MerchantInfo merchantInfo= merchantInfoService.findMerchantInfoByUserId(merchantNo);
				if (merchantInfo != null) {
					sp.setShareName(merchantInfo.getMerchantName());
				}
			}
			//对账状态
//			DuiAccountDetail detail = duiAccountDetailService.findDuiAccountDetailByOrderReferenceNo(sp.getOrderNo());
//			if(detail != null ){
//				sp.setCheckAccountStatus(detail.getCheckAccountStatus());
//			}
		}
		return list;
	}

	@Override
	public List<SuperPushShare> exportSuperPushShareList(SuperPushShare superPushShare) {
		if (StringUtils.isNotBlank(superPushShare.getCreateTime1())) {
			superPushShare.setCreateTime1(superPushShare.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getCreateTime2())) {
			superPushShare.setCreateTime2(superPushShare.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(superPushShare.getShareTime1())) {
			superPushShare.setShareTime1(superPushShare.getShareTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getShareTime2())) {
			superPushShare.setShareTime2(superPushShare.getShareTime2() + " 23:59:59");
		}
		
		return superPushShareMapper.exportSuperPushShareList(superPushShare);
	}

	
	@Override
	public Map<String, Object> findSuperPushShareCollection(SuperPushShare superPushShare) {
		if (StringUtils.isNotBlank(superPushShare.getCreateTime1())) {
			superPushShare.setCreateTime1(superPushShare.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getCreateTime2())) {
			superPushShare.setCreateTime2(superPushShare.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(superPushShare.getShareTime1())) {
			superPushShare.setShareTime1(superPushShare.getShareTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getShareTime2())) {
			superPushShare.setShareTime2(superPushShare.getShareTime2() + " 23:59:59");
		}
		return superPushShareMapper.findSuperPushShareCollection(superPushShare);
	}
	
	@Override
	public List<Map<String, Object>> findCollectionGropByShareNo(String createTime1) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("createTime1", createTime1);
		return superPushShareMapper.findCollectionGropByShareNo(params);
	}

	@Override
	public int updateSuperPushShareBatch(List<SuperPushShare> list) {
		return superPushShareMapper.updateSuperPushShareBatch(list);
	}

	@Override
	@Transactional
	public Map<String, Object> updateSuperPushShareSplitBatch(List<SuperPushShare> superPushShareList){
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 200;
		List<SuperPushShare> asdList = new ArrayList<>();
		List<List<?>> superPushShareSplitList = ListUtil.batchList(superPushShareList, batchCount);
		for (List<?> clist : superPushShareSplitList) {
			for (Object object : clist) {
				SuperPushShare asd = (SuperPushShare) object;
				asdList.add(asd);
			}
			if (asdList.size() > 0) {
				log.info("修改超级推商户分润记录{}条",asdList.size());
				int j = this.updateSuperPushShareBatch(asdList);
				if (j > 0) {
					i = i + j;
				}
				if (!asdList.isEmpty()) {
					asdList.clear();
				}
			}
		}
		if (i > 0) {
			msg.put("status", true);
			msg.put("msg", "执行成功");
		}
		else{
			msg.put("status", true);
			msg.put("msg", "没有修改任何数据");
		}
		return msg;
	}

	@Override
	public List<SuperPushShare> findSuperPushShareListEnterByModel(SuperPushShareDaySettle superPushShareDaySettle) {
		return superPushShareMapper.findSuperPushShareListEnterByModel(superPushShareDaySettle);
	}

	@Override
	public List<SuperPushShare> findSuperPushShareListCollectionByModel(
			SuperPushShareDaySettle superPushShareDaySettle) {
		return superPushShareMapper.findSuperPushShareListCollectionByModel(superPushShareDaySettle);
	}

	@Override
	public Map<String, Object> findSuperPushShareCollectionTotalAmount(SuperPushShare superPushShare) {
		
		if (StringUtils.isNotBlank(superPushShare.getCreateTime1())) {
			superPushShare.setCreateTime1(superPushShare.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getCreateTime2())) {
			superPushShare.setCreateTime2(superPushShare.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(superPushShare.getShareTime1())) {
			superPushShare.setShareTime1(superPushShare.getShareTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShare.getShareTime2())) {
			superPushShare.setShareTime2(superPushShare.getShareTime2() + " 23:59:59");
		}
		
		return superPushShareMapper.findSuperPushShareCollectionTotalAmount(superPushShare);
	}

}
