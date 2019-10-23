package cn.eeepay.framework.service.bill.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SysDictMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.RedisUtil;

@Service("sysDictService")
@Transactional
public class SysDictServiceImpl implements SysDictService{
	private static final Logger log = LoggerFactory.getLogger(SysDictServiceImpl.class);
	@Resource
	public SysDictMapper sysDictMapper;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private RedisService redisService;

	@PostConstruct
	@Override
	public void init() throws Exception {
		List<SysDict> sysDictList= sysDictMapper.findAllSysDict();
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
				//对sysDicts排序
				Collections.sort(sysDicts,new Comparator<SysDict>(){
		            public int compare(SysDict arg0, SysDict arg1) {
		            	if (arg0.getOrderNo() == null) {
		            		arg0.setOrderNo(0);
						}
		            	if (arg1.getOrderNo() == null) {
		            		arg1.setOrderNo(0);
						}
		                return arg0.getOrderNo().compareTo(arg1.getOrderNo());
		            }
		        });
				return sysDicts;
			}
		}
		else{
			return sysDictMapper.findSysDictGroup(sysKey);
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

	@Override
	public int insertSysDict(SysDict sysDict) throws Exception {
		String key = Constants.sys_dict_list_redis_key + ":" + sysDict.getSysKey();
		redisService.insertSet(key, sysDict);
		return sysDictMapper.insertSysDict(sysDict);
	}

	@Override
	public int updateSysDict(SysDict oldSysDict,SysDict newSysDict) throws Exception {
		String key = Constants.sys_dict_list_redis_key + ":" + oldSysDict.getSysKey();
		boolean isMember = redisService.selectIsMemberForSet(key, oldSysDict);
		if (isMember) {
			redisService.deleteMemberForSet(key, oldSysDict);
			redisService.insertSet(key, newSysDict);
		}
		return sysDictMapper.updateSysDict(newSysDict);
	}



	@Override
	public int deleteSysDict(Integer id) throws Exception {
		SysDict sysDict = findSysDictById(id);
		String key = Constants.sys_dict_list_redis_key + ":" + sysDict.getSysKey();
		redisService.deleteMemberForSet(key, sysDict);
		return sysDictMapper.deleteSysDict(id);
	}

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
		return sysDictMapper.findSysDictList(sysDict, sort, page);
	}

	@Override
	public boolean findSysDictExist(SysDict sysDict) throws Exception {
		if(sysDictMapper.findSysDictExist(sysDict)!=null){
			return true ;
		}
		return false;
	}

	@Override
	public SysDict findSysDictById(Integer id) {
		return sysDictMapper.findSysDictById(id);
	}

	@Override
	public List<SysDict> findAllSysDict() throws Exception {
		return sysDictMapper.findAllSysDict();
	}

	@Override
	public SysDict findSysDuiAccountGroup(String key) {
		return sysDictMapper.findSysDuiAccountGroup(key);
	}



}
