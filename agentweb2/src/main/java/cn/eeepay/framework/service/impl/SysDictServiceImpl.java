package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.RedisService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service("sysDictService")
public class SysDictServiceImpl implements SysDictService{
	@Resource
	public SysDictDao sysDictDao;
//	@Resource
//	private RedisUtil redisUtil;
	@Resource
	private RedisService redisService;

	@PostConstruct
	@Override
	public void init() throws Exception {
		List<SysDict> sysDictList= sysDictDao.findAllSysDict();
		List<String> keys = new ArrayList<String>();
		for (SysDict sysDict : sysDictList) {
			String key = Constants.sys_dict_list_redis_key +":"+sysDict.getSysKey();
			if (redisService.exists(key)) {
				keys.add(key);
			}
		}
		redisService.delete(keys);
		
		for (SysDict sysDict : sysDictList) {
			String key = Constants.sys_dict_list_redis_key +":"+sysDict.getSysKey();
			redisService.insertSet(key, sysDict);
		}
		System.out.println("sysDictService init");
		
	}
	
	@Override
	public Map<String, String> selectMapByKey(String key) {
		Map<String, String> sysDictMap = new HashMap<>();
		List<SysDict> sysDictList = sysDictDao.selectListByKey(key);
		if(sysDictList!=null && sysDictList.size()>0){
			for(SysDict sysDict: sysDictList){
				sysDictMap.put(sysDict.getSysValue(), sysDict.getSysName());
			}
		}
		return sysDictMap;
	}
	
	@Override
	public SysDict findSysDictByKeyValue(String sysKey,String sysValue) throws Exception {
		List<SysDict> sysDictList = findSysDictGroup(sysKey);
		for (SysDict sysDict : sysDictList) {
			if (sysDict.getSysValue().equals(sysValue)) {
				return sysDict;
			}
		}
		return null;
		//return sysDictDao.findSysDict(sysKey,sysValue);
	}



	@Override
	public List<SysDict> findSysDictGroup(String sysKey) throws Exception {
		String key = Constants.sys_dict_list_redis_key + ":" + sysKey;
		if (redisService.exists(key)) {
			Object  sysDictListRedis  = redisService.select(key);
			if(sysDictListRedis instanceof Set){
				Set sysDictList = (Set)sysDictListRedis;
				ArrayList<SysDict> sysDicts = new ArrayList<SysDict>(sysDictList);
				return sysDicts;
			}
		}
		else{
			return sysDictDao.findSysDictGroup(sysKey);
		}
		return null;
	}


//	@Override
//	public List<SysDict> getAllConfigListByRedis() throws Exception {
//		String key = Constants.sys_dict_list_redis_key + ":*";
//		if (redisService.exists(key)) {
//			Object  sysDictListRedis  = redisService.select(key);
//			if(sysDictListRedis instanceof Set){
//				Set sysDictList = (Set)sysDictListRedis;
//				ArrayList<SysDict> sysDicts = new ArrayList<SysDict>(sysDictList);
//				return sysDicts;
//			}
//		}
//		else{
//			//如果redis key 没有，或者redis没启动，用数据库获取数据
//			List<SysDict> sysDictList= sysDictDao.findAllSysDict();
//			return sysDictList;
//		}
//		return null;
//	}

//	@Override
//	public int insertSysDict(SysDict sysDict) throws Exception {
//		String key = Constants.sys_dict_list_redis_key + ":" + sysDict.getSysKey();
//		redisService.insertSet(key, sysDict);
//		return sysDictDao.insertSysDict(sysDict);
//	}
//
//	@Override
//	public int updateSysDict(SysDict oldSysDict,SysDict newSysDict) throws Exception {
//		String key = Constants.sys_dict_list_redis_key + ":" + oldSysDict.getSysKey();
//		boolean isMember = redisService.selectIsMemberForSet(key, oldSysDict);
//		if (isMember) {
//			redisService.deleteMemberForSet(key, oldSysDict);
//			redisService.insertSet(key, newSysDict);
//		}
//		return sysDictDao.updateSysDict(newSysDict);
//	}
//
//
//
//	@Override
//	public int deleteSysDict(Integer id) throws Exception {
//		SysDict sysDict = findSysDictById(id);
//		String key = Constants.sys_dict_list_redis_key + ":" + sysDict.getSysKey();
//		redisService.deleteMemberForSet(key, sysDict);
//		return sysDictDao.deleteSysDict(id);
//	}

//	@Override
//	public int deleteSysDictByParams(String sysKey, String sysValue) throws Exception {
//		List<SysDict> sysDictList = getAllConfigListByRedis();
//		for (SysDict sysDict : sysDictList) {
//			if(sysDict.getSysKey().equals(sysKey) && sysDict.getSysValue().equals(sysValue)){
//				sysDictList.remove(sysDict);
//			}
//		}
//		redisService.insertList(Constants.sys_dict_list_redis_key, sysDictList);
//		return sysDictDao.deleteSysDictByParams(sysKey, sysValue);
//	}



