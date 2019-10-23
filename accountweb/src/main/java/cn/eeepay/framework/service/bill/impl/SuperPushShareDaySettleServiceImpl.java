package cn.eeepay.framework.service.bill.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.SuperPushShare;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.SuperPushEnterAccountService;
import cn.eeepay.framework.service.bill.SuperPushShareDaySettleService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.service.nposp.SuperPushShareService;
import cn.eeepay.framework.util.ListUtil;

@Service("superPushShareDaySettleService")
@Transactional
public class SuperPushShareDaySettleServiceImpl implements SuperPushShareDaySettleService{
	
	private static final Logger log = LoggerFactory.getLogger(SuperPushShareDaySettleServiceImpl.class);
	@Resource
	public SuperPushShareDaySettleMapper superPushShareDaySettleMapper;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public SuperPushShareService superPushShareService;
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public MerchantInfoService merchantInfoService;
	@Resource
	public SuperPushEnterAccountService superPushEnterAccountService;
	@Override
	public int insert(SuperPushShareDaySettle superPushShareDaySettle) {
		return superPushShareDaySettleMapper.insert(superPushShareDaySettle);
	}
	@Override
	public int update(SuperPushShareDaySettle superPushShareDaySettle) {
		return superPushShareDaySettleMapper.updateSuperPushShareDaySettle(superPushShareDaySettle);
	}
	@Override
	public int delete(Integer id) {
		return superPushShareDaySettleMapper.delete(id);
	}
	@Override
	public List<SuperPushShareDaySettle> findSuperPushShareDaySettleList(
			SuperPushShareDaySettle superPushShareDaySettle, Sort sort, Page<SuperPushShareDaySettle> page) {
		
		if (StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime1())) {
			superPushShareDaySettle.setCreateTime1(superPushShareDaySettle.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime2())) {
			superPushShareDaySettle.setCreateTime2(superPushShareDaySettle.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1())) {
			superPushShareDaySettle.setGroupTime1(superPushShareDaySettle.getGroupTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1())) {
			superPushShareDaySettle.setGroupTime2(superPushShareDaySettle.getGroupTime2() + " 23:59:59");
		}
		superPushShareDaySettleMapper.findSuperPushShareDaySettleList(superPushShareDaySettle, sort, page);
		List<SuperPushShareDaySettle> list = page.getResult();
		for (SuperPushShareDaySettle sp : list) {
			String shareType = sp.getShareType();
			if(StringUtils.isNotBlank(shareType)){
				if (shareType.equals("0")) {
					//代理商
					String agentNo = sp.getShareNo();
					AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
					if(agentInfo != null){
						sp.setShareName(agentInfo.getAgentName());	
					}
				}
				if (shareType.equals("1")) {
					//商户
					String merchantNo = sp.getShareNo();
					MerchantInfo merchantInfo = merchantInfoService.findMerchantInfoByUserId(merchantNo);
					if(merchantInfo != null){
						sp.setShareName(merchantInfo.getMerchantName());
					}
				}
			}
		}
		return list;
	}
	@Override
	public Map<String, Object> superPushShareCollection(String createTime1, String operater) throws Exception {
		String collectionBatchNo = genericTableService.createSuperPushShareCollectionBatchNo();
		log.info("超级推汇总生成批次号 collectionBatchNo------> " + collectionBatchNo);
		List<Map<String, Object>> mapList = superPushShareService.findCollectionGropByShareNo(createTime1);
		if(mapList == null || mapList.size() == 0){
			Map<String,Object> result = new HashMap<>();
			result.put("status", true);
			result.put("msg", createTime1 + "没有要汇总的数据！");
			return result;
		}
		
		List<SuperPushShareDaySettle> list = new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			SuperPushShareDaySettle sp = new SuperPushShareDaySettle();
			sp.setCollectionBatchNo(collectionBatchNo);
			sp.setEnterAccountStatus("NOENTERACCOUNT");
			sp.setGroupTime(new Date());
			sp.setShareNo(map.get("shareNo").toString());
			sp.setShareType(map.get("shareType").toString());
			sp.setCreateTime((Date)map.get("createTime"));
			sp.setShareTotalAmount(new BigDecimal(map.get("shareTotalAmount").toString()));
			sp.setShareTotalNum(Integer.valueOf(map.get("shareTotalNum").toString()));
			list.add(sp);
		}
		
		Map<String, Object> result = new HashMap<>();
		try {
			result = this.insertSuperPushShareDaySettleSplitBatch(list);
			//汇总成功之后需要更新 分润明细表中的 汇总批次，汇总状态
			if((boolean)result.get("status")){
				List<SuperPushShareDaySettle> SuperPushShareDaySettleList = superPushShareDaySettleMapper.findSuperPushShareDaySettleByCollectionBatchNo(collectionBatchNo);
				for (SuperPushShareDaySettle superPushShareDaySettle : SuperPushShareDaySettleList) {
					List<SuperPushShare> superPushShareList = superPushShareService.findSuperPushShareListCollectionByModel(superPushShareDaySettle);
					for (SuperPushShare superPushShare : superPushShareList) {
						superPushShare.setCollectionBatchNo(collectionBatchNo);
						superPushShare.setCollectionStatus("COLLECTIONED");
					}
					Map<String, Object> resultShare = superPushShareService.updateSuperPushShareSplitBatch(superPushShareList);
					if((boolean)resultShare.get("status")){
						result.put("status", true);
						result.put("msg", "汇总成功！");
					}else{
						result.put("status", false);
						result.put("msg", "汇总失败！");
						throw new RuntimeException(createTime1 + "汇总数据出现异常！数据进行回滚！");
					}
				}
				
			}else{
				result.put("status", true);
				result.put("msg", "更新数据失败，汇总失败！");
			}
		} catch (Exception e) {
			result.put("status", true);
			result.put("msg", "更新数据失败，汇总失败！");
			throw new RuntimeException(createTime1 + "汇总数据出现异常！数据进行回滚！" +e.getMessage());
		}
		return result;
	}
	
	@Override
	public int insertSuperPushShareDaySettleBatch(List<SuperPushShareDaySettle> list) throws Exception {
		return superPushShareDaySettleMapper.insertSuperPushShareDaySettleBatch(list);
	}
	@Override
	public Map<String, Object> insertSuperPushShareDaySettleSplitBatch(
			List<SuperPushShareDaySettle> superPushShareDaySettleList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
	    int i = 0;
	    int batchCount = 200;
	    List<SuperPushShareDaySettle> asdList = new ArrayList<>();
	    List<List<?>> superPushShareDaySettleSplitList = ListUtil.batchList(superPushShareDaySettleList, batchCount);
	    for (List<?> clist : superPushShareDaySettleSplitList) {
	      for (Object object : clist) {
	    	  SuperPushShareDaySettle asd = (SuperPushShareDaySettle) object;
	    	  asdList.add(asd);
	      }
	      if (asdList.size() > 0) {
	        log.info("插入超级推日结表{}条",asdList.size());
	        int j = this.insertSuperPushShareDaySettleBatch(asdList);
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
	      msg.put("msg", "没有任何数据插入");
	    }
	    return msg;
	}
	@Override
	public Map<String, Object> findSuperPushShareDaySettleCollection(SuperPushShareDaySettle superPushShareDaySettle)
			throws Exception {
		
		if (StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime1())) {
			superPushShareDaySettle.setCreateTime1(superPushShareDaySettle.getCreateTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime2())) {
			superPushShareDaySettle.setCreateTime2(superPushShareDaySettle.getCreateTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1())) {
			superPushShareDaySettle.setGroupTime1(superPushShareDaySettle.getGroupTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1())) {
			superPushShareDaySettle.setGroupTime2(superPushShareDaySettle.getGroupTime2() + " 23:59:59");
		}
		
		return superPushShareDaySettleMapper.findSuperPushShareDaySettleCollection(superPushShareDaySettle);
	}
	@Override
	public Map<String, Object> superPushBatchEnterAccount(String selectEnterId) throws Exception {
		String [] selectEcterIdList = selectEnterId.split(",");
		List<SuperPushShareDaySettle> superPushShareDaySettleList = new ArrayList<SuperPushShareDaySettle>();
		for (String id : selectEcterIdList) {
			SuperPushShareDaySettle superPushShareDaySettle = null;
			superPushShareDaySettle =  superPushShareDaySettleMapper.findSuperPushShareDaySettleById(id);
			if(superPushShareDaySettle != null){
				superPushShareDaySettleList.add(superPushShareDaySettle);
			}
		}
		Map<String, Object>  result = this.enterSuperPushAccountList(superPushShareDaySettleList);
		return result;
	}
	private Map<String,Object> enterSuperPushAccountList(List<SuperPushShareDaySettle> superPushShareDaySettleList) throws JsonParseException, JsonMappingException, IOException {
		Map<String,Object> msg = new HashMap<>();
		for (SuperPushShareDaySettle superPushShareDaySettle : superPushShareDaySettleList) {
			try {
				Map<String, Object>  result = superPushEnterAccountService.superPushEnterAccount(superPushShareDaySettle);
				Boolean resultStatus =  (Boolean) result.get("status");
				String resultMsg =  (String) result.get("msg");
				if (resultStatus) {
					msg.put("status", resultStatus);
					msg.put("msg", resultMsg);
					log.info(msg.toString());
				}else{
					msg.put("status", resultStatus);
					msg.put("msg", resultMsg);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.info("异常:"+e);
				String s = String.format("商户或者代理商%s入账异常:"+ e.getMessage(), superPushShareDaySettle.getShareNo());
				log.info(s);
				superPushShareDaySettle.setEnterAccountMessage(s);
				superPushShareDaySettleMapper.updateSuperPushShareDaySettle(superPushShareDaySettle);
			}
		}
		return msg;

	}
	@Override
	public SuperPushShareDaySettle findSuperPushShareDaySettleById(Integer id) {
		return superPushShareDaySettleMapper.findSuperPushShareDaySettleById(id.toString());
	}
	@Override
	public List<SuperPushShareDaySettle> exportSuperPushShareDaySettleList(
			SuperPushShareDaySettle superPushShareDaySettle) {
		return superPushShareDaySettleMapper.exportSuperPushShareDaySettleList(superPushShareDaySettle);
	}
	@Override
	public Map<String, Object> judgeSuperPushShareEnterTodayAccount(String currentDate) {
		return superPushShareDaySettleMapper.judgeSuperPushShareEnterTodayAccount(currentDate);
	}
	@Override
	public Map<String, Object> superPushEnterTodayAccount(String currentDate) throws Exception{
		List<SuperPushShareDaySettle> superPushShareDaySettleList =  null;
		Map<String, Object>  result = new HashMap<>();
		superPushShareDaySettleList =  superPushShareDaySettleMapper.findSuperPushShareTodaySettleByCurrentDate(currentDate);
		if(superPushShareDaySettleList == null || superPushShareDaySettleList.size() == 0 ){
			result.put("status", false);
			result.put("msg", "没有要入账的数据！");
		}else{
			result = this.enterSuperPushAccountList(superPushShareDaySettleList);
		}
		return result;
	}

}