	@Override
	public SysDict findSysDictByKeyName(String sysKey, String sysName) throws Exception {
		List<SysDict> sysDicts = findSysDictGroup(sysKey);
		for (SysDict sysDict : sysDicts) {
			if (sysDict.getSysName().equals(sysName)) {
				return sysDict;
			}
		}
		return null;
	}



	@Override
	public List<SysDict> findSysDicts(SysDict sysDict, Sort sort, Page<SysDict> page) throws Exception {
		return sysDictDao.findSysDictList(sysDict, sort, page);
	}

	@Override
	public boolean findSysDictExist(SysDict sysDict) throws Exception {
		if(sysDictDao.findSysDictExist(sysDict)!=null){
			return true ;
		}
		return false;
	}

	@Override
	public SysDict findSysDictById(Integer id) {
		return sysDictDao.findSysDictById(id);
	}

	@Override
	public List<SysDict> selectTranStatusAllDict() {
		return sysDictDao.selectTranStatusAllDict();
	}

	@Override
	public List<SysDict> selectTransTypeAllDict() {
		return sysDictDao.selectTransTypeAllDict();
	}

	@Override
	public List<SysDict> selectServiceTypeAllDict() {
		return sysDictDao.selectServiceTypeAllDict();
	}

	@Override
	public List<SysDict> selectPayMethodTypeAllDict() {
		return sysDictDao.selectPayMethodTypeAllDict();
	}

	@Override
	public List<SysDict> selectCardTypeAllDict() {
		return sysDictDao.selectCardTypeAllDict();
	}

	@Override
	public JSONObject selectDictAndChildren() {
		List<SysDict> list = sysDictDao.selectAllDict();
		JSONObject json=new JSONObject();
		if(list != null && list.size()>0){
			for(int i=0; i< list.size(); i++){
				SysDict dic=list.get(i);
				if("COUPON_CODE".equals(dic.getSysKey())&&"7".equals(dic.getSysValue()))
					continue;
				JSONArray array=null;
				if(json.containsKey(dic.getSysKey())){
					array=json.getJSONArray(dic.getSysKey());
				}else{
					array=new JSONArray();
					json.put(dic.getSysKey(), array);
				}
				JSONObject item=new JSONObject();
				item.put("text", dic.getSysName());
				if("INT".equals(dic.getType()))
					item.put("value", Integer.parseInt(dic.getSysValue()));
				else
					item.put("value", dic.getSysValue());
				array.add(item);
			}
		}
		return json;
	}

	@Override
	public SysDict selectRestPwd() {
		return sysDictDao.selectRestPwd();
	}

	@Override
	public String SelectServiceId(String sysKey) {
		return sysDictDao.SelectServiceId(sysKey);
	}

	@Override
	public SysDict findAgentWebShareSwitch() {
		return sysDictDao.findAgentWebShareSwitch();
	}

	@Override
	public SysDict findAgentWebPromotionSwitch() {
		return sysDictDao.findAgentWebPromotionSwitch();
	}

	@Override
	public SysDict findAgentWebCashBackSwitch(){
		return sysDictDao.findAgentWebCashBackSwitch();
	}

	@Override
	public List<SysDict> listSysDictGroup(String keyName) {
		return sysDictDao.listSysDictGroup(keyName);
	}

	@Override
	public String getSuperPushBpId() {
		return sysDictDao.getSuperPushBpId();
	}

	@Override
	public List<String> findActivityTypeBy(String[] types) {
		return sysDictDao.findActivityTypeBy(types);
	}

	@Override
	public List<SysDict> selectByKey(String sysKey) {
		return sysDictDao.selectByKey(sysKey);
	}

	@Override
	public SysDict getByKey(String sysKey) {
		return sysDictDao.getByKey(sysKey);
	}

	@Override
	public String getStringValueByKey(String sysKey) {
		SysDict byKey = getByKey(sysKey);
		return byKey == null ? "" : byKey.getSysValue();
	}

	@Override
	public String getTradeGroupByAgentInfo(String agentType, String agentOem) {
		String tradeGroup = sysDictDao.findTradeGroupByAgentInfo(agentType,agentOem);
		if (!StringUtils.hasLength(tradeGroup)) {
			tradeGroup = sysDictDao.findTradeGroupByAgentInfo(agentType,"000000");
		}
		return tradeGroup;
	}
}
